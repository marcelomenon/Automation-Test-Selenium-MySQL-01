package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class chromeTest {
	
	//private static WebDriver driver;
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
	System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe");
	WebDriver driver = new ChromeDriver();
	
	driver.manage().window().maximize();
	
	File file = new File("src/dataFile.properties");
	  
	FileInputStream fileInput = null;
	try {
		fileInput = new FileInputStream(file);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	Properties prop = new Properties();
	
	//load properties file
	try {
		prop.load(fileInput);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	driver.get(prop.getProperty("url"));
		
	driver.findElement(By.xpath(".//input[@name='brand']")).sendKeys(prop.getProperty("brand"));
	
	//getText is returning an empty string, in this case the solution is getAttribute("value")
	String brandText = driver.findElement(By.xpath(".//input[@name='brand']")).getAttribute("value");
	
	driver.findElement(By.xpath(".//input[@name='type']")).sendKeys(prop.getProperty("type"));
	
	//getText is returning an empty string, in this case the solution is getAttribute("value")
	String typeText = driver.findElement(By.xpath(".//input[@name='type']")).getAttribute("value");
	
	new Select (driver.findElement(By.xpath(".//select[@name='year']"))).selectByVisibleText("2016");
	
	driver.findElement(By.xpath(".//input[@value='gasoline']")).click();
	
	driver.findElement(By.xpath(".//input[@value='buy']")).click();
	
	driver.findElement(By.xpath(".//input[@type='submit']")).click();
	
	//to validate the scenario
	String message01 = driver.findElement(By.xpath("html/body")).getText();
	System.out.println(message01);
	String message02 = ("Car " + brandText + " " + typeText + " " + 2016 + " " + "gasoline" + " " + "to " + "buy" + " " + "added successful"); 
    
	try{
			Assert.assertEquals(message02, message01);
			System.out.println("The message is correct");
	}catch(AssertionError e){
			System.out.println("The message is not correct");
	}
	
	//Load MySQL JDBC Driver
    Class.forName("com.mysql.jdbc.Driver");
        
    //Creating connection to the database
    Connection con = DriverManager.getConnection(prop.getProperty("dbURL"),prop.getProperty("username"),prop.getProperty("password"));
        
        
    //Creating statement object
    Statement st = con.createStatement();
    String selectquery = "select * from cars";
    	
    //Executing the SQL Query and store the results in ResultSet
    ResultSet rs = st.executeQuery(selectquery);
    	
    //Verify the database and print the last result		
	if (rs.last()){
	        String id = rs.getString(1);								        
            String brand = rs.getString(2);	
            String type = rs.getString(3);
            String year = rs.getString(4);
            String fuel = rs.getString(5);
            String market = rs.getString(6);
            System. out.println(id+"  "+brand+"  "+type+"  "+year+"  "+fuel+"  "+market);
		
		try{
			Assert.assertEquals(brand, brandText);
			Assert.assertEquals(type, typeText);
			System.out.println("Test Case Passed");
	}catch(AssertionError e){
			System.out.println("Test Case Failed");
	}
		}
    //Closing DB Connection
    con.close();
		
    driver.close();
	}
}