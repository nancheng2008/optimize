package edu.jhu.hlt.optimize;

import edu.jhu.hlt.optimize.function.Function;
import edu.jhu.prim.vector.IntDoubleVector;

/**
 * An optimization technique for minimization and maximization.
 * 
 * @author mgormley
 * @author noandrews
 * 
 */
public interface Optimizer<T extends Function> {

    /**
     * Minimizes a function starting from some initial point.
     * 
     * @param function The function to optimize.
     * @param point The input/output point. The initial point for minimization
     *            should be passed in. When this method returns this parameter
     *            will contain the point at which the minimizer terminated,
     *            possibly the minimum.
     * @return True if the optimizer terminated at a local or global optima.
     *         False otherwise.
     */
    boolean minimize(T function, IntDoubleVector point);

    /**
     * Maximizes a function starting from some initial point.
     * 
     * @param function The function to optimize.
     * @param point The input/output point. The initial point for maximization
     *            should be passed in. When this method returns this parameter
     *            will contain the point at which the maximizer terminated,
     *            possibly the maximum.
     * @return True if the optimizer terminated at a local or global optima.
     *         False otherwise.
     */
    boolean maximize(T function, IntDoubleVector point);

}
