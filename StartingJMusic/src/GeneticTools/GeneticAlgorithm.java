package GeneticTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import Comparators.IndividualComparatorByObjective;
import Comparators.IndividualComparatorCrowdingDistance;
import Comparators.IndividualComparatorNonDominationRank;
import Constants.MutationConstants;
import NoteEnconding.Track;

public class GeneticAlgorithm {
	private ArrayList<Individual> population;
	private int populationLength;
	private int generations;
	private double crossOverRate;
	private double mutationRate;
	private int musicLengthBar;
	private String selectionMethod;
	private String crossOverMethod;
	private String fitnessMethod;
	private String mutationMethod;
	private ArrayList<Double> convergence;
	private String generationType;
	//private Individual bestI;
	
	public GeneticAlgorithm (int populationLength, int generations,
			double crossOverRate, double mutationRate, int musicLengthBar, String selectionMethod,
			String crossOverMethod, String fitnessMethod, String mutationMethod,
			String generationType) {
			
		
		this.setPopulation(new ArrayList<Individual>());
		this.setConvergence(new ArrayList<Double>());
		this.setPopulationLength(populationLength);
		this.setGenerations(generations);
		this.setCrossOverRate(crossOverRate);
		this.setMutationRate(mutationRate);
		this.setMusicLengthBar(musicLengthBar);
		this.setSelectionMethod(selectionMethod);
		this.setCrossOverMethod(crossOverMethod);
		this.setFitnessMethod(fitnessMethod);
		this.setMutationMethod(mutationMethod);
		this.setGenerationType(generationType);
		//this.bestI = new Individual(generationType);
		
	}

	private int getPopulationLength() {
		return populationLength;
	}
	private void setPopulationLength(int populationLength) {
		this.populationLength = populationLength;
	}

	private double getMutationRate() {
		return mutationRate;
	}
	private void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	private double getCrossOverRate() {
		return crossOverRate;
	}
	private void setCrossOverRate(double crossOverRate) {
		this.crossOverRate = crossOverRate;
	}

	private int getGenerations() {
		return generations;
	}
	private void setGenerations(int generations) {
		this.generations = generations;
	}
	
	private int getMusicLengthBar() {
		return musicLengthBar;
	}
	private void setMusicLengthBar(int musicLengthBar) {
		this.musicLengthBar = musicLengthBar;
	}

	private ArrayList<Individual> getPopulation() {
		return population;
	}

	private void setPopulation(ArrayList<Individual> population) {
		this.population = population;
	}
	
	public String getSelectionMethod() {
		return selectionMethod;
	}

	public void setSelectionMethod(String selectionMethod) {
		this.selectionMethod = selectionMethod;
	}

	private String getCrossOverMethod() {
		return crossOverMethod;
	}

	private void setCrossOverMethod(String crossOverMethod) {
		this.crossOverMethod = crossOverMethod;
	}

	private String getFitnessMethod() {
		return fitnessMethod;
	}

	private void setFitnessMethod(String fitnessMethod) {
		this.fitnessMethod = fitnessMethod;
	}

	public String getMutationMethod() {
		return mutationMethod;
	}

	public void setMutationMethod(String mutationMethod) {
		this.mutationMethod = mutationMethod;
	}

	private ArrayList<Double> getConvergence() {
		return convergence;
	}

	private void setConvergence(ArrayList<Double> convergence) {
		this.convergence = convergence;
	}

	private String getGenerationType() {
		return generationType;
	}

	private void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	public void initializeRandomPopulation() {
		for (int i = 0; i < this.getPopulationLength(); i++) {
			Individual ind = new Individual(getMusicLengthBar(), getGenerationType());
			ind.createTrack();
			ind.getZipfMetrics().setZipfCountMethod(getFitnessMethod());
			Fitness.fitness(ind, getFitnessMethod());
			getPopulation().add(ind);
		}
	}
			
