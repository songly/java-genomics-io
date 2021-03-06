package edu.unc.genomics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Miscellaneous low-key file utilities
 * such as guessing whether a file is ASCII text, counting the number of lines in a file,
 * or sorting a file analogous to the UNIX "sort" command
 * 
 * @author timpalpant
 * 
 */
public class FileUtils {
	
	private static final Logger log = Logger.getLogger(FileUtils.class);
	public static final int DEFAULT_BLOCK_SIZE = 4096;

	/**
	 * Guess whether a file is ASCII text format, using a default threshold where the number
	 * of non-ASCII characters found must be < 30%
	 * @param p the file to determine if it is ASCII
	 * @return true if the file has > 70% ASCII characters, false otherwise
	 * @throws IOException
	 */
	public static boolean isAsciiText(Path p) throws IOException {
		char[] buf = new char[DEFAULT_BLOCK_SIZE];
		try (BufferedReader reader = Files.newBufferedReader(p, Charset.forName("US-ASCII"))) {
			reader.read(buf);
		} catch (MalformedInputException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Return the number of lines in an ASCII file, analogous to the UNIX "wc -l" command
	 * @param p the file to count lines
	 * @return the number of lines in p
	 * @throws IOException if an IOException occurs while reading from p
	 */
	public static int countLines(Path p) throws IOException {
		int count = 0;
		try (BufferedReader reader = Files.newBufferedReader(p, Charset.defaultCharset())) {
			String line;
			while ((line = reader.readLine()) != null) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Sorts a file using an external merge-sort like the UNIX "sort" command
	 * Sorts lines based on the provided comparator
	 * @param input the file to sort
	 * @param output the output file (sorted)
	 * @param cmp compare two lines from the file according to this Comparator
	 * @throws IOException
	 */
	public static void sort(final Path input, final Path output, final Comparator<String> cmp) throws IOException {
		// Use an external sort to sort the file in chunks
		log.debug("Sorting file "+input+" to "+output);
		List<Path> pieces = ExternalSort.sortInBatch(input, cmp);
		
		// Merge the sorted chunks together
		log.debug("Merging sorted chunks");
		ExternalSort.mergeSortedFiles(pieces, output, cmp);
	}

}
