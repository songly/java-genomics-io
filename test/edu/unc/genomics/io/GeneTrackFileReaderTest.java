package edu.unc.genomics.io;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.unc.genomics.Interval;

public class GeneTrackFileReaderTest extends AbstractIntervalFileReaderTest {

	public static final Path TEST_GENETRACK = Paths.get("test/fixtures/test.genetrack");

	@Before
	public void setUp() throws Exception {
		test = new GeneTrackFileReader(TEST_GENETRACK);
	}

	@Test
	public void testCount() {
		assertEquals(10, test.count());
	}

	@Test
	public void testChromosomes() {
		assertTrue(test.chromosomes().contains("chrI"));
		assertTrue(test.chromosomes().contains("chrII"));
		assertTrue(test.chromosomes().contains("chrIII"));
		assertTrue(test.chromosomes().contains("chrIV"));
		assertEquals(4, test.chromosomes().size());
	}

	@Test
	public void testQuery() {
		Iterator<? extends Interval> it = test.query("chrI", 10, 20);
		int count = 0;
		while (it.hasNext()) {
			it.next();
			count++;
		}
		assertEquals(2, count);
	}

	@Test
	public void testIterator() {
		int count = 0;
		for (Interval entry : test) {
			count++;
		}
		assertEquals(10, count);
	}

	@Test
	public void testLoadAll() {
		List<? extends Interval> all = test.loadAll();
		assertEquals(10, all.size());
	}

}
