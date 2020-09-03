import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class LogExtractor {

	public static void main(String[] args) {
		
		//input from command line
		Instant from = Instant.parse(args[1]);
		Instant to = Instant.parse(args[3]);
		String path = args[5];
		
		//INPUT ERROR: if "from" timestamp occurs after "to" timestamp
		if(from.compareTo(to) > 0) {
			System.out.println("Check Your Inputs...\"From\" timestamp occurs after \"to\" timestamp!");
			return;
		}
		
		/*****Initialising variables*****/
		
		BufferedReader bfr = null;
		ReversedLinesFileReader rfr = null;
		int i = 1;
		
		//foundFile indicates whether the file containing "from" timestamp is found or not
		//foundFrom indicates whether the line containing "from" timestamp is found or not
		//done indicates whether all the lines containing "to" timestamp are printed or not
		boolean foundFile = false, done = false, foundFrom = false;
		
		//lastTime contains the timestamp of last line of each file
		Instant lastTime = null;
		String line;
		
		//curr stores the timestamp of the current line being checked
		Instant curr;
		
		//secDiff contains the duration in seconds between two timestamps
		long secDiff;
		
		//numLines is the number of lines to be skipped before next check
		long numLines = 0;
		File file;
		
		try {
			//loop till the there are no more files or required logs are printed
			while(i <= 18203 && done == false) {
				
				// filename containing 6 digits(pad with 0s)
				String fileNum = String.format("%06d", i);
				file = new File(path + "LogFile-" + fileNum + ".log");
				
				//execute until the file containing "from" timestamp is found 
				if(foundFile == false) {
					rfr = new ReversedLinesFileReader(file);
					String lastLine = rfr.readLine();
				
					// extract timestamp from last log line of current file
					lastTime = extractTime(lastLine);
					
					//if last line contains timestamp "after" the "from" timestamp 
					if(lastTime.compareTo(from) >= 0) {
						foundFile = true;
					}
					else {
						++i;
						continue;
					}
				}
				
				
				bfr = new BufferedReader(new FileReader(file));
				while ((line = bfr.readLine()) != null) {
					
					//execute this block if "from" timestamp is not found yet
					if(foundFrom == false) {
					
						if(numLines == 0){
						
							curr = extractTime(line);
							
							//finding difference between current timestamp and "from" timestamp
							secDiff = Duration.between(curr,from).getSeconds();
							
							//these many lines will be skipped before the next check
							numLines = secDiff * 100000;
							
							//if current timestamp equals "from" timestamp
							if(curr.equals(from)) {
								foundFrom = true;
								System.out.println(line);
								numLines = 0;
							}
							
						}
						else {
							--numLines;
						}
						
					}
					else {
						
						if(numLines == 0) {
							curr = extractTime(line);
							//if current line's timestamp occurs before or at the same time as "to" timestamp
							if(curr.compareTo(to) <= 0) {
								//finding difference between current timestamp and "to" timestamp
								secDiff = Duration.between(curr,to).getSeconds();
								numLines = secDiff * 100000;
							}
							else {
								done = true;
								break;
							}
						}
						else {
							--numLines;
						}
						System.out.println(line);
					
					}
				}
				
				++i;
	
		}
			
		bfr.close();
		rfr.close();
		if(foundFrom == false) {
			System.out.println("\"from\" timestamp not found in log files!");
		}
		if(done == false) {
			System.out.println("\"to\" timestamp not found in log files!");
		}
			
		}
		catch(IOException e) {
			System.out.println("I/O Exception!");
		}

	}

	//extract timestamp from the given line
	private static Instant extractTime(String string) {
		String time = string.split(",")[0];
		return Instant.parse(time);
	}

}