	public ArrayList<Individual> runGenetic() {
		initializeRandomPopulation();
		ArrayList<Individual> offSpring = new ArrayList<Individual>();
		for (int i = 0;i < getGenerations(); i++) {
			Individual maxIAux = returnMaxIndividual();
			getConvergence().add(new Double(maxIAux.getFitness()));
			offSpring.add(maxIAux.clone());
			if(i%(getGenerations()/5) == 0)
				System.out.println("Geração  " + i);
			
			if(i == (getGenerations()*4/5))
				if(getMutationMethod() == MutationConstants.MUTATE_ALL_METHODS_COPYING_LATER)
					setMutationMethod(MutationConstants.MUTATE_ALL_METHODS);
			
			
			while (offSpring.size() < getPopulationLength()) {
				Random r = new Random();
				if (r.nextFloat() < getCrossOverRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual i2 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual[] ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod());
					Fitness.fitness(ii[0], getFitnessMethod());
					Fitness.fitness(ii[1], getFitnessMethod());
					if (offSpring.size() < getPopulationLength() -1) {
						offSpring.add(ii[0]);
						offSpring.add(ii[1]);
					}
					else {
						offSpring.add(ii[0]);
						break;
					}
				}
				if(r.nextFloat() < getMutationRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Date d = new Date(System.currentTimeMillis());
					i1.getTrack().setName("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
					if (offSpring.size() < getPopulationLength()) {
						offSpring.add(Mutation.mutation(i1, getMutationMethod()));
						Fitness.fitness(i1, getFitnessMethod());
					}
					else
						break;
				}
			}
			setPopulation(offSpring);
			offSpring = new ArrayList<Individual>();
		}
		//getConvergence().add(returnMaxIndividual());
		return getPopulation();
	}
	
	public ArrayList<Individual> runGeneticPaired() {
		initializeRandomPopulation();
		ArrayList<Individual> offSpring = new ArrayList<Individual>();
		for (int i = 0;i < getGenerations(); i++) {
			Individual maxIAux = returnMaxIndividual();
			offSpring.add(maxIAux.clone());
			getConvergence().add(new Double(maxIAux.getFitness()));
			if(i%(getGenerations()/5) == 0)
				System.out.println("Geração  " + i);
			
			if(i == (getGenerations()*4/5))
				if(getMutationMethod() == MutationConstants.MUTATE_ALL_METHODS_COPYING_LATER)
					setMutationMethod(MutationConstants.MUTATE_ALL_METHODS);
			
			Collections.shuffle(getPopulation());
			Random r = new Random();
			for (int j = 0; j < getPopulationLength(); j = j + 2) {
				if (r.nextFloat() < getCrossOverRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual i2 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual[] ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod());
					Fitness.fitness(ii[0], getFitnessMethod());
					Fitness.fitness(ii[1], getFitnessMethod());
					getPopulation().add(ii[0]);
					getPopulation().add(ii[1]);
				}
			}
			for (Individual iPop: getPopulation()) {
				if (r.nextFloat() < getMutationRate()) {
					Date d = new Date(System.currentTimeMillis());
					iPop.getTrack().setName("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
					Mutation.mutation(iPop, getMutationMethod());
					Fitness.fitness(iPop, getFitnessMethod());
				}
			}
			
			while (offSpring.size() < getPopulationLength())
				offSpring.add(Selection.binaryTournament(getPopulation()));
			setPopulation(offSpring);
			offSpring = new ArrayList<Individual>();
		}
		//getConvergence().add(returnMaxIndividual());
		return getPopulation();
	}
	
	public ArrayList<Individual> nsga2 () {
		
		initializeRandomPopulation();
		fastNonDominatedSort(getPopulation());
		crowdingDistance(getPopulation());
		for (int i = 0;i < getGenerations(); i++) {
			if(i%300==0) 
				System.out.println("Geração: " + i);
			ArrayList<Individual> offSpring = new ArrayList<Individual>();
			while (offSpring.size() < getPopulationLength()) {
				Random r = new Random();
				if (r.nextFloat() < getCrossOverRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual i2 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual[] ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod());
					Fitness.fitness(ii[0], getFitnessMethod());
					Fitness.fitness(ii[1], getFitnessMethod());
					if (offSpring.size() < getPopulationLength() -1) {
						offSpring.add(ii[0]);
						offSpring.add(ii[1]);
					}
					else {
						offSpring.add(ii[0]);
						break;
					}
				}
				if(r.nextFloat() < getMutationRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Date d = new Date(System.currentTimeMillis());
					i1.getTrack().setName("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
					if (offSpring.size() < getPopulationLength()) {
						offSpring.add(Mutation.mutation(i1, getMutationMethod()).clone());
						Fitness.fitness(i1, getFitnessMethod());
					}
					else
						break;
				}
			}
			
			getPopulation().addAll(offSpring);
			fastNonDominatedSort(getPopulation());
			crowdingDistance(getPopulation());
			Collections.sort(population, new IndividualComparatorNonDominationRank());
			offSpring = new ArrayList<Individual>();
			int initialNRank = getPopulation().get(0).getNonDominationRank();
			int initialNRankIndex = 0;
			for (Individual ind: getPopulation()) {
				if (ind.getNonDominationRank() != initialNRank) {
					ArrayList<Individual> front = new ArrayList<Individual>(getPopulation().subList(initialNRankIndex, getPopulation().indexOf(ind)));
					initialNRank++;
					initialNRankIndex = getPopulation().indexOf(ind);
					if (offSpring.size() + front.size() < getPopulationLength())
						offSpring.addAll(front);
					else if (offSpring.size() + front.size() == getPopulationLength()) {
						offSpring.addAll(front);
						break;
					}
					else {
						Collections.sort(front, new IndividualComparatorCrowdingDistance());
						offSpring.addAll(front.subList(0, getPopulationLength()-offSpring.size()));
						break;
					}
				}
			}
			i++;
			population = offSpring;
		}
		return population;
		
	}
	
