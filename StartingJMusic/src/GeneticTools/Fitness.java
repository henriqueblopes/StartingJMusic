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
			Class[] classes = new Class[] {Track.class};
			Method m = Fitness.class.getMethod(method, classes);			
			Object[] objs = new Object[] {i.getTrack()};
			
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
	public static double zipfFitness (Track t){
		Track tAux = copyNoteSequence(t);
		
		double lambida = 0.5;
		double a = 0.0; 
		
		double fitness = 0.0;
		a = Metrics.ZipfMetrics.pitchMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.pitchDistanceMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.pitchDurationMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.chromaticPitchDistanceMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.chromaticPitchDurationMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.chromaticPitchMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.melodicIntervalMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.melodicBigramMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.melodicTrigramMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.durationMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
	
		a = Metrics.ZipfMetrics.rhythmMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.rhythmIntervalMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.rhythmBigramMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.rhythmTrigramMetricCalculator(t);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		//fitness += zipfFractalFitness(t);
		return fitness;
	}
	
	private static double zipfFractalFitness (Track tr) {
		double lambida = 0.5;
		double a = 0.0; 
		double fitness = 0.0;
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.CHROMATICPITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.CHROMATICPITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.CHROMATICPITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.DURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.MELODICBIGAM_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.MELODICINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.MELODICTRIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.PITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.PITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.PITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.RHYTHM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.RHYTHMBIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.RHYTHMINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = Metrics.ZipfMetrics.fractalMetricCalculator(tr, 4, ZipfLawConstants.RHYTHMTRIGAM_METRIC_CALCULATOR);
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
	public static double euclidianDistanceZipf (Track t) {
		double d[] = {-1.0413437415181142, -1.0689903240752452, -0.979938795101525, -1.1417581827340004,
				-0.941195143091278, -1.2674521655781357, -2.441830403088082, -1.577780960412904,
				-0.9530187483128054, -0.6184725109536818, -1.3204251851844273, -1.149547690753303,
				-1.6968032080083315, -2.576768672422933};
		double fitT[] = {Metrics.ZipfMetrics.pitchMetricCalculator(t),
				Metrics.ZipfMetrics.pitchDistanceMetricCalculator(t),
				Metrics.ZipfMetrics.pitchDurationMetricCalculator(t),
				Metrics.ZipfMetrics.chromaticPitchDistanceMetricCalculator(t),
				Metrics.ZipfMetrics.chromaticPitchDurationMetricCalculator(t),
				Metrics.ZipfMetrics.chromaticPitchMetricCalculator(t),
				Metrics.ZipfMetrics.melodicIntervalMetricCalculator(t),
				Metrics.ZipfMetrics.melodicBigramMetricCalculator(t),
				Metrics.ZipfMetrics.melodicTrigramMetricCalculator(t),
				Metrics.ZipfMetrics.durationMetricCalculator(t),
				Metrics.ZipfMetrics.rhythmMetricCalculator(t),
				Metrics.ZipfMetrics.rhythmIntervalMetricCalculator(t),
				Metrics.ZipfMetrics.rhythmBigramMetricCalculator(t),
				Metrics.ZipfMetrics.rhythmTrigramMetricCalculator(t)};
		double counter = 0.0;
		for (int i =0; i < 14; i++) {
			counter += (d[i] -fitT[i])*(d[i] -fitT[i]);
		}
		return -Math.pow(counter,0.5);
	}
	
	
}
