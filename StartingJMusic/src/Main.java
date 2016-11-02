import java.util.ArrayList;
import java.util.Random;

import Constants.Constants;
import Constants.FitnessConstants;
import Constants.MutationConstants;
import Constants.ZipfLawConstants;
import GeneticTools.Fitness;
import GeneticTools.GeneticAlgorithm;
import GeneticTools.Individual;
import Instrument.SawtoothInst;
import Metrics.FuxMetrics;
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
		//pcaInput();
		//evalInputMusic();
		runNSGA2(0, 10);
		runNSGA2(1, 10);
		//runGenetic(0, 1);
		//runGenetic(1, 1);
		
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
		System.out.println("ChrPitchDistanceMetric: " + max.getZipfMetrics().chromaticPitchDistanceMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchDurationMetric: " + max.getZipfMetrics().chromaticPitchDurationMetricCalculator(max.getTrack()));
		System.out.println("ChrPitchMetric: " + max.getZipfMetrics().chromaticPitchMetricCalculator(max.getTrack()));
		System.out.println("MelodicIntMetric: " + max.getZipfMetrics().melodicIntervalMetricCalculator(max.getTrack()));
		System.out.println("MelodicBigamMetric: " + max.getZipfMetrics().melodicBigramMetricCalculator(max.getTrack()));
		System.out.println("MelodicTrigamMetric: " + max.getZipfMetrics().melodicTrigramMetricCalculator(max.getTrack()));
		System.out.println("DurationMetric: " + max.getZipfMetrics().durationMetricCalculator(max.getTrack()));
		System.out.println("RhythmMetric: " + max.getZipfMetrics().rhythmMetricCalculator(max.getTrack()));
		System.out.println("RhythmIntervalMetric: " + max.getZipfMetrics().rhythmIntervalMetricCalculator(max.getTrack()));
		System.out.println("RhythmBigramMetric: " + max.getZipfMetrics().rhythmBigramMetricCalculator(max.getTrack()));
		System.out.println("RhythmTrigamMetric: " + max.getZipfMetrics().rhythmTrigramMetricCalculator(max.getTrack()));
		System.out.println("Fitness: " + max.getFitness()) ;
	}
	
	public static void printCoefFuxFitness(Individual max) {
		//max.getZipfMetrics().convertAll(max.getTrack());
		System.out.println("fux1 " + FuxMetrics.fux1EightNotes(max.getTrack()));
		System.out.println("fux2 " + FuxMetrics.fux2OneClimax(max.getTrack()));
		System.out.println("fux3 " + FuxMetrics.fux3ClimaxOnStrongBeat(max.getTrack()));
		System.out.println("fux4 " + FuxMetrics.fux4HorizontalConsonantIntervals(max.getTrack()));
		System.out.println("fux5 " + FuxMetrics.fux5ConjunctStepwise(max.getTrack()));
		System.out.println("fux6 " + FuxMetrics.fux6LargeLeapFollStepwise(max.getTrack()));
		System.out.println("fux7 " + FuxMetrics.fux7LargeLeapFollStepwise(max.getTrack()));
		System.out.println("fux8 " + FuxMetrics.fux8ClimaxConsonantTonic(max.getTrack()));
		System.out.println("fux9 " + FuxMetrics.fux9MaxTwoConsecutiveLeaps(max.getTrack()));
		System.out.println("fux10 " + FuxMetrics.fux10MaxTwoLargeLeaps(max.getTrack()));
		System.out.println("fux11 " + FuxMetrics.fux11LongStepwise(max.getTrack()));
		System.out.println("fux12 " + FuxMetrics.fux12ChangedDirections(max.getTrack()));
		System.out.println("fux13 " + FuxMetrics.fux13TonicEndNote(max.getTrack()));
		System.out.println("fux14 " + FuxMetrics.fux14PenultimateLeadingTone(max.getTrack()));
		System.out.println("fux15 " + FuxMetrics.fux15ConsonantMotionInterval(max.getTrack()));
		System.out.println("fux16 " + FuxMetrics.fux16LargeMotionInterval(max.getTrack()));
		System.out.println("fux19 " + FuxMetrics.fux19LargestInterval(max.getTrack()));
		
		
	}
	
	public static void runNSGA2 (int inScale, int nReplics) {
		for (int i = 0; i< nReplics; i++) {
			if (inScale ==1) {
				Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
				Fitness.scale = 1;
			}
				
			String selection = Constants.BINARY_TOURNAMENT_CROWDED_COMPARISON;
			String crossOver = Constants.CROSS_OVER_BAR;
			String fitness = FitnessConstants.MULTI_OBJECTIVE_FITNESS;
			String mutation = MutationConstants.MUTATE_RHYTHM_TRIGRAM_BAR;
			String generationType = Constants.BAR_REMAINING_DURATION;
			GeneticAlgorithm ga = new GeneticAlgorithm(300, 1500, 0.90, 0.3, 30, selection, crossOver, fitness, mutation, generationType);
			ga.nsga2();
			//ga.exportConvergence();
			ArrayList<Individual> firstFront = ga.returnFirstFront();
			ga.writeFirstFront(firstFront);
			Random rFront = new Random();
			Individual max = firstFront.get(rFront.nextInt(firstFront.size()));
			//checkIndividualEquality(firstFront.get(0), firstFront.get(1));
			//Individual max = firstFront.get(0);
			//Fitness.fitness(max, fitness);
			System.out.println("fitness: (" + max.fitnesses[0] + ", " + max.fitnesses[1] + ")");
			max.getTrack().setName(fitness+mutation+max.getTrack().getName());
			if (inScale ==1) {
				max.getTrack().setName("C+"+max.getTrack().getName());
				max.getTrack().trackToScaleMidi(0, fitness+mutation);
			}
			else
				max.getTrack().trackToMidi(fitness+mutation);
			
			//max.getZipfMetrics().writeZipfData(max.getTrack());
			//printCoefFitness(max);
		}

	}
	public static void runGenetic (int inScale, int nReplics) {
		for (int i = 0; i< nReplics; i++) {
			if (inScale ==1) {
				Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
				Fitness.scale = 1;
			}
				
			String selection = Constants.BINARY_TOURNAMENT;
			String crossOver = Constants.CROSS_OVER_BAR;
			String fitness = FitnessConstants.FUX_FITNESS;
			String mutation = MutationConstants.CHANGE_ONE_NOTE_BAR;
			String generationType = Constants.BAR_REMAINING_DURATION;
			GeneticAlgorithm ga = new GeneticAlgorithm(200, 1500, 0.90, 0.3, 30, selection, crossOver, fitness, mutation, generationType);
			ga.runGeneticPaired();
			ga.exportConvergence();
			Individual max = ga.returnMaxIndividual();
			max.getTrack().setName(fitness+mutation+max.getTrack().getName());
			if (inScale ==1) {
				max.getTrack().setName("C+"+max.getTrack().getName());
				max.getTrack().trackToScaleMidi(0, fitness+mutation);
			}
			else
				max.getTrack().trackToMidi(fitness+mutation);
			
			max.getZipfMetrics().writeZipfData(max.getTrack());
			
			Fitness.euclidianDistanceZipf(max);
			printCoefFitness(max);
		}

	}
	
	public static void evalInputMusic () {
		//String fitness = FitnessConstants.ZIPF_FITNESS_ERROR_FIT;
		String fitness = FitnessConstants.ZIPF_FITNESS_ERROR_FIT;
		//String fitness = FitnessConstants.FUX_FITNESS;
		Score tmp = new Score("tmp.mid");
		Read.midi(tmp, "CreatedMelodies/multiObjectiveFitnessmutateRhythmTrigramBar/multiObjectiveFitnessmutateRhythmTrigramBarM_2016_10_26_13_52_17.mid");
		//Read.midi(tmp, "CreatedMelodiesInC+/fuxFitnessmutateRhythmTrigramBar/C+fuxFitnessmutateRhythmTrigramBarM_2016_9_28_11_5_38.mid");
		//Read.midi(tmp, "CreatedMelodies/zipfFitnessErrorFitmutateAllMethodsCopyingLater/zipfFitnessErrorFitmutateAllMethodsCopyingLaterM_2016_6_3_15_29_8.mid");
		
		//Read.midi(tmp, "TrechosMidis/mc-Bach,JohannSebastian-ConcertoInAMinor.mid");
		//Read.midi(tmp, "TrechosMidis/mc-BachJohannSebastian-SuitenFurVioloncelloN1.mid");
		//Read.midi(tmp, "TrechosMidis/mc-mc-Beethoven-FurElise.mid");
		//Read.midi(tmp, "TrechosMidis/mc-bethoven-quintasinfonia1.mid");
		//Read.midi(tmp, "TrechosMidis/mc-VivaldiAntonio-LaPrimavera(Allegro1).mid");
		//Read.midi(tmp, "TrechosMidis/ra-beattles-heyjude.mid");
		//Read.midi(tmp, "TrechosMidis/ra-DeepPurple-SmokeOnTheWater.mid");
		//Read.midi(tmp, "TrechosMidis/ra-LedZeppelin-GoodTimesBadTimes.mid");
		//Read.midi(tmp, "TrechosMidis/ra-Queen-IWantItAll.mid");
		//Read.midi(tmp, "TrechosMidis/ra-Rainbow-ManontheSilverMountain.mid");
		//Read.midi(tmp, "TrechosMidis/sg-JohnsonEric-CliffsofDover.mid");
		//Read.midi(tmp, "TrechosMidis/sg-megadeth-TornadoOfSouls-Solo.mid");
		//Read.midi(tmp, "TrechosMidis/sg-tunnel_vision_ver2.mid");
		//Read.midi(tmp, "TrechosMidis/sg-waldir_azevedo_brasileirinho.mid");
		//Read.midi(tmp, "TrechosMidis/sg-wormanityIntoTheBattlefield.mid");
		//Read.midi(tmp, "TrechosMidis/vm-BarkAtTheMoon.mid");
		//Read.midi(tmp, "TrechosMidis/vm-Blind Guardian-Nightfall.mid");
		//Read.midi(tmp, "TrechosMidis/vm-CowboysFromHell.mid");
		//Read.midi(tmp, "TrechosMidis/vm-Metallica-MasterOfPuppets.mid");
		//Read.midi(tmp, "TrechosMidis/vm-the_evil_that_men_do.mid");
		
		Track t = new Track(tmp.getPart(0).getPhrase(0), "Track1");
		Individual i1 = new Individual(t, Constants.NOTE);
		i1.getZipfMetrics().setZipfCountMethod(fitness);
		printCoefFuxFitness(i1);
		printCoefFitness(i1);
		Fitness.fitness(i1, fitness);
		System.out.println("Fitness: " + i1.fitnesses[0] +  " " +i1.fitnesses[1]);
		
		//printCoefFitness(i1);
		i1.getZipfMetrics().writeZipfData(i1.getTrack());
	}
	
	public static void testPerformanceFitness () {
		long time = System.currentTimeMillis();
		for (int i = 0; i< 10000; i++) {
			Individual i1 = new Individual(40, Constants.NOTE);
			i1.createTrack();
			Fitness.fitness(i1, FitnessConstants.ZIPF_FITNESS);
		}
		System.out.println("Tempo de 10000 avaliações: " + (System.currentTimeMillis() - time));
	}
	
	public static void evalRandomInvididual () {
		String fit = FitnessConstants.FUX_FITNESS;
		Individual i1 = new Individual(3, Constants.BAR_REMAINING_DURATION);
		i1.createTrack();
		i1.getTrack().setName("justRandomTest.mid");
		i1.getZipfMetrics().setZipfCountMethod(fit);
		//i1.getZipfMetrics().pitchBigramMetricCalculator(i1.getTrack());
		Fitness.fitness(i1, fit);
		//printCoefFitness(i1);
		//i1.getTrack().trackToScaleMidi(0, fitness+mutation);
		//i1.getTrack().trackToMidi("");
		//i1.getZipfMetrics().writeZipfData(i1.getTrack());
		
	}
	
	public static void pcaInput () {
		String[] input = { "TrechosMidis/mc-Bach,JohannSebastian-ConcertoInAMinor.mid",
			"TrechosMidis/mc-BachJohannSebastian-SuitenFurVioloncelloN1.mid",
			"TrechosMidis/mc-Beethoven-FurElise.mid",
			"TrechosMidis/mc-bethoven-quintasinfonia1.mid",
			"TrechosMidis/mc-VivaldiAntonio-LaPrimavera(Allegro1).mid",
			"TrechosMidis/ra-beattles-heyjude.mid",
			"TrechosMidis/ra-DeepPurple-SmokeOnTheWater.mid",
			"TrechosMidis/ra-LedZeppelin-GoodTimesBadTimes.mid",
			"TrechosMidis/ra-Queen-IWantItAll.mid",
			"TrechosMidis/ra-Rainbow-ManontheSilverMountain.mid",
			"TrechosMidis/sg-JohnsonEric-CliffsofDover.mid",
			"TrechosMidis/sg-megadeth-TornadoOfSouls-Solo.mid",
			"TrechosMidis/sg-tunnel_vision_ver2.mid",
			"TrechosMidis/sg-waldir_azevedo_brasileirinho.mid",
			"TrechosMidis/sg-wormanityIntoTheBattlefield.mid",
			"TrechosMidis/vm-BarkAtTheMoon.mid",
			"TrechosMidis/vm-Blind Guardian-Nightfall.mid",
			"TrechosMidis/vm-CowboysFromHell.mid",
			"TrechosMidis/vm-Metallica-MasterOfPuppets.mid",
			"TrechosMidis/vm-the_evil_that_men_do.mid"
			};
		
		Score[] tmp = new Score[20];
		
		String fitness = FitnessConstants.ZIPF_FRACTAL_FITNESS_PRINT;
		for (int i = 0; i< input.length; i++) {
			tmp[i] = new Score("tmp.mid");
			Read.midi(tmp[i], input[i]);
			
		}
		for (int i = 0; i< input.length; i++) {
			Track t = new Track(tmp[i].getPart(0).getPhrase(0), "Track1");
			Individual i1 = new Individual(t, Constants.NOTE);
			i1.getZipfMetrics().setZipfCountMethod(fitness);
			Fitness.fitness(i1, fitness);
			System.out.println();
		}

	}
	
	public static void checkIndividualEquality(Individual i1, Individual i2) {
		System.out.println("What?");
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			if (nh.getMidiPitch() == i2.getTrack().getNoteSequence().get(i1.getTrack().getNoteSequence().indexOf(nh)).getMidiPitch())
				System.out.println("YEah");
		}
	}
}