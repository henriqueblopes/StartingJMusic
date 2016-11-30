package JMusicTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

import Comparators.IndividualComparatorByObjective;
import GeneticTools.GeneticAlgorithm;
import GeneticTools.Individual;

public abstract class FileTools {
	public static void exportDatedFile (String content, String folder, String filename) {
				Date d = new Date(System.currentTimeMillis());
	
		File file = new File(folder+"/"+filename+".dat");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			StringTokenizer st = new StringTokenizer(content, "\n");
			PrintWriter gravarArq = new PrintWriter(bw);
			
			while (st.hasMoreElements()) {
				gravarArq.println(st.nextElement());
			}			
			//bw.write(content);
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		StringTokenizer st = new StringTokenizer(content, "\n");
	}
	
	public static void writeFrontToFile (ArrayList<Individual> firstFront, int n, int it) {
		//Collections.sort(firstFront, new IndividualComparatorByObjective(0));
		String s = new String();
		for (Individual i: firstFront) {
			Double a = i.fitnesses[0];
			s +=  a.toString() + " ";
			a = i.fitnesses[1];
			s +=  a.toString() + "\n";
		}
		FileTools.exportDatedFile(s, "testMultiObjFront", it+"F_"+n);
	}
	
	
}
