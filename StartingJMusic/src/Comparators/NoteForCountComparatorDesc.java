package Comparators;

import java.util.Comparator;

import Metrics.NoteForCount;

public class NoteForCountComparatorDesc implements Comparator<NoteForCount>{
	@Override
	public int compare(NoteForCount arg0, NoteForCount arg1) {
		return -(arg0.getCount() - arg1.getCount());  
	}
}

