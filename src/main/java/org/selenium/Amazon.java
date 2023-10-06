package org.selenium;

import org.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.assertj.core.api.Assertions.*;

import static org.rsm.HomeWork.iWaitFor;

public class Amazon {
    private By searchFieldOnHomePage = By.id("twotabsearchtextbox");
    private By buttonSubmitSearch = By.id("nav-search-submit-button");
    private By firstBookInTheList = By.xpath("//span[@class=\"a-size-medium a-color-base a-text-normal\"][1]");
    private By typeOfTheFirstEntryInTheList = By.xpath("//div[@class='a-row a-size-base a-color-base'][1]//a");
    private By paperbackLinkInTheFirstEntryInTheList = By.xpath("//div[@class='a-row a-size-base a-color-base'][1]//a[contains(text(),'Paperback')]");
    private By paperbackPriceWholeOfTheFirstEntryInTheList = By.xpath("//span[@class='a-price-whole'][1]");
    private By paperbackPriceFractionOfTheFirstEntryInTheList = By.xpath("//span[@class='a-price-fraction'][1]");
    private By productTitle = By.id("productTitle");
    private By productType = By.xpath("//a[@id='a-autoid-15-announce']//span[1]");
    private By productPrice = By.xpath("//a[@id='a-autoid-15-announce']//child::span[2]//child::span[1]");
    private By buttonCookies = By.id("sp-cc-accept");
    private By checkBoxAddGift = By.id("gift-wrap");
    private By buttonAddToBasket = By.id("add-to-cart-button");
    private By messageAlert = By.xpath("//div[@class='a-box a-alert-inline a-alert-inline-success sw-atc-message']//following-sibling::span");
    private By buttonGoToBasket = By.linkText("Go to basket");
    private By productTitleInTheBasket = By.xpath("//span[@class='a-truncate-cut'][1]");
    private By productTypeInTheBasket = By.xpath("//span[@class='a-size-small sc-product-binding a-text-bold']");
    private By productQuantityInTheBasket = By.className("a-dropdown-prompt");
    private By subTotalAmountInTheBasket = By.xpath("//span[@id='sc-subtotal-amount-activecart']//child::span");
    private By checkBoxProductIsAGift = By.id("sc-buy-box-gift-checkbox");
    private static Float firstEntryInListPaperbackPrice;


    public void performTest(String urlAddress, String bookName, String productType ) {

        Browser.getDriver().navigate().to(urlAddress);
        // time to input check code
        iWaitFor(15);
        clickOnCookiesButton();
        findBookByName(bookName);
        verifyTheNameOfTheFirstEntryInTheListIs(bookName);
        verifyTheFirstEntryInTheListHasType(productType);
        verifyTheFirstEntryInTheListHasPaperbookTypeWithPrice();
        clickOnPaperbackVersionInFirstEntryInTheList();
        verifyProductTitle(bookName);
        verifyTheBookIsPaperback();
        verifyTheBookPrice();
        markProductAsGift();
        clickOnAddToBasket();
        verifyProductIsAddedToBasket();
        clickOnGoToBasket();
        verifyProductTitleInTheBasket(bookName);
        verifyProductTypeInTheBasket(productType);
        verifyProductQuantityInTheBasket(1);
        verifyProductIsAGift();
        verifySubTotalAmountInTheBasket();
        //iWaitFor(15);
        Browser.driver.quit();
    }

    private void clickOnCookiesButton() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(buttonCookies)).click();
    }

    public void findBookByName( String bookName ){
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(searchFieldOnHomePage)).sendKeys(bookName);
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(buttonSubmitSearch)).click();
    }

    public void verifyTheNameOfTheFirstEntryInTheListIs(String nameOfTheBook ) {
        String bookDescription = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(firstBookInTheList)).getText();
        assertThat(bookDescription).containsIgnoringCase(nameOfTheBook);
    }

    public void verifyTheFirstEntryInTheListHasType(String bookType) {
        String bookTypeText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(typeOfTheFirstEntryInTheList)).getText();
        assertThat(bookTypeText).isEqualToIgnoringCase(bookType);
    }

    private void verifyTheFirstEntryInTheListHasPaperbookTypeWithPrice() {
        String paperBookPriceWholePart = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(paperbackPriceWholeOfTheFirstEntryInTheList)).getText();
        String paperBookPriceFractionPart = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(paperbackPriceFractionOfTheFirstEntryInTheList)).getText();
        firstEntryInListPaperbackPrice = Float.parseFloat(paperBookPriceWholePart) + Float.parseFloat(paperBookPriceFractionPart);
        assertThat(firstEntryInListPaperbackPrice).isGreaterThan(0);
    }
    public void clickOnPaperbackVersionInFirstEntryInTheList() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(paperbackLinkInTheFirstEntryInTheList)).click();
    }

    public void verifyProductTitle(String bookName) {
        String productTitleText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).getText();
        assertThat(productTitleText).containsIgnoringCase(bookName);

    }
    public void verifyTheBookIsPaperback() {
        String productTypeText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productType)).getText();
        assertThat(productTypeText).isEqualToIgnoringCase("Paperback");
    }
    public void verifyTheBookPrice() {
        String productPriceText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice)).getText();
        String numericString = productPriceText.replaceAll("[^\\d.]", "");
        Float priceValue = Float.parseFloat(numericString);
        assertThat(priceValue).isEqualTo(firstEntryInListPaperbackPrice);
    }

    public void markProductAsGift() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(checkBoxAddGift)).click();
    }
    public void clickOnAddToBasket() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(buttonAddToBasket)).click();
    }

    public void verifyProductIsAddedToBasket() {
        String confirmationMessage = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(messageAlert)).getText();
        assertThat(confirmationMessage).isEqualToIgnoringCase("Added to Basket");
    }
    public void clickOnGoToBasket() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(buttonGoToBasket)).click();
    }

    public void verifyProductTitleInTheBasket(String bookName) {
        String productTitleText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productTitleInTheBasket)).getText();
        assertThat(productTitleText).containsIgnoringCase(bookName);
    }
    public void verifyProductTypeInTheBasket(String productType ) {
        String productTypeText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productTypeInTheBasket)).getText();
        assertThat(productTypeText).containsIgnoringCase(productType);
    }
    public void verifyProductQuantityInTheBasket(int quantityExpected) {
        String quantityText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(productQuantityInTheBasket)).getText();
        Integer quantityValue = Integer.parseInt(quantityText);
        assertThat(quantityValue).isEqualTo(quantityExpected);
    }
    public void verifySubTotalAmountInTheBasket() {
        String subTotalAmountText = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(subTotalAmountInTheBasket)).getText();
        String numericString = subTotalAmountText.replaceAll("[^\\d.]", "");
        Float subTotalAmount = Float.parseFloat(numericString);
        assertThat(subTotalAmount).isEqualTo(firstEntryInListPaperbackPrice);
    }

    public void verifyProductIsAGift() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(checkBoxProductIsAGift)).isSelected();
    }
}
