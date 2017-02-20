package Metrics;
import Constants.Constants;
import Constants.FitnessConstants;
import Constants.ZipfLawConstants;
import GeneticTools.Fitness;
import GeneticTools.Individual;
import GeneticTools.Mutation;
import JMusicTools.FileTools;
import NoteEnconding.NoteHerremans;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import jm.constants.Durations;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import NoteEnconding.Track;

public class ZipfMetrics implements Serializable{
	
	//private ArrayList<NoteHerremans> pitchDistances;
	private ArrayList<NoteHerremans> chromaticPitchs;
	private ArrayList<NoteHerremans> melodicIntervals;
	private ArrayList<NoteHerremans> rhythms;
	private ArrayList<NoteHerremans> rhythmIntervals;
	
	private ArrayList<NoteForCount>  countedPitchs;
	private ArrayList<NoteForCount>  countedPitchDistances;
	private ArrayList<NoteForCount>  countedPitchDurations;
	private ArrayList<NoteForCount>  countedChromaticPitchDurations;
	private ArrayList<NoteForCount>  countedChromaticPitchDistances;
	private ArrayList<NoteForCount>  countedChromaticPitchs;
	private ArrayList<NoteForCount>  countedMelodicIntervals;
	private ArrayList<NoteForCount>  countedMelodicBigrams;
	private ArrayList<NoteForCount>  countedMelodicTrigrams;
	private ArrayList<NoteForCount>  countedDurations;
	private ArrayList<NoteForCount>  countedRhythms;
	private ArrayList<NoteForCount>  countedRhythmIntervals;
	private ArrayList<NoteForCount>  countedRhythmBigrams;
	private ArrayList<NoteForCount>  countedRhythmTrigrams;
	
	private String zipfData;
	private static String zipfCountMethod = FitnessConstants.ZIPF_FITNESS;
	
	public ZipfMetrics (Track t) {
		zipfData = "";
		chromaticPitchs = convertMod12Pitch(t.getNoteSequence());
		setMelodicIntervals(convertMelodicInterval(t.getNoteSequence()));
		rhythms = convertRhythm(t.getNoteSequence());
		setRhythmIntervals(convertRhythmInterval(rhythms));
	}
	
	public void initZipfVector (Track t) {
		chromaticPitchs = convertMod12Pitch(t.getNoteSequence());
		setMelodicIntervals(convertMelodicInterval(t.getNoteSequence()));
		rhythms = convertRhythm(t.getNoteSequence());
		setRhythmIntervals(convertRhythmInterval(rhythms));
	}
		
	public double fractalMetricCalculator(Track tr, int noteMin, String zipfLaw) {
		HashMap<Integer, ArrayList<Double>> angMap = new HashMap<Integer, ArrayList<Double>>();
		int heightTree =1;
		for (int i = tr.getNoteSequence().size(); i >noteMin; i=i/2) {
			angMap.put(heightTree, new ArrayList<Double>());
			heightTree++;
		}
		recursiveFractalMetricCalculator(tr, angMap, 1, zipfLaw, heightTree-1);
		SimpleRegression sr = new SimpleRegression();
		
		heightTree = 2;
		for (int i = tr.getNoteSequence().size()/2; i >noteMin; i=i/2) {
			int frequency = countFractalProximity(angMap.get(heightTree), angMap.get(1).get(0));
			if (frequency == 0)
				sr.addData(Math.log(i), 0);
			else
				sr.addData(Math.log(i), Math.log(frequency));
			heightTree++;
		}
		if(heightTree ==2)
			return 0.0;
		if(heightTree ==3)
			return 0.0;
			//return -0.5;
		initZipfVector(tr);
		return sr.getSlope();
	}
	
	private int countFractalProximity (ArrayList<Double> slopes, Double targetSlope) {
		int count = 0;
		for (Double d: slopes) {
			if (d > targetSlope+targetSlope*Constants.FRACTAL_PROXIMITY)
				if (d < targetSlope-targetSlope*Constants.FRACTAL_PROXIMITY)
					count++;
		}
		return count;
	}
	
