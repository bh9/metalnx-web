package com.emc.metalnx.integration.test.mygroups;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.integration.test.utils.UserUtils;
import com.emc.metalnx.test.generic.UITest;

/**
 * Test class that checks if a user who is not in any group can access the My Groups page.
 *
 */
public class RodsUserNotInAnyGroupTest {

    private static String pwd = "webdriver";
    private static String rodsUsername;
    private static WebDriver driver = null;
    private By groupBookmarksTable = By.id("groupBookmarksTable");
    private By groupBookmarksTableBody = By.cssSelector("#groupBookmarksTable tbody tr td");

    /************************************* TEST SET UP *************************************/

    @BeforeClass
    public static void setUpBeforeClass() throws DataGridException {
        UITest.setUpBeforeClass();
        driver = UITest.getDriver();

        rodsUsername = "userMyGroupsRodsUser" + System.currentTimeMillis();
        UserUtils.createUser(rodsUsername, pwd, UITest.RODS_USER_TYPE, driver);
    }

    @Before
    public void setUp() {
        UITest.login(rodsUsername, pwd);
    }

    @After
    public void tearDown() {
        UITest.logout();
    }

    @AfterClass
    public static void tearDownAfterClass() throws DataGridException {
        UserUtils.removeUser(rodsUsername, driver);

        if (driver != null) {
            driver.quit();
            driver = null;
            UITest.setDriver(null);
        }
    }

    @Test
    public void testRodsUserNotInAnyGroupShouldSeeNothingOnMyGroupsPage() {
        driver.get(UITest.MY_GROUPS_PAGE);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(groupBookmarksTable));
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(groupBookmarksTableBody));

        Assert.assertNotNull(driver.findElement(By.cssSelector("#groupBookmarksTable .dataTables_empty")));
    }
}
