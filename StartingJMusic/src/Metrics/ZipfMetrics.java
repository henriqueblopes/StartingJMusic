package Metrics;
import Constants.Constants;
import GeneticTools.Fitness;
import GeneticTools.Individual;
import GeneticTools.Mutation;
import NoteEnconding.NoteHerremans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import jm.constants.Durations;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import NoteEnconding.Track;

public abstract class ZipfMetrics {
	
	public static double pitchMetricCalculator(Track tr) {
		return pitchMetricCalculator(tr, 128);
	}
	
	public static double fractalMetricCalculator(Track tr, int noteMin, String zipfLaw) {
		double a = pitchMetricCalculator(tr, 128);
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
		return sr.getSlope();
	}
	
	private static int countFractalProximity (ArrayList<Double> slopes, Double targetSlope) {
		int count = 0;
		for (Double d: slopes) {
			if (d > targetSlope+targetSlope*Constants.FRACTAL_PROXIMITY)
				if (d < targetSlope-targetSlope*Constants.FRACTAL_PROXIMITY)
					count++;
		}
		return count;
	}
	
	private static void recursiveFractalMetricCalculator (Track tr , HashMap<Integer, ArrayList<Double>> angMap, Integer mapPosition, String method, Integer maxHeight) {
		try {
			Class[] classes = new Class[] {Track.class};
			Method m = ZipfMetrics.class.getMethod(method, classes);					
			try {
				Object[] objs = new Object[] {tr};
				Double a = (Double) m.invoke(m, objs);
				if (angMap.get(mapPosition) == null)
					System.out.println("erro");
				angMap.get(mapPosition).add(a);
				if (mapPosition < maxHeight) {
					Track t1half = new Track("aux");
					t1half.setNoteSequence(new ArrayList(tr.getNoteSequence().subList(0, tr.getNoteSequence().size()/2)));
					Track t2half = new Track("aux");
					t2half.setNoteSequence(new ArrayList(tr.getNoteSequence().subList(tr.getNoteSequence().size()/2, tr.getNoteSequence().size())));
					recursiveFractalMetricCalculator(t1half, angMap, mapPosition+1, method, maxHeight);
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
	public static double chromaticPitchMetricCalculator(Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		ArrayList<NoteHerremans> nh = convertMod12Pitch(t.getNoteSequence()); 
		t.setNoteSequence(nh);
		return pitchMetricCalculator(t, 12);
	}
	
	//seria bom fazer mais alguns testes (fixar semente e fazer resultado na mão)
	public static double pitchDurationMetricCalculator(Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		int cP[] = countPitchDurationFrequency(t.getNoteSequence());
		return calculateLinearRegression(cP);
	}
	//se o de cima funciona, esse funciona
	public static double chromaticPitchDurationMetricCalculator(Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		ArrayList<NoteHerremans> nh = convertMod12Pitch(t.getNoteSequence());
		int cP[] = countPitchDurationFrequency(nh);
		return calculateLinearRegression(cP);
	}
	
	public static double melodicIntervalMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicIntervalFrequency(melodicIntervals, 128*2);
		return calculateLinearRegression(cP);
	}
	
	//Acredito estar funcionando perfeitamente
	public static double melodicBigramMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicNgramFrequency(melodicIntervals, 2);
		return calculateLinearRegression(cP);
	}
	
	public static double melodicTrigramMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> melodicIntervals = new ArrayList<NoteHerremans>();
		melodicIntervals = convertMelodicInterval(tr.getNoteSequence());
		int cP[] = countMelodicNgramFrequency(melodicIntervals, 3);
		return calculateLinearRegression(cP);
	}
	
	public static double durationMetricCalculator (Track tr) {
		int cP[] = countDurationFrequency(tr.getNoteSequence());
		return calculateLinearRegression(cP);
		
	}
	
	//teste pendente
	public static double pitchDistanceMetricCalculator (Track tr) {
		ArrayList<NoteForCount> nfcs = convertPitchDistance(tr);
		int cP[] = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		return calculateLinearRegression(cP);
	}
	//se pitchDistanceMetric funciona, este tb funciona.
	public static double chromaticPitchDistanceMetricCalculator (Track tr) {
		Track t = new Track("aux");
		t.setNoteSequence(Fitness.copyNoteSequence(tr).getNoteSequence());
		ArrayList<NoteHerremans> nh = convertMod12Pitch(t.getNoteSequence()); 
		t.setNoteSequence(nh);
		return pitchDistanceMetricCalculator(t);
	}
	
	public static double rhythmMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> nhs = convertRhythm(tr.getNoteSequence());
		int cP[] = countRhythmFrequency(nhs);
		return calculateLinearRegression(cP);
	}
	
