package GeneticTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GeneticAlgorithm {
	private ArrayList<Individual> population;
	private int populationLength;
	private int generations;
	private int maxIterations;
	private double crossOverRate;
	private double mutationRate;
	private int musicLengthBar;
	private String selectionMethod;
	private String crossOverMethod;
	private String fitnessMethod;
	private String mutationMethod;
	private ArrayList<Individual> convergence;
	
	public GeneticAlgorithm (int populationLength, int generations, int maxIterations, 
			double crossOverRate, double mutationRate, int musicLengthBar, String selectionMethod,
			String crossOverMethod, String fitnessMethod, String mutationMethod) {
		
		this.setPopulation(new ArrayList<Individual>());
		this.setConvergence(new ArrayList<Individual>());
		this.setPopulationLength(populationLength);
		this.setGenerations(generations);
		this.setMaxIterations(maxIterations);
		this.setCrossOverRate(crossOverRate);
		this.setMutationRate(mutationRate);
		this.setMusicLengthBar(musicLengthBar);
		this.setSelectionMethod(selectionMethod);
		this.setCrossOverMethod(crossOverMethod);
		this.setFitnessMethod(fitnessMethod);
		this.setMutationMethod(mutationMethod);
		
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

	private int getMaxIterations() {
		return maxIterations;
	}
	private void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
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

	private ArrayList<Individual> getConvergence() {
		return convergence;
	}

	private void setConvergence(ArrayList<Individual> convergence) {
		this.convergence = convergence;
	}

	public void initializeRandomPopulation() {
		for (int i = 0; i < this.getPopulationLength(); i++) {
			Individual ind = new Individual(getMusicLengthBar());
			ind.createTrack();
			Fitness.fitness(ind, getFitnessMethod());
			getPopulation().add(ind);
		}
	}
		
	public ArrayList<Individual> runGenetic() {
		initializeRandomPopulation();
		ArrayList<Individual> offSpring = new ArrayList();
		for (int i = 0;i < getGenerations(); i++) {
			getConvergence().add(returnMaxIndividual());
			System.out.println("Gera��o  " + i);
			offSpring.add(returnMaxIndividual());
			while (offSpring.size() < getPopulationLength()) {
				Random r = new Random();
				if (r.nextFloat() < getCrossOverRate()) {
					Individual i1 = new Individual(getMusicLengthBar());
					i1.setTrack(Fitness.copyNoteSequence(Selection.selection(getPopulation(),getSelectionMethod()).getTrack()));
					Individual i2 = new Individual(getMusicLengthBar());
					i2.setTrack(Fitness.copyNoteSequence(Selection.binaryTournament(getPopulation()).getTrack()));
					Individual[] ii = CrossOver.crossOver(i1, i2, getMusicLengthBar(), getCrossOverMethod()).clone();
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
					Individual i1 = new Individual(getMusicLengthBar());
					i1.setTrack(Fitness.copyNoteSequence(Selection.binaryTournament(getPopulation()).getTrack()));
					Date d = new Date(System.currentTimeMillis());
					i1.getTrack().setName("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
					if (offSpring.size() < getPopulationLength()) {
						Fitness.fitness(i1, getFitnessMethod());
						offSpring.add(Mutation.mutation(i1, getMutationMethod()));
					}
					else
						break;
				}
			}
			setPopulation(offSpring);
			offSpring = new ArrayList();
		}
		getConvergence().add(returnMaxIndividual());
		return getPopulation();
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
		for (Individual i: getConvergence()) {
			content += String.valueOf(i.getFitness());
			content += " ";
		}
		
		Date d = new Date(System.currentTimeMillis());
	
		File file = new File("/Users/Henrique/master/workspace/StartingJMusic/Convergencia/convergence_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".dat");
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
	
	
}

