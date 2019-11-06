package car;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DesignAndOrderCarsBrowserTest {
  
  private static HtmlUnitDriver browser;
  
  @LocalServerPort
  private int port;
  
  @Autowired
  TestRestTemplate rest;
  
  @BeforeClass
  public static void setup() {
    browser = new HtmlUnitDriver();
    browser.manage().timeouts()
        .implicitlyWait(10, TimeUnit.SECONDS);
  }
  
  @AfterClass
  public static void closeBrowser() {
    browser.quit();
  }
  
  @Test
  public void testDesignACarPage_HappyPath() throws Exception {
    browser.get(homePageUrl());
    clickDesignACar();
    assertDesignPageElements();
    buildAndSubmitACar("Basic Car", "FLTO", "GRBF", "CHED", "TMTO");
    clickBuildAnotherCar();
    buildAndSubmitACar("Another Car", "COTO", "CARN", "JACK", "LETC");
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }
  
  @Test
  public void testDesignACarPage_EmptyOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignACar();
    assertDesignPageElements();
    buildAndSubmitACar("Basic Car", "FLTO", "GRBF", "CHED", "TMTO");
    submitEmptyOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }

  @Test
  public void testDesignACarPage_InvalidOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignACar();
    assertDesignPageElements();
    buildAndSubmitACar("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO");
    submitInvalidOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }

  //
  // Browser test action methods
  //
  private void buildAndSubmitACar(String name, String... features) {
    assertDesignPageElements();

    for (String feature : features) {
      browser.findElementByCssSelector("input[value='" + feature + "']").click();      
    }
    browser.findElementByCssSelector("input#name").sendKeys(name);
    browser.findElementByCssSelector("form").submit();
  }

  private void assertDesignPageElements() {
    assertEquals(designPageUrl(), browser.getCurrentUrl());
    List<WebElement> featureGroups = browser.findElementsByClassName("feature-group");
    assertEquals(5, featureGroups.size());
    
    WebElement carGroup = browser.findElementByCssSelector("div.feature-group#car");
    List<WebElement> cars = carGroup.findElements(By.tagName("div"));
    assertEquals(2, cars.size());
    assertFeatures(carGroup, 0, "FLTO", "Flour Tortilla");
    assertFeatures(carGroup, 1, "COTO", "Corn Tortilla");
    
    WebElement makeGroup = browser.findElementByCssSelector("div.feature-group#make");
    List<WebElement> makes = makeGroup.findElements(By.tagName("div"));
    assertEquals(2, makes.size());
    assertFeatures(makeGroup, 0, "GRBF", "Ground Beef");
    assertFeatures(makeGroup, 1, "CARN", "Carnitas");

    WebElement yearGroup = browser.findElementByCssSelector("div.feature-group#year");
    List<WebElement> years = makeGroup.findElements(By.tagName("div"));
    assertEquals(2, years.size());
    assertFeatures(yearGroup, 0, "CHED", "Cheddar");
    assertFeatures(yearGroup, 1, "JACK", "Monterrey Jack");

    WebElement colorGroup = browser.findElementByCssSelector("div.feature-group#color");
    List<WebElement> colors = makeGroup.findElements(By.tagName("div"));
    assertEquals(2, colors.size());
    assertFeatures(colorGroup, 0, "TMTO", "Diced Tomatoes");
    assertFeatures(colorGroup, 1, "LETC", "Lettuce");

  }
  

  private void fillInAndSubmitOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#deliveryName", "Ima Hungry");
    fillField("input#deliveryStreet", "1234 Culinary Blvd.");
    fillField("input#deliveryCity", "Foodsville");
    fillField("input#deliveryState", "CO");
    fillField("input#deliveryZip", "81019");
    fillField("input#ccNumber", "4111111111111111");
    fillField("input#ccExpiration", "10/19");
    fillField("input#ccCVV", "123");
    browser.findElementByCssSelector("form").submit();
  }

  private void submitEmptyOrderForm() {
    assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());
    browser.findElementByCssSelector("form").submit();
    
    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(9, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Delivery name is required"));
    assertTrue(validationErrors.contains("Street is required"));
    assertTrue(validationErrors.contains("City is required"));
    assertTrue(validationErrors.contains("State is required"));
    assertTrue(validationErrors.contains("Zip code is required"));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));    
  }

  private List<String> getValidationErrorTexts() {
    List<WebElement> validationErrorElements = browser.findElementsByClassName("validationError");
    List<String> validationErrors = validationErrorElements.stream()
        .map(el -> el.getText())
        .collect(Collectors.toList());
    return validationErrors;
  }

  private void submitInvalidOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#deliveryName", "I");
    fillField("input#deliveryStreet", "1");
    fillField("input#deliveryCity", "F");
    fillField("input#deliveryState", "C");
    fillField("input#deliveryZip", "8");
    fillField("input#ccNumber", "1234432112344322");
    fillField("input#ccExpiration", "14/91");
    fillField("input#ccCVV", "1234");
    browser.findElementByCssSelector("form").submit();
    
    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(4, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));    
  }

  private void fillField(String fieldName, String value) {
    WebElement field = browser.findElementByCssSelector(fieldName);
    field.clear();
    field.sendKeys(value);
  }
  
  private void assertFeatures(WebElement featureGroup, 
                                int featureIdx, String id, String name) {
    List<WebElement> makes = featureGroup.findElements(By.tagName("div"));
    WebElement features = makes.get(featureIdx);
    assertEquals(id, 
    		features.findElement(By.tagName("input")).getAttribute("value"));
    assertEquals(name, 
    		features.findElement(By.tagName("span")).getText());
  }

  private void clickDesignACar() {
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    browser.findElementByCssSelector("a[id='design']").click();
  }

  private void clickBuildAnotherCar() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    browser.findElementByCssSelector("a[id='another']").click();
  }

 
  //
  // URL helper methods
  //
  private String designPageUrl() {
    return homePageUrl() + "design";
  }

  private String homePageUrl() {
    return "http://localhost:" + port + "/";
  }

  private String orderDetailsPageUrl() {
    return homePageUrl() + "orders";
  }

  private String currentOrderDetailsPageUrl() {
    return homePageUrl() + "orders/current";
  }

}
