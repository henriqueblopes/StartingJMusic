package Metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import NoteEnconding.NoteHerremans;

public abstract class MusicMetricsUtil {
	public static ArrayList<NoteHerremans> convertMelodicInterval (ArrayList<NoteHerremans> nh) {
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
}
