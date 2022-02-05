package net.yoedtos.docman.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HtmlFrontIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlFrontIT.class);
	
	private WebClient webClient;
	
	
	@BeforeClass 
	public static void prepare() throws IOException {
  
	}
  
	@AfterClass
	public static void finish() throws IOException {
		removeFiles();
	}
	 
	
	@Before
	public void init() throws Exception {
		webClient = new WebClient();
	}
	
	@After
	public void close() throws Exception {
		webClient.close();
	}
	
	@Test
	public void test1IndexPage() throws FailingHttpStatusCodeException, IOException {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage("http://127.0.0.1:8080/docman/index.jsp");
		Assert.assertEquals("Web Document Manager", page.getTitleText());
		LOGGER.info("Index.jsp: " + page.getTitleText());
		
		page = webClient.getPage("http://127.0.0.1:8080/docman/");
		Assert.assertEquals("Web Document Manager", page.getTitleText());
		LOGGER.info("Context root: " + page.getTitleText());
		
	}
	
	@Test
	public void test2HomeToPages() throws FailingHttpStatusCodeException, IOException {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage home = webClient.getPage("http://127.0.0.1:8080/docman/index.jsp");
	
		HtmlPage search = home.getAnchorByText("Search").click();
		Assert.assertNotNull(search.getFormByName("searchForm"));
		home = search.getAnchorByText("Home").click();
		Assert.assertNotNull(home.getFormByName("uploadForm"));
		
		HtmlPage list = home.getAnchorByText("List").click();
		Assert.assertTrue(list.asXml().contains("<div id=\"dropModal\" class=\"w3-modal\">"));
		home = list.getAnchorByText("Home").click();
		Assert.assertNotNull(home.getFormByName("uploadForm"));
		
		search = list.getAnchorByText("Search").click();
		Assert.assertNotNull(search.getFormByName("searchForm"));
		list = search.getAnchorByText("List").click();
		Assert.assertTrue(list.asXml().contains("<div id=\"dropModal\" class=\"w3-modal\">"));
	}
	
	@Test(expected= FailingHttpStatusCodeException.class)
	public void test3PageNoFoundAndThrowException() throws FailingHttpStatusCodeException, IOException {
		webClient.getPage("http://127.0.0.1:8080/docman/noexist.jsp");
	}
	
	@Test(expected= FailingHttpStatusCodeException.class)
	public void test4SubmitValidationAndThrowException() throws FailingHttpStatusCodeException, IOException {
		webClient.getOptions().setThrowExceptionOnScriptError(true);
		HtmlPage page = webClient.getPage("http://127.0.0.1:8080/docman/document.jsp");
		HtmlForm uploadForm =  page.getFormByName("uploadForm");
		page = uploadForm.getInputByValue("Upload").click();
	}
	
	@Test
	public void test5UpLoadDocument() throws FailingHttpStatusCodeException, IOException {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		HtmlPage page = webClient.getPage("http://127.0.0.1:8080/docman/document.jsp");
		HtmlForm uploadForm =  page.getFormByName("uploadForm");
		
		uploadForm.getInputByName("title").setValueAttribute("Duke Logo");
		uploadForm.getInputByName("author").setValueAttribute("javaUser");
		
		HtmlSelect typeSelect = uploadForm.getSelectByName("type");
		typeSelect.setSelectedAttribute(typeSelect.getOptionByValue("IMAGE"), true);
		
	 	HtmlTextArea description = page.getElementByName("description");
	 	description.setTextContent("Logo of java mascote - Duke ");
		
	 	HtmlFileInput fileInput = uploadForm.getInputByName("file");
	 	String filePath = getClass().getClassLoader().getResource("duke-java.png").toExternalForm();
	 	fileInput.setValueAttribute(filePath);
	 	
		HtmlTextArea tags = page.getElementByName("tags");
		tags.setText("Image, Logo");
		
		page = uploadForm.getInputByValue("Upload").click();
		Assert.assertTrue(page.asText().contains("Process Success"));
	}
	
	@Test
	public void test6SearchDukeWord() throws FailingHttpStatusCodeException, IOException {
		
		HtmlPage page = webClient.getPage("http://127.0.0.1:8080/docman/search.jsp");
	
		HtmlForm searchForm =  page.getFormByName("searchForm");
		searchForm.getInputByName("keyword").setValueAttribute("duke");
		
		HtmlSelect fieldSelect = searchForm.getSelectByName("field");
		HtmlOption option = fieldSelect.getOptionByText("All");
		fieldSelect.setSelectedAttribute(option, true);
		
		page = searchForm.getInputByValue("Search").click();
		Assert.assertTrue(page.asXml().contains("<ul class=\"w3-ul w3-card-4\">"));
	}
	
	@Test
	public void test7ListPageAndDelete() throws FailingHttpStatusCodeException, IOException {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		HtmlPage home = webClient.getPage("http://127.0.0.1:8080/docman/index.jsp");
		HtmlPage list = home.getAnchorByText("List").click();
		HtmlTable listTable = list.getHtmlElementById("listTable");
		List<HtmlTableBody> bodies = listTable.getBodies();
		Assert.assertEquals(1, bodies.size());
	}
	
	private static void removeFiles() throws IOException {
		String path = System.getProperty("user.home") + "/data/docman/";
		
		File dbPath = new File(path + "db");
		File indexPath = new File(path + "lucene");
		if(dbPath.exists()) {
			FileUtils.deleteDirectory(dbPath);
		}
		
		if(indexPath.exists()) {
			FileUtils.deleteDirectory(indexPath);
		}
		LOGGER.info("Test resources clean up");
	}
}


