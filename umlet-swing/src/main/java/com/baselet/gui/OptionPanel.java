package com.baselet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.diagram.DiagramHandler;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel implements ActionListener {

	private final Logger log = LoggerFactory.getLogger(OptionPanel.class);
	private static OptionPanel optionPanel;

	private final JFrame optionFrame;
	private final JCheckBox showStickingpolygon = new JCheckBox();
	private final JCheckBox showGrid = new JCheckBox();
	private final JCheckBox enableCustomElements = new JCheckBox();
	private final JCheckBox checkForUpdates = new JCheckBox();
	private final JCheckBox developerMode = new JCheckBox();
	private final JTextField pdfFont = new HintTextField("Path to font e.g.; c:/windows/fonts/msgothic.ttc,1");
	private final JTextField pdfFontBold = new HintTextField("same as above but used for bold text");
	private final JTextField pdfFontItalic = new HintTextField("same as above but used for italic text");
	private final JTextField pdfFontBoldItalic = new HintTextField("same as above but used for bold+italic text");
	private final JComboBox uiManager;
	private final JComboBox defaultFontsize = new JComboBox(new Integer[] { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 });
	private final JComboBox propertiesPanelFontsize = new JComboBox(new Integer[] { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 });
	private final JComboBox defaultFontFamily = new JComboBox(Constants.fontFamilyList.toArray(new String[Constants.fontFamilyList.size()]));

	private final Vector<String> uisTechnicalNames = new Vector<String>();

	private OptionPanel() {
		setLayout(new GridLayout(0, 2, 4, 4));
		setAlignmentX(Component.LEFT_ALIGNMENT);

		ArrayList<String> uisHumanReadableName = new ArrayList<>();
		LookAndFeelInfo[] lookAndFeelInfoArray = Constants.lookAndFeels.toArray(new LookAndFeelInfo[Constants.lookAndFeels.size()]);
		for (LookAndFeelInfo info : lookAndFeelInfoArray) {
			uisTechnicalNames.add(info.getClassName());
			uisHumanReadableName.add(info.getName());
		}
		uiManager = new JComboBox(uisHumanReadableName.toArray());

		this.add(new JLabel("Show sticking polygon"));
		this.add(showStickingpolygon);
		this.add(new JLabel("Show grid"));
		this.add(showGrid);
		this.add(new JLabel("Enable Custom Elements"));
		this.add(enableCustomElements);
		this.add(new JLabel("Check for " + Program.getInstance().getProgramName() + " updates"));
		this.add(checkForUpdates);
		if (Program.getInstance().getRuntimeType() == RuntimeType.STANDALONE) {
			this.add(new JLabel(Program.getInstance().getProgramName() + " style"));
			this.add(uiManager);
		}
		this.add(new JLabel("Default fontsize"));
		this.add(defaultFontsize);
		this.add(new JLabel("Properties panel fontsize (requires restart)"));
		this.add(propertiesPanelFontsize);
		this.add(new JLabel("Default fontfamily"));
		this.add(defaultFontFamily);
		this.add(new JLabel("Developer Mode (show extended Element Info)"));
		this.add(developerMode);
		this.add(new JLabel("Optional font to embedd in PDF - normal text"));
		this.add(pdfFont);
		this.add(new JLabel("Optional font to embedd in PDF - bold text"));
		this.add(pdfFontBold);
		this.add(new JLabel("Optional font to embedd in PDF - italic text"));
		this.add(pdfFontItalic);
		this.add(new JLabel("Optional font to embedd in PDF - bold+italic"));
		this.add(pdfFontBoldItalic);

		JButton button_ok = new JButton("Ok");
		button_ok.setActionCommand("Ok");
		button_ok.addActionListener(this);
		JButton button_cancel = new JButton("Cancel");
		button_cancel.setActionCommand("Cancel");
		button_cancel.addActionListener(this);

		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.X_AXIS));
		button_panel.add(Box.createHorizontalGlue());
		button_panel.add(button_cancel);
		button_panel.add(Box.createRigidArea(new Dimension(20, 0)));
		button_panel.add(button_ok);
		button_panel.add(Box.createHorizontalGlue());
		button_panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel parent = new JPanel();
		parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
		parent.add(Box.createRigidArea(new Dimension(10, 10)));
		parent.add(this);
		parent.add(Box.createRigidArea(new Dimension(0, 20)));
		parent.add(button_panel);
		parent.add(Box.createRigidArea(new Dimension(0, 20)));

		optionFrame = new JFrame(Program.getInstance().getProgramName() + " Options");
		optionFrame.setContentPane(parent);
		optionFrame.pack(); // autoresize of the optionframe
	}

	public static OptionPanel getInstance() {
		if (optionPanel == null) {
			optionPanel = new OptionPanel();
		}
		return optionPanel;
	}

	public void showOptionPanel() {
		showStickingpolygon.setSelected(SharedConfig.getInstance().isShow_stickingpolygon());
		showGrid.setSelected(Config.getInstance().isShow_grid());
		enableCustomElements.setSelected(Config.getInstance().isEnable_custom_elements());
		checkForUpdates.setSelected(Config.getInstance().isCheckForUpdates());
		developerMode.setSelected(SharedConfig.getInstance().isDev_mode());
		uiManager.setSelectedIndex(uisTechnicalNames.indexOf(Config.getInstance().getUiManager()));
		defaultFontsize.setSelectedItem(Config.getInstance().getDefaultFontsize());
		propertiesPanelFontsize.setSelectedItem(Config.getInstance().getPropertiesPanelFontsize());
		defaultFontFamily.setSelectedItem(Config.getInstance().getDefaultFontFamily());
		pdfFont.setText(Config.getInstance().getPdfExportFont());
		pdfFontBold.setText(Config.getInstance().getPdfExportFontBold());
		pdfFontItalic.setText(Config.getInstance().getPdfExportFontItalic());
		pdfFontBoldItalic.setText(Config.getInstance().getPdfExportFontBoldItalic());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				optionFrame.setLocationRelativeTo(CurrentGui.getInstance().getGui().getMainFrame());
				optionFrame.setVisible(true);
				optionFrame.toFront();
			}
		});
	}

	private void hideOptionPanel() {
		optionFrame.setVisible(false);
	}

	// ok or cancel button pressed
	@Override
	public void actionPerformed(ActionEvent ae) {
		hideOptionPanel();

		if ("Ok".equals(ae.getActionCommand())) {
			SharedConfig.getInstance().setShow_stickingpolygon(showStickingpolygon.isSelected());
			Config.getInstance().setShow_grid(showGrid.isSelected());
			Config.getInstance().setEnable_custom_elements(enableCustomElements.isSelected());
			Config.getInstance().setCheckForUpdates(checkForUpdates.isSelected());
			SharedConfig.getInstance().setDev_mode(developerMode.isSelected());
			Config.getInstance().setDefaultFontsize((Integer) defaultFontsize.getSelectedItem());
			Config.getInstance().setPdfExportFont(pdfFont.getText());
			Config.getInstance().setPdfExportFontBold(pdfFontBold.getText());
			Config.getInstance().setPdfExportFontItalic(pdfFontItalic.getText());
			Config.getInstance().setPdfExportFontBoldItalic(pdfFontBoldItalic.getText());

			String newui = uisTechnicalNames.get(uiManager.getSelectedIndex());
			// only set look and feel if it has changed, because it messes up frame-size
			if (newui != null && !newui.equals(Config.getInstance().getUiManager())) {
				Config.getInstance().setUiManager(newui);
				CurrentGui.getInstance().getGui().setLookAndFeel(newui, optionFrame);
			}

			// redraw every element to apply changes (like show stickingpolygon, fontsize, ...)
			for (DiagramHandler d : Main.getInstance().getDiagramsAndPalettes()) {
				d.getFontHandler().resetFontSize();
				d.getDrawPanel().updateElements();
				d.getDrawPanel().repaint();
			}
			Config.getInstance().setPropertiesPanelFontsize((Integer) propertiesPanelFontsize.getSelectedItem());

			String newfamily = (String) defaultFontFamily.getSelectedItem();
			Config.getInstance().setDefaultFontFamily(newfamily);
		}
	}
}
