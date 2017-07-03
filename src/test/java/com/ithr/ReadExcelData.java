package com.ithr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.ithr.ReadObjects;

/*---------------------------------------------------------------------------------------------------------------------------
 * Class Name  : ReadExcelData
 * Description : This class reads data from the excel and return the excel sheet object to the main class
 * 			   : Generates a template spreadsheet for the report to update the status of the execution of the test case
 * Author      :  Viswa 
 -----------------------------------------------------------------------------------------------------------------------------*/

public class ReadExcelData {
    public static String MSJUserName = "hrtstoc";
    // public static String MSJPassword ="4PaUdDHBVqYIl/Cgnh6Rfw==";
    public static String MSJPassword = "umBZFb2DyZy9TwRrmOIY8A==";
    private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41,
	    0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };

    public Sheet readExcel(String filePath, String fileName, String sheetName)
	    throws IOException {
	// Create a object of File class to open xlsx file
	File file = new File(filePath + "/" + fileName);
	// Create an object of FileInputStream class to read excel file
	FileInputStream inputStream = new FileInputStream(file);
	Workbook ExcelWorkBook = null;
	// Find the File Extension either .xls or .xlsx
	String fileFormat = fileName.substring(fileName.indexOf("."));
	// Check condition if the file is xlsx file
	if (fileFormat.equals(".xlsx")) {
	    // If it is xlsx file then create object of XSSFWorkbook class
	    ExcelWorkBook = new XSSFWorkbook(inputStream);
	}
	// Check condition if the file is xls file
	else if (fileFormat.equals(".xls")) {
	    // If it is xls file then create object of XSSFWorkbook class
	    ExcelWorkBook = new HSSFWorkbook(inputStream);
	}
	// Read sheet inside the workbook by its name
	Sheet ExcelSheet = ExcelWorkBook.getSheet(sheetName);
	return ExcelSheet;
    }

    // This method will create a excel result sheet template in .xls format
    public static void ExcelResultTemplate(String filename) throws Exception {
	String MSJServerName = null;
	FileOutputStream fileOut = new FileOutputStream(new File(
		System.getProperty("user.dir") + "//" + filename));
	HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet worksheet = workbook.createSheet("ResultSheet");

	Font font = workbook.createFont();
	font.setFontHeightInPoints((short) 10);
	font.setFontName("Arial");
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	// font.setBold(true);
	CellStyle style = workbook.createCellStyle();
	// HSSFColor lightGray = setColor(workbook,(byte) 0xE0,
	// (byte)0xE0,(byte) 0xE0);
	// style.setFillForegroundColor(lightGray.getIndex());
	style.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	style.setFont(font);
	style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	// Extract The Server Name
	ReadObjects object = new ReadObjects();
	Properties ObjectsOR = object.getObjectRepository();
	String BaseURL = object.getPropertyValues();
	WebDriver driver = object.getDriver();
	driver.get(BaseURL);
	try {
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
	    Thread.sleep(15000);
	    // MSJServerName=driver.findElement(By.xpath(".//*[@id='Stats']/dl/dd[2]")).getText();
	    System.out.println("Server Name : " + MSJServerName);
	    driver.quit();
	} catch (Exception e) {
	    e.printStackTrace();
	    return;
	}
	Row row = worksheet.createRow(0);
	row.createCell(0).setCellValue(
		"TESTCASE_NAME - " + "Server Name : " + MSJServerName);
	row.createCell(1).setCellValue("EXECUTION_STATUS");
	row.createCell(2).setCellValue(
		"SCRIPT_EXECUTION_TIME : " + "(IN SECONDS )");
	row.createCell(3).setCellValue("COMMENTS");
	for (int i = 0; i < row.getLastCellNum(); i++) {
	    row.getCell(i).setCellStyle(style);

	    worksheet.autoSizeColumn(i);
	}

	workbook.write(fileOut);
	// workbook.close();
	fileOut.flush();
	fileOut.close();
    }

    // This method will update the results in the excel result sheet created at
    // runtime
    public static void UpdateResults(String filename, String TESTCASE_NAME,
	    String EXECUTION_STATUS, String SCRIPT_EXECUTION_TIME,
	    String COMMENTS) throws IOException {

	int rowNum;
	File file = new File(filename);
	InputStream inp = new FileInputStream(System.getProperty("user.dir")
		+ "//" + file);
	HSSFWorkbook workbook = new HSSFWorkbook(inp);
	Sheet sheet = workbook.getSheet("ResultSheet");
	rowNum = sheet.getLastRowNum();
	rowNum = rowNum + 1;
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue(TESTCASE_NAME);
	row.createCell(1).setCellValue(EXECUTION_STATUS);
	row.createCell(2).setCellValue(SCRIPT_EXECUTION_TIME);
	row.createCell(3).setCellValue(COMMENTS);
	// Formatting the Cells
	Font font = workbook.createFont();
	// font.setColor(IndexedColors.GREEN.getIndex());
	font.setFontHeightInPoints((short) 10);
	font.setFontName("Arial");
	CellStyle style = workbook.createCellStyle();
	style.setFont(font);
	style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	// Creating the Font with Green/RED Color based on Execution Status
	HSSFFont font1 = workbook.createFont();
	if (EXECUTION_STATUS != "FAIL") {
	    font1.setColor(IndexedColors.GREEN.getIndex());
	} else {
	    font1.setColor(IndexedColors.RED.getIndex());
	}
	font1.setFontHeightInPoints((short) 10);
	font1.setFontName("Arial");
	CellStyle style1 = workbook.createCellStyle();
	style1.setFont(font1);
	style1.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	style1.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	style1.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	style1.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	// Updating the Cells with Font Changes
	for (int i = 0; i < row.getLastCellNum(); i++) {
	    if (i == 1) {
		row.getCell(i).setCellStyle(style1);
		sheet.autoSizeColumn(i);
	    } else {
		row.getCell(i).setCellStyle(style);
		sheet.autoSizeColumn(i);
	    }
	}
	// Write the output to a file
	FileOutputStream fileOut = new FileOutputStream(
		System.getProperty("user.dir") + "//" + file);
	workbook.write(fileOut);
	// workbook.close();
	fileOut.flush();
	fileOut.close();
    }
}