package edu.jhu.hlt.util.math;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.Logger;

import edu.jhu.hlt.optimize.GPGO;

public class SquaredExpKernel implements Kernel {

	static Logger log = Logger.getLogger(SquaredExpKernel.class);
	
	double var;
	double len_scale;
	
	public SquaredExpKernel(double var, double len_scale) {
		this.var = var;
		this.len_scale = len_scale;
	}
	
	public SquaredExpKernel() {
		this.var = 1d;
		this.len_scale = 1d;
	}
	
	@Override
	public DerivativeStructure k(RealVector x, DerivativeStructure [] x_star) {
		assert(x_star.length > 0);
		assert(x.getDimension() == x_star.length);
		//log.info("len_scale = " + len_scale);
		//log.info("var = " + var);
		//log.info("[in squaredexp] x1="+x.getEntry(0));
		//log.info("[in squaredexp] x2="+x_star[0].getValue());
		DerivativeStructure res = new DerivativeStructure(x_star[0].getFreeParameters(), x_star[0].getOrder(), 0d);
		for(int i=0; i<x.getDimension(); i++) {
			res = res.add( x_star[i].subtract(x.getEntry(i)).pow(2) );
		}
		res = res.multiply(-1.0/(len_scale*len_scale));
		res = res.exp().multiply(var*var);
		//log.info("[in squaredexp] k="+res.getValue());
		return res;
	}
	
	@Override
	public double k(RealVector x1, RealVector x2) {
		double res = 0d;
		for(int i=0; i<x1.getDimension(); i++) {
			res += Math.pow(x1.getEntry(i) - x2.getEntry(i),2)/len_scale*len_scale;
		}
		res *= -0.5;
		res = Math.exp(res);
		return var*var*res;
	}

	@Override
	public RealMatrix K(RealMatrix X) {
		RealMatrix K = MatrixUtils.createRealMatrix(X.getColumnDimension(), X.getColumnDimension());
		for(int i=0; i<X.getColumnDimension(); i++) {
			for(int j=0; j<X.getColumnDimension(); j++) {
				K.setEntry(i,j, k(X.getColumnVector(i), X.getColumnVector(j)));
			}
		}
		return K;
	}

	@Override
	public DerivativeStructure k(DerivativeStructure[] x, DerivativeStructure[] y) {
		DerivativeStructure res = new DerivativeStructure(x[0].getFreeParameters(), x[0].getOrder(), 0d);
		for(int i=0; i<x.length; i++) {
			res = res.add( y[i].negate().add(x[i]).pow(2).divide(len_scale*len_scale) );
		}
		res = res.multiply(-0.5);
		res = res.exp();
		return res.multiply(var*var);
	}

	@Override
	public double grad_k(RealVector x1, RealVector x2, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double identity_grad_k(RealVector x1, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