	public static double rhythmIntervalMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> nhs = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmFrequency(nhs);
		return calculateLinearRegression(cP);
		
	}
	
	public static double rhythmBigramMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> rhythmIntervals = new ArrayList<NoteHerremans>();
		rhythmIntervals = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmNgramFrequency(rhythmIntervals, 2);
		return calculateLinearRegression(cP);
	}
	
	public static double rhythmTrigramMetricCalculator (Track tr) {
		ArrayList<NoteHerremans> rhythmIntervals = new ArrayList<NoteHerremans>();
		rhythmIntervals = convertRhythmInterval(convertRhythm(tr.getNoteSequence()));
		int cP[] = countRhythmNgramFrequency(rhythmIntervals, 3);
		return calculateLinearRegression(cP);
	}
	
		
	private static ArrayList<NoteForCount> convertPitchDistance (Track tr) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: tr.getNoteSequence()) {
			double distance = nh.getDuration();
			for (NoteHerremans nh2: tr.getNoteSequence().subList(tr.getNoteSequence().indexOf(nh)+1, tr.getNoteSequence().size())) {
				if(nh2.getMidiPitch() == nh.getMidiPitch()) {
					if (nh2.getTied() != 2) {
						int flag = 0;
						for (NoteForCount nfc: nfcs) {
							if(nfc.getDuration() >= distance-0.005 && nfc.getDuration() <= distance+0.005) {
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
	
	public static ArrayList<NoteHerremans> convertMod12Pitch (ArrayList<NoteHerremans> nh) {
		Iterator<NoteHerremans> i = nh.iterator();
		NoteHerremans a = i.next();
		try {
			while (a != null) {
				if (a.getMidiPitch() <0) {
					
				}
				else {
					a.setMidiPitch(a.getMidiPitch()%12);;
				}
				a = i.next();
				
			}
		}
		catch (NoSuchElementException e) {
			//System.out.println("Terminada a modulação cromática das notas.");
		}
		return nh;
		
	}
	
	private static ArrayList<NoteHerremans> convertMelodicInterval (ArrayList<NoteHerremans> nh) {
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
	
	private static ArrayList<NoteHerremans> convertRhythm (ArrayList<NoteHerremans> noteSequence) {
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
	
	private static ArrayList<NoteHerremans> convertRhythmInterval (ArrayList<NoteHerremans> rhythmSequence) {
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

	
	private static double pitchMetricCalculator(Track tr,  int length) {
		//tr.sortNoteSequence();
		
		int cP[] = countPitchFrequency(tr.getNoteSequence(), length);
		SimpleRegression sr = new SimpleRegression();
		int i = cP.length-1;
		while (cP[i] != 0) {
			sr.addData(Math.log(cP.length-i), Math.log(cP[i]));
			i--;
			if(i<0)
				break;
		}
		double a = sr.getMeanSquareError();
		a = sr.getR();
		a = sr.getSlopeStdErr();
		a = sr.getRSquare();
		return sr.getSlope();
		
	}
	
	private static  int[] countPitchFrequency(ArrayList<NoteHerremans> pitches, int length) {
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
	
	
	private static  int[] countMelodicIntervalFrequency(ArrayList<NoteHerremans> pitches, int length) {
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
	
	private static int[] countMelodicBigramFrequency (ArrayList<NoteHerremans> nhs) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		NoteHerremans previousNfc = null;
		NoteHerremans currentNfc;
		for (NoteHerremans nh: nhs) {
			 currentNfc = nh;
			 if(previousNfc != null) {
				 NoteForCount nfc = new NoteForCount(new NoteHerremans(0, 0.0));
				 nfc.getnGram().add(previousNfc);
				 nfc.getnGram().add(currentNfc);
				 insertMelodicNGram(nfcs, nfc);		 
			 }
			 previousNfc = currentNfc;
		}
		int cP[] = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		return cP;
	}
	
	private static int[] countMelodicNgramFrequency (ArrayList<NoteHerremans> nhs, int nGramOrder) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			NoteForCount nfc = new NoteForCount(new NoteHerremans(0, 0.0));
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				nfc.getnGram().add(nh2);
			}
			insertMelodicNGram(nfcs, nfc);
		}
		int cP[] = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		return cP;
	}
	
	private static int[] countDurationFrequency (ArrayList<NoteHerremans> noteSequence) {
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
	
	private static int[] countPitchDurationFrequency(ArrayList<NoteHerremans> noteSequence) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		NoteForCount nfc = new NoteForCount(new NoteHerremans(0,0.0));
		NoteHerremans nh = new NoteHerremans(0, 0.0);
		int flag = 0;
		for (NoteHerremans nhAux: noteSequence) {
			if(nhAux.getTied() == 1) {
				nfc = new NoteForCount(nhAux);
				nh = nhAux;
			}
			else {
				if(nhAux.getTied() == 2) {
					nfc.setDuration(nfc.getDuration()+nhAux.getDuration());
					nh.setDuration(nh.getDuration()+nhAux.getDuration());
				}
				if(nhAux.getTied() == 0) {
					nfc = new NoteForCount(nhAux);
					nh = nhAux;
				}
				for (NoteForCount nfcAux: nfcs) {
					if (nfcAux.getMidiPitch() == nh.getMidiPitch()) {
						if(nfcAux.getDuration() < nh.getDuration()+0.005 && nfcAux.getDuration() > nh.getDuration()-0.005) {
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
		for (NoteForCount nfcAux: nfcs) {
			cP[nfcs.indexOf(nfcAux)] = nfcAux.getCount();
		}
		Arrays.sort(cP);
		return cP;
	}
	
	private static int[] countRhythmFrequency (ArrayList<NoteHerremans> rhythmSequence) {
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
		for (NoteForCount nfcAux: nfcs) {
			cP[nfcs.indexOf(nfcAux)] = nfcAux.getCount();
		}
		Arrays.sort(cP);
		return cP;
	}
	
	private static int[] countRhythmNgramFrequency (ArrayList<NoteHerremans> nhs, int nGramOrder) {
		ArrayList<NoteForCount> nfcs = new ArrayList<NoteForCount>();
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			NoteForCount nfc = new NoteForCount(new NoteHerremans(0, 0.0));
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				nfc.getnGram().add(nh2);
			}
			insertRhythmNGram(nfcs, nfc);
		}
		int cP[] = new int[nfcs.size()];
		for (NoteForCount nfc: nfcs) {
			cP[nfcs.indexOf(nfc)] = nfc.getCount();
		}
		Arrays.sort(cP);
		return cP;
	}
	private static double calculateLinearRegression(int cP[]) {
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
		double a = sr.getMeanSquareError();
		a = sr.getR();
		a = sr.getSlopeStdErr();
		a = sr.getRSquare();
		return sr.getSlope();
		
	}
	
	private static int returnDuration (double duration) {
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
	
	private static void insertMelodicNGram (ArrayList<NoteForCount> nfcs, NoteForCount nfcIn) {
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
	
	private static void insertRhythmNGram (ArrayList<NoteForCount> nfcs, NoteForCount nfcIn) {
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
}
