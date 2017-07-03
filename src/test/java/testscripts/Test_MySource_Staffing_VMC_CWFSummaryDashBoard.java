package testscripts;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.ithr.ReadExcelData;
import com.ithr.ReadObjects;
import com.ithr.UIOperation;

public class Test_MySource_Staffing_VMC_CWFSummaryDashBoard {
    
    @Test
    public void test_MySource_Staffing_VMC_CWFSummaryDashBoard()
	    throws Exception {
	int rowCount_TestData = 0,columnindex;	
	String FilePath = null,AppURL;
	String FileName = "MySource_Staffing_VMC_CWFSummaryDashBoard.xls";	
	ReadObjects object = new ReadObjects();
	Properties allObjects = object.getObjectRepository();	
	AppURL = object.getPropertyValues();
	System.out.println("Application Environment :"+AppURL);
	FilePath = "TestData//TopHitPages//BackOffice_Staffing//";
	FileInputStream TestInputDataStream = new FileInputStream(new File(
		System.getProperty("user.dir") + "//TestData//TopHitPages//BackOffice_Staffing//"
			+ FileName));
	Workbook TestData_ExcelWorkBook = null;
	// Find the File Extension either .xls or .xlsx
	String TestInputData_fileFormat = FileName.substring(FileName
		.indexOf("."));
	// Check condition if the file is xlsx file
	if (TestInputData_fileFormat.equals(".xlsx")) {	  
	    TestData_ExcelWorkBook = new XSSFWorkbook(TestInputDataStream);
	}else if (TestInputData_fileFormat.equals(".xls")) {	   
	    TestData_ExcelWorkBook = new HSSFWorkbook(TestInputDataStream);
	}
	// Get iterator to all the rows in current sheet
	Sheet TestDataSheet = TestData_ExcelWorkBook.getSheet("TestInputData");
	// ***********************************************************************************************************	
	ReadExcelData file = new ReadExcelData();
	Sheet ReadExcelData = file.readExcel(FilePath, FileName,
		"KeywordFramework");
	System.out.println("FileName :   " + FileName);
	// Read the Number of rows in the TestInputData Sheet
	rowCount_TestData = TestDataSheet.getLastRowNum()
		- TestDataSheet.getFirstRowNum();
	System.out
		.println("The Number of Test Cases Rows in the Test Input Data sheet :   "
			+ rowCount_TestData);
	for (int TestInputDatacounter = 1; TestInputDatacounter <= rowCount_TestData; TestInputDatacounter++) {
	    columnindex = 0;
	    Row TestDataRow = TestDataSheet.getRow(TestInputDatacounter);
	    WebDriver webdriver = object.getDriver();
	    // ImplicityWait Statement
	    webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	    UIOperation operation = new UIOperation(webdriver);
	    // Find number of rows in excel file
	    int rowCount = ReadExcelData.getLastRowNum()
		    - ReadExcelData.getFirstRowNum();
	    // Create a loop over all the rows of excel file to read it
	    for (int i = 1; i < rowCount; i++) {
		// Loop over all the rows
		Row row = ReadExcelData.getRow(i);
		// Check if the first cell contain a value, if yes, That
		// means it is the new test case name
		if (row.getCell(0).toString().length() == 0) {
		    // Print test case detail on console
		    System.out.println(row.getCell(1).toString() + "----"
			    + row.getCell(2).toString() + "----"
			    + row.getCell(3).toString() + "----"
			    + row.getCell(4).toString());
		    String Value = row.getCell(4).toString();
		    int Value_Num;
		    // To read the Test Input Data from the Test Input
		    // Data sheet, where
		    // "VALUE field value is "TESTINPUTDATA""
		    if (Value.equalsIgnoreCase("TestInputData")) {
			switch (TestDataRow.getCell(columnindex).getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: {
			    if (HSSFDateUtil.isCellDateFormatted(TestDataRow
				    .getCell(columnindex))) {
				DateFormat df = new SimpleDateFormat(
					"MM/dd/yyyy");
				Value = df.format(TestDataRow.getCell(
					columnindex).getDateCellValue());
			    } else {
				Value_Num = (int) TestDataRow.getCell(
					columnindex).getNumericCellValue();
				Value = Integer.toString(Value_Num);
			    }
			    break;
			} // First Case block
			case HSSFCell.CELL_TYPE_STRING: {
			    Value = TestDataRow.getCell(columnindex)
				    .getStringCellValue().trim();
			}
			} // End of Switch block
			columnindex = columnindex + 1;
		    } // End of If Statement
		      // Call perform function to perform operation on
		      // UI
		    operation.perform(allObjects, row.getCell(1).toString(),
			    row.getCell(2).toString(), row.getCell(3)
				    .toString(), Value);
		} else {
		    // Print the new test case name when it started
		    System.out.println("New Testcase->"
			    + row.getCell(0).toString() + " Started");		 
		}
	    } // Inner For Loop (Keyword sheet for loop)
	    webdriver.quit(); // Closing of the WebDriver
	} // TestInputData For Loop
    } // End of Function/Method
} // End of Class 