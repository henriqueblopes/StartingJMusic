package Metrics;

import java.util.ArrayList;
import java.util.Comparator;

import NoteEnconding.NoteHerremans;

public class NoteForCount extends NoteHerremans{
	
	private int count;
	private ArrayList<NoteHerremans> nGram;
	
	public NoteForCount(NoteHerremans nh) {
		super(nh);
		setCount(1);
		setnGram(new ArrayList<NoteHerremans>());
		
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<NoteHerremans> getnGram() {
		return nGram;
	}

	public void setnGram(ArrayList<NoteHerremans> nGram) {
		this.nGram = nGram;
	}
	
	class NoteForCountComparator implements Comparator<NoteForCount>{
		@Override
		public int compare(NoteForCount arg0, NoteForCount arg1) {
			return arg0.getCount() - arg1.getCount();  
		}
	}
}
