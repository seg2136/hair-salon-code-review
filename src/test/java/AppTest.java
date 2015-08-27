// import java.util.ArrayList;

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
    assertThat(pageSource()).contains("Your Favorite Restaurants!");
  }

  @Test
  public void cuisineIsCreatedTest() {
    goTo("http://localhost:4567/");
    fill("#description").with("Italian");
    submit(".btn");
    assertThat(pageSource()).contains("Italian");
  }

  @Test
  public void cuisineIsDisplayedTest() {
    Cuisine myCuisine = new Cuisine("Italian");
    myCuisine.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
    goTo(cuisinePath);
    assertThat(pageSource()).contains("Italian");
  }

  @Test
  public void cuisineRestaurantsFormIsDisplayed() {
    Cuisine myCuisine = new Cuisine("Italian");
    myCuisine.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
    goTo(cuisinePath);
    assertThat(pageSource()).contains("Add a Restaurant to Italian");
  }



  @Test
  public void restaurantIsAddedAndDisplayed() {
    Cuisine myCuisine = new Cuisine("Italian");
    myCuisine.save();
    Restaurant firstRestaurant = new Restaurant("Olive Garden", myCuisine.getId());
    firstRestaurant.save();
    Restaurant secondRestaurant = new Restaurant("Palermo", myCuisine.getId());
    secondRestaurant.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
    goTo(cuisinePath);
    assertThat(pageSource()).contains("Olive Garden");
    assertThat(pageSource()).contains("Palermo");
  }
}
