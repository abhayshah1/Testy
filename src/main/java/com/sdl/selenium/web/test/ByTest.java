package com.sdl.selenium.web.test;

import com.sdl.selenium.web.WebLocator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ByTest {
    private static WebLocator container = new WebLocator(By.cls("container"));
    private static String CONTAINER_PATH = "//*[contains(concat(' ', @class, ' '), ' container ')]";

    @DataProvider
    public static Object[][] testConstructorPathDataProvider() {
        return new Object[][]{
//                {new WebLocator(), "//*"},
//                {new WebLocator(By.id("Id")), "//*[@id='Id']"},
//                {new WebLocator(container), CONTAINER_PATH + "//*"},
//                {new WebLocator(container, By.id("Id")), CONTAINER_PATH + "//*[@id='Id']"},

                {new TextField(By.id("Id"), By.text("text"), By.icon("icon")), "//*[@id='Id' and @name='name' and count(.//*[contains(@class, 'icon')]) > 0]"},
                {new TextField(), "//*[@id='Id' and @name='name' and count(.//*[contains(@class, 'icon')]) > 0]"},

//                {new TextField(By.id("Id")), "//*[@id='Id']"},
//                {new TextField(container), CONTAINER_PATH + "//*"},
//                {new TextField(container, By.id("Id"), By.xpath("")), CONTAINER_PATH + "//*[@id='Id']"},
//                {new TextField(container, By.id("Id"), By.text("ttt")), CONTAINER_PATH + "//*[@id='Id' and contains(text(),'ttt')]"},
        };
    }

    @Test(dataProvider = "testConstructorPathDataProvider")
    public void getPathSelectorCorrectlyFromConstructors(WebLocator el, String expectedXpath) {
//        Assert.assertEquals(el.getPath(), expectedXpath);
        Assert.assertEquals(el.getPathBuilder().getPath(), expectedXpath);
    }
}