package GeneticTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import javax.security.auth.callback.ChoiceCallback;

import Comparators.IndividualComparatorByObjective;
import Comparators.IndividualComparatorCrowdingDistance;
import Comparators.IndividualComparatorNonDominationRank;
import Constants.FitnessConstants;
import Constants.MutationConstants;
import JMusicTools.FileTools;
import NoteEnconding.NoteHerremans;
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
	private ArrayList<Individual> initialIndividuals;
	//private Individual bestI;
	
	public GeneticAlgorithm (int populationLength, int generations,
			double crossOverRate, double mutationRate, int musicLengthBar, String selectionMethod,
			String crossOverMethod, String fitnessMethod, String mutationMethod,
			String generationType, ArrayList<Individual> initialIndividuals) {
			
		
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
		this.setInitialIndividuals(initialIndividuals);
		
		
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

	public void setPopulation(ArrayList<Individual> population) {
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

	public ArrayList<Double> getConvergence() {
		return convergence;
	}

	public void setConvergence(ArrayList<Double> convergence) {
		this.convergence = convergence;
	}

	private String getGenerationType() {
		return generationType;
	}

	private void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	private ArrayList<Individual> getInitialIndividuals() {
		return initialIndividuals;
	}

	private void setInitialIndividuals(ArrayList<Individual> initialIndividuals) {
		this.initialIndividuals = initialIndividuals;
	}

	public void initializeRandomPopulation() {
		if (getInitialIndividuals() != null)
			getPopulation().addAll(initialIndividuals);
		while (getPopulation().size() < this.getPopulationLength()) {
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
			
			if(!getFitnessMethod().equals(FitnessConstants.ZIPF_FITNESS))
				maxIAux.getZipfMetrics().setZipfCountMethod(FitnessConstants.ZIPF_FITNESS_ERROR_FIT);
			Fitness.fitness(maxIAux, FitnessConstants.MULTI_OBJECTIVE_FITNESS);
			Fitness.fitness(maxIAux, getFitnessMethod());
			getConvergence().add(new Double(maxIAux.fitnesses[0]));
			getConvergence().add(new Double(maxIAux.fitnesses[1]));
			
			offSpring.add(maxIAux.clone());
			
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
		System.out.println("Otimização Terminada.");
		return getPopulation();
	}
	
	public ArrayList<Individual> nsga2 () {
		
		initializeRandomPopulation();
		fastNonDominatedSort(getPopulation());
		crowdingDistance(getPopulation());
		for (int i = 0;i < getGenerations(); i++) {
			if (i%25 == 0 && i < 101)
				exportFrontVectors(i);
			
			if(i%(getGenerations()/5)==0) 
				System.out.println("Geração: " + i + " pop: " + getPopulation().size());
			if(getPopulation().size() <getPopulationLength())
				System.out.println("Erro no tamanho da pop");
			ArrayList<Individual> offSpring = new ArrayList<Individual>();
			while (offSpring.size() < getPopulationLength()) {
				Random r = new Random();
				if (r.nextFloat() < getCrossOverRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Individual i2 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					while (checkIndividualEquality(i1, i2)) {
						i2 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					}
					
					Individual[] ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod());
					//while (checkIndividualEquality(ii[0], i1) || checkIndividualEquality(ii[0], i2)) {
						//ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod());
					//}
					Fitness.fitness(ii[0], getFitnessMethod());
					Fitness.fitness(ii[1], getFitnessMethod());
					if(ii[0].fitnesses[0] == 0.0 || ii[0].fitnesses[1] == 0.0)
						System.out.println("errorCrossOver");
					if (offSpring.size() < getPopulationLength() -1) {
											
						//if(!existTwin(ii[0], getPopulation())) 
							offSpring.add(ii[0]);
						//if(!existTwin(ii[1], getPopulation()))
							offSpring.add(ii[1]);
						/*if(existTwin(ii[0], getPopulation())) {
							//System.out.println("bug");
						}
						if(existTwin(ii[1], getPopulation())) {
							//System.out.println("bug");
						}*/
							
					}
					else {
						//if(!existTwin(ii[0], getPopulation())) 
							offSpring.add(ii[0]);
						/*if(existTwin(ii[0], getPopulation()));
							//System.out.println("bug");
						break;*/
					}
				}
				if(r.nextFloat() < getMutationRate()) {
					Individual i1 = Selection.selection(getPopulation(),getSelectionMethod()).clone();
					Date d = new Date(System.currentTimeMillis());
					i1.getTrack().setName("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
					if (offSpring.size() < getPopulationLength()) {
						Individual i2 = Mutation.mutation(i1, getMutationMethod()).clone();
						Fitness.fitness(i2, getFitnessMethod());
						//if (!checkIndividualEquality(i1, i2)) {
							offSpring.add(i2);
							/*if(offSpring.get(offSpring.size()-1).fitnesses[0] == 0.0)
								System.out.println("errorMutation");*/
						//}
				//		existTwin(i2, getPopulation());
						
					}
					else
						break;
				}
			}
			
			//seekErrorFitness("beforeAddAll");
			getPopulation().addAll(offSpring);
			
			//seekErrorFitness("addAllOffSpring");
			fastNonDominatedSort(getPopulation());
			//seekErrorFitness("fastNDSort");
			crowdingDistance(getPopulation());
			//seekErrorFitness("crowDistance");
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
						try {
							//Collections.sort(front, new IndividualComparatorCrowdingDistance());
							front = insertionSortCrowding(front);
						}
						catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
						offSpring.addAll(front.subList(0, getPopulationLength()-offSpring.size()));
						break;
					}
				}
			}
			
			//if(offSpring.get(offSpring.size()-1).getNonDominationRank() > 1)
				//System.out.println(i + ": rank: " + offSpring.get(offSpring.size()-1).getNonDominationRank());
			population = offSpring;
		}
		//exportFrontVectors(populationLength);
		System.out.println("Otimização Multiobjetivo concluída.");
		return population;
		
	}
	
	private void seekErrorFitness(String s) {
		for (Individual i: population)
			if (i.fitnesses[0]== 0.0)
				System.out.println("error " + s);
	}

	private void calculateFrontCrowdingDistance (ArrayList<Individual> front) {
		
		for (Individual i: front) 
			i.setCrowdingdistance(0.0);
		for (int m = 0; m < front.get(0).fitnesses.length; m ++) {
			try {
				Collections.sort(front, new IndividualComparatorByObjective(m));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			front.get(0).setCrowdingdistance(Double.POSITIVE_INFINITY);
			front.get(front.size()-1).setCrowdingdistance(Double.POSITIVE_INFINITY);
			
			if (front.size()>2) {
				ArrayList<Individual> middleFront = new ArrayList<Individual>(front.subList(1, front.size()-1));
				for (Individual i: middleFront) {
					if(front.indexOf(i)-1 == -1)
						System.out.println("erro");
					
					//Perguntar
					if ((front.get(0).fitnesses[m]- front.get(front.size()-1).fitnesses[m]) == 0.0)
						i.setCrowdingdistance(i.getCrowdingdistance()+0.0);
					else {
						i.setCrowdingdistance(i.getCrowdingdistance() - 
								(front.get(front.indexOf(i)+1).fitnesses[m]	- front.get(front.indexOf(i)-1).fitnesses[m])
								/(front.get(0).fitnesses[m]- front.get(front.size()-1).fitnesses[m]));
					}
					if(Double.isNaN(i.getCrowdingdistance()))
						System.out.println("Error CrowdDIstance");
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

	public void fastNonDominatedSort(ArrayList<Individual> population) {
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
	
	public ArrayList<Individual> returnFirstFront () {
		fastNonDominatedSort(population);
		Collections.sort(population, new IndividualComparatorNonDominationRank());
		Individual aux = population.get(0);
		int lastIndexFront = getPopulation().size();
		for (Individual i: population) {
			if (i.getNonDominationRank() != aux.getNonDominationRank()) {
				lastIndexFront = population.indexOf(i);
				break;
			}
		}
		return new ArrayList<Individual>(population.subList(0, lastIndexFront));
	}
	
	public void exportConvergence (String midName) {
		String content = "";
		int j =0;
		for (Double i: getConvergence()) {
			content += i.toString();
			content += " ";
			
			if (j%2 ==1)
				content += "\n";
			j++;
		}
		
		Date d = new Date(System.currentTimeMillis());
	
		File file = new File("Convergencia/convergence_"+midName+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".dat");
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
	
	public ArrayList<Individual> insertionSortCrowding (ArrayList<Individual> front) {
		ArrayList<Individual> orderedFront = new ArrayList<Individual>();
		int n = front.size();
		for (int i = 0; i < n; i ++) {
			orderedFront.add(returnMaxCrowdind(front));
		}
		return orderedFront;
	}
	public Individual returnMaxCrowdind (ArrayList<Individual> front) {
		Individual max = front.get(0);
		for (Individual i: front) {
			if (i.getCrowdingdistance() > max.getCrowdingdistance()) {
				if (i.getCrowdingdistance() == Double.NaN || i.getCrowdingdistance() == Double.NEGATIVE_INFINITY)
					System.out.println("Pode ter dado erro no insertion");
				max = i;
			}
		}
		front.remove(max);
		return max;
	}
	
	public void writeFirstFront (ArrayList<Individual> firstFront) {
		Collections.sort(firstFront, new IndividualComparatorByObjective(0));
		String s = new String();
		for (Individual i: firstFront) {
			Double a = i.fitnesses[0];
			s +=  a.toString() + " ";
			a = i.fitnesses[1];
			s +=  a.toString() + "\n";
		}
		FileTools.exportDatedFile(s, "multiObjFront", "F_"+firstFront.get(0).getTrack().getName());
	}
	
	public boolean  checkIndividualEquality(Individual i1, Individual i2) {
		//System.out.println("What?");
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			if (nh.getMidiPitch() == i2.getTrack().getNoteSequence().get(i1.getTrack().getNoteSequence().indexOf(nh)).getMidiPitch()) {
				if (nh.getDuration() == i2.getTrack().getNoteSequence().get(i1.getTrack().getNoteSequence().indexOf(nh)).getDuration()) {
					//System.out.println("YEah");
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
				
		}
		return true;
	}
	
	public boolean existTwin (Individual i2, ArrayList<Individual> iList) {
		for (Individual i: iList) {
			if (checkIndividualEquality(i2, i) == true)
				return true;
		}
		return false;
	}
	
	public void exportFrontVectors (int it) {
		ArrayList<Individual> pop = new ArrayList<Individual>(getPopulation());
		int i = 1;
		while (!population.isEmpty()) {
			ArrayList<Individual> front = returnFirstFront();
			this.population = new ArrayList<Individual>(population.subList(front.size(), population.size()));
			Collections.sort(front, new IndividualComparatorByObjective(0));
			FileTools.writeFrontToFile(front, i, it, "");
			i++;
		}
		setPopulation(pop);
		fastNonDominatedSort(population);
	}
	
	public ArrayList<Individual> returnMusicsToListen (int amount, ArrayList<Individual> front) {
		ArrayList<Individual> musicsToListen = new ArrayList<Individual>();
		int i = 0;
		Random r = new Random();
		while (i < amount || front.isEmpty()) {
			Individual ind = front.get(r.nextInt(front.size()));
			if (!existTwin(ind, musicsToListen)) {
				musicsToListen.add(ind);
			}
			front.remove(ind);
			i++;
		}
		return musicsToListen;
	}
	
	
}

