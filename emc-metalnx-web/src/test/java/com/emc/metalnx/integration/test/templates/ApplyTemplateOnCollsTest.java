package com.emc.metalnx.integration.test.templates;

import java.util.ArrayList;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.metalnx.integration.test.utils.CollectionUtils;
import com.emc.metalnx.integration.test.utils.TemplateUtils;
import com.emc.metalnx.test.generic.UITest;

/**
 * Class that tests the application of a template with metadata fields on collections.
 */
public class ApplyTemplateOnCollsTest {
    private static final Logger logger = LoggerFactory.getLogger(TemplateTest.class);

    private static WebDriver driver = null;

    private static List<String> attributes = null;
    private static List<String> values = null;
    private static List<String> units = null;
    private String templateName = null;

    /************************************* TEST SET UP *************************************/

    @BeforeClass
    public static void setUpBeforeClass() {
        UITest.setUpBeforeClass();
        driver = UITest.getDriver();

        attributes = new ArrayList<String>();
        values = new ArrayList<String>();
        units = new ArrayList<String>();

        for (int i = 0; i < TemplateUtils.TOTAL_METADATA_FIELDS; i++) {
            attributes.add(TemplateUtils.attribute + i);
            values.add(TemplateUtils.value + i);
            units.add(TemplateUtils.unit + i);
        }
    }

    @Before
    public void setUp() throws Exception {
        templateName = TemplateUtils.TEMPLATE_TEST_NAME + System.currentTimeMillis();
        UITest.login();
        TemplateUtils.createTemplateWithFields(driver, templateName, TemplateUtils.SYSTEM_TEMPLATE_TYPE);
        CollectionUtils.createCollection(driver, TemplateUtils.TEST_COLLS[0]);
    }

    @After
    public void tearDown() throws Exception {
        TemplateUtils.removeTemplate(templateName, driver);
        CollectionUtils.goToUserHome(driver, UITest.RODS_USERNAME);
        CollectionUtils.removeColl(driver, TemplateUtils.TEST_COLLS[0]);
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#treeViewTable tbody tr td")));
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

    /************************************* TESTS *************************************/

    /**
     * Method that tests if a template can be applied to a collection. The template is not changed.
     */
    @Test
    public void testApplyTemplate() {
        logger.info("Apply template with no metadata fields");

        driver.get(UITest.COLLECTIONS_URL);
        Assert.assertEquals(UITest.COLLECTIONS_URL, driver.getCurrentUrl());

        TemplateUtils.findTemplateToApply(driver, templateName, TemplateUtils.TEST_COLLS);
        TemplateUtils.submitApplyTemplateForm(driver);
        TemplateUtils.isSuccessMessageShown(driver);
        TemplateUtils.checkIfMetadataWasAdded(driver, true, attributes, values, units, TemplateUtils.TEST_COLLS);
    }
}
