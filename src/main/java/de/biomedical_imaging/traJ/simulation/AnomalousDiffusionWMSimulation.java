package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import de.biomedical_imaging.traJ.Trajectory;
/*
 * Anomalosu Diffusion according to :
 * Anomalous Protein Diffusion in Living Cells as Seen by Fluorescence Correlation Spectroscopy
 * 
 * The spatial increments in x and y direction are choosen via the Weierstrass-Mandelbrot function 
 */
public class AnomalousDiffusionWMSimulation extends AbstractSimulator {

	private double diffusioncoefficient;
	private double timelag;
	private int dimension;
	private int numberOfSteps;
	private double alpha;
	
	public AnomalousDiffusionWMSimulation(double diffusioncoefficient, double timelag, int dimension,int numberOfSteps, double alpha) {
		this.diffusioncoefficient = diffusioncoefficient;
		this.timelag = timelag;
		this.numberOfSteps = numberOfSteps;
		this.dimension = dimension;
		this.alpha = alpha;
	}
	
	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = new Trajectory(dimension);
		t.add(new Point3d(0, 0, 0));
		double[] incrx = generateIncrements();
		double[] incry = generateIncrements();
	
		for(int i = 1; i <= numberOfSteps; i++) {
			Point3d pos = new Point3d();
			pos.setX(t.get(i-1).x + incrx[i-1]); //Math.sqrt(2*diffusioncoefficient*timelag);
			pos.setY(t.get(i-1).y + incry[i-1]); //Math.sqrt(2*diffusioncoefficient*timelag));
			t.add(pos);
		}

		return t;
	}
	
	public double[] generateIncrements(){
		double gamma = Math.sqrt(Math.PI);
		double H = alpha/2;
		double[] wxs = new double[numberOfSteps];
		double[] increments = new double[numberOfSteps];
		
		double[] phasesx = new double[48+8+1];
		for(int j = 0; j < phasesx.length; j++){
			phasesx[j] = CentralRandomNumberGenerator.getInstance().nextDouble()*2*Math.PI;
		}
		
		for(int t = 1; t <= numberOfSteps; t++){
			double tStar = 2*Math.PI*t/numberOfSteps;
			double wx = 0;
			
			for(int n = -8; n <= 48; n++){
				double phasex = phasesx[n+8];
				wx += (Math.cos(phasex)-Math.cos(Math.pow(gamma, n) * tStar + phasex))/Math.pow(gamma, n*H);
			}
			double prevwx = (t-2)>=0?wxs[t-2]:0;
			wxs[t-1] = wx;
			increments[t-1] = wxs[t-1]-prevwx; 
		}
		
		return increments;
	}

	
	
	

}
