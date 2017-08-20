package com.baselet.standalone;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class ASVTest {

	@Test
	public void testCreateFile() {
		File file = new File("C:\\Users\\Rick\\Desktop\\dummy.txt");
		boolean doesFileExist = false;
		try {
			file.createNewFile();
			doesFileExist = file.exists();
		} catch (Exception e) {
			e.printStackTrace();
			doesFileExist = false;
		}

		assertTrue(doesFileExist);

		file.delete();
	}
}