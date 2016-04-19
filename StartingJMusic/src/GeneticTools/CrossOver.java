package GeneticTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public abstract class CrossOver {
	public static Individual[] crossOver(Individual i1, Individual i2, int musicLengthBar, String method) {
		//Class<?> parameterTypes;
		try {
			Class[] classes = new Class[] {Individual.class, Individual.class, int.class};
			Method m = CrossOver.class.getMethod(method, classes);			
			Object[] objs = new Object[] {i1, i2, musicLengthBar};
			
			try {
				return (Individual[]) m.invoke(m, objs);
				
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
	public static Individual[] crossOverBar(Individual i1, Individual i2, int musicLengthBar) {
		Individual[] crossedOver = new Individual[2];
		Random r = new Random();
		int a = r.nextInt(musicLengthBar-1);
		a += 1;
		
		ArrayList<NoteHerremans> nh1 = new ArrayList();
		ArrayList<NoteHerremans> nh2 = new ArrayList();
		
		int i = 0;
		for (NoteHerremans nh: i1.getTrack().getNoteSequence()) {
			if (nh.getMeasure() == a+1)	break;
			i++;
		}
		
		int j = 0;
		for (NoteHerremans nh: i2.getTrack().getNoteSequence()) {
			if (nh.getMeasure() == a+1) break;
			j++;
		}
		
		crossIndividual(i1, i2, i, j, nh1, nh2);
		crossedOver[0] = new Individual(musicLengthBar, Constants.Constants.BAR);
		Date d = new Date(System.currentTimeMillis());
		crossedOver[0].setTrack(new Track("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid"));
		crossedOver[0].getTrack().setNoteSequence((new ArrayList<NoteHerremans>(nh1)));
		crossedOver[1] = new Individual(musicLengthBar, Constants.Constants.BAR);
		crossedOver[1].setTrack(new Track("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid"));
		crossedOver[1].getTrack().setNoteSequence((new ArrayList<NoteHerremans>(nh2)));
		return crossedOver;
	}
	
	private static void crossIndividual(Individual i1, Individual i2, int i, int j, ArrayList <NoteHerremans> nh1, 
			ArrayList <NoteHerremans> nh2) {
		for (int k = 0; k<i; k++) {
			nh1.add(i1.getTrack().getNoteSequence().get(k));
		}
		for (int k = j; k<i2.getTrack().getNoteSequence().size(); k++) {
			nh1.add(i2.getTrack().getNoteSequence().get(k));
		}
		
		for (int k = 0; k<j; k++) {
			nh2.add(i2.getTrack().getNoteSequence().get(k));
		}
		for (int k = i; k<i1.getTrack().getNoteSequence().size(); k++) {
			nh2.add(i1.getTrack().getNoteSequence().get(k));
		}
		
		if (nh1.get(i-1).getTied() == 1) {
			nh1.get(i-1).setTied(0);
			nh1.get(i).setTied(0);
		}		
		if (nh2.get(j-1).getTied() == 1) {
			nh2.get(j-1).setTied(0);
			nh2.get(j).setTied(0);
		}
		
	}
	
	public static Individual[] crossOverNote(Individual i1, Individual i2, int musicLength) {
		Random r = new Random();
		int a = r.nextInt(musicLength-1)+1;
		ArrayList<NoteHerremans> nh1 = new ArrayList();
		ArrayList<NoteHerremans> nh2 = new ArrayList();
		crossIndividual(i1, i2, a, a, nh1, nh2);
		Individual[] crossedOver = new Individual[2];
		
		Date d = new Date(System.currentTimeMillis());
		Track aux = new Track("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
		aux.setNoteSequence((new ArrayList<NoteHerremans>(nh1)));
		crossedOver[0] = new Individual(aux, Constants.Constants.NOTE);
		
		aux = new Track("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
		aux.setNoteSequence((new ArrayList<NoteHerremans>(nh2)));
		crossedOver[1] = new Individual(aux, Constants.Constants.NOTE);
		return crossedOver;
	}
}
