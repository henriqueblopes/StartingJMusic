package Metrics;

import java.util.Comparator;

public class NoteForCountComparator implements Comparator<NoteForCount>{
	@Override
	public int compare(NoteForCount arg0, NoteForCount arg1) {
		return arg0.getCount() - arg1.getCount();  
	}
}
