import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.rules.ExternalResource;
import org.sql2o.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;


public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();


  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Hair Salon!");
  }

  @Test
  public void stylistIsCreatedTest() {
    goTo("http://localhost:4567/");
    fill("#stylist_name").with("Sasha");
    submit(".btn");
    assertThat(pageSource()).contains("Sasha");
  }

  @Test
  public void stylistIsDisplayedTest() {
    Stylist myStylist = new Stylist("Sasha");
    myStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Sasha");
  }

  @Test
  public void stylistClientsFormIsDisplayed() {
    Stylist myStylist = new Stylist("Sasha");
    myStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Add a Client to Sasha");
  }

  @Test
  public void clientIsAddedAndDisplayed() {
    Stylist myStylist = new Stylist("Sasha");
    myStylist.save();
    Client firstClient = new Client("Michelle", myStylist.getId());
    firstClient.save();
    Client secondClient = new Client("Katie", myStylist.getId());
    secondClient.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Michelle");
    assertThat(pageSource()).contains("Katie");
  }
}
