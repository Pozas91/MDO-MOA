package moa.gui;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.core.TimingUtils;
import moa.streams.generators.LEDGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Experiment2 {

    public Experiment2() {
    }

    public void run(int numInstances, boolean isTesting, int testingUntil) {
        Classifier learner = new NaiveBayes();
        LEDGenerator stream = new LEDGenerator();
        stream.prepareForUse();

        learner.setModelContext(stream.getHeader());
        learner.prepareForUse();

        int numberSamplesCorrect = 0;
        int numberSamples = 0;
        List<Instance> instancesToTest = new ArrayList<>();
        boolean preciseCPUTiming = TimingUtils.enablePreciseTiming();
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();

        while (stream.hasMoreInstances() && numberSamples < numInstances) {
            Instance trainInst = stream.nextInstance().getData();
            learner.trainOnInstance(trainInst);
            instancesToTest.add(trainInst);
            numberSamples++;
        }

        int numberOfSamplesToTesting = 0;

        if (isTesting) {
            // Create iterator of instances.
            Iterator<Instance> iterator = instancesToTest.iterator();

            while (iterator.hasNext() && numberSamplesCorrect < testingUntil) {
                // Get next instance.
                Instance instance = iterator.next();

                if (learner.correctlyClassifies(instance)) {
                    // If is correct, accumulate.
                    numberSamplesCorrect++;
                } else {
                    // If not reset numberSamplesCorrect.
                    numberSamplesCorrect = 0;
                }

                // Accumulate number of samples necessary.
                numberOfSamplesToTesting++;
            }
        }

        double accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberOfSamplesToTesting;
        double time = TimingUtils.nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread() - evaluateStartTime);
        System.out.println(numberSamples + " instances processed with " + accuracy + "% accuracy in " + time + " seconds.");
    }

    public static void main(String[] args) throws IOException {
        Experiment2 exp = new Experiment2();
        exp.run(10000, true, 100);
    }
}