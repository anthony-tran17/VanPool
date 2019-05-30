import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TeamProjectApplicationTests{
    static WebDriver webDriver;

    @Test
    public void Login(){
        webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        webDriver.get("http://localhost:8080");
        webDriver.manage().window().maximize();
        webDriver.findElement(By.xpath("//a[text() = 'Login']")).click();
        webDriver.findElement(By.xpath("//input[@name='email']")).sendKeys("Admin@gmail.com");
        webDriver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin");
        webDriver.findElement(By.xpath("//input[@value='Login']")).click();
        String url = webDriver.getCurrentUrl();
        assertEquals("Admin Home Page!",url,"http://localhost:8080/login");
        TeamProjectApplicationTests tests = new TeamProjectApplicationTests();
        tests.addRoute();
        tests.addVehicles();
    }


    public void addRoute(){
        int index = 0;
       webDriver.findElement(By.xpath("//a[text()='Manage Routes']")).click();
        WebElement baseTable = webDriver.findElement(By.tagName("table"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        int initialSize = tableRows.size();
        webDriver.findElement(By.xpath("//a[text()='Add Routes']")).click();
        webDriver.findElement(By.xpath(("//input[@name='fromPlace']"))).sendKeys("Iowa City");
       webDriver.findElement(By.xpath("//input[@name='toPlace']")).sendKeys("Cedar Rapids");
       webDriver.findElement(By.xpath("//input[@name='cost']")).sendKeys("15.0");
        Select vehicleNo = new Select(webDriver.findElement(By.id("vehicleno")));
        vehicleNo.selectByVisibleText("BY567");
        webDriver.findElement(By.xpath(("//input[@value='Add Route']"))).click();
        WebElement changeTable = webDriver.findElement(By.tagName("table"));
        List<WebElement> changeRows = changeTable.findElements(By.tagName("tr"));
        int finalSize = changeRows.size();
        if(initialSize < finalSize){
            System.out.println("Added successfully");
        }
    }

    public void addVehicles(){
        webDriver.findElement(By.xpath("//a[text()='Home']")).click();
        webDriver.findElement(By.xpath("//a[text()='Manage Vehicles']")).click();
        WebElement baseTable = webDriver.findElement(By.tagName("table"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        int initialSize = tableRows.size();
        webDriver.findElement(By.xpath("//a[text()='Add vehicle']")).click();
        webDriver.findElement(By.xpath(("//input[@name='vehicleNo']"))).sendKeys("BYV676");
        webDriver.findElement(By.xpath("//input[@name='vehicleType']")).sendKeys("Toyota Corolla");
        webDriver.findElement(By.xpath("//input[@name='owner']")).sendKeys("Admin@gmail.com");
        webDriver.findElement(By.xpath("//input[@name='maxOcc']")).sendKeys("10");
        webDriver.findElement(By.xpath("//input[@value='Add vehicle']")).click();
        WebElement changeTable = webDriver.findElement(By.tagName("table"));
        List<WebElement> changeRows = changeTable.findElements(By.tagName("tr"));
        int finalSize = changeRows.size();
        if(initialSize < finalSize){
            System.out.println(" vehicle Added successfully");
        }


    }

    @Before
    public void browserOpen(){
        System.setProperty("webdriver.chrome.driver","/Applications/chromedriver");
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }



    @After
    public void closeBrowser(){
        webDriver.quit();
    }



}