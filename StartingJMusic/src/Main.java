import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import JMusicTools.FileTools;
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
		
		
		//runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 1000, 300);
		
		
		//evalRandomInvididual();
		//testPerformanceFitness();
		//pcaInput();
		//evalInputMusic();
		ArrayList<Individual> iArray = new ArrayList<Individual>();
		/*for (int i =0; i< 2; i++) {
			Individual ind = runGenetic(0, 1, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			ind.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(ind, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
			iArray.add(ind);
		}
		for (int i =0; i< 2; i++) {
			Individual ind = runGenetic(0, 1, FitnessConstants.FUX_FITNESS);
			ind.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(ind, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
			iArray.add(ind);
		}*/
		//runNSGA2(0, 1, iArray);
		//runNSGA2(0, 1, null);
		//runNSGA2(1, 10);
		//generateMassPopulation(10000, 30, Constants.BAR_REMAINING_DURATION, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
		/*runNSGAIIParetoCombined(0, 3, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 50, 100);
		ArrayList<Individual> bestFux = runGeneticConvergence(0, 3, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 50, 100);
		ArrayList<Individual> bestZipf = runGeneticConvergence(0, 3, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 50, 100);
		ArrayList<Individual> initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", 33+"_p"+300+"_c"+0.9+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", 33+"_p"+300+"_c"+0.9+"_m"+0.3);
		runNSGAIIParetoCombined(0, 3, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 50, 100);*/
		
		/*ArrayList<Individual> bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 1000, 300);
		ArrayList<Individual> bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 1000, 300);
		ArrayList<Individual> initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		ObjectOutputStream objectOut;
		try {
			objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("initPop.dat")));
			objectOut.writeObject(initPop);
			objectOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ArrayList<Individual> bestFux = new ArrayList<Individual>();
		ArrayList<Individual> bestZipf = new ArrayList<Individual>();
		ArrayList<Individual> initPop = new ArrayList<Individual>();
		
		
		/*ObjectInputStream objectIn;
		try {
			objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("initPop.dat")));
			ArrayList<Individual> initPopIn = (ArrayList<Individual>) objectIn.readObject();
			objectIn.close();
			bestFux = new ArrayList<Individual>(initPopIn.subList(0, 33));
			bestZipf = new ArrayList<Individual>(initPopIn.subList(33, 66));
			writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+300+"_c"+0.9+"_m"+0.1);
			writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+300+"_c"+0.9+"_m"+0.1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		
		//bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 1000, 300);
		/*
		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 1000, 300);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 1000, 300);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 1000, 300);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", 33+"_p"+300+"_c"+0.9+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", 33+"_p"+300+"_c"+0.9+"_m"+0.3);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 1000, 300);*/
		
		//runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.1, 1000, 300);
		/*bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.9, 0.1, 1000, 300);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.1, 1000, 300);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+300+"_c"+0.9+"_m"+0.1);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+300+"_c"+0.9+"_m"+0.1);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.1, 1000, 300);
		
		//runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.3, 1000, 300);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.7, 0.3, 1000, 300);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.7, 0.3, 1000, 300);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+300+"_c"+0.7+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+300+"_c"+0.7+"_m"+0.3);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.3, 1000, 300);
		
		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.1, 1000, 300);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.7, 0.1, 1000, 300);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.7, 0.1, 1000, 300);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+300+"_c"+0.7+"_m"+0.1);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+300+"_c"+0.7+"_m"+0.1);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.1, 1000, 300);
		
		
		bestFux = runGeneticConvergence(0, 20, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 4000, 300);
		bestZipf = runGeneticConvergence(0, 20, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 4000, 300);
		writeObjectivesMusic(bestFux, "fux", 20+"_p"+300+"_c"+0.9+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", 20+"_p"+300+"_c"+0.9+"_m"+0.3);

		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 1000, 600);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.9, 0.3, 1000, 600);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.3, 1000, 600);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", 33+"_p"+600+"_c"+0.9+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", 33+"_p"+600+"_c"+0.9+"_m"+0.3);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.3, 1000, 600);
		
		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.1, 1000, 600);
		bestFux = runGeneticConvergence(0, 1, FitnessConstants.FUX_FITNESS, 0.9, 0.1, 100, 600);
		bestZipf = runGeneticConvergence(0, 1, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.9, 0.1, 10, 600);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+600+"_c"+0.9+"_m"+0.1);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+600+"_c"+0.9+"_m"+0.1);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.9, 0.1, 1000, 600);
		
		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.3, 1000, 600);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.7, 0.3, 1000, 600);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.7, 0.3, 1000, 600);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+600+"_c"+0.7+"_m"+0.3);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+600+"_c"+0.7+"_m"+0.3);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.3, 1000, 600);
		
		runNSGAIIParetoCombined(0, 33, null, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.1, 1000, 600);
		bestFux = runGeneticConvergence(0, 33, FitnessConstants.FUX_FITNESS, 0.7, 0.1, 1000, 600);
		bestZipf = runGeneticConvergence(0, 33, FitnessConstants.ZIPF_FITNESS_ERROR_FIT, 0.7, 0.1, 1000, 600);
		initPop = new ArrayList<Individual>();
		initPop.addAll(bestFux);
		initPop.addAll(bestZipf);
		evalMultObjInidividuals(initPop, FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
		writeObjectivesMusic(bestFux, "fux", "_fux"+33+"_p"+600+"_c"+0.7+"_m"+0.1);
		writeObjectivesMusic(bestZipf, "zipf", "_zipf"+33+"_p"+600+"_c"+0.7+"_m"+0.1);
		runNSGAIIParetoCombined(0, 33, initPop, FitnessConstants.MULTI_OBJECTIVE_FITNESS, 0.7, 0.1, 1000, 600);
*/		
		/*ArrayList<Individual> iArray = new ArrayList<Individual>();
		for (int i =0; i< 6; i++)
			iArray.add(runGenetic(0, 1));
		writeObjectivesMusic(iArray, "fux");*/
		//runGenetic(1, 1);
		
		//generateRandomInvididuals(3);
		
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
	
	public static void runNSGA2 (int inScale, int nReplics, ArrayList<Individual> initialIndividuals) {
		for (int i = 0; i< nReplics; i++) {
			if (inScale ==1) {
				Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
				Fitness.scale = 1;
			}
				
			String selection = Constants.BINARY_TOURNAMENT_CROWDED_COMPARISON;
			String crossOver = Constants.CROSS_OVER_BAR;
			String fitness = FitnessConstants.MULTI_OBJECTIVE_FITNESS;
			String mutation = MutationConstants.MUTATE_MELODIC_AND_RHYTHM_TRIGRAM_BAR;
			String generationType = Constants.BAR_REMAINING_DURATION;
			GeneticAlgorithm ga = new GeneticAlgorithm(300, 600, 0.90, 0.3, 30, selection, crossOver, fitness, mutation, generationType, initialIndividuals);
			ga.nsga2();
			//ga.exportConvergence();
			ArrayList<Individual> firstFront = ga.returnFirstFront();
			ga.writeFirstFront(firstFront);
			Random rFront = new Random();
			ArrayList<Individual> musicsToListen = ga.returnMusicsToListen(5, firstFront);
			for (Individual max: musicsToListen) {
				System.out.println("fitness: (" + max.fitnesses[0] + ", " + max.fitnesses[1] + ")");
				max.getTrack().setName(fitness+mutation+max.getTrack().getName());
				if (inScale ==1) {
					max.getTrack().setName("C+"+max.getTrack().getName());
					max.getTrack().trackToScaleMidi(0, fitness+mutation);
				}
				else
					max.getTrack().trackToMidi(fitness+mutation);
			}
			
			
			//max.getZipfMetrics().writeZipfData(max.getTrack());
			//printCoefFitness(max);
		}

	}
	
	public static void runNSGAIIParetoCombined (int inScale, int nReplics, ArrayList<Individual> initialIndividuals, String fitness, double crossOverRate, double mutationRate, int generations, int popSize) {
		ArrayList<Individual> paretoCombined = new ArrayList<Individual>();
		String midname = "";
		String selection = Constants.BINARY_TOURNAMENT_CROWDED_COMPARISON;
		String crossOver = Constants.CROSS_OVER_BAR;
		String mutation = MutationConstants.MUTATE_MELODIC_AND_RHYTHM_TRIGRAM_BAR;
		String generationType = Constants.BAR_REMAINING_DURATION;
		
		for (int i = 0; i< nReplics; i++) {
			if (inScale ==1) {
				Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
				Fitness.scale = 1;
				midname = midname + "C+";
			}
			GeneticAlgorithm ga = new GeneticAlgorithm(popSize, generations, crossOverRate, mutationRate, 30, selection, crossOver, fitness, mutation, generationType, initialIndividuals);
			ga.nsga2();
			//ga.exportConvergence();
			ArrayList<Individual> firstFront = ga.returnFirstFront();
			paretoCombined.addAll(firstFront);
			System.out.println("Réplica Pareto Combinado " + (i+1) + " concluída.");
			
		}
		GeneticAlgorithm ga2 = new GeneticAlgorithm(popSize, generations, crossOverRate, mutationRate, 30, selection, crossOver, fitness, mutation, generationType, initialIndividuals);
		ga2.setPopulation(paretoCombined);
		ArrayList<Individual> firstFrontParetoCombined = ga2.returnFirstFront();
		midname = midname +"ParetoCombined_"+nReplics+"_p"+popSize+"_c"+crossOverRate+"_m"+mutationRate+"_"+firstFrontParetoCombined.get(0).getTrack().getName();
		if(initialIndividuals != null)
			midname = "InitPop" + midname;
		FileTools.writeFrontToFile(firstFrontParetoCombined, 900 , 0, midname);
		
		ArrayList<Individual> musicsToListen = ga2.returnMusicsToListen(5, firstFrontParetoCombined);
		for (Individual max: musicsToListen) {
			System.out.println("fitness: (" + max.fitnesses[0] + ", " + max.fitnesses[1] + ")");
			max.getTrack().setName(fitness +"F1_"+max.fitnesses[0]+"_F2_"+max.fitnesses[1]+"_"+ mutation + "ParetoCombined_" +nReplics+"_p"+popSize+"_c"+crossOverRate+"_m"+mutationRate+"_"+max.getTrack().getName());
			if(initialIndividuals != null)
				max.getTrack().setName("InitPop"+max.getTrack().getName());
			if (inScale ==1) {
				max.getTrack().setName("C+"+max.getTrack().getName());
				max.getTrack().trackToScaleMidi(0, fitness+mutation);
				
					
			}
			else
				max.getTrack().trackToMidi(fitness+mutation);
		}
		
	}
	
	public static ArrayList<Individual> runGeneticConvergence (int inScale, int nReplics, String fitness, double crossOverRate, double mutationRate, int generations, int popSize) {
		String meanS = "mean_";
		if (inScale ==1) {
			Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
			Fitness.scale = 1;
			meanS = "C+" + meanS;
		}
		String selection = Constants.BINARY_TOURNAMENT;
		String crossOver = Constants.CROSS_OVER_BAR;
		String mutation = MutationConstants.MUTATE_MELODIC_AND_RHYTHM_TRIGRAM_BAR;
		String generationType = Constants.BAR_REMAINING_DURATION;
		ArrayList<GeneticAlgorithm> gAs = new ArrayList<GeneticAlgorithm>();
		for (int i=0; i< nReplics; i++) {
			GeneticAlgorithm ga = new GeneticAlgorithm(popSize, generations, crossOverRate, mutationRate, 30, selection, crossOver, fitness, mutation, generationType, null);
			ga.runGeneticPaired();
			gAs.add(ga);
			System.out.println("Réplica "+ (i+1) +" concluída.");
			Individual max = ga.returnMaxIndividual();
			if(!fitness.equals(FitnessConstants.ZIPF_FITNESS))
				max.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(max, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
			max.getTrack().setName(fitness+"F1_"+max.fitnesses[0]+"_F2_"+max.fitnesses[1]+"_"+ mutation + "MonoObj_" +nReplics+"_p"+popSize+"_c"+crossOverRate+"_m"+mutationRate+"_"+max.getTrack().getName());
			if (inScale ==1) {
				max.getTrack().setName("C+"+max.getTrack().getName());
				max.getTrack().trackToScaleMidi(0, fitness+mutation);
			}
			else
				max.getTrack().trackToMidi(fitness+mutation);
			
		}
		GeneticAlgorithm gaEnd =  new GeneticAlgorithm(popSize, generations, crossOverRate, mutationRate, 30, selection, crossOver, fitness, mutation, generationType, null);
		ArrayList<Double> convMean = new ArrayList<Double>();
		for (int j = 0; j< generations; j++) {
			Double x = 0.0; Double y = 0.0; 
			for (GeneticAlgorithm ga: gAs) {
				x += ga.getConvergence().get(0);
				ga.getConvergence().remove(0);
				y += ga.getConvergence().get(0);
				ga.getConvergence().remove(0);
			}
			x = x/nReplics;
			y = y/nReplics;
			convMean.add(x);
			convMean.add(y);
		}
		gaEnd.setConvergence(convMean);
		gaEnd.exportConvergence(fitness+"_"+nReplics+"_p"+popSize+"_c"+crossOverRate+"_m"+mutationRate);
		ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();
		for (GeneticAlgorithm ga: gAs) {
			bestIndividuals.add(ga.returnMaxIndividual());
		}
		return bestIndividuals;
	}
	public static Individual runGenetic (int inScale, int nReplics, String fitness, double crossOverRate, double mutationRate) {
		for (int i = 0; i< nReplics; i++) {
			if (inScale ==1) {
				Constants.RANGE_MAX_PITCH = Constants.RANGE_MIN_PITCH + 3*7;
				Fitness.scale = 1;
			}
				
			String selection = Constants.BINARY_TOURNAMENT;
			String crossOver = Constants.CROSS_OVER_BAR;
			String mutation = MutationConstants.MUTATE_MELODIC_AND_RHYTHM_TRIGRAM_BAR;
			String generationType = Constants.BAR_REMAINING_DURATION;
			GeneticAlgorithm ga = new GeneticAlgorithm(300, 600, crossOverRate, mutationRate, 30, selection, crossOver, fitness, mutation, generationType, null);
			ga.runGeneticPaired();
			ga.exportConvergence("");
			Individual max = ga.returnMaxIndividual();
			max.getTrack().setName(fitness+mutation+crossOverRate+mutationRate+max.getTrack().getName());
			if (inScale ==1) {
				max.getTrack().setName("C+"+max.getTrack().getName());
				max.getTrack().trackToScaleMidi(0, fitness+mutation);
			}
			else
				max.getTrack().trackToMidi(fitness+mutation);
			
			//max.getZipfMetrics().writeZipfData(max.getTrack());
			
			//Fitness.euclidianDistanceZipf(max);
			//printCoefFitness(max);
			return max;
		}
		return null;

	}
	public static void writeObjectivesMusic (ArrayList<Individual> iArray, String fitnessUsed, String midname) {
		String fitness = FitnessConstants.MULTI_OBJECTIVE_FITNESS;
		for (Individual i: iArray) {
			i.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(i, fitness);
			System.out.println("Fitness: " + i.fitnesses[0] +  " " +i.fitnesses[1]);
		}
		if (fitnessUsed.equals("fux"))
			FileTools.writeFrontToFile(iArray, 1, 1, midname);
		else
			FileTools.writeFrontToFile(iArray, 1, 2, midname);
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
	
	public static void generateRandomInvididuals (int n) {
		for (int i = 0; i<n; i++) {
			Individual i1 = new Individual(30, Constants.BAR_REMAINING_DURATION);
			i1.createTrack();
			i1.getTrack().setName("randomMelody"+(i+1)+".mid");
			i1.getTrack().trackToMidi("RandomMelodies");
		}
		
		
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
	
	public static void generateMassPopulation (int amount, int musicLenghtNotes, String generationType, String fitness) {
		ArrayList<Individual> massPopulation = new ArrayList<Individual>();
		for (int i = 0; i< amount; i++) {
			Individual ind = new Individual(musicLenghtNotes, generationType);
			ind.createTrack();
			ind.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(ind, fitness);
			massPopulation.add(ind);
		}
		FileTools.writeFrontToFile(massPopulation, 0, 901, "");
		System.out.println("Mass Random Population Generated.");
	}
	
	public static void evalMultObjInidividuals (ArrayList<Individual> iArray, String fitness) {
		for (Individual i: iArray) {
			if(!fitness.equals(FitnessConstants.ZIPF_FITNESS))
				i.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(i, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
		}
	}
}