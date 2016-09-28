package GeneticTools;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.math3.analysis.function.Pow;

import Constants.ZipfLawConstants;
import Metrics.FuxMetrics;
import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public abstract class Fitness {
	
	public static int scale = 0;
	
	public static void fitness (Individual i, String method) {
		i.getZipfMetrics().initZipfVector(i.getTrack());
		Individual iAux = new Individual(i.getMusicLenthBars(), i.getGenerationType());
		if (scale == 1 ) {
			iAux.setTrack(Track.copyNoteSequence(i.getTrack()));
			i.getTrack().trackToCMajor();
			
		}
		
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
		if (scale == 1 ) {
			i.setTrack(iAux.getTrack());
		}
	}
	
	public static double multiObjectiveFitness(Individual i) {
		i.fitnesses[0] = Fitness.fuxFitness(i);
		i.fitnesses[1] = Fitness.zipfFitnessErrorFit(i);
		return 0.0;
	}
	public static double zipfFitness (Individual i){
		
		Track tAux = Track.copyNoteSequence(i.getTrack());
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
	
	public static double zipfFractalFitness (Individual i) {
		double lambida = 0.5;
		double a = 0.0; 
		double fitness = 0.0;
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.DURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICBIGAM_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICTRIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCH_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCHDISTANCE_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCHDURATION_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMBIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMINTERVAL_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		a = i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMTRIGAM_METRIC_CALCULATOR);
		fitness += Math.pow(Math.E,(-Math.pow((-1.0-a),2.0)/lambida));
		
		return fitness;
	}
	/*public static Track copyNoteSequence (Track t) {
		Track t2 = new Track(t.getName());
		for (NoteHerremans nh: t.getNoteSequence()) {
			NoteHerremans nh2 = new NoteHerremans(nh);
			t2.getNoteSequence().add(nh2);
		}
		return t2;
	}*/
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
	public static double zipfNormalAndFractalFitness (Individual i) {
		return zipfFitness(i) + zipfFractalFitness(i);
	}
	
	public static double fuxFitness (Individual i) {
		double fit = -FuxMetrics.fux1EightNotes(i.getTrack());
		fit -= FuxMetrics.fux2OneClimax(i.getTrack());
		fit -= FuxMetrics.fux3ClimaxOnStrongBeat(i.getTrack());
		fit -= FuxMetrics.fux4HorizontalConsonantIntervals(i.getTrack());
		fit -= FuxMetrics.fux5ConjunctStepwise(i.getTrack());
		fit -= FuxMetrics.fux6LargeLeapFollStepwise(i.getTrack());
		fit -= FuxMetrics.fux7LargeLeapFollStepwise(i.getTrack());
		fit -= FuxMetrics.fux8ClimaxConsonantTonic(i.getTrack());
		fit -= FuxMetrics.fux9MaxTwoConsecutiveLeaps(i.getTrack());
		fit -= FuxMetrics.fux10MaxTwoLargeLeaps(i.getTrack());
		fit -= FuxMetrics.fux11LongStepwise(i.getTrack());
		fit -= FuxMetrics.fux12ChangedDirections(i.getTrack());
		fit -= FuxMetrics.fux13TonicEndNote(i.getTrack());
		fit -= FuxMetrics.fux14PenultimateLeadingTone(i.getTrack());
		fit -= FuxMetrics.fux15ConsonantMotionInterval(i.getTrack());
		fit -= FuxMetrics.fux16LargeMotionInterval(i.getTrack());
		fit -= FuxMetrics.fux19LargestInterval(i.getTrack());
		
		return fit;
	}
	
	public static double zipfFractalFitnessPrint(Individual i) {
		Track tAux = Track.copyNoteSequence(i.getTrack());
		if (i.getZipfMetrics() == null)
			System.out.println("Shit!");
		double lambida = 0.5;
		double a = 0.0; 
		
		double fitness = 0.0;
		//Double.valueOf(a).
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().pitchMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().pitchDistanceMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().pitchDurationMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().chromaticPitchDistanceMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().chromaticPitchDurationMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().chromaticPitchMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().melodicIntervalMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().melodicBigramMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().durationMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().rhythmMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().rhythmIntervalMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().rhythmBigramMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack())),2.0)/lambida)) * 100) / 100+ " ");
		//System.out.print(i.getZipfMetrics().pitchBigramMetricCalculator(i.getTrack())+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCH_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCHDISTANCE_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.CHROMATICPITCHDURATION_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.DURATION_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICBIGAM_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICINTERVAL_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.MELODICTRIGAM_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCH_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCHDISTANCE_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.PITCHDURATION_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHM_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMBIGAM_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMINTERVAL_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		System.out.print(Math.floor(Math.pow(Math.E,(-Math.pow((-1.0-i.getZipfMetrics().fractalMetricCalculator(i.getTrack(), Constants.Constants.FRACTAL_MIN_NOTES, ZipfLawConstants.RHYTHMTRIGAM_METRIC_CALCULATOR)),2.0)/lambida)) * 100) / 100+ " ");
		
		return 0.0;
	}
	
}
