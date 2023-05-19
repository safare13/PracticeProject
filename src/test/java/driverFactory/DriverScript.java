package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript extends FunctionLibrary {
String inputpath ="./FileInput/DataEngine.xlsx";
String outputpath ="./FileOutput/HybridResults.xlsx";
ExtentReports report;
ExtentTest test;
public void startTest()throws Throwable
{
	String Module_Status="";
	//call excelfile util class methods
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in mastertestcases sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//store corresponding sheet into variable
			String TCModule =xl.getCellData("MasterTestCases", i, 1);
			//define path of ExtentReport
			report = new ExtentReports("./Reports/target"+TCModule+"_"+FunctionLibrary.generateDate()+".html");
			//start test case here
			test= report.startTest(TCModule);
			//iterate all rows in TCModule sheet
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				
				//call all cells
				String Description =xl.getCellData(TCModule, j, 0);
				String ObjectType =xl.getCellData(TCModule, j, 1);
				String LocatorType =xl.getCellData(TCModule, j, 2);
				String LocatorValue = xl.getCellData(TCModule, j, 3);
				String TestData =xl.getCellData(TCModule, j, 4);
				try {
					if(ObjectType.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(driver, LocatorType, LocatorValue, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(driver, LocatorType, LocatorValue, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(driver, LocatorType, LocatorValue);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(driver, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("mouseClick"))
					{
						FunctionLibrary.mouseClick(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("categoryTable"))
					{
						FunctionLibrary.categoryTable(driver, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("captureSnumber"))
					{
						FunctionLibrary.captureSnumber(driver, LocatorType, LocatorValue);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("captureCnumber")) {
						FunctionLibrary.captureCnumber(driver, LocatorType, LocatorValue);
						test.log(LogStatus.INFO, Description);
					}
					else if(ObjectType.equalsIgnoreCase("customerTable")) {
						FunctionLibrary.customerTable(driver);
						test.log(LogStatus.INFO, Description);
					}
					//write as pass into status cell TCModule
					xl.setCelldata(TCModule, j, 5, "Pass", outputpath);
					test.log(LogStatus.PASS, Description);
					Module_Status="True";
					
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as fail into status cell TCModule
					xl.setCelldata(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Description);
					Module_Status ="False";
					File srcFile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(srcFile, new File("./ScreenShot/"+Description+FunctionLibrary.generateDate()+" "+".png"));
					String image = test.addScreenCapture("./ScreenShot/"+Description+FunctionLibrary.generateDate()+" "+".png");
					test.log(LogStatus.FAIL, image);
					break;		
				}
				catch(AssertionError a)
				{
					File srcFile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(srcFile, new File("./ScreenShot/"+Description+FunctionLibrary.generateDate()+" "+".png"));
					String image = test.addScreenCapture("./ScreenShot/"+Description+FunctionLibrary.generateDate()+" "+".png");
					test.log(LogStatus.FAIL, image);
					break;		
				}
				if(Module_Status.equalsIgnoreCase("True"))
				{
					xl.setCelldata("MasterTestCases", i, 3, "Pass", outputpath);
				}
				else
				{
					xl.setCelldata("MasterTestCases", i, 3, "Fail", outputpath);
				}
				report.endTest(test);
				report.flush();
			}
		}
		else
		{
			//write as Blocked which are falg to N
			xl.setCelldata("MasterTestCases", i, 3, "Blocked", outputpath);
		}
		
		
		}
}
}

