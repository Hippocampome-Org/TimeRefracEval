/* 
    Author: Nate Sutton, 2023

    A concise code version of the Izhikevich model used in (Venkadesh, et al., 2019).
    This code is designed to allow testing and plotting a refractory period added to
    the neuron model.
    
    References: 
    Simple models of quantitative firing phenotypes in hippocampal neurons: comprehensive coverage of intrinsic diversity. PLOS Computational Biology 2019 Oct 28;15(10):e1007462; doi: 10.1371/journal.pcbi.1007462.
    https://stackoverflow.com/questions/22954452/how-do-you-use-apache-commons-math-3-0-to-solve-odes-with-jacobian 
*/

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TimeRefracEval {
    // Izhikevich parameters
    protected static double k = 0.62;
    protected static double a = 0.005;
    protected static double b = 11.69;
    protected static double d = 3.0;
    protected static double cM = 118.0;
    protected static double vR = -58.53;
    protected static double vT = -43.52;
    protected static double vPeak = 11.48;
    protected static double vMin = -49.52;

    // Experiment parameters
    protected static float t_0 = 0; // initial time
    protected static float t_N = 1100; // end time
    protected static double appCurrent = 200; // current to apply
    protected static double timeMin = 100; // current start time
    protected static double timeMax = 1000; // current end time
    protected static double sS = 0.1;//0.01;//1; // step size
    public static final int SOMA_IDX = 0;
    protected static double refrac_time = 150.0; // ms of refractory period
    protected static double last_spike_t = 0.0;
    protected static double v0=vR;
    protected static double u0=0;
    protected static String outputFilename = "neuron_voltage.csv";
    protected static BufferedWriter outputFile;

    // Event handler parameters
    protected static double maxCheckInterval = 0.01;//0.01 // maximal time interval between switching function checks (this interval prevents missing sign changes in case the integration steps becomes very large)
    protected static double convergenceThreshold = 0.001;//0.001 //  convergence threshold in the event time search
    protected static int maxIterationCount = 100;//100 // upper limit of the iteration count in the event time search

    public static void main(String[] args) throws IOException {
        ClassicalRungeKuttaIntegrator rkt = new ClassicalRungeKuttaIntegrator(sS);
        FirstOrderDifferentialEquations ode = new Izhikevich9pModelMC();               
        double[] y0 = new double[] {v0, u0}; // initial state
        double[] y = new double[y0.length]; 
        outputFile = new BufferedWriter(new FileWriter(outputFilename, false));

        StateRecorder stateRecorder = new StateRecorder();  
        rkt.addStepHandler(stateRecorder);
        SpikeEventHandler spikeEventHandler = new SpikeEventHandler();
        rkt.addEventHandler(spikeEventHandler, maxCheckInterval, convergenceThreshold , maxIterationCount); // Univariate Solver - default root finding algorithm; 5th parm
        rkt.integrate(ode, t_0, y0, t_N, y);
        spikeEventHandler.getSpikeNumber();
        outputFile.close();
    }   

    private static class Izhikevich9pModelMC implements FirstOrderDifferentialEquations {
        protected boolean spike_detected = false;

        public Izhikevich9pModelMC() {}
        public int getDimension() {return 2;}
        @Override public void computeDerivatives(double t, double[] y, double[] dy)
                throws MaxCountExceededException {

            double V0 = y[0]; double U0 = y[1]; // Soma
            double appCurrentSoma;
            if (t >= timeMin && t <= timeMax) {appCurrentSoma = appCurrent;} 
            else {appCurrentSoma = 0;}

            // refractory period
            if (V0 >= vPeak) {last_spike_t=t; spike_detected=true;}
            else if (spike_detected && t-last_spike_t>refrac_time) {spike_detected=false;}

            dy[0] = ((k * (V0 - vR) * (V0 - vT)) - U0 + appCurrentSoma) / cM;
            dy[1] = a * ((b * (V0 - vR)) - U0);

            if (spike_detected && t-last_spike_t<=refrac_time && t<=timeMax && V0<vPeak) {
                dy[0] = 0; dy[1] = 0;
            }
        }
    }

    private static class SpikeEventHandler implements EventHandler {
        ArrayList<Double> spikeTimes = new ArrayList<>();
        
        @Override public Action eventOccurred(double t, double[] y, boolean increasing) {     
            if(!increasing) {return Action.RESET_STATE;}
            else {return Action.CONTINUE;}
        }

        public SpikeEventHandler() {}
        @Override public double g(double t, double[] y) {
            return vPeak - y[0];
        }
        @Override public void init(double arg0, double[] arg1, double arg2) {}
        @Override public void resetState(double t, double[] y) {
            spikeTimes.add(t);
            y[0] = vMin;
            y[1] += d;
        }   

        public void getSpikeNumber() {System.out.print("Spikes: ");System.out.println(spikeTimes.size());}
    }

    static class StateRecorder implements StepHandler { /* continuous output model of numeric solver */
        public StateRecorder() {}       
        @Override public void handleStep(StepInterpolator interpolator, boolean isLast)
                throws MaxCountExceededException {   
            try {
                outputFile.write(Double.toString(interpolator.getCurrentTime()));
                outputFile.write(",");
                outputFile.write(Double.toString(interpolator.getInterpolatedState()[0]));
                outputFile.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override public void init(double arg0, double[] arg1, double arg2) { }     
    }
}