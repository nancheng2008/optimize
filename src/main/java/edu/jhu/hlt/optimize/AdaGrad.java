package edu.jhu.hlt.optimize;

import org.apache.log4j.Logger;

import edu.jhu.hlt.optimize.function.DifferentiableBatchFunction;
import edu.jhu.hlt.util.Prm;
import edu.jhu.prim.arrays.DoubleArrays;
import edu.jhu.prim.util.Lambda.FnIntDoubleToVoid;
import edu.jhu.prim.vector.IntDoubleVector;

/**
 * AdaGrad (Duchi et al., 2010) -- a first order stochastic gradient method with
 * parameter-specific learning rates.
 * 
 * @author mgormley
 */
public class AdaGrad implements GainSchedule {

    /** Options for this optimizer. */
    public static class AdaGradPrm extends Prm {
        /** The scaling parameter for the learning rate. */
        public double eta = 0.1;
        /**
         * The amount added (epsilon) to the sum of squares inside the square
         * root. This is to combat the issue of tiny gradients throwing the hole
         * optimization off early on.
         */
        public double constantAddend = 1e-9;
    }
    
    private static final Logger log = Logger.getLogger(AdaGrad.class);

    private AdaGradPrm prm;
    private double[] gradSumSquares;
    
    /**
     * Constructs an SGD optimizer.
     */
    public AdaGrad(AdaGradPrm prm) {
        this.prm = prm;
    }

    /**
     * Initializes all the parameters for optimization.
     */
    @Override
    public void init(DifferentiableBatchFunction function) {
        gradSumSquares = new double[function.getNumDimensions()];
    }

    /** A tie-in for subclasses such as AdaGrad. */
    public void takeNoteOfGradient(IntDoubleVector gradient) {
        gradient.iterate(new FnIntDoubleToVoid() {            
            @Override
            public void call(int index, double value) {
                gradSumSquares[index] += value * value;
                assert !Double.isNaN(gradSumSquares[index]);
            }
        });
    }
    
    /**
     * Gets the learning rate for the current iteration.
     * @param iterCount The current iteration.
     * @param i The index of the current model parameter. 
     */
    public double getLearningRate(int iterCount, int i) {
        if (gradSumSquares[i] < 0) {
            throw new RuntimeException("Gradient sum of squares entry is < 0: " + gradSumSquares[i]);
        }
        double learningRate = prm.eta / Math.sqrt(prm.constantAddend + gradSumSquares[i]);
        assert !Double.isNaN(learningRate);
        if (learningRate == Double.POSITIVE_INFINITY) {
            if (gradSumSquares[i] != 0.0) {
                log.warn("Gradient was non-zero but learningRate hit positive infinity: " + gradSumSquares[i]);
            }
            // Just return zero. The gradient is probably 0.0.
            return 0.0;
        }
        return learningRate;
    }

    @Override
    public GainSchedule copy() {
        AdaGradPrm otherPrm = Prm.clonePrm(this.prm);
        AdaGrad other = new AdaGrad(otherPrm);
        other.gradSumSquares = DoubleArrays.copyOf(this.gradSumSquares);
        return other;
    }

    @Override
    public double getEta0() {
        return prm.eta;
    }

    @Override
    public void setEta0(double eta0) {
        prm.eta = eta0;
    }

}
