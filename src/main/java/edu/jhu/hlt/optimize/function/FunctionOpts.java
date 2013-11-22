package edu.jhu.hlt.optimize.function;

import edu.jhu.hlt.util.math.Vectors;
import edu.jhu.prim.vector.IntDoubleVector;

/**
 * Function operations.
 * 
 * @author mgormley
 */
public class FunctionOpts {

    public static DifferentiableFunction negate(DifferentiableFunction f) {
        return new NegateFunction(f);
    }
    
    public static DifferentiableFunction scale(DifferentiableFunction f, double multiplier) {
        return new ScaleFunction(f, multiplier);
    }

    public static Function negate(Function f) {
        return new NegateFunction1(f);
    }
    
    public static Function scale(Function f, double multiplier) {
        return new ScaleFunction1(f, multiplier);
    }
    
	public static class FunctionWithConstraints implements ConstrainedFunction {
		private Function f;
		private Bounds b;
		
		public FunctionWithConstraints(Function f, Bounds b) {
			this.f = f;
			this.b = b;
		}
		

		@Override
		public double getValue(IntDoubleVector point) {
			return f.getValue(point);
		}

		@Override
		public int getNumDimensions() {
			return f.getNumDimensions();
		}
		@Override
		public Bounds getBounds() {
			return b;
		}
		@Override
		public void setBounds(Bounds b) {
			this.b = b;
		}
	}
	
	public static class DifferentiableFunctionWithConstraints implements ConstrainedDifferentiableFunction {
		
		private DifferentiableFunction f;
		private Bounds b;
		
		public DifferentiableFunctionWithConstraints(DifferentiableFunction f, Bounds b) {
			this.f = f;
			this.b = b;
		}
		
		@Override
		public IntDoubleVector getGradient(IntDoubleVector pt) {
			return f.getGradient(pt);
		}

		@Override
		public double getValue(IntDoubleVector point) {
			return f.getValue(point);
		}
		@Override
		public int getNumDimensions() {
			return f.getNumDimensions();
		}
		@Override
		public Bounds getBounds() {
			return b;
		}
		@Override
		public void setBounds(Bounds b) {
			this.b = b;
		}

		@Override
		public ValueGradient getValueGradient(IntDoubleVector point) {
			return f.getValueGradient(point);
		}
		
	}
	
    /** Wrapper which negates the input function. */
    // TODO: Rename to NegateDifferentiableFunction.
    // TODO: Make private.
    public static class NegateFunction extends ScaleFunction implements DifferentiableFunction {
    
        public NegateFunction(DifferentiableFunction function) {
            super(function, -1.0);
        }
        
    }
    
    /** Wrapper which scales the input function. */
    // TODO: Rename to ScaleDifferentiableFunction.
    // TODO: Make private.
    public static class ScaleFunction implements DifferentiableFunction {
    
        private DifferentiableFunction function;
        private double multiplier;
        
        public ScaleFunction(DifferentiableFunction function, double multiplier) {
            this.function = function;
            this.multiplier = multiplier;
        }
        
    
        @Override
        public IntDoubleVector getGradient(IntDoubleVector pt) {
            IntDoubleVector g = function.getGradient(pt);
            g.scale(multiplier);
            return g;
        }
    
        @Override
        public int getNumDimensions() {
            return function.getNumDimensions();
        }

		@Override
		public double getValue(IntDoubleVector point) {
			return multiplier*function.getValue(point);
		}


		@Override
		public ValueGradient getValueGradient(IntDoubleVector point) {
			return new ValueGradient(getValue(point), getGradient(point));
		}
    
    }
    

    /** Wrapper which negates the input function. */
    // TODO: Drop the 1 from this name.
    private static class NegateFunction1 extends ScaleFunction1 implements Function {
    
        public NegateFunction1(Function function) {
            super(function, -1.0);
        }
        
    }
    
    /** Wrapper which scales the input function. */
    // TODO: Drop the 1 from this name.
    private static class ScaleFunction1 implements Function {
    
        private Function function;
        private double multiplier;
        
        public ScaleFunction1(Function function, double multiplier) {
            this.function = function;
            this.multiplier = multiplier;
        }
    
        @Override
        public int getNumDimensions() {
            return function.getNumDimensions();
        }


        @Override
        public double getValue(IntDoubleVector pt) {
            return multiplier*function.getValue(pt);
        }
    
    }
    
    /** Wrapper which adds the input functions. */
    public static class AddFunctions implements DifferentiableFunction {
    
        private DifferentiableFunction[] functions;
        
        public AddFunctions(DifferentiableFunction... functions) {
            int numDims = functions[0].getNumDimensions();
            for (DifferentiableFunction f : functions) {
                if (numDims != f.getNumDimensions()) {
                    throw new IllegalArgumentException("Functions have different dimension.");
                }
            }
            this.functions = functions;
        }
    
        @Override
        public double getValue(IntDoubleVector val) {
        	double sum = 0.0;
        	for(DifferentiableFunction f : functions) {
        		sum += f.getValue(val);
        	}
        	return sum;
        }
        
        @Override
        public IntDoubleVector getGradient(IntDoubleVector pt) {
        	IntDoubleVector ret = functions[0].getGradient(pt);
        	for(int i=1; i<functions.length; i++){
        		ret.add(functions[i].getGradient(pt));
        	}
        	return ret;
        }
    
        @Override
        public int getNumDimensions() {
            return functions[0].getNumDimensions();
        }

		@Override
		public ValueGradient getValueGradient(IntDoubleVector point) {
			return new ValueGradient(getValue(point), getGradient(point));
		}
    
    }

}
