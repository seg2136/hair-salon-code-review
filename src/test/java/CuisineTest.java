import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;


public class CuisineTest {
//
    @Rule
    public DatabaseRule database = new DatabaseRule();


    @Test
    public void all_emptyAtFirst() {
      assertEquals(Cuisine.all().size(), 0);
    }

    @Test
    public void equals_returnsTrueIfDescriptionsAretheSame() {
      Cuisine firstCuisine = new Cuisine("Italian");
      Cuisine secondCuisine = new Cuisine("Italian");
      assertTrue(firstCuisine.equals(secondCuisine));
    }

    @Test
    public void save_savesIntoDatabase_true() {
      Cuisine myCuisine = new Cuisine("Italian");
      myCuisine.save();
      assertTrue(Cuisine.all().get(0).equals(myCuisine));
    }

    @Test
    public void find_findsCuisineInDatabase_true() {
      Cuisine myCuisine = new Cuisine("Italian");
      myCuisine.save();
      Cuisine savedCuisine = Cuisine.find(myCuisine.getId());
      assertTrue(myCuisine.equals(savedCuisine));
    }

    @Test
    public void getRestaurants_retrievesAllRestaurantFromDatabase_restaurantsList() {
      Cuisine myCuisine = new Cuisine("Italian");
      myCuisine.save();
      Restaurant firstRestaurant = new Restaurant("McDonalds", myCuisine.getId());
      firstRestaurant.save();
      Restaurant secondRestaurant = new Restaurant("Olive Garden", myCuisine.getId());
      secondRestaurant.save();
      Restaurant[] restaurants = new Restaurant[] {firstRestaurant, secondRestaurant};
      assertTrue(myCuisine.getRestaurants().containsAll(Arrays.asList(restaurants)));
    }

}