	private void recursiveFractalMetricCalculator (Track tr , HashMap<Integer, ArrayList<Double>> angMap, Integer mapPosition, String method, Integer maxHeight) {
		try {
			Class[] classes = new Class[] {Track.class};
			Method m = ZipfMetrics.class.getMethod(method, classes);					
			try {
				Object[] objs = new Object[] {tr};
				Double a = (Double) m.invoke(this, objs);
				if (angMap.get(mapPosition) == null)
					System.out.println("erro");
				angMap.get(mapPosition).add(a);
				if (mapPosition < maxHeight) {
					Track t1half = new Track("aux");
					t1half.setNoteSequence(new ArrayList<NoteHerremans>(tr.getNoteSequence().subList(0, tr.getNoteSequence().size()/2)));
					Track t2half = new Track("aux");
					t2half.setNoteSequence(new ArrayList<NoteHerremans>(tr.getNoteSequence().subList(tr.getNoteSequence().size()/2, tr.getNoteSequence().size())));
					initZipfVector(t1half);
					recursiveFractalMetricCalculator(t1half, angMap, mapPosition+1, method, maxHeight);
					initZipfVector(t2half);
					recursiveFractalMetricCalculator(t2half, angMap, mapPosition+1, method, maxHeight);
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public double pitchMetricCalculator(Track tr) {
		int cP[] = countPitchFrequency(tr.getNoteSequence(), 128);
		return performZipfCalculation(cP);
		//return pitchMetricCalculator(tr, 128);
	}
	
	//teste pendente
	public double pitchDistanceMetricCalculator (Track tr) {
		ArrayList<NoteForCount> nfcs = convertCountPitchDistance(tr);
		int cP[] = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		return performZipfCalculation(cP);
	}
	
	//seria bom fazer mais alguns testes (fixar semente e fazer resultado na mão)
	public double pitchDurationMetricCalculator(Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(Track.copyNoteSequence(tr).getNoteSequence());
		int cP[] = countPitchDurationFrequency(t.getNoteSequence());
		return performZipfCalculation(cP);
	}
	
	//se pitchDistanceMetric funciona, este tb funciona.
	public double chromaticPitchDistanceMetricCalculator (Track tr) {
		Track t = new Track("aux");
		//t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		t.setNoteSequence(chromaticPitchs);
		double a = pitchDistanceMetricCalculator(t);  
		return a;
	}
	//se o de cima funciona, esse funciona
	public double chromaticPitchDurationMetricCalculator(Track tr) {
		/*Track t = new Track("aux");
		//t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		t.setNoteSequence(chromaticPitchs);*/
		int cP[] = countPitchDurationFrequency(chromaticPitchs);
		return performZipfCalculation(cP);
	}
	
	public double chromaticPitchMetricCalculator(Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(chromaticPitchs);
		int cP[] = countPitchFrequency(t.getNoteSequence(), 12);
		return performZipfCalculation(cP);
	}
	
	public double melodicIntervalMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		//melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicIntervalFrequency(getMelodicIntervals(), 128*2);
		return performZipfCalculation(cP);
	}
	
	//Acredito estar funcionando perfeitamente
	public double melodicBigramMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		//melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicNgramFrequency(getMelodicIntervals(), 2);
		return performZipfCalculation(cP);
	}
	
	public double melodicTrigramMetricCalculator (Track tr) {
	//	ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		//melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicNgramFrequency(getMelodicIntervals(), 3);
		return performZipfCalculation(cP);
	}
	
	public double durationMetricCalculator (Track tr) {
		int cP[] = countDurationFrequency(tr.getNoteSequence());
		return performZipfCalculation(cP);
		
	}
	
	
	
	
	public double rhythmMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> nhs = convertRhythm(tr.getNoteSequence());
		int cP[] = countRhythmFrequency(rhythms);
		return performZipfCalculation(cP);
	}
	
	public double rhythmIntervalMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> nhs = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmFrequency(getRhythmIntervals());
		return performZipfCalculation(cP);
		
	}
	
