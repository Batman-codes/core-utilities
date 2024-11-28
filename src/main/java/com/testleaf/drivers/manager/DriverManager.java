package com.testleaf.drivers.manager;

import com.testleaf.constants.BrowserTestEngine;
import com.testleaf.constants.BrowserTypes;
import com.testleaf.web.browser.Browser;
import com.testleaf.web.browser.PwBrowserImpl;
import com.testleaf.web.browser.SeBrowserRAAPIImpl;

public class DriverManager {

	private static Browser browser;
	
	public static Browser getBrowser(BrowserTestEngine browserEngine, BrowserTypes browserType) {
		
		if(browser == null) {
			browser = setupBrowser(browserEngine, browserType);
		}
		return browser;
	}

	private static Browser setupBrowser(BrowserTestEngine browserEngine, BrowserTypes browserType) {
		
		switch (browserEngine) {
		case SELENIUM: {
			return new SeBrowserRAAPIImpl(browserType);
		}
		case PLAYWRIGHT: {
			return new PwBrowserImpl(browserType);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + browserEngine);
		}
	}
}