package moa.gui;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.core.TimingUtils;
import moa.streams.generators.LEDGenerator;

import java.io.IOException;


public class Experiment_2 {

    public Experiment_2() {
    }

    public void run(int numInstances, boolean isTesting, int testingUntil) {
        Classifier learner = new NaiveBayes();
        LEDGenerator stream = new LEDGenerator();
        stream.prepareForUse();

        learner.setModelContext(stream.getHeader());
        learner.prepareForUse();

        int numberSamples = 0;
        boolean preciseCPUTiming = TimingUtils.enablePreciseTiming();
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();

        /*
          Primero entreno con él número indicado de instancias
         */
        while (stream.hasMoreInstances() && numberSamples < numInstances) {
            Instance trainInst = stream.nextInstance().getData();
            learner.trainOnInstance(trainInst);
            numberSamples++;
        }

        int numberOfSamplesTested = 0;
        int numberSamplesCorrect = 0;

        /*
          Después si está activado el testing, empezamos a testear hasta que consigamos 100 aciertos seguidos.
         */
        if (isTesting) {
            while (stream.hasMoreInstances() && numberSamplesCorrect < testingUntil) {
                // Get next instance.
                Instance instance = stream.nextInstance().getData();

                if (learner.correctlyClassifies(instance)) {
                    // If is correct, accumulate.
                    numberSamplesCorrect++;
                }

                // Accumulate number of samples necessary.
                numberOfSamplesTested++;
            }
        }

        double accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberOfSamplesTested;
        double time = TimingUtils.nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread() - evaluateStartTime);
        System.out.println(numberSamples + " instances processed with " + accuracy + "% accuracy in " + time + " seconds.");
    }

    public static void main(String[] args) throws IOException {
        Experiment_2 exp = new Experiment_2();
        exp.run(10000, true, 100);
    }
}