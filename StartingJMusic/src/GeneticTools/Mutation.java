package GeneticTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

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
		int pitchAux = r.nextInt(128);
		if (pitchAux != 128) 
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
	
}
