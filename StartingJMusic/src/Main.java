import java.util.ArrayList;

import Constants.Constants;
import Constants.MutationConstants;
import Constants.ZipfLawConstants;
import GeneticTools.Fitness;
import GeneticTools.GeneticAlgorithm;
import GeneticTools.Individual;
import Instrument.SawtoothInst;
import Metrics.ZipfMetrics;
import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;
import jm.JMC;
import jm.music.data.*;
import jm.util.*;
import jm.audio.*;
import jm.constants.Durations;
import jm.constants.Instruments;
import jm.constants.RhythmValues;

public final class Main implements JMC {
    
	public static void main(String[] args){
		
		/*Score tmp = new Score("tmp.mid");
		Read.midi(tmp, "ind1.mid");
		Track t = new Track(tmp.getPart(0).getPhrase(0), "Track1");*/
		
		
		//max.getZipfMetrics().rhythmIntervalMetricCalculator(i1.getTrack());
		//max.getZipfMetrics().rhythmBigramMetricCalculator(i1.getTrack());
		//max.getZipfMetrics().rhythmTrigramMetricCalculator(i1.getTrack());
		//i1.getTrack().setName("exibicaoaleatoria.mid");
		/*i1.getTrack().trackToMidi();*/
		
		//evalRandomInvididual();
		//testPerformanceFitness();
		//evalInputMusic();
		runGenetic();		
	}
	
	public static void printMusic(Individual i1) {
		int i = 1;
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			System.out.print(nh.getMidiPitch() + "," + nh.getDuration()+ "," + nh.getTied() + " ");
			if( nh.getMeasure() !=i) {
				System.out.println();
				i++;
			}
		}
		System.out.println(); System.out.println();
	}
	public static void printChromaticMusic(Individual i1) {
		int i = 1;
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			System.out.print(nh.getMidiPitch()%12 + " ");
			if( nh.getMeasure() !=i) {
				System.out.println();
				i++;
			}
		}
		System.out.println(); System.out.println();
	}
	
	public static void printMusicDuration(Individual i1) {
		int i = 1;
		double tied = 0;
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			if (nh.getTied()==1)
				tied = nh.getDuration();
			if(nh.getTied() ==2)
				System.out.print(nh.getDuration()+tied + " ");
			if(nh.getTied() ==0)
				System.out.print(nh.getDuration() + " ");
			if( nh.getMeasure() !=i) {
				System.out.println();
				i++;
			}
		}
		System.out.println(); System.out.println();
	}
	public static void printCoefFitness(Individual max) {
		//max.getZipfMetrics().convertAll(max.getTrack());
		System.out.println("PitchMetric: " + max.getZipfMetrics().pitchMetricCalculator(max.getTrack()));
		System.out.println("PitchDistanceMetric: " + max.getZipfMetrics().pitchDistanceMetricCalculator(max.getTrack()));
		System.out.println("PitchDurationMetric: " + max.getZipfMetrics().pitchDurationMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchMetric: " + max.getZipfMetrics().chromaticPitchMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchDistanceMetric: " + max.getZipfMetrics().chromaticPitchDistanceMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchDurationMetric: " + max.getZipfMetrics().chromaticPitchDurationMetricCalculator(max.getTrack()));
		System.out.println("DurationMetric: " + max.getZipfMetrics().durationMetricCalculator(max.getTrack()));
		System.out.println("MelodicIntMetric: " + max.getZipfMetrics().melodicIntervalMetricCalculator(max.getTrack()));
		System.out.println("MelodicBigamMetric: " + max.getZipfMetrics().melodicBigramMetricCalculator(max.getTrack()));
		System.out.println("MelodicTrigamMetric: " + max.getZipfMetrics().melodicTrigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmBigramMetric: " + max.getZipfMetrics().rhythmBigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmTrigamMetric: " + max.getZipfMetrics().rhythmTrigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmIntervalMetric: " + max.getZipfMetrics().rhythmIntervalMetricCalculator(max.getTrack()));
		System.out.println("RhythmMetric: " + max.getZipfMetrics().rhythmMetricCalculator(max.getTrack()));
		System.out.println("Fitness: " + max.getFitness()) ;
	}
	
	public static void runGenetic () {
		String selection = Constants.BINARY_TOURNAMENT;
		String crossOver = Constants.CROSS_OVER_NOTE;
		String fitness = Constants.EUCLIDIAN_DISTANCE_ZIPF;
		String mutation = MutationConstants.MUTATE_MELODIC_AND_RHYTHM_TRIGRAM;
		GeneticAlgorithm ga = new GeneticAlgorithm(200, 2000, 0.95, 0.5, 120, selection, crossOver, fitness, mutation, Constants.NOTE);
		ArrayList<Individual> sol = ga.runGenetic();
		ga.exportConvergence();
		Individual max = ga.returnMaxIndividual();
		max.getTrack().trackToMidi();
		Fitness.euclidianDistanceZipf(max);
		printCoefFitness(max);
		//shit
	}
	
	public static void evalInputMusic () {
		Score tmp = new Score("tmp.mid");
		//Read.midi(tmp, "midis/megadeth-TornadoOfSouls-Solo.mid");
		Read.midi(tmp, "TrechosMidis/bethoven-quintasinfonia1.mid");
		//Read.midi(tmp, "TrechosMidis/JohnsonEric-CliffsofDover.mid");
		Track t = new Track(tmp.getPart(0).getPhrase(0), "Track1");
		Individual i1 = new Individual(t, Constants.BAR);
		Fitness.fitness(i1, Constants.ZIPF_FITNESS);
		System.out.println("Fitness: " + i1.getFitness()) ;
		printCoefFitness(i1);
	}
	
	public static void testPerformanceFitness () {
		long time = System.currentTimeMillis();
		for (int i = 0; i< 10000; i++) {
			Individual i1 = new Individual(40, Constants.NOTE);
			i1.createTrack();
			Fitness.fitness(i1, Constants.ZIPF_FITNESS);
		}
		System.out.println("Tempo de 10000 avaliações: " + (System.currentTimeMillis() - time));
	}
	
	public static void evalRandomInvididual () {
		Individual i1 = new Individual(200, Constants.NOTE);
		i1.createTrack();
		Fitness.fitness(i1, Constants.ZIPF_FITNESS);
		printCoefFitness(i1);
	}
	
}