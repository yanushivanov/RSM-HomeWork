package org.rsm;

import java.util.concurrent.TimeUnit;

import org.browser.Browser;
import org.foobar.FooBar;
import org.selenium.Amazon;

/**
 * Hello world!
 *
 */
public class HomeWork
{
    public static void iWaitFor(int seconds) {
        try {
            TimeUnit.SECONDS.sleep((long)seconds);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String inputString = args[0];
        System.out.println("Input  : " + inputString.replaceAll(" ",""));
        FooBar myFooBar = new FooBar();
        //FooBarTests
        System.out.println("Output : " + myFooBar.fooBar(inputString.replaceAll(" ","")));

        // amazon.co.uk test
        Amazon amazon = new Amazon();
        try {
            amazon.performTest(  "https:////amazon.co.uk",
                                 "Harry Potter and the Cursed Child - Parts One and Two",
                                "Paperback");
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            // Close the browser and quit the driver
            if (Browser.driver!= null) {
                Browser.driver.quit();
            }
        }
    }
}
