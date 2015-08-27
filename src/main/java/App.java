import java.util.HashMap;
// import java.util.ArrayList;
// import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
   staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    //HTTP GET METHOD FOR DISPLAYING HOMEPAGE
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      //categories here can be anything as long as it matches $categories
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST METHOD FOR SAVING CUISINES
    post("/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Cuisine newCuisine = new Cuisine(description);
      newCuisine.save();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED CUISINES
    get("/cuisines/:id", (request, response) -> {
      response.redirect("/cuisines/" + request.params(":id") + "/restaurants");
      return null;
    });

    //HTTP POST METHOD FOR SAVING RESTAURANTS TO CUISINE
    post("/cuisines/:id/restaurants", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      Restaurant newRestaurant = new Restaurant(name, cuisine.getId());
      newRestaurant.save();
      model.put("cuisine", cuisine);
      model.put("restaurant", newRestaurant);
      model.put("restaurants", Restaurant.allCuisineRestaurants(Integer.parseInt(request.params(":id"))));
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED RESTAURANTS ON CUISINE PAGE
    get("/cuisines/:id/restaurants", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("restaurants", Restaurant.allCuisineRestaurants(Integer.parseInt(request.params(":id"))));
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET/POST METHODS FOR UPDATING CUISINE NAME
    get("cuisines/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("template", "templates/update-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("cuisines/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      String description = request.queryParams("description");
      cuisine.update(description);
      response.redirect("/");
      return null;
    });

    //HTTP GET/POST METHODS FOR UPDATING RESTAURANT NAME
    get("/cuisines/:cuisine_id/restaurants/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      model.put("restaurant", restaurant);
      model.put("cuisine", cuisine);
      model.put("template", "templates/restaurant-update-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/restaurants/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      String name = request.queryParams("name");
      restaurant.update(name);
      response.redirect("/cuisines/" + cuisine.getId() + "/restaurants");
      return null;
    });

    //HTTP POST DELETE METHOD FOR CUISINE
    post("/cuisines/:id/delete", (request, response) -> {
      //need to put :id in the url so that we can grab it below
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("template", "templates/index.vtl");
      cuisine.delete();
      model.put("cuisines", Cuisine.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST DELETE METHOD FOR RESTAURANT
    post("/cuisines/:cuisine_id/restaurants/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      model.put("template", "templates/cuisine.vtl");
      restaurant.delete();
      model.put("cuisine", cuisine);
      model.put("restaurants", Restaurant.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/restaurants/delete-all", (request, response) -> {
      //need to put :id in the url so that we can grab it below
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      Restaurant restaurant = new Restaurant(name, cuisine.getId());
      restaurant.deleteRestaurants();
      model.put("cuisine", cuisine);
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }
}
