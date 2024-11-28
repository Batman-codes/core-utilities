package com.testleaf.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testleaf.constants.BrowserTestEngine;
import com.testleaf.constants.BrowserTypes;
import com.testleaf.constants.LocatorType;
import com.testleaf.drivers.manager.DriverManager;
import com.testleaf.web.api.APIClient;
import com.testleaf.web.api.ResponseAPI;
import com.testleaf.web.browser.Browser;
import com.testleaf.web.element.*;

public class TestBrowserEngines {
	
	public static void main(String[] args) {
		
		// Get the browser
		Browser browser = DriverManager.getBrowser(BrowserTestEngine.PLAYWRIGHT, BrowserTypes.CHROME);
		APIClient api = (APIClient) browser;
		// load the url
		browser.navigateTo("http://leaftaps.com/opentaps");
		
		// Find the username and enter
		Edit username = browser.locateEdit(LocatorType.ID, "username");
		username.type("demosalesmanager");
		
		// Find the password and enter
		Edit password = browser.locateEdit(LocatorType.ID, "password");
		password.type("crmsfa");
		
		// Find the login button and click
		Button login = browser.locateButton(LocatorType.CLASS, "decorativeSubmit");
		login.click();

		//Click on crm/sfa link to go to homepage
		Link homeLink = browser.locateLink(LocatorType.LINKTEXT, "CRM/SFA");
		homeLink.click();

		//Click Create Lead by LinkText
		Link createLeadLink = browser.locateLink(LocatorType.LINKTEXT, "Create Lead");
		createLeadLink.click();
		
		// Print the title
		String title = browser.getTitle();
		System.out.println("The title is " + title);

		// Get the company name, first name and last name from API (test data >> Mockaroo)
		// https://api.mockaroo.com/api/5cd160c0?count=1&key=1b952600
		ResponseAPI responseAPI = api.get("https://api.mockaroo.com/api/5cd160c0?count=1&key=1b952600");
		
		System.out.println(responseAPI.getStatusCode());
		System.out.println(responseAPI.getResponseBody());

		/*
		 * "company_name":"Eazzy","first_name":"Eugine","last_name":"Slides"}
		 */
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(responseAPI.getResponseBody().toString());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonNode.get("company_name").toString() + jsonNode.get("first_name").toString());

		browser.locateEdit(LocatorType.ID, "createLeadForm_companyName").type(jsonNode.get("company_name").toString());
		browser.locateEdit(LocatorType.ID, "createLeadForm_firstName").type(jsonNode.get("first_name").toString());
		browser.locateEdit(LocatorType.ID, "createLeadForm_lastName").type(jsonNode.get("last_name").toString());
		//Select an option from the dropdown -> source
		Dropdown dropdown = browser.locateDropdown(LocatorType.ID, "createLeadForm_dataSourceId");

		dropdown.selectOptionByValue("LEAD_COLDCALL");

		browser.locateButton(LocatorType.NAME, "submitButton").click();
		String newLeadFirstName = browser.locateElement(LocatorType.ID, "viewLead_firstName_sp").getText();
		System.out.println("First Name : " + newLeadFirstName);

		// Release the browser
		browser.releaseBrowser();
		
		// Close the browser
		browser.closeBrowser();
		
		
		
	}

}