	private void calculateFrontCrowdingDistance (ArrayList<Individual> front) {
		
		for (Individual i: front) 
			i.setCrowdingdistance(0.0);
		for (int m = 0; m < front.get(0).fitnesses.length; m ++) {
			Collections.sort(front, new IndividualComparatorByObjective(m));
			front.get(0).setCrowdingdistance(Double.POSITIVE_INFINITY);
			front.get(front.size()-1).setCrowdingdistance(Double.POSITIVE_INFINITY);
			
			if (front.size()>2) {
				ArrayList<Individual> middleFront = new ArrayList<Individual>(front.subList(1, front.size()-1));
				for (Individual i: middleFront) {
					if(front.indexOf(i)-1 == -1)
						System.out.println("erro");
					i.setCrowdingdistance(i.getCrowdingdistance() + 
						(front.get(front.indexOf(i)+1).fitnesses[m]	- front.get(front.indexOf(i)-1).fitnesses[m])
						/(front.get(0).fitnesses[m]- front.get(front.size()-1).fitnesses[m]));
				}
			}
		}
		
	}
	
	//testar
	private void crowdingDistance(ArrayList<Individual> population) {
		Collections.sort(population, new IndividualComparatorNonDominationRank());
		int nRank = population.get(0).getNonDominationRank();
		int initialNRank = 0;
		for (Individual i: population) {
			if (i.getNonDominationRank() != nRank) {
				ArrayList<Individual> front = new ArrayList<Individual>(population.subList(initialNRank, population.indexOf(i)));
				calculateFrontCrowdingDistance(front);
				initialNRank = population.indexOf(i);
				nRank++;
			}
		}
		calculateFrontCrowdingDistance(new ArrayList<Individual>(population.subList(initialNRank, population.size())));
	}

	private void fastNonDominatedSort(ArrayList<Individual> population) {
		ArrayList<Individual> firstFront = new ArrayList<Individual>();
		for (Individual p: population) {
			p.setDomination(new ArrayList<Individual>());
			p.setDominationCounter(0);
			for (Individual q: population) {
				if (p.dominates(q))
					p.getDomination().add(q);
				else
					if (q.dominates(p))
						p.setDominationCounter(p.getDominationCounter()+1);
			}
			if (p.getDominationCounter() == 0) {
				p.setNonDominationRank(1);
				firstFront.add(p);
			}
		}
		int i = 1;
		while (!firstFront.isEmpty()) {
			ArrayList<Individual> nextFront = new ArrayList<Individual>();
			for (Individual p: firstFront) {
				for (Individual q: p.getDomination()) {
					q.setDominationCounter(q.getDominationCounter()-1);
					if (q.getDominationCounter() == 0) {
						q.setNonDominationRank(i+1);
						nextFront.add(q);
					}
						
				}
			}
			i++;
			firstFront = nextFront;
		}
	}

	public Individual returnMaxIndividual () {
		Individual max = getPopulation().get(0);
		for (Individual i: getPopulation()) {
			if (i.getFitness() > max.getFitness())
				max = i;
		}
		
		return max;
	}
	
	public void exportConvergence () {
		String content = "";
		for (Double i: getConvergence()) {
			content += i.toString();
			content += " ";
		}
		
		Date d = new Date(System.currentTimeMillis());
	
		File file = new File("Convergencia/convergence_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".dat");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkDuplicity () {
		for (Individual i: population) {
			for (Individual i2: population.subList(population.indexOf(i)+1, population.size())) {
				if (i == i2) 
					System.out.println("duplicity " + population.indexOf(i) +" "+  population.lastIndexOf(i2));
			}
		}
	}
	
	
}

