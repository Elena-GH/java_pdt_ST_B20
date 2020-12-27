package ru.pdt.st.addressbook.bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(features = "classpath:bdd", plugin = {"pretty", "html:build/cucumber-report"})
// pretty - формат отчетов для консоли
// build/cucumber-report - дополнительная опция, расположение отчетов
public class GroupTests extends AbstractTestNGCucumberTests {
  // Класс запускатель для сценариев Cucumber
}
