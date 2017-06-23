package com.baselet.gui.listener;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.gui.BrowserLauncher;

public class HyperLinkActiveListener implements HyperlinkListener {

	private static Logger log = LoggerFactory.getLogger(HyperLinkActiveListener.class);

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				BrowserLauncher.openURL(e.getURL().toString());
			} catch (Exception ex) {
				log.info("", ex);
			}
		}
	}
}
