package edu.jhu.hlt.optimize;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import edu.jhu.hlt.optimize.function.FunctionOpts;
import edu.jhu.hlt.optimize.functions.SumSquares;
import edu.jhu.hlt.optimize.functions.XSquared;
import edu.jhu.hlt.util.JUnitUtils;
import edu.jhu.hlt.util.math.Vectors;
import edu.jhu.prim.vector.IntDoubleDenseVector;

public class GradientDescentWithLineSearchTest {

	   @Test
	    public void testNegXSquared() {
		   BasicConfigurator.configure();
		   Logger.getRootLogger().setLevel(Level.DEBUG);
		   
	       GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	       double[] max = new double[]{ 9.0 };
	       opt.maximize(new FunctionOpts.NegateFunction(new XSquared()), new IntDoubleDenseVector(max));
	       assertEquals(0.0, max[0], 1e-10);      
	    }
	   
	   @Test
	   public void testXSquared2() {
		   BasicConfigurator.configure();
		   Logger.getRootLogger().setLevel(Level.DEBUG);
	    	
	       GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	       double[] max = new double[]{ 13.3 };
	       opt.minimize(new XSquared(), new IntDoubleDenseVector(max));
	       assertEquals(0.0, max[0], 1e-10);  
	   }
	    
	    @Test
	    public void testXSquared() {
	    	BasicConfigurator.configure();
	    	Logger.getRootLogger().setLevel(Level.DEBUG);
	    	
	        GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	        double[] max = new double[]{ 9.0 };
	        opt.minimize(new XSquared(), new IntDoubleDenseVector(max));
	        assertEquals(0.0, max[0], 1e-10);        
	    }
	    
	    @Test
	    public void testSumSquares() {
	    	BasicConfigurator.configure();
	    	Logger.getRootLogger().setLevel(Level.DEBUG);
	    	
	        GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	        double[] initial = new double[3];
	        initial[0] = 9;
	        initial[1] = 2;
	        initial[2] = -7;
	        opt.minimize(new SumSquares(initial.length), new IntDoubleDenseVector(initial));
	        double[] max = initial;
	        JUnitUtils.assertArrayEquals(new double[] {0.0, 0.0, 0.0} , max, 1e-10);
	    }
	    
	    @Test
	    public void testNegSumSquares() {
	    	BasicConfigurator.configure();
	    	Logger.getRootLogger().setLevel(Level.DEBUG);
	    	
	        GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	        double[] initial = new double[3];
	        initial[0] = 9;
	        initial[1] = 2;
	        initial[2] = -7;
	        opt.maximize(new FunctionOpts.NegateFunction(new SumSquares(initial.length)), new IntDoubleDenseVector(initial));
	        double[] max = initial;
	        JUnitUtils.assertArrayEquals(new double[] {0.0, 0.0, 0.0} , max, 1e-10);
	    }
	    
	    @Test
	    public void testOffsetNegSumSquares() {
	    	BasicConfigurator.configure();
	    	Logger.getRootLogger().setLevel(Level.DEBUG);
	    	
	        GradientDescentWithLineSearch opt = new GradientDescentWithLineSearch(100);
	        double[] initial = new double[] { 9, 2, -7};
	        double[] offsets = new double[] { 3, -5, 11};
	        opt.maximize(new FunctionOpts.NegateFunction(new SumSquares(offsets)), new IntDoubleDenseVector(initial));
	        double[] max = initial;
	        Vectors.scale(offsets, -1.0);
	        JUnitUtils.assertArrayEquals(offsets, max, 1e-10);
	    }
	
}
