package com.baselet.control.config;

import java.awt.Font;
import java.awt.Point;
import java.io.File;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Os;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;

public class Config {

	private static Logger log = LoggerFactory.getLogger(Config.class);

	private static Config instance = new Config();

	private final String defaultFileHome = System.getProperty("user.dir");

	private String uiManager;
	private String openFileHome = defaultFileHome;
	private String saveFileHome = defaultFileHome;
	private String programVersion;

	private String lastUsedPalette = ""; // default is empty string not null because null cannot be stored as property
	private String pdfExportFont = ""; // eg in Windows: "pdf_export_font = c:/windows/fonts/msgothic.ttc,1"
	private String pdfExportFontBold = "";
	private String pdfExportFontItalic = "";
	private String pdfExportFontBoldItalic = "";
	private boolean checkForUpdates = true;
	private int printPadding = 20;
	private Point programLocation = new Point(5, 5);
	private Dimension programSize = new Dimension(960, 750);
	private int mailSplitPosition = 250;
	private int rightSplitPosition = 400;
	private int mainSplitPosition = 600;
	private boolean enableCustomElements = true;
	private boolean showGrid = false;
	private boolean startMaximized = false;
	private String defaultFontFamily = Font.SANS_SERIF;
	private Integer defaultFontsize = 14;
	private Integer propertiesPanelFontsize = 11;

	public Config() {
		if (Program.getInstance().getRuntimeType() != RuntimeType.BATCH) { // batchmode shouldn't access UIManager.class
			initUiManager();
		}
	}

	public static Config getInstance() {
		return instance;
	}

	private void initUiManager() {
		boolean isMac = SystemInfo.OS == Os.MAC;
		boolean isEclipsePlugin = Program.getInstance().getRuntimeType() == RuntimeType.ECLIPSE_PLUGIN && "com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(UIManager.getSystemLookAndFeelClassName());

		// The default MacOS theme looks ugly, therefore we set metal
		if (isMac || isEclipsePlugin) {
			uiManager = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		else {
			uiManager = UIManager.getSystemLookAndFeelClassName();
		}
	}

	public String getUiManager() {
		return uiManager;
	}

	public void setUiManager(String uiManager) {
		this.uiManager = uiManager;
	}

	public String getOpenFileHome() {
		return getFileHelper(openFileHome, defaultFileHome);
	}

	public void setOpenFileHome(String openFileHome) {
		log.trace("setting openFileHome path to: " + openFileHome);
		this.openFileHome = openFileHome;
	}

	public String getSaveFileHome() {
		return getFileHelper(saveFileHome, defaultFileHome);
	}

	private String getFileHelper(String fileLocToCheck, String defaultValue) {
		try {
			if (new File(fileLocToCheck).exists()) {
				return fileLocToCheck;
			}
		} catch (Exception e) {
			log.error("", e);
		}

		// if stored location doesn't exist or there is an exception while accessing the location, return default value
		return defaultValue;
	}

	public void setSaveFileHome(String saveFileHome) {
		log.trace("setting saveFileHome path to: " + saveFileHome);
		this.saveFileHome = saveFileHome;
	}

	public void setProgramVersion(String cfgVersion) {
		programVersion = cfgVersion;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public String getLastUsedPalette() {
		return lastUsedPalette;
	}

	public void setLastUsedPalette(String lastUsedPalette) {
		this.lastUsedPalette = lastUsedPalette;
	}

	public String getPdfExportFont() {
		return pdfExportFont;
	}

	public void setPdfExportFont(String pdfExportFont) {
		this.pdfExportFont = pdfExportFont;
	}

	public String getPdfExportFontBold() {
		return pdfExportFontBold;
	}

	public void setPdfExportFontBold(String pdfExportFontBold) {
		this.pdfExportFontBold = pdfExportFontBold;
	}

	public String getPdfExportFontItalic() {
		return pdfExportFontItalic;
	}

	public void setPdfExportFontItalic(String pdfExportFontItalic) {
		this.pdfExportFontItalic = pdfExportFontItalic;
	}

	public String getPdfExportFontBoldItalic() {
		return pdfExportFontBoldItalic;
	}

	public void setPdfExportFontBoldItalic(String pdfExportFontBoldItalic) {
		this.pdfExportFontBoldItalic = pdfExportFontBoldItalic;
	}

	public boolean isCheckForUpdates() {
		return checkForUpdates;
	}

	public void setCheckForUpdates(boolean checkForUpdates) {
		this.checkForUpdates = checkForUpdates;
	}

	public int getPrintPadding() {
		return printPadding;
	}

	public void setPrintPadding(int printPadding) {
		this.printPadding = printPadding;
	}

	public Point getProgram_location() {
		return programLocation;
	}

	public void setProgram_location(Point program_location) {
		programLocation = program_location;
	}

	public Dimension getProgram_size() {
		return programSize;
	}

	public void setProgram_size(Dimension program_size) {
		programSize = program_size;
	}

	public int getMail_split_position() {
		return mailSplitPosition;
	}

	public void setMail_split_position(int mail_split_position) {
		mailSplitPosition = mail_split_position;
	}

	public int getRight_split_position() {
		return rightSplitPosition;
	}

	public void setRight_split_position(int right_split_position) {
		rightSplitPosition = right_split_position;
	}

	public int getMain_split_position() {
		return mainSplitPosition;
	}

	public void setMain_split_position(int main_split_position) {
		mainSplitPosition = main_split_position;
	}

	public boolean isEnable_custom_elements() {
		return enableCustomElements;
	}

	public void setEnable_custom_elements(boolean enable_custom_elements) {
		enableCustomElements = enable_custom_elements;
	}

	public boolean isShow_grid() {
		return showGrid;
	}

	public void setShow_grid(boolean show_grid) {
		showGrid = show_grid;
	}

	public boolean isStart_maximized() {
		return startMaximized;
	}

	public void setStart_maximized(boolean start_maximized) {
		startMaximized = start_maximized;
	}

	public String getDefaultFontFamily() {
		return defaultFontFamily;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		this.defaultFontFamily = defaultFontFamily;
	}

	public Integer getDefaultFontsize() {
		return defaultFontsize;
	}

	public void setDefaultFontsize(Integer defaultFontsize) {
		this.defaultFontsize = defaultFontsize;
	}

	public Integer getPropertiesPanelFontsize() {
		return propertiesPanelFontsize;
	}

	public void setPropertiesPanelFontsize(Integer propertiesPanelFontsize) {
		this.propertiesPanelFontsize = propertiesPanelFontsize;
	}
}
