package com.baselet.control.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunningFileChecker extends TimerTask {

	private static Logger log = LoggerFactory.getLogger(RunningFileChecker.class);

	private final File file;
	private final CanOpenDiagram canOpenDiagram;

	public RunningFileChecker(File file, CanOpenDiagram canOpenDiagram) {
		this.canOpenDiagram = canOpenDiagram;
		this.file = file;
	}

	@Override
	public void run() {
		Path.safeCreateFile(file, false);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String filename = reader.readLine();
			if (filename != null) {
				Path.safeDeleteFile(file, false);
				Path.safeCreateFile(file, true);
				canOpenDiagram.doOpen(filename);
			}
		} catch (Exception ex) {
			log.info("", ex);
		}
	}

}
