package Constants;

import Metrics.ZipfMetrics;

public final class Constants {
	
	//CrossOvers
	public static final String CROSS_OVER_BAR = "crossOverBar";
	public static final String CROSS_OVER_NOTE = "crossOverNote";
	
	//Selections
	public static final String BINARY_TOURNAMENT = "binaryTournament";
	
	//Fitness
	public static final String ZIPF_FITNESS = "zipfFitness";
	public static final String  EUCLIDIAN_DISTANCE_ZIPF = "euclidianDistanceZipf";
	public static final String  ZIPF_FITNESS_ERROR_FIT = "zipfFitnessErrorFit";
	public static final String ZIPF_FITNESS_RSQUARE = "zipfFitnessRSquare";
	
	//Mutations
	/*public static final String CHANGE_ONE_NOTE = "changeOneNote";
	public static final String MUTATE_MELODIC_TRIGRAM = "mutateMelodicTrigram";*/
	
	//Constantes Numéricas
	public static final int N_DURATIONS = 18;
	public static final int RANGE_MIN_PITCH = 36;
	public static int RANGE_MAX_PITCH = 72;
	public static final double EPSILON_DURATION = 0.005;
	public static final double FRACTAL_PROXIMITY = 0.2;
	
	//Constantes de limitação de Duração
	public static final int WITHOUT_NOTHING = 18;
	public static final int WITHOUT_TRIPLETS = 11;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED = 10;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED_DOTTED = 6;
	
	//Constantes de geração musical
	public static final String BAR = "bar";
	public static final String NOTE = "note";
	
	
	

}
