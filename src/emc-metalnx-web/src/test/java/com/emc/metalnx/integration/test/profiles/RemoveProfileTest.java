package com.emc.metalnx.integration.test.profiles;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.metalnx.integration.test.utils.ProfileUtils;
import com.emc.metalnx.test.generic.UITest;

import junit.framework.Assert;

public class RemoveProfileTest {
    private static final Logger logger = LoggerFactory.getLogger(RemoveProfileTest.class);

    private static WebDriver driver = null;

    private static final String PROFILE_NAME = "Profile_Name";

    private static final String PROFILE_DESCRIPTION = "Profile Description";

    /************************************* TEST SET UP *************************************/

    @BeforeClass
    public static void setUpBeforeClass() {
        UITest.setUpBeforeClass();
        driver = UITest.getDriver();
    }

    @Before
    public void setUp() throws Exception {
        UITest.login();
        WebDriverWait wait = new WebDriverWait(driver, 8);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page-wrapper")));

        ProfileUtils.addUserProfile(PROFILE_NAME, PROFILE_DESCRIPTION, null, driver);
    }

    /**
     * After each test the user created for the test should be removed.
     */
    @After
    public void tearDown() throws Exception {
        UITest.logout();
    }

    /**
     * After all tests are done, the test must quit the driver. This will close every window
     * associated with the current driver instance.
     */

    @AfterClass
    public static void tearDownAfterClass() {
        if (driver != null) {
            driver.quit();
            driver = null;
            UITest.setDriver(null);
        }
    }

    /*
     * ********************************************************************************************
     * *********************************** REMOVE PROFILE *****************************************
     * ********************************************************************************************
     */

    /**
     * Test method for removing a user profile. It verifies if success message is displayed and
     * profile does not appear anymore in profile list.
     */
    @Test
    public void testRemoveUserProfile() {
        logger.info("Test for removing a user profile");

        driver.get(UITest.PROFILES_URL);

        ProfileUtils.removeProfile(PROFILE_NAME, driver);

        WebDriverWait wait = new WebDriverWait(driver, 8);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userProfilesListTable")));

        WebElement divAlertSucess = driver.findElement(By.className("alert-success"));
        assertTrue(divAlertSucess.isDisplayed());
        assertTrue(divAlertSucess.getText().contains(PROFILE_NAME));

        Assert.assertTrue(!ProfileUtils.isProfileInList(PROFILE_NAME, driver));
    }

}