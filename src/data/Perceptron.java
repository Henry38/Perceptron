package data;

import java.util.ArrayList;

public class Perceptron {
	
	private double[] weights;
	
	public Perceptron() {
		//this.weights = new ArrayList<Double>();
	}
	
	private double sum(double[] x) {
		double value = 0.0;
		
		int N = this.weights.length;
		for (int i = 0; i < N; i++) {
			value += (i == 0 ? 1 : x[i-1]) * this.weights[i];
		}
		
		return value;
	}
	
	private double f(double x) {
		return 1. / (1. + Math.exp(-x));
	}
	
//	private double deriv(double x) {
//		return Math.exp(-x) / Math.pow(1 + Math.exp(-x), 2);
//	}
	
	private double activation(double s) {
		return f(s);
	}
	
	public void training(ArrayList<double[]> inputs, ArrayList<Boolean> expectedOutput) {
		// check input and output have same size
		if (inputs.size() > 0 && inputs.size() != expectedOutput.size()) {
			return;
		}
		
		int N = inputs.get(0).length + 1;
		
		// initialize random weights
		// w0, w1, ..., wn
		this.weights = new double[N];
		
		for (int i = 0; i < N; i++) {
			this.weights[i] = (2 * Math.random()) - 1;
		}
		
		double alpha = 0.5;		// step learning
		int n = 0;				// loop count
		int iter_max = 20;		// max iteration
		boolean no_error;		// stop loop if no error occurs
		
		do {
			no_error = true;
			
			// train on all data
			for (int i = 0; i < inputs.size(); i++) {
				double[] x = inputs.get(i);
				double sum = sum(x);
				
				boolean t = expectedOutput.get(i);
				boolean ti = activation(sum) >= 0.5;
				
				// compute weights correction
				if (t != ti) {
					no_error = false;
					
					double[] delta_w = new double[N];
					
					// compute delta correction
					for (int j = 0; j < N; j++) {
						
						double res = 0.0;
						for (int ii = 0; ii < inputs.size(); ii++) {
							double[] xx = inputs.get(ii);
							double sum_xx = sum(xx);
							double diff = (expectedOutput.get(ii) ? 1 : 0) - activation(sum_xx);
							double f_xx = f(sum_xx);
							double deriv = f_xx * (1 - f_xx) * (j == 0 ? 1 : xx[j-1]);
							res += diff * deriv; 
						}
						res = -res;
						
						delta_w[j] -= alpha * res;
						
						// naive incremental algorithm
						//this.weights[j] += alpha * (ti ? -2 : 2) * (j == 0 ? 1 : x[j-1]);
						// use pseudo  partial derivative (there is no sum, but still works ...)
						//double f = f(sum);
						//double partial_deriv = f * (1 - f);
						//double xi = (j == 0 ? 1 : x[j-1]);
						//this.weights[j] += alpha * (ti ? -1 : 1) * partial_deriv * xi;
					}
					
					// apply delta correction
					for (int j = 0; j < N; j++) {
						this.weights[j] += delta_w[j];
					}
				}
			}
			
			// increment loop
			n++;
			
		} while (n < iter_max && !no_error);
	}
	
	public void print_weights() {
		int N = this.weights.length;
		for (int i = 0; i < N; i++) {
			System.out.println(this.weights[i]);
		}
	}
}
