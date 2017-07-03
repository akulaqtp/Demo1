package com.ithr;

import java.util.Date;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/*---------------------------------------------------------------------------------------------------------------------------
 * Class Name  : UIOperation
 * Description : This class performs the various operations on the GUI objects on the screen
 * Author      : Viswa 
 -----------------------------------------------------------------------------------------------------------------------------*/

public class UIOperation {

    WebDriver driver;
    String Parent_Window, Parent_Window1;
    String strActualText;
    Integer num;
    public String EmpID = null;
    public String EmpName = null;
    public Row TestDataRow;
    public String strRequestInitiatedBy = "hrtstoc";
    public String strInventionID, strInventionId;
    public static String strDocketNumber = "";
    public static String BaseURL = null;
    public static String MSJUserName = "hrtstoc";
      String MSJPassword = "Tmp659r5YzLKXZtIxd1/og==";

    private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41,
	    0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };

    String FilePath, NewHire_EmployeeFileName, NewHire_Employee_SheetName,
	    NewHire_Default_FileName, NewHire_Default_SheetName,
	    FilePathOutput, NewHire_UpdateDetails_FileName;
    String NewHire_QCData_FileName, NewHire_QCData_SheetName,
	    NewHire_QCOffer_FileName, NewHire_QCOffer_SheetName, Path;

    // Constructor for UIOperation class
    public UIOperation(WebDriver driver) {
	this.driver = driver;
    }

    // UIOperation method is used for performing various genreal operation on
    // the GUI Objects in the Screen
    public void perform(Properties p, String operation, String objectName,
	    String objectType, String value) throws NoSuchAlgorithmException,
	    NoSuchProviderException, NoSuchPaddingException,
	    InvalidKeyException, InvalidAlgorithmParameterException,
	    IllegalBlockSizeException, BadPaddingException, IOException,
	    Exception {
	// Load the Properties file
	com.ithr.ReadObjects PropFile = new com.ithr.ReadObjects();
	Properties ObjProperties = PropFile.getObjectRepository();
	FilePath = ObjProperties.getProperty("FilePath");
	FilePathOutput = ObjProperties.getProperty("FilePathOutput");
	BaseURL = PropFile.getPropertyValues();

	// To Perform Click
	if (operation.toUpperCase().equals("CLICK")) {
	    // Perform click
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .click();
	    Thread.sleep(3000);
	}
	// To go the the Given URL
	else if (operation.toUpperCase().equals("GOTOURL")) {
	    String URL = BaseURL + value;
	    System.out.println("URL : " + URL);
	    driver.get(URL);
	    driver.manage().window().maximize();
	}
	// Login to the Application
	else if (operation.toUpperCase().equals("LOGIN")) {
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	    final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	    final String decryptedString = new String(cipher.doFinal(Base64
		    .decodeBase64(MSJPassword)));
	    driver.findElement(By.id("USER")).clear();
	    driver.findElement(By.id("USER")).sendKeys(MSJUserName);
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys(decryptedString);
	    driver.findElement(By.xpath("//input[@value='Login']")).click();
	    Thread.sleep(3000);
	}
	// Navigate to the given URL
	else if (operation.toUpperCase().equals("NAVIGATE_TO_URL")) {
	    String URL = BaseURL + value;
	    System.out.println(" Base URL " + BaseURL);
	    System.out.println("  URL " + value);
	    System.out.println("  URL1 " + URL);
	    driver.navigate().to(URL);
	}
	// CLEAR TEXT IN TEXT FIELD
	else if (operation.toUpperCase().equals("CLEARTEXT")) {
	    // Perform click
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    Thread.sleep(1000);
	}
	// Verify Object Existance
	else if (operation.toUpperCase().equals("VERIFY_OBJECT_EXIST")) {
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .isDisplayed();
	    if (true) {
		driver.findElement(this.getObject(p, objectName, objectType))
			.click();
	    }
	    Thread.sleep(1000);
	}
	// To click on a link and then close the page
	else if (operation.toUpperCase().equals("CLICK_A_LINK_CLOSE_A_PAGE")) {
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .click();
	    Thread.sleep(10000);
	}
	// To Set Dynamic Text by appending current date to the value by
	// clearing existing Value
	else if (operation.toUpperCase().equals("SETDYNAMICTEXT")) {
	    // Clear Data in a text field
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    // Get Current Date and input in the Date Field
	    DateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmss");
	    Date date = new Date();
	    String CurrentDate = dateFormat.format(date);
	    String textToBeSet = value + CurrentDate;
	    // Set text on control
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(textToBeSet);
	    Thread.sleep(3000);
	    System.out.println("Value " + value);
	}
	// To Set Dynamic Email by appending current date to the First Name and
	// making email with first name
	else if (operation.toUpperCase().equals("SETDYNAMICEMAIL")) {
	    // Clear Data in a text field
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    // Get Current Date and input in the Date Field
	    DateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmss");
	    Date date = new Date();
	    String CurrentDate = dateFormat.format(date);
	    String emailToBeSet = value + CurrentDate + "@gmail.com";
	    // Set text on control
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(emailToBeSet);
	    Thread.sleep(3000);
	}
	// verify the presence of element
	else if (operation.toUpperCase().equals("VERIFY_PRESENCE_OF_ELEMENT")) {

	    driver.findElement(this.getObject(p, objectName, objectType))
		    .isDisplayed();
	}

	// To Perform Click and Wait Long Time
	else if (operation.toUpperCase().equals("HANDLE_DIRECT_REPORTEES")) {
	    // Perform click
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .click();
	    Thread.sleep(30000);
	}
	// To Perform Click and Wait Long Time
	else if (operation.toUpperCase().equals("CLICK_WITH_LONGWAIT")) {
	    // Perform click
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .click();
	    Thread.sleep(30000);
	}
	// click an element based on Index
	else if (operation.toUpperCase().equals("CLICK_BY_INDEX")) {
	    Thread.sleep(2000);

	    List<WebElement> stateList = driver.findElements(this.getObject(p,
		    objectName, objectType));
	    int index = Integer.parseInt(value);
	    stateList.get(index).click();
	}
	// Select a Value from the State ListBox
	else if (operation.toUpperCase().equals("SELECT_STATE_HRSITE")) {
	    Thread.sleep(2000);
	    driver.findElement(By.xpath(".//*[@id='newStateDD']")).click();
	    Thread.sleep(2000);
	    System.out.println("value" + value);
	    driver.findElement(
		    By.xpath("html/body/div[7]/div/div[" + value + "]"))
		    .click();
	    System.out.println("value" + value);
	}
	// To Check an Object Existence
	if (operation.toUpperCase().equals("WAIT_FOR_EVENT_COMPLETE")) {
	    String successMsg = driver.findElement(
		    this.getObject(p, objectName, objectType)).getText();
	    while (!(successMsg.contains(value))) {
		Thread.sleep(50000);
		if (successMsg.contains(value)) {
		    break;
		}
	    }
	}
	// To Set Text in the Text Box By clearing existing Value
	else if (operation.toUpperCase().equals("SETTEXT")) {
	    // Clear Data in a text field
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    // Set text on control
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(value);
	    Thread.sleep(3000);
	    System.out.println("Value " + value);
	}
	// To Set the Date in the Text Box By clearing existing Value
	else if (operation.toUpperCase().equals("SET_CURRENTDATE")) {
	    // Clear Data in a text field
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    // Get Current Date and input in the Date Field
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = new Date();
	    String CurrentDate = dateFormat.format(date);
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(CurrentDate);
	    Thread.sleep(3000);
	}
	// To Set EMPLID in Bo-HRMS-Workforce-Employeement Screen
	else if (operation.toUpperCase().equals("SETEMPLID_EMPLOYEEMENT")) {
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(EmpID);
	    // Thread.sleep(100);
	}
	// To Set Text by Handling Dialog
	else if (operation.toUpperCase().equals("SETTEXT_DIALOG_HANDLE")) {
	    // Clear Data in a text field
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .clear();
	    Thread.sleep(100);
	    driver.switchTo().alert().accept(); // Handle Dialog
	    Thread.sleep(3000);
	    // Set text on control
	    System.out.println("Value " + value);
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(value);
	    System.out.println("Value " + value);
	    Thread.sleep(3000);
	}
	// To set Text Without Clearing the Text Field value
	else if (operation.toUpperCase().equals("SETTEXTWITHOUTCLEAR")) {
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(value);
	    Thread.sleep(3000);
	}
	// UnCheck the Checkbox if checked
	else if (operation.toUpperCase().equals("UNCHECK_CHECKBOX_IF_CHECKED")) {
	    boolean isSelect = driver.findElement(
		    this.getObject(p, objectName, objectType)).isSelected();
	    if (isSelect == true) {
		driver.findElement(this.getObject(p, objectName, objectType))
			.click();
	    }
	}

	// To Click on a specific WebTable Row
	else if (operation.toUpperCase().equals("CLICK_A_ROW_IN_WEBTABLE")) {
	    // Click on a specific WebTable Row
	    for (int i = 2; i <= 10; i++) {
		String Cell_Value = driver.findElement(
			By.xpath(".//*[@id='gridview-1029']/table/tbody/tr["
				+ i + "]/td[3]/div")).getText();
		if (!Cell_Value.contains("Intern")) {
		    String WebTableCellValue = driver
			    .findElement(
				    By.xpath(".//*[@id='gridview-1029']/table/tbody/tr["
					    + i + "]/td[1]/div")).getText();
		    driver.findElement(By.linkText(WebTableCellValue)).click();
		    // Thread.sleep(5000);
		    break;
		}
	    }
	}

	// To Select the Candidate in the New Hire Prcocess2 Page (New Hrie
	// ->Process2 Search (Search the Candidate based on the Candidate Name)
	else if (operation.toUpperCase().equals("NEW_HIRE_PROCESS2")) {
	    Thread.sleep(3500);
	    System.out.println("Value :" + value);
	    // Search the Candidate in NewHire->Process2 Page
	    Actions builder2 = new Actions(driver);
	    // Highlighting the Name column
	    builder2.moveToElement(
		    driver.findElement(By
			    .xpath("html/body/div[5]/div[5]/div/div[2]/div/div[2]/div/div/div[3]/div")))
		    .perform();
	    Thread.sleep(2000);
	    // Clicking on Name Filter
	    driver.findElement(
		    By.xpath("html/body/div[5]/div[5]/div/div[2]/div/div[2]/div/div/div[3]/div/div"))
		    .click();
	    Thread.sleep(1000);
	    Actions builder = new Actions(driver);
	    System.out.println("In side the New Hire Process2");
	    // Highlighting Filters
	    builder.moveToElement(
		    driver.findElement(By
			    .xpath("html/body/div[11]/div/div[2]/div[2]/div[4]/a/span")))
		    .perform();
	    // builder.moveToElement(driver.findElement(By.xpath(".//*[@id='menucheckitem-1077-textEl']"))).perform();
	    Thread.sleep(1000);
	    // Clearing the text in text fields
	    driver.findElement(
		    By.xpath("html/body/div[13]/div/div[2]/div/table/tbody/tr/td[2]/input"))
		    .clear();
	    System.out.println("value : " + value);
	    driver.findElement(
		    By.xpath("html/body/div[13]/div/div[2]/div/table/tbody/tr/td[2]/input"))
		    .sendKeys(value);
	}

	// To Select the Job code
	else if (operation.toUpperCase().equals("SELECT_JOBCODE")) {
	    driver.findElement(By.xpath(".//*[@id='jobCodeSearchCont']/a/img"))
		    .click();
	    // Thread.sleep(5000);
	    driver.findElement(By.linkText("A")).click();
	    // Thread.sleep(3000);
	    driver.findElement(By.linkText("AAP/EEO Specialist")).click();
	    // Thread.sleep(3000);
	}
	// To Get Text
	else if (operation.toUpperCase().equals("GETTEXT")) {
	    // Get text of an element
	    String Text = driver.findElement(
		    this.getObject(p, objectName, objectType)).getText();
	    System.out.println(" Text : " + Text);
	}

	// Check If CheckBox is not Selected
	else if (operation.toUpperCase().equals("VERIFY_CHECK_CHECKBOX")) {
	    boolean isSelect1 = driver.findElement(
		    By.xpath(".//*[@id='sameAddressId']")).isSelected();
	    if (isSelect1 == false) {
		driver.findElement(By.xpath(".//*[@id='sameAddressId']"))
			.click();
	    }

	}

	// To WAIT for 10 seconds
	else if (operation.toUpperCase().equals("WAIT")) {
	    Thread.sleep(15000);
	}// LONG WAIT
	 // To WAIT for 10 seconds
	else if (operation.toUpperCase().equals("LONGWAIT")) {
	    Thread.sleep(35000);
	}
	// FileUpload
	else if (operation.toUpperCase().equals("FILEUPLOAD")) {
	    // Get text of an element
	    File f = new File(FilePathOutput + "/" + value);
	    Path = f.getAbsolutePath();
	    // System.out.println(Path);
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(Path);
	}
	// FileUpload--.doc files
	else if (operation.toUpperCase().equals("FILEUPLOADFORDOCFILES")) {
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .click();
	    // Get text of an element
	    File f = new File(FilePathOutput + "/" + value + ".doc");
	    Path = f.getAbsolutePath();
	    driver.findElement(this.getObject(p, objectName, objectType))
		    .sendKeys(Path);
	}

	// To Get Text
	else if (operation.toUpperCase().equals("GETTEXT_EMPID")) {
	    // Get text of an element
	    EmpID = driver.findElement(By.xpath(".//*[@id='empId-0-1']"))
		    .getText();
	    System.out.println(" EMPID  " + EmpID);
	    return;
	}
	// To Switch to Child Window
	else if (operation.toUpperCase().equals("SWITCHTOCHILDWINDOW")) {
	    Parent_Window = driver.getWindowHandle();
	    // Switching from parent window to child window
	    for (String Child_Window : driver.getWindowHandles()) {
		driver.switchTo().window(Child_Window);
	    }
	}
	// To Switch to Second Child Window
	else if (operation.toUpperCase().equals("SWITCHTO_SECOND_CHILDWINDOW")) {
	    Parent_Window1 = driver.getWindowHandle();
	    // Switching from parent window to child window
	    for (String Child_Window : driver.getWindowHandles()) {
		driver.switchTo().window(Child_Window);
	    }
	}
	// To Switch to Second Parent Window
	else if (operation.toUpperCase().equals("SWITCHTO_SECOND_PARENTWINDOW")) {
	    driver.switchTo().window(Parent_Window1);
	}
	// To Switch to Parent Window
	else if (operation.toUpperCase().equals("SWITCHTOPARENTWINDOW")) {
	    driver.switchTo().window(Parent_Window);
	}
	// To Handle Dialogs
	else if (operation.toUpperCase().equals("HANDLEDIALOG")) {
	    driver.switchTo().alert().accept();
	    Thread.sleep(3000);
	}
	// To Handle Dialogs with Long wait
	else if (operation.toUpperCase().equals("HANDLEDIALOG_WITHLONGWAIT")) {
	    driver.switchTo().alert().accept();
	    Thread.sleep(3000);
	}
	// To Handle Dialogs with JobCode Error Message Text
	else if (operation.toUpperCase().equals("HANDLEDIALOG_WITHTEXT")) {
	    Alert alert = driver.switchTo().alert();
	    String AlertString = alert.getText();
	    System.out
		    .println("Job code Input after selecting Offer Letter value as Initial");
	    System.out.println("Alert String " + AlertString);
	    if (AlertString.toLowerCase().contains(
		    "Offer Job Code is inactive".toLowerCase())) {
		alert.accept();
		// Thread.sleep(3000);
		System.out
			.println("Job code Input after selecting Offer Letter value as Initial");
		driver.findElement(
			By.xpath(".//*[@id='formWF:offerJobCodeId0']")).clear();
		driver.switchTo().alert().accept();
		driver.findElement(
			By.xpath(".//*[@id='formWF:offerJobCodeId0']"))
			.sendKeys("8874");
		Thread.sleep(3000);
		driver.findElement(
			By.xpath(".//*[@id='formWF:offerJustificationId0']"))
			.sendKeys("Other");
		driver.findElement(
			By.xpath(".//*[@id='formWF:offerJustificationId0']"))
			.sendKeys("Other");
		driver.findElement(By.id("formWF:btn_save2")).click();
		Thread.sleep(45000);
	    }
	}
	// To Select a List value based on the value in a List box
	else if (operation.toUpperCase().equals("SELECT")) {
	    Select oSelection = new Select(driver.findElement(this.getObject(p,
		    objectName, objectType)));
	    oSelection.selectByValue(value);
	    Thread.sleep(3000);
	}
	// To Select a List value based on the Index in a list box
	else if (operation.toUpperCase().equals("SELECTBASEDONINDEX")) {
	    Select oSelection = new Select(driver.findElement(this.getObject(p,
		    objectName, objectType)));
	    int Index = Integer.parseInt(value);
	    oSelection.selectByIndex(Index);
	    Thread.sleep(4000);
	}
	// Select an option In Combo box based on the option Name
	else if (operation.toUpperCase().equals("SELECT_OPTION_BY_NAME")) {
	    List<WebElement> options = driver.findElements(By
		    .className("x-boundlist-item"));
	    for (WebElement element : options) {
		if (element.getText().equalsIgnoreCase(value)) {
		    element.click();
		    System.out.println(element.getText());
		    System.out.println(value);
		    break;
		}
	    }
	    Thread.sleep(3000);
	}

	// Select all strips and click on a specified text
	else if (operation.toUpperCase().equals("SELECT_STRIP_N_CLICK")) {
	    List<WebElement> options = driver.findElements(By
		    .className("x-tab-strip-text"));
	    for (WebElement element : options) {
		if (element.getText().equalsIgnoreCase(value)) {
		    element.click();
		    break;
		}
	    }
	    Thread.sleep(3000);
	}
	// To Select a List value based on the Visible Text in a list box
	else if (operation.toUpperCase().equals("SELECT_BY_VISIBLETEXT")) {
	    // Select a value from the List Box
	    Select oSelection = new Select(driver.findElement(this.getObject(p,
		    objectName, objectType)));
	    oSelection.selectByVisibleText(value);
	    Thread.sleep(3000);
	}
	// To Focus an Object
	else if (operation.toUpperCase().equals("ACTIONPERFORM")) {
	    // Navigate to the specified Link in the Menus.;
	    Actions builder = new Actions(driver);
	    builder.moveToElement(
		    driver.findElement(this
			    .getObject(p, objectName, objectType))).perform();
	}
	// To Validate the Expected & Actual values
	else if (operation.toUpperCase().equals("ASSERT")) {
	    // Get text of an element
	    strActualText = driver.findElement(
		    this.getObject(p, objectName, objectType)).getText();
	    Assert.assertEquals(strActualText, value);
	    System.out.println("Assertion Passed :"
		    + "Both Actual and Expected values are matching");
	}
	// Switch to a frame
	else if (operation.toUpperCase().equals("SWITCH_TO_FRAME")) {
	    driver.switchTo().frame(value);
	}
	// Switch to a Main WINDOW from Frame
	else if (operation.toUpperCase().equals("SWITCH_TO_PARENT_FROM_FRAME")) {

	    driver.switchTo().defaultContent();
	}

	else if (operation.toUpperCase().equals("NAVIGATE_TO_URL")) {

	    String baseURL = p.getProperty("url");
	    driver.navigate().to(baseURL + p.getProperty(value));
	}

    }// End of Method

    // *********************************************************************************************************************************************
    /**
     * Find element BY using object type and value Get the Object based on the
     * description provided in the Excel sheet
     * 
     * @param p
     * @param objectName
     * @param objectType
     * @return
     * @throws Exception
     */
    private By getObject(Properties p, String objectName, String objectType)
	    throws Exception {
	// Find by xpath
	if (objectType.equalsIgnoreCase("XPATH")) {
	    return By.xpath(p.getProperty(objectName));
	}
	// find by class
	else if (objectType.equalsIgnoreCase("CLASSNAME")) {
	    return By.className(p.getProperty(objectName));
	}
	// find by name
	else if (objectType.equalsIgnoreCase("NAME")) {
	    return By.name(p.getProperty(objectName));
	}
	// find by css
	else if (objectType.equalsIgnoreCase("CSS")) {
	    return By.cssSelector(p.getProperty(objectName));
	}
	// find by link
	else if (objectType.equalsIgnoreCase("LINK")) {
	    return By.linkText(p.getProperty(objectName));
	}
	// find by ID
	else if (objectType.equalsIgnoreCase("ID")) {
	    return By.id(p.getProperty(objectName));
	}
	// find by partial link
	else if (objectType.equalsIgnoreCase("PARTIALLINKTEXT")) {
	    return By.partialLinkText(p.getProperty(objectName));
	} else {
	    throw new Exception("Wrong object type");
	}
    } // End of Method
} // End of Class
