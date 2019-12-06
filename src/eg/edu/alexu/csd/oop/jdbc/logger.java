package eg.edu.alexu.csd.oop.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class logger {
	private static logger instance;

	public static logger getInstance() {
		if (logger.instance == null) {
			logger.instance = new logger();
		}
		return logger.instance;
	}

	public Logger log;

	private FileHandler fh;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private logger() {
		final File f = new File("log.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (final IOException e) {
				System.out.println("Failed to create log file.");
				e.printStackTrace();
			}
		}
		try {
			fh = new FileHandler("log.txt", true);
		} catch (SecurityException | IOException e) {
			System.out.println("Failed to handle log file.");
			e.printStackTrace();
		} // Appends to log.txt file.
		log = Logger.getLogger("MainLog");
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		log.setLevel(Level.INFO);
	}

}
