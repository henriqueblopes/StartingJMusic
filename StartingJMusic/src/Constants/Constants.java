package Constants;

import Metrics.ZipfMetrics;

public final class Constants {
	
	//CrossOvers
	public static final String CROSS_OVER_BAR = "crossOverBar";
	public static final String CROSS_OVER_NOTE = "crossOverNote";
	
	//Selections
	public static final String BINARY_TOURNAMENT = "binaryTournament";
	
	
	
	//Mutations
	/*public static final String CHANGE_ONE_NOTE = "changeOneNote";
	public static final String MUTATE_MELODIC_TRIGRAM = "mutateMelodicTrigram";*/
	
	//Constantes Numéricas
	public static final int N_DURATIONS = 18;
	public static final int RANGE_MIN_PITCH = 36;
	public static int RANGE_MAX_PITCH = 72;
	public static final double EPSILON_DURATION = 0.01;
	public static final double FRACTAL_PROXIMITY = 0.2;
	public static final int FRACTAL_MIN_NOTES = 14;
	public static final double BAR_TEMPO = 4.0;
	
	//Constantes de limitação de Duração
	public static final int WITHOUT_NOTHING = 18;
	public static final int WITHOUT_TRIPLETS = 11;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED = 10;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED_DOTTED = 6;
	public static final int WITHOUT_TRIPLETS_DOUBLEDOTTED_DOTTED_FUSE = 5;
	
	//Constantes de geração musical
	public static final String BAR = "bar";
	public static final String NOTE = "note";
	public static final String BAR_REMAINING_DURATION = "createTrackBarRemainingDuration";
	
	//FuxConstants
	public static final int FUX_LENGTH_BARS = 16;
	
	

}
