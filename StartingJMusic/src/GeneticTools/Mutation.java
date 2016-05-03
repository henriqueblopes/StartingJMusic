package GeneticTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.analysis.function.Constant;

import Constants.Constants;
import Metrics.NoteForCount;
import NoteEnconding.NoteHerremans;

public class Mutation {
	public static Individual mutation(Individual i, String method) {
		try {
			Class[] classes = new Class[] {Individual.class};
			Method m = Mutation.class.getMethod(method, classes);			
			Object[] objs = new Object[] {i};
			
			try {
				return (Individual) m.invoke(m, objs);
				
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Individual changeOneNote(Individual i) {
		Random r = new Random();
		int a = r.nextInt(i.getTrack().getNoteSequence().size());
		int pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(a).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(a).setMidiPitch(-1);
		
		if (i.getTrack().getNoteSequence().get(a).getTied() == 1) {
			i.getTrack().getNoteSequence().get(a).setTied(0);
			i.getTrack().getNoteSequence().get(a+1).setTied(0);
		}
		else if (i.getTrack().getNoteSequence().get(a).getTied() == 2) {
			i.getTrack().getNoteSequence().get(a).setTied(0);
			i.getTrack().getNoteSequence().get(a-1).setTied(0);
		}
		
		a = r.nextInt(i.getTrack().getNoteSequence().size()-2)+1;
		if(i.getTrack().getNoteSequence().get(a-1).getMeasure() == i.getTrack().getNoteSequence().get(a).getMeasure()) {
			i.getTrack().getNoteSequence().get(a-1).setDuration(i.getTrack().getNoteSequence().get(a-1).getDuration() 
					+ i.getTrack().getNoteSequence().get(a).getDuration()/2);
			i.getTrack().getNoteSequence().get(a).setDuration(i.getTrack().getNoteSequence().get(a).getDuration()/2);
		}
		else if (i.getTrack().getNoteSequence().get(a+1).getMeasure() == i.getTrack().getNoteSequence().get(a).getMeasure()) {
			i.getTrack().getNoteSequence().get(a+1).setDuration(i.getTrack().getNoteSequence().get(a+1).getDuration() 
					+ i.getTrack().getNoteSequence().get(a).getDuration()/2);
			i.getTrack().getNoteSequence().get(a).setDuration(i.getTrack().getNoteSequence().get(a).getDuration()/2);
		}
		return i;
	}
	
	public static Individual mutateMelodicTrigram (Individual i) {
		double targetSlope = -0.62;
		int nGramOrder = 3;
		Random r = new Random();
		int a = 0;
		if (i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack()) > targetSlope) {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2)+i.getZipfMetrics().getCountedMelodicTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2);
		}
		else {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2);
			else
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2)+i.getZipfMetrics().getCountedMelodicTrigrams().size()/2;
		}
			
		int b = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().get(a).getCount())+1;
		int c = 1;
		
		NoteForCount trigram = i.getZipfMetrics().getCountedMelodicTrigrams().get(a);
		
		ArrayList<NoteHerremans> nhs = i.getZipfMetrics().getMelodicIntervals();
		int flag = 1;
		int positionTrigram = 0;
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				if(nh2.getMidiPitch() != trigram.getnGram().get(
						nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder).indexOf(nh2)).getMidiPitch()) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				if (c < b) {
					c++;
					flag = 0;
				}
				else {
					positionTrigram = nhs.indexOf(nh);
					break;
				}
			
			}
			
		}
		int pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		positionTrigram++;
		pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		return i;
	}
	
	public static Individual mutateRhythmTrigram (Individual i) {
		double targetSlope = -1.15;
		int nGramOrder = 3;
		Random r = new Random();
		int a = 0;
		if (i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack()) > targetSlope) {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2)+i.getZipfMetrics().getCountedRhythmTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2);
		}
		else {
			if (r.nextInt(10) > 8)
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2)+i.getZipfMetrics().getCountedRhythmTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2);
		}
		int b = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().get(a).getCount())+1;
		
		int c = 1;
		
		NoteForCount trigram = i.getZipfMetrics().getCountedRhythmTrigrams().get(a);
		
		ArrayList<NoteHerremans> nhs = i.getZipfMetrics().getRhythmIntervals();
		int flag = 1;
		int positionTrigram = 0;
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				double durationCompared = trigram.getnGram().get(
						nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder).indexOf(nh2)).getDuration();
				if(nh2.getDuration() < durationCompared - Constants.EPSILON_DURATION || 
						nh2.getDuration() > durationCompared + Constants.EPSILON_DURATION) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				if (c < b) {
					c++;
					flag = 0;
				}
				else {
					positionTrigram = nhs.indexOf(nh);
					break;
				}
			}
		}
		i.getTrack().getNoteSequence().get(positionTrigram+1).setDuration(i.randomDuration());
		i.getTrack().getNoteSequence().get(positionTrigram+2).setDuration(i.randomDuration());
		return i;
	}
	
	public static Individual mutateMelodicAndRhythmTrigram (Individual i) {
		mutateMelodicTrigram(i);
		mutateRhythmTrigram(i);
		return i;
	}
	
}
