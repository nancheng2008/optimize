package edu.jhu.hlt.util.math;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface Kernel {
	public DerivativeStructure k(DerivativeStructure [] x, DerivativeStructure [] y);
	public DerivativeStructure k(RealVector x, DerivativeStructure [] x_star);
	public double k(RealVector x1, RealVector x2);
	public RealMatrix K(RealMatrix x);             // TODO: shouldn't use a dense matrix here since it's pos. semi-def.
	
	// x1 fixed; partial derivative wrt x2[i]
	public double grad_k(RealVector x1, RealVector x2, int i);
	
	// partial derivative wrt x1[i] of k(x1, x1)
	public double identity_grad_k(RealVector x1, int i);
}
