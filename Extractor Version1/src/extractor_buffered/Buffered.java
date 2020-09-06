package extractor_buffered;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class Buffered {

	public static void main(String[] args) {
		
		
		long ts = System.currentTimeMillis();
		long linesPrinted = 0;
		
		//input from command line
		String from = args[1];
		String to = args[3];
		String path = args[5];
		
		Instant fromInstant = Instant.parse(from);
		Instant toInstant = Instant.parse(to);
		
		//corner cases
		if(fromInstant.compareTo(toInstant) > 0) {
			System.out.println("Check Your Inputs...\"From\" timestamp occurs after \"to\" timestamp!");
			return;
		}
		
		//Opening directory
		BufferedReader bfr = null;
		ReversedLinesFileReader rfr = null;
		int i = 1;
		boolean flag = false, done = false, foundFrom = false;
		long linesChecked = 0;
		long searchtime = 0;
		Instant lastTime = null;
		int diff = 0;
		String line;
		Instant curr;
		long secDiff;
		long numLines = 0;
		try {
			
			while(i <= 49 && done == false) {
				
				// filename should be changed to contain 6 digits
				File file = new File(path + "LogFile-" + i + ".log");
							
				/**********************Buffered**********************************/
				bfr = new BufferedReader(new FileReader(file));
				
				if(flag == false) {
					line = bfr.readLine();
					curr = extractTime(line);
					secDiff = Duration.between(curr,fromInstant).getSeconds();
					numLines = secDiff * 100000;
				}
				
				//finding "from" timestamp 
				while((line = bfr.readLine()) != null) {
					
					if(numLines == 0) {
						++linesChecked;
						curr = extractTime(line);
						if(flag == false) {
							if(curr.compareTo(fromInstant) == 0) {
								flag = true;
								searchtime = System.currentTimeMillis();
							}
							secDiff = Duration.between(curr,fromInstant).getSeconds();
							numLines = secDiff * 100000;
						}
						else {
							if(curr.compareTo(toInstant) <= 0) {
								secDiff = Duration.between(curr,toInstant).getSeconds();
								numLines = secDiff * 100000;
							}
							else {
								done = true;
								break;
							}
						}
					}
					else {
						--numLines;
					}
					
					if(flag == true) {
						System.out.println(line);
						++linesPrinted;
					}
				}
				
				++i;
			}
			
			if(flag == false) {
				System.out.println("\"to\" timestamp not found in log files!");
			}
			bfr.close();
			System.out.println("Lines checked = " + linesChecked);
			System.out.println("Lines Printed = " + linesPrinted);
			long te = System.currentTimeMillis();
			System.out.println("time = " + (te - ts));
			System.out.println("search time = " + (searchtime - ts));
		}
		catch(IOException e) {
			System.out.println("I/O Exception!");
		}

	}

	private static Instant extractTime(String string) {
		String time = string.split(",")[0];
		return Instant.parse(time);
	}

}
