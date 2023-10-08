package org.rsm;

import java.util.concurrent.TimeUnit;

import org.browser.Browser;
import org.foobar.FooBar;
import org.selenium.Amazon;
import org.threads.FileTransferManager;

/**
 * Hello world!
 *
 */
public class HomeWork
{
    public static final String uploadURL = "http://uploadserver.com";
    public static void iWaitFor(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String inputString = args[0];
        System.out.println("Input  : " + inputString.replace(" ",""));
        FooBar myFooBar = new FooBar();
        //FooBarTests
        System.out.println("Output : " + myFooBar.fooBar(inputString.replace(" ","")));

        // amazon.co.uk test
        Browser.browserName = "chrome";
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

        //Threads - Download - Upload files
        FileTransferManager manager = new FileTransferManager();
        FileTransferManager.TransferReport report = manager.transferFiles(123456); // Provide the packageId here
        report.printReport();
        System.out.println("Successful   : " + report.getSuccessCount());
        System.out.println("Unsuccessful : " + report.getFailureCount());
    }
}
