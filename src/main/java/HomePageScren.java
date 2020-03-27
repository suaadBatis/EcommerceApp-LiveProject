
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static java.time.Duration.ofSeconds;
import static org.testng.AssertJUnit.*;

public class HomePageScren extends BaseClass {
    @Test(priority = 1)
    public void HomePageScrenForEcommerce() throws IOException, InterruptedException {
        startServer();
        AndroidDriver<AndroidElement> driver = capabilities("Application");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElementById("com.androidsample.generalstore:id/nameField").sendKeys("Suaad ");
        driver.hideKeyboard();
        driver.findElementByXPath("*//*[@text='Female']").click();
        driver.findElementById("android:id/text1").click();
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Argentina\"));");
        // driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"" + containedText + "\").instance(0))"));
        driver.findElementByXPath("*//*[@text='Argentina']").click();
        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
    }

    @Test(dependsOnMethods = "HomePageScrenForEcommerce", priority = 2)
    public void TostMessage() {
        driver.findElementById("com.androidsample.generalstore:id/appbar_btn_back").click();
        driver.findElementById("com.androidsample.generalstore:id/nameField").clear();
        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
        String toastMessage = driver.findElementByXPath("//android.widget.Toast[1]").getAttribute("name");
        System.out.println("toastMessage");
        Assert.assertEquals("Please enter your name", toastMessage);
    }

    @Test(dependsOnMethods = "TostMessage", priority = 3)
    public void ProductList() {
        driver.findElementById("com.androidsample.generalstore:id/nameField").sendKeys("Suaad 2 ");
        driver.hideKeyboard();
        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
        // to copy all list
        System.out.println(driver.findElements(By.id("com.androidsample.generalstore:id/productName")).size());
        // driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Nike SFB Jungle\"));");
        WebElement radioGroup = driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()" + ".resourceId(\"com.androidsample.generalstore:id/rvProductList\")).scrollIntoView("
                + "new UiSelector().text(\"Jordan 6 Rings\"));");
        assertNotNull(radioGroup.getLocation());
    }

    @Test(dependsOnMethods = "ProductList", priority = 4)
    public void CheckoutScreen() throws InterruptedException {
        int count = driver.findElements(By.id("com.androidsample.generalstore:id/productName")).size();
        for (int i = 0; i < count; i++) {
            MobileElement element = (MobileElement) driver.findElements(By.id("com.androidsample.generalstore:id/productName")).get(i);
            String text = element.getText();
            System.out.println(text);
            if (text.equalsIgnoreCase("Jordan 6 Rings")) {
                MobileElement element2 = (MobileElement) driver.findElements(By.id("com.androidsample.generalstore:id/productAddCart")).get(i);
                element2.click();
                break;
            }
        }
        driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
        Thread.sleep(1000);
        //assert text
        String lastpageText = driver.findElement(By.id("com.androidsample.generalstore:id/productName")).getText();
        Assert.assertEquals("Jordan 6 Rings", lastpageText);
    }

    @Test(dependsOnMethods = "CheckoutScreen", priority = 5)
    public void totalAmount() throws InterruptedException {
        driver.findElementById("com.androidsample.generalstore:id/appbar_btn_back").click();
        driver.findElementById("com.androidsample.generalstore:id/appbar_btn_back").click();
        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
        By.xpath("//*[@text='ADD TO CART']").findElements(driver).get(0).click();
        By.xpath("//*[@text='ADD TO CART']").findElements(driver).get(0).click();

        driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
        Thread.sleep(1000);
        // assert amount
        MobileElement element = (MobileElement) driver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).get(0);
        String amount1 = element.getText().substring(1);// to remove $ used substring
        double amountValue1 = Double.parseDouble(amount1);// to let the number as double


        MobileElement element2 = (MobileElement) driver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).get(1);
        String amount2 = element2.getText().substring(1);
        double amountValue2 = Double.parseDouble(amount2);

        System.out.println(amountValue1);
        System.out.println(amountValue2);
        double Sumtotal = amountValue2 + amountValue1;
        // to check the calculation of the sum product = to the total amount
        String total = driver.findElement(By.id("com.androidsample.generalstore:id/totalAmountLbl")).getText();
        total = total.substring(1);
        double totalValue = Double.parseDouble(total);

        assertEquals(Sumtotal, totalValue);

        System.out.println(Sumtotal);
        System.out.println(totalValue);

    }
    @Test(dependsOnMethods = "totalAmount" )
    public void  mobileGestures() throws InterruptedException {

        MobileElement checkbox = (MobileElement) driver.findElementByClassName("android.widget.CheckBox");
        Assert.assertTrue("checkbox",true);


        //checkbox.click();
       TouchAction t = new TouchAction(driver);
       t.tap(tapOptions().withElement(element(checkbox))).perform();
       MobileElement tc = (MobileElement) driver.findElementByXPath("*//*[@text='Please read our terms of conditions']");
        t.longPress(longPressOptions().withElement(element(tc)).withDuration(ofSeconds(2))).release().perform();
        MobileElement clickText = (MobileElement) driver.findElementById("android:id/button1");
        t.tap(tapOptions().withElement(element(clickText))).perform();
        driver.findElementById("com.androidsample.generalstore:id/btnProceed").click();
    }
  @Test(dependsOnMethods = "mobileGestures")
  public void WebPage(){

  }
    public double getAmount(String value) {
        value = value.substring(1);
        double amountValue2 = Double.parseDouble(value);
        return amountValue2;
    }

}

