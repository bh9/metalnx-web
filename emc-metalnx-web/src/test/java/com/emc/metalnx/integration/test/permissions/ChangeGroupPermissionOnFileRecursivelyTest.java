package com.emc.metalnx.integration.test.permissions;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.integration.test.group.GroupUtils;
import com.emc.metalnx.integration.test.utils.CollectionUtils;
import com.emc.metalnx.integration.test.utils.FileUtils;
import com.emc.metalnx.test.generic.UITest;

/**
 * Selenium test for Group permission on the Collectinos Mgmt. Page.
 * This class tests modifying a group permission on a collection. This
 * permission is applied using the recursive option.
 *
 */
public class ChangeGroupPermissionOnFileRecursivelyTest {

    private static WebDriver driver = null;
    private static String groupName = null;

    /************************************** TEST SET UP **************************************/

    @BeforeClass
    public static void setUpBeforeClass() throws DataGridException {
        UITest.setUpBeforeClass();
        driver = UITest.getDriver();

        FileUtils.forceRemoveFilesFromDirAsAdmin("/" + UITest.IRODS_ZONE, UITest.TEST_FILE_NAMES);
        FileUtils.uploadToDirAsAdmin("/" + UITest.IRODS_ZONE, UITest.TEST_FILE_NAMES);
    }

    @Before
    public void setUp() throws Exception {
        UITest.login();

        groupName = "grouppermissionrecursive" + System.currentTimeMillis();
        GroupUtils.createGroupWithPermissions(driver, groupName, "read", UITest.TEST_FILE_NAMES);
    }

    /**
     * After each test the user created for the test should be removed.
     */
    @After
    public void tearDown() throws Exception {
        GroupUtils.removeGroup(groupName, driver);
        UITest.logout();
    }

    /**
     * After all tests are done, the test must quit the driver. This will close every window
     * associated with the current driver instance.
     *
     * @throws DataGridException
     */

    @AfterClass
    public static void tearDownAfterClass() throws DataGridException {
        FileUtils.forceRemoveFilesFromDirAsAdmin("/" + UITest.IRODS_ZONE, UITest.TEST_FILE_NAMES);

        if (driver != null) {
            driver.quit();
            driver = null;
            UITest.setDriver(null);
        }
    }

    /********************************************* TESTS *********************************************/

    /**
     * Test changing the permission of a group to WRITE on a collection recursively.
     */
    @Test
    public void testChangePermissionOfGroupToWrite() {
        CollectionUtils.changePermissionOfGroup(driver, groupName, "write", UITest.TEST_FILE_NAMES[0]);
        Assert.assertFalse(driver.getCurrentUrl().equalsIgnoreCase(UITest.HTTP_ERROR_500_URL));
    }

    /**
     * Test changing the permission of a group to OWN on a collection recursively.
     */
    @Test
    public void testChangePermissionOfGroupToOwn() {
        CollectionUtils.changePermissionOfGroup(driver, groupName, "own", UITest.TEST_FILE_NAMES[0]);
        Assert.assertFalse(driver.getCurrentUrl().equalsIgnoreCase(UITest.HTTP_ERROR_500_URL));
    }

    /**
     * Test changing the permission of a group to NONE on a collection recursively.
     */
    @Test
    public void testChangePermissionOfGroupToNone() {
        CollectionUtils.changePermissionOfGroup(driver, groupName, "none", UITest.TEST_FILE_NAMES[0]);
        Assert.assertFalse(driver.getCurrentUrl().equalsIgnoreCase(UITest.HTTP_ERROR_500_URL));
    }

    /**
     * Test changing the permission of a group to READ on a collection recursively.
     */
    @Test
    public void testChangePermissionOfGroupToRead() {
        /*
         * In order for the group to show up in the permission table, the permission has to exist (different than NONE).
         * Then, since the group is created with READ permission, we need to set it to WRITE or OWN to
         * be able to test READ.
         */
        CollectionUtils.changePermissionOfGroup(driver, groupName, "write", UITest.TEST_FILE_NAMES[0]);

        CollectionUtils.changePermissionOfGroup(driver, groupName, "read", UITest.TEST_FILE_NAMES[0]);
        Assert.assertFalse(driver.getCurrentUrl().equalsIgnoreCase(UITest.HTTP_ERROR_500_URL));
    }
}