	public double rhythmBigramMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> rhythmIntervals = new ArrayList<NoteHerremans>();
		//rhythmIntervals = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmNgramFrequency(getRhythmIntervals(), 2);
		return performZipfCalculation(cP);
	}
	
	public double rhythmTrigramMetricCalculator (Track tr) {
		//ArrayList<NoteHerremans> rhythmIntervals = new ArrayList<NoteHerremans>();
		//rhythmIntervals = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmNgramFrequency(getRhythmIntervals(), 3);
		return performZipfCalculation(cP);
	}
	
	public double pitchBigramMetricCalculator (Track tr) {
		int cP[] = countMelodicNgramFrequency(tr.getNoteSequence(), 2);
		return performZipfCalculation(cP);
	}
	
		
	private ArrayList<NoteForCount> convertCountPitchDistance (Track tr) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: tr.getNoteSequence()) {
			double distance = nh.getDuration();
			for (NoteHerremans nh2: tr.getNoteSequence().subList(tr.getNoteSequence().indexOf(nh)+1, tr.getNoteSequence().size())) {
				if(nh2.getMidiPitch() == nh.getMidiPitch()) {
					if (nh2.getTied() != 2) {
						int flag = 0;
						for (NoteForCount nfc: nfcs) {
							if(nfc.getDuration() >= distance-Constants.EPSILON_DURATION && nfc.getDuration() <= distance+0.005) {
								nfc.setCount(nfc.getCount()+1);
								flag = 1;
								break;
							}
						}
						if(flag == 0) {
							nfcs.add(new NoteForCount(new NoteHerremans(0, distance)));
							distance = 0.0;
							break;
						}
					}
				}
				distance += nh2.getDuration();
			}
		}
		return nfcs;
		
	}
	
	public ArrayList<NoteHerremans> convertMod12Pitch (ArrayList<NoteHerremans> nh) {
		ArrayList<NoteHerremans> nhConverted = new ArrayList<NoteHerremans>();
		Iterator<NoteHerremans> i = nh.iterator();
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				if (a.getMidiPitch() <0) {
					nhConverted.add(new NoteHerremans(a));
				}
				else {
					NoteHerremans aux = new NoteHerremans(a);
					aux.setMidiPitch(a.getMidiPitch()%12);;
					nhConverted.add(aux);
				}
				a = i.next();
				
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a modulação cromática das notas.");
		}
		return nhConverted;
		
	}
	
	public ArrayList<NoteHerremans> convertMelodicInterval (ArrayList<NoteHerremans> nh) {
		ArrayList<NoteHerremans> returnNh = new ArrayList<NoteHerremans>();
		Iterator<NoteHerremans> i = nh.iterator();
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				int pitcha = a.getMidiPitch();
				a = i.next();
				if (a.getMidiPitch() <0) {
					a = i.next();
					if (a == null)
						break;
				}
				returnNh.add(new NoteHerremans(a.getMidiPitch()-pitcha,0.0));
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a subtração melódica.");
		}
		return returnNh;
		
	}
	
	private ArrayList<NoteHerremans> convertRhythm (ArrayList<NoteHerremans> noteSequence) {
		ArrayList<NoteHerremans> nhs = new ArrayList<NoteHerremans>();
		NoteHerremans nhAux = new NoteHerremans(0, 0.0);
		for (NoteHerremans nh: noteSequence) {
			if (nh.getMidiPitch() == -1) {
				if (nhAux.getDuration() != 0.0) {
					nhAux.setDuration(nhAux.getDuration()+nh.getDuration());
					//nhAux.setMidiPitch(nh.getMidiPitch());
				}
			}
			else {
				if (nh.getTied() == 1 ) {
					if(nhAux.getMidiPitch() != -1)
						nhs.add(nhAux);
					nhAux = new NoteHerremans(nh);
				}
				if (nh.getTied() == 2) {
					nhAux.setDuration(nhAux.getDuration()+nh.getDuration());
				}
				if (nh.getTied() == 0) {
					if (nhAux.getDuration() != 0.0) {
						nhs.add(nhAux);
						nhAux = new NoteHerremans(nh);
					}
					else {
						nhAux = new NoteHerremans(nh);
					}
				}
			}
		}
		nhs.add(nhAux);
		return nhs;
	}
	
	private ArrayList<NoteHerremans> convertRhythmInterval (ArrayList<NoteHerremans> rhythmSequence) {
		ArrayList<NoteHerremans> returnNh = new ArrayList<NoteHerremans>();
		Iterator<NoteHerremans> i = rhythmSequence.iterator();
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				double rhythma = a.getDuration();
				a = i.next();
				returnNh.add(new NoteHerremans(0,a.getDuration()/rhythma));
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a subtração melódica.");
		}
		return returnNh;		
	}

	
	private int[] countPitchFrequency(ArrayList<NoteHerremans> pitches, int length) {
		Iterator<NoteHerremans> i = pitches.iterator();
		int countedPitches[] = new int[length+1];
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				if (a.getMidiPitch() <0) {
					countedPitches[length]++;
				}
				else {
					countedPitches[a.getMidiPitch()]++;
				}
				a = i.next();
				
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a contade de frequência de notas.");
		}
		Arrays.sort(countedPitches);
		return countedPitches;
	}
	
	
	private int[] countMelodicIntervalFrequency(ArrayList<NoteHerremans> pitches, int length) {
		Iterator<NoteHerremans> i = pitches.iterator();
		int countedPitches[] = new int[length*2];
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				//if (a.getMidiPitch()+127<0)
				countedPitches[a.getMidiPitch()+127]++;
				a = i.next();
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a contade de frequência de notas.");
		}
		Arrays.sort(countedPitches);
		return countedPitches;
	}
	
	private int[] countMelodicNgramFrequency (ArrayList<NoteHerremans> nhs, int nGramOrder) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			NoteForCount nfc = new NoteForCount(new NoteHerremans(0, 0.0));
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				nfc.getnGram().add(nh2);
			}
			insertMelodicNGram(nfcs, nfc);
		}
		int cP[] = new int[nfcs.size()];
		Collections.sort(nfcs, new NoteForCountComparator());
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		//Arrays.sort(cP);
		if(nGramOrder == 2 )
			this.countedMelodicBigrams = nfcs;
		if (nGramOrder == 3 )
			this.setCountedMelodicTrigrams(nfcs);
		return cP;
	}
	
	private int[] countDurationFrequency (ArrayList<NoteHerremans> noteSequence) {
		int[] frequencyDuration = new int[Constants.N_DURATIONS];
		
		double tied = 0;
		for (NoteHerremans nh: noteSequence) {
			if (nh.getTied()==1)
				tied = nh.getDuration();
			if(nh.getTied() ==2)
				frequencyDuration[returnDuration(nh.getDuration()+tied)]++;
			if(nh.getTied() ==0)
				frequencyDuration[returnDuration(nh.getDuration())]++;
		}
		Arrays.sort(frequencyDuration);
		return frequencyDuration;
	}
	
	private int[] countPitchDurationFrequency(ArrayList<NoteHerremans> noteSequence) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		NoteForCount nfc = new NoteForCount(new NoteHerremans(0,0.0));
		NoteHerremans nh = new NoteHerremans(0, 0.0);
		int flag = 0;
		for (NoteHerremans nhAux: noteSequence) {
			if(nhAux.getTied() == 1) {
				nfc = new NoteForCount(nhAux);
				nh = new NoteHerremans(nhAux);
			}
			else {
				if(nhAux.getTied() == 2) {
					nfc.setDuration(nfc.getDuration()+nhAux.getDuration());
					nh.setDuration(nh.getDuration()+nhAux.getDuration());
				}
				if(nhAux.getTied() == 0) {
					nfc = new NoteForCount(nhAux);
					nh = new NoteHerremans(nhAux);
				}
				for (NoteForCount nfcAux: nfcs) {
					if (nfcAux.getMidiPitch() == nh.getMidiPitch()) {
						if(nfcAux.getDuration() < nh.getDuration()+Constants.EPSILON_DURATION && nfcAux.getDuration() > nh.getDuration()-Constants.EPSILON_DURATION) {
							nfcAux.setCount(nfcAux.getCount()+1);
							flag = 1;
							break;
						}
					}
				}
				if (flag == 0) {
					nfcs.add(nfc);
				}
				flag = 0;
			}
		}
		int cP[] = new int[nfcs.size()];
		Collections.sort(nfcs, new NoteForCountComparator());
		for (NoteForCount nfcAux: nfcs) {
			cP[nfcs.indexOf(nfcAux)] = nfcAux.getCount();
		}
		this.setCountedPitchDurations(nfcs);
		//Arrays.sort(cP);
		return cP;
	}
	
	private int[] countRhythmFrequency (ArrayList<NoteHerremans> rhythmSequence) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: rhythmSequence) {
			int flag = 0;
			for (NoteForCount nfcAux: nfcs) {
				if(nfcAux.getDuration() > nh.getDuration()-0.005 && nfcAux.getDuration() < nh.getDuration()+0.005) {
					nfcAux.setCount(nfcAux.getCount()+1);
					flag =1;
					break;
				}
			}
			if (flag ==0) {
				nfcs.add(new NoteForCount(nh));
			}
		}
		int cP[] = new int[nfcs.size()];
		Collections.sort(nfcs, new NoteForCountComparator());
		for (NoteForCount nfcAux: nfcs) {
			cP[nfcs.indexOf(nfcAux)] = nfcAux.getCount();
		}
		this.countedRhythms = nfcs;
		//Arrays.sort(cP);
		return cP;
	}
	
	private int[] countRhythmNgramFrequency (ArrayList<NoteHerremans> nhs, int nGramOrder) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			NoteForCount nfc = new NoteForCount(new NoteHerremans(0, 0.0));
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				nfc.getnGram().add(nh2);
			}
			insertRhythmNGram(nfcs, nfc);
		}
		int cP[] = new int[nfcs.size()];
		Collections.sort(nfcs, new NoteForCountComparator());
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		if (nGramOrder == 2)
			this.countedRhythmBigrams = nfcs;
		if (nGramOrder == 3)
			this.setCountedRhythmTrigrams(nfcs);
		//Arrays.sort(cP);
		return cP;
	}
	private double performZipfCalculation (int cP[]) {
		if (this.getZipfCountMethod().equals(FitnessConstants.ZIPF_FITNESS))
			return calculateLinearRegression(cP);
		else if (this.getZipfCountMethod().equals(FitnessConstants.ZIPF_FITNESS_ERROR_FIT))
			return calculateErrorFit(cP);
		else if (this.getZipfCountMethod().equals(FitnessConstants.ZIPF_FITNESS_RSQUARE))
			return calculateLinearRegression(cP);
		
		return calculateLinearRegression(cP);
		
	}
	private double calculateLinearRegression(int cP[]) {
		//tr.sortNoteSequence();
		SimpleRegression sr = new SimpleRegression();
		if(cP.length == 0)
			return 0.0;
		if(cP.length == 1)
			return 0.0;
			//return -0.5;
		int i = cP.length-1;
		while (cP[i] != 0) {
			sr.addData(Math.log(cP.length-i), Math.log(cP[i]));
			i--;
			if(i<0)
				break;
		}
		
		if(Double.isNaN(sr.getRSquare())) {
			return 1.0;
		}
			
		double a = sr.getMeanSquareError();
		a = sr.getR();
		a = sr.getSlopeStdErr();
		if (getZipfCountMethod().equals(FitnessConstants.ZIPF_FITNESS_RSQUARE))
			return sr.getRSquare();
		else
			return sr.getSlope();
		
	}
	
	private double calculateErrorFit(int cP[]) {
		double a = 0.0;
		double b = -1.0;
		double xMean = 0.0;
		double yMean = 0.0;
		double errorFit = 0.0;
		
		if(cP.length == 0)
			return -10.0;
		if(cP.length == 1)
			return -10.0;
			//return -0.5;
		int i = cP.length-1;
		while (cP[i] != 0) {
			xMean += Math.log(cP.length-i);
			yMean += Math.log(cP[i]);
			i--;
			if(i<0)
				break;
		}
		xMean = xMean/(cP.length-i-1);
		yMean = yMean/(cP.length-i-1);
		
		a = yMean - b*xMean;
		
		i = cP.length-1;
		while (cP[i] != 0) {
			errorFit += Math.pow(Math.log(cP[i]) - a - b*Math.log(cP.length-i), 2);
			i--;
			if(i<0)
				break;
		}
		if (cP.length-i-1 == 0) {
			System.out.println("Erro no perform ErrorFit");
			return 1.0;
		}
		return errorFit/(cP.length-i-1);
	}
	
	private int returnDuration (double duration) {
		if (duration == Durations.SEMIBREVE)
			return 0;
		else if (duration == Durations.DOTTED_MINIM)
			return 1;
		else if (duration == Durations.DOUBLE_DOTTED_MINIM)
			return 2;
		else if (duration == Durations.MINIM)
			return 3;
		else if (duration == Durations.MINIM_TRIPLET)
			return 4;
		else if (duration == Durations.QUARTER_NOTE)
			return 5;
		else if (duration == Durations.QUARTER_NOTE_TRIPLET)
			return 6;
		else if (duration == Durations.DOTTED_QUARTER_NOTE)
			return 7;
		else if (duration == Durations.DOUBLE_DOTTED_QUARTER_NOTE)
			return 8;
		else if (duration == Durations.EIGHTH_NOTE)
			return 9;
		else if (duration == Durations.DOTTED_EIGHTH_NOTE)
			return 10;
		else if (duration == Durations.DOUBLE_DOTTED_EIGHTH_NOTE)
			return 11;
		else if (duration == Durations.EIGHTH_NOTE_TRIPLET)
			return 12;
		else if (duration == Durations.SIXTEENTH_NOTE)
			return 13;
		else if (duration == Durations.SIXTEENTH_NOTE_TRIPLET)
			return 14;
		else if (duration == Durations.DOTTED_SIXTEENTH_NOTE)
			return 15;
		else if (duration == Durations.THIRTYSECOND_NOTE)
			return 16;
		else 
			return 17;
	}
	
	private void insertMelodicNGram (ArrayList<NoteForCount> nfcs, NoteForCount nfcIn) {
		int flag = 1;
		for (NoteForCount nfc: nfcs) {
			for(NoteHerremans nh: nfc.getnGram()) {
				if(nh.getMidiPitch() != nfcIn.getnGram().get(nfc.getnGram().indexOf(nh)).getMidiPitch()) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				nfc.setCount(nfc.getCount()+1);
				return;
			}
		}
		nfcs.add(nfcIn);
		return;
	}
	
	private void insertRhythmNGram (ArrayList<NoteForCount> nfcs, NoteForCount nfcIn) {
		int flag = 1;
		for (NoteForCount nfc: nfcs) {
			for(NoteHerremans nh: nfc.getnGram()) {
				if(nh.getDuration() < nfcIn.getnGram().get(nfc.getnGram().indexOf(nh)).getDuration()-0.005
						|| nh.getDuration() > nfcIn.getnGram().get(nfc.getnGram().indexOf(nh)).getDuration()+0.005) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				nfc.setCount(nfc.getCount()+1);
				return;
			}
		}
		nfcs.add(nfcIn);
		return;
	}
	
	public void printMusic(ArrayList<NoteHerremans> nhs) {
		int i = 1;
		for (NoteHerremans nh: nhs) {
			System.out.print(nh.getMidiPitch() + "," + nh.getDuration()+ "," + nh.getTied() + " ");
			if( nh.getMeasure() !=i) {
				System.out.println();
				i++;
			}
		}
		System.out.println(); System.out.println();
	}
	
	public ArrayList<NoteForCount> getCountedMelodicTrigrams() {
		return countedMelodicTrigrams;
	}

	public void setCountedMelodicTrigrams(ArrayList<NoteForCount> countedMelodicTrigrams) {
		this.countedMelodicTrigrams = countedMelodicTrigrams;
	}

	public ArrayList<NoteForCount> getCountedRhythmTrigrams() {
		return countedRhythmTrigrams;
	}

	public void setCountedRhythmTrigrams(ArrayList<NoteForCount> countedRhythmTrigrams) {
		this.countedRhythmTrigrams = countedRhythmTrigrams;
	}

	public ArrayList<NoteForCount> getCountedPitchDurations() {
		return countedPitchDurations;
	}

	public void setCountedPitchDurations(ArrayList<NoteForCount> countedPitchDurations) {
		this.countedPitchDurations = countedPitchDurations;
	}

	public ArrayList<NoteHerremans> getMelodicIntervals() {
		return melodicIntervals;
	}

	public void setMelodicIntervals(ArrayList<NoteHerremans> melodicIntervals) {
		this.melodicIntervals = melodicIntervals;
	}

	public ArrayList<NoteHerremans> getRhythmIntervals() {
		return rhythmIntervals;
	}

	public void setRhythmIntervals(ArrayList<NoteHerremans> rhythmIntervals) {
		this.rhythmIntervals = rhythmIntervals;
	}

	private String getZipfCountMethod() {
		return zipfCountMethod;
	}

	public void setZipfCountMethod(String zipfCountMethod) {
		ZipfMetrics.zipfCountMethod = zipfCountMethod;
		if(zipfCountMethod.equals(FitnessConstants.MULTI_OBJECTIVE_FITNESS))
			ZipfMetrics.zipfCountMethod = FitnessConstants.ZIPF_FITNESS_ERROR_FIT;
	}

	/*public class NoteForCountComparator implements Comparator<NoteForCount>{
		@Override
		public int compare(NoteForCount arg0, NoteForCount arg1) {
			return arg0.getCount() - arg1.getCount();  
		}
	}*/
	
	public void writeZipfData(Track tr) {
		int[] cP = countPitchFrequency(tr.getNoteSequence(), 128);
		calcZipfString(cP);
		
		ArrayList<NoteForCount> nfcs = convertCountPitchDistance(tr);
		cP = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		calcZipfString(cP);
		
		Track t = new Track("aux");
		t.setNoteSequence(Track.copyNoteSequence(tr).getNoteSequence());
		cP = countPitchDurationFrequency(t.getNoteSequence());
		calcZipfString(cP);
		
		t.setNoteSequence(chromaticPitchs);
		nfcs = convertCountPitchDistance(t);
		cP = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		calcZipfString(cP);
		
		cP = countPitchDurationFrequency(chromaticPitchs);
		calcZipfString(cP);		
		
		t.setNoteSequence(chromaticPitchs);
		cP = countPitchFrequency(t.getNoteSequence(), 12);
		calcZipfString(cP);
		
		cP = countMelodicIntervalFrequency(getMelodicIntervals(), 128*2);
		calcZipfString(cP);
		
		cP = countMelodicNgramFrequency(getMelodicIntervals(), 2);
		calcZipfString(cP);
		
		cP = countMelodicNgramFrequency(getMelodicIntervals(), 3);
		calcZipfString(cP);
		
		cP = countDurationFrequency(tr.getNoteSequence());
		calcZipfString(cP);
		
		cP = countRhythmFrequency(rhythms);
		calcZipfString(cP);
		
		cP = countRhythmFrequency(getRhythmIntervals());
		calcZipfString(cP);
		
		cP = countRhythmNgramFrequency(getRhythmIntervals(), 2);
		calcZipfString(cP);
		
		cP = countRhythmNgramFrequency(getRhythmIntervals(), 3);
		calcZipfString(cP);
		
		FileTools.exportDatedFile(zipfData, "PlotMaxMelody", tr.getName());
		
	}
	private void calcZipfString(int[] cP) {
		if(cP.length == 0)
			return;
		int i = cP.length-1;
		while (cP[i] != 0) {
			Double a = Math.log(cP[i]);
			a.toString().substring(0, 3);
			zipfData += a.toString() + " ";
			i--;
			if(i<0)
				break;
		}
		for (int j = cP.length-1-i; j<=200; j++)
			zipfData += "-1.0 ";
		zipfData += "\n";
	}
}
