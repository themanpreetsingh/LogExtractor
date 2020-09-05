import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

public class LogCreator {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		for(int i = 1; i <= 10; ++i) {
			try {
				String numFile = String.format("%06d", i);
				File file = new File("D:\\Logs\\Zero\\" + "LogFile-" + numFile + ".log");
				if(file.createNewFile()) {
					System.out.println("created " + "D:\\Logs\\Small\\" + "LogFile-" + i + ".log");
					FileWriter fw = new FileWriter("D:\\Logs\\Zero\\" + "LogFile-" + numFile + ".log");
					Clock c = Clock.systemDefaultZone();
					for(int j = 0; j < 1000; ++j) {
						Instant in = c.instant();
						fw.write(in.toString() + ", field " + i + j + ", field " + i + j*j + "\n");
					}
					fw.close();
				}
				else {
					System.out.println("Not created");
				}
			}
			catch(IOException e){
				System.out.println("Exception");
			}
			
			
//			Clock c = Clock.systemDefaultZone();
//			Instant in = c.instant();
//			System.out.println(in.toString() + ", field " + i + ", field " + i + "\n");
		}
		long e = System.currentTimeMillis();
		System.out.println("time = " + (e - s));
	}

}
