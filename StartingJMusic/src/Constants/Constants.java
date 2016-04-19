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
	
	//Mutations
	/*public static final String CHANGE_ONE_NOTE = "changeOneNote";
	public static final String MUTATE_MELODIC_TRIGRAM = "mutateMelodicTrigram";*/
	
	//Constantes Numéricas
	public static final int N_DURATIONS = 18;
	public static final int RANGE_MIN_PITCH = 48;
	public static final int RANGE_MAX_PITCH = 84;
	public static final double EPSILON_DURATION = 0.005;
	public static final double FRACTAL_PROXIMITY = 0.2;
	
	//Constantes de limitação de Duração
	public static final int WITHOUT_NOTHING = 17;
	public static final int WITHOUT_TRIPLETS = 12;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED = 9;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED_DOTTED = 5;
	
	//Constantes de geração musical
	public static final String BAR = "bar";
	public static final String NOTE = "note";
	
	
	

}
