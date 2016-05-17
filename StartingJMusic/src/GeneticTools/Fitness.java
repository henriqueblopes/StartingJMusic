package GeneticTools;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.math3.analysis.function.Pow;

import Constants.ZipfLawConstants;
import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public abstract class Fitness {
	
	public static void fitness (Individual i, String method) {
		//i.setFitness(zipfFitness(i.getTrack()));
		try {
			Class[] classes = new Class[] {Individual.class};
			Method m = Fitness.class.getMethod(method, classes);			
			Object[] objs = new Object[] {i};
			if (m == null)
				System.out.println("shit");
			try {
				i.setFitness( (double) m.invoke(m, objs));
				
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static double zipfFitness (Individual i){
		
		Track tAux = copyNoteSequence(i.getTrack());
		if (i.getZipfMetrics() == null)
			System.out.println("Shit!");
		double lambida = 0.5;
		double a = 0.0; 
		
		double fitness = 0.0;
		a = i.getZipfMetrics().pitchMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().pitchDistanceMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().pitchDurationMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().chromaticPitchDistanceMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().chromaticPitchDurationMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().chromaticPitchMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().melodicIntervalMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().melodicBigramMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().durationMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
	
		a = i.getZipfMetrics().rhythmMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().rhythmIntervalMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().rhythmBigramMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().pitchBigramMetricCalculator(i.getTrack());
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		//fitness += zipfFractalFitness(i);
		return fitness;
	}
	
	private static double zipfFractalFitness (Individual i) {
		double lambida = 0.5;
		double a = 0.0; 
		double fitness = 0.0;
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.CHROMATICPITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.CHROMATICPITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.CHROMATICPITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.DURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.MELODICBIGAM_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.MELODICINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.MELODICTRIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.PITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.PITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.PITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.RHYTHM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.RHYTHMBIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.RHYTHMINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), 4, ZipfLawConstants.RHYTHMTRIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		return fitness;
	}
	public static Track copyNoteSequence (Track t) {
		Track t2 = new Track(t.getName());
		for (NoteHerremans nh: t.getNoteSequence()) {
			NoteHerremans nh2 = new NoteHerremans(nh);
			t2.getNoteSequence().add(nh2);
		}
		return t2;
	}
	public static double euclidianDistanceZipf (Individual i) {
		//ZipfMetrics.convertAll(i.getTrack());
		double d[] = {-1.0413437415181142, -1.0689903240752452, -0.979938795101525, -1.1417581827340004,
				-0.941195143091278, -1.2674521655781357, -2.441830403088082, -1.577780960412904,
				-0.9530187483128054, -0.6184725109536818, -1.3204251851844273, -1.149547690753303,
				-1.6968032080083315, -2.576768672422933};
		double fitT[] = {i.getZipfMetrics().pitchMetricCalculator(i.getTrack()),
				i.getZipfMetrics().pitchDistanceMetricCalculator(i.getTrack()),
				i.getZipfMetrics().pitchDurationMetricCalculator(i.getTrack()),
				i.getZipfMetrics().chromaticPitchDistanceMetricCalculator(i.getTrack()),
				i.getZipfMetrics().chromaticPitchDurationMetricCalculator(i.getTrack()),
				i.getZipfMetrics().chromaticPitchMetricCalculator(i.getTrack()),
				i.getZipfMetrics().melodicIntervalMetricCalculator(i.getTrack()),
				i.getZipfMetrics().melodicBigramMetricCalculator(i.getTrack()),
				i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack()),
				i.getZipfMetrics().durationMetricCalculator(i.getTrack()),
				i.getZipfMetrics().rhythmMetricCalculator(i.getTrack()),
				i.getZipfMetrics().rhythmIntervalMetricCalculator(i.getTrack()),
				i.getZipfMetrics().rhythmBigramMetricCalculator(i.getTrack()),
				i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack())};
		double counter = 0.0;
		for (int j =0; j < 14; j++) {
			counter += (d[j] -fitT[j])*(d[j] -fitT[j]);
		}
		return -Math.pow(counter,0.5);
	}
	
	public static double zipfFitnessErrorFit (Individual i){
		double a = 0.0;
		a += i.getZipfMetrics().pitchMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().pitchDistanceMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().pitchDurationMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().chromaticPitchDistanceMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().chromaticPitchDurationMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().chromaticPitchMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().melodicIntervalMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().melodicBigramMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().durationMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().rhythmMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().rhythmIntervalMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().rhythmBigramMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack());
		a += i.getZipfMetrics().pitchBigramMetricCalculator(i.getTrack());
		return -a;
	}
	
	public static double zipfFitnessRSquare (Individual i){
		return -zipfFitnessErrorFit(i);
	}
	
}
