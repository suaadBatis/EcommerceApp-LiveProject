import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;

public class BaseClass {
    public static AndroidDriver driver;
    public AppiumDriverLocalService service;

    @BeforeClass
    public void killAllNodes() throws IOException, InterruptedException
    {
        //kill the processing node
        Runtime.getRuntime ().exec("/usr/bin/killall -KILL node");
        Thread.sleep(3000);

    }

    public AppiumDriverLocalService startServer() {
        boolean flag = checkIfServerIsRuning(4723);
        if (!flag) {
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();
        }
        return service;
    }

    // to check if the server is running or will skip this method
    public static boolean checkIfServerIsRuning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();

        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public static void startEmulator() throws IOException, InterruptedException {
            System.out.println(" Congrats Your  Emulator is starting .... ..... .... ");
            Runtime.getRuntime().exec( System.getProperty("user.dir")+"/src/main/resources/startEmulator.sh");
            Thread.sleep(1000);

    }
    public static AndroidDriver<AndroidElement> capabilities(String AppName) throws IOException, InterruptedException {
        // here connect to properties file
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "/src/main/gloabl.properties");//create general path
        Properties pro = new Properties();
        pro.load(file);
        File AppDire = new File("app");
        File app = new File(AppDire, (String) pro.get(AppName));
        //String device =System.getProperty("deviceName");
        String device = (String) pro.get("device");
       if (device.contains("emulator")) {
            startEmulator();
        }
        String platform = (String) pro.get("platform");
        String version = (String) pro.get("version");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", app.getAbsolutePath());
        desiredCapabilities.setCapability("deviceName", device);
        desiredCapabilities.setCapability("platformName", platform);
        desiredCapabilities.setCapability("platformVersion", version);
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, "true");
        URL remoteUrl = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        return driver;
    }

    @AfterClass
    public void treadDown(){
        driver.quit();
    }
}
