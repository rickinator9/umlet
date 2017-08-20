package com.baselet.standalone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.ComponentSwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.uml.Class;
import com.baselet.element.interfaces.Component;
import com.baselet.element.interfaces.DrawHandlerInterface;

@RunWith(MockitoJUnitRunner.class)
public class ASVTest {

	private static final String DIRECTORY = "C:\\Users\\Rick\\Desktop\\";
	private static final String EXISTING_FILE_NAME = "rwTest.txt";

	@Mock
	DrawHandlerInterface drawHandler;

	@Test
	public void testCreateFileFR1() {
		String fileName = "dummy.txt";
		File file = new File(DIRECTORY + fileName);
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

	@Test
	public void testZoomLevelFR2() {
		// Set zoom level.
		float zoom = 1.5f;
		int gridSize = (int) (SharedConstants.DEFAULT_GRID_SIZE * zoom);

		// Initialise rectangle.
		int x = 0;
		int y = 0;
		int width = 100;
		int height = 100;
		Rectangle rect = new Rectangle(x, y, width, height);

		// Mock behavior of drawHandler.
		when(drawHandler.getGridSize()).thenReturn(gridSize);

		// Create the grid element.
		NewGridElement element = new Class();
		Component component = new ComponentSwing(element);
		element.init(rect, "", "", component, drawHandler);

		// Scale manually.
		int zoomedX = zoom(x);
		int zoomedY = zoom(y);
		int zoomedWidth = zoom(width);
		int zoomedHeight = zoom(height);

		// Set up the rectangles to compare.
		Rectangle zoomedRectangle = new Rectangle(zoomedX, zoomedY, zoomedWidth, zoomedHeight);
		Rectangle realRectangle = element.getRealRectangle();

		// Test if they are equal.
		assertEquals(zoomedRectangle, realRectangle);
	}

	private int zoom(int value) {
		return value * SharedConstants.DEFAULT_GRID_SIZE / drawHandler.getGridSize();
	}

	@Test
	public void testReadWriteFR3() {
		String text = "test";
		File file = new File(DIRECTORY + EXISTING_FILE_NAME);

		// Write some text.
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(text);
		} catch (Exception e) {
			assertTrue(false);
		}

		// Read some text.
		String fileContents = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			fileContents = sb.toString();
		} catch (Exception e) {
			assertTrue(false);
		}

		assertEquals(text, fileContents);
	}

	@Test
	public void testFileExistsFR4() {
		File file = new File(DIRECTORY + EXISTING_FILE_NAME);
		boolean doesFileExist = file.exists();

		assertTrue(doesFileExist);
	}
}