import java.util.ArrayList;

import Constants.Constants;
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
		
		//Individual i1 = new Individual(40);
		/*i1.createTrack();
		Fitness.fitness(i1, Constants.ZIPF_FITNESS);
		i1.getTrack().setName("exibicaoaleatoria.mid");*/
		/*i1.getTrack().trackToMidi();*/
		
		Score tmp = new Score("tmp.mid");
		//Read.midi(tmp, "midis/megadeth-TornadoOfSouls-Solo.mid");
		Read.midi(tmp, "TrechosMidis/bethoven-quintasinfonia1.mid");
		//Read.midi(tmp, "TrechosMidis/JohnsonEric-CliffsofDover.mid");
		Track t = new Track(tmp.getPart(0).getPhrase(0), "Track1");
		Individual i1 = new Individual(t);
		Fitness.fitness(i1, Constants.ZIPF_FITNESS);
		System.out.println("Fitness: " + i1.getFitness()) ;
		printCoefFitness(i1);
		
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
		System.out.println("PitchMetric: " + ZipfMetrics.pitchMetricCalculator(max.getTrack()));
		System.out.println("PitchDistanceMetric: " + ZipfMetrics.pitchDistanceMetricCalculator(max.getTrack()));
		System.out.println("PitchDurationMetric: " + ZipfMetrics.pitchDurationMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchMetric: " + ZipfMetrics.chromaticPitchMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchDistanceMetric: " + ZipfMetrics.chromaticPitchDistanceMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchDurationMetric: " + ZipfMetrics.chromaticPitchDurationMetricCalculator(max.getTrack()));
		System.out.println("DurationMetric: " + ZipfMetrics.durationMetricCalculator(max.getTrack()));
		System.out.println("MelodicIntMetric: " + ZipfMetrics.melodicIntervalMetricCalculator(max.getTrack()));
		System.out.println("MelodicBigamMetric: " + ZipfMetrics.melodicBigramMetricCalculator(max.getTrack()));
		System.out.println("MelodicTrigamMetric: " + ZipfMetrics.melodicTrigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmBigramMetric: " + ZipfMetrics.rhythmBigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmTrigamMetric: " + ZipfMetrics.rhythmTrigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmIntervalMetric: " + ZipfMetrics.rhythmIntervalMetricCalculator(max.getTrack()));
		System.out.println("RhythmMetric: " + ZipfMetrics.rhythmMetricCalculator(max.getTrack()));
		System.out.println("Fitness: " + max.getFitness()) ;
	}
	
	public static void runGenetic () {
		String selection = Constants.BINARY_TOURNAMENT;
		String crossOver = Constants.CROSS_OVER_BAR;
		String fitness = Constants.EUCLIDIAN_DISTANCE_ZIPF;
		String mutation = Constants.CHANGE_ONE_NOTE;
		GeneticAlgorithm ga = new GeneticAlgorithm(200, 2000, 100, 0.9, 0.3, 30, selection, crossOver, fitness, mutation);
		ArrayList<Individual> sol = ga.runGenetic();
		ga.exportConvergence();
		Individual max = ga.returnMaxIndividual();
		max.getTrack().trackToMidi();
		Fitness.euclidianDistanceZipf(max.getTrack());
		printCoefFitness(max);
	}
	
}