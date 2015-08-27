import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  protected void before() {
  DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/best_restaurant_test", null, null);
}

protected void after() {
  try(Connection con = DB.sql2o.open()) {
    String deleteCuisinesQuery = "DELETE FROM cuisines *;";
    String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
    con.createQuery(deleteCuisinesQuery).executeUpdate();
    con.createQuery(deleteRestaurantsQuery).executeUpdate();
  }
}
}
