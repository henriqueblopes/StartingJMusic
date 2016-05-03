package JMusicTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.StringTokenizer;

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
}
