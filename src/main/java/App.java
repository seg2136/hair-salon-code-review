import java.util.HashMap;
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
      model.put("stylists", Stylist.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST METHOD FOR SAVING STYLISTS
    post("/stylists", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String stylistName = request.queryParams("stylist_name");
      Stylist newStylist = new Stylist(stylistName);
      newStylist.save();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED STYLISTS
    get("/stylists/:id", (request, response) -> {
      response.redirect("/stylists/" + request.params(":id") + "/clients");
      return null;
    });

    //HTTP POST METHOD FOR SAVING CLIENTS TO STYLIST
    post("/stylists/:id/clients", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      String clientName = request.queryParams("client_name");
      Client newClient = new Client(clientName, stylist.getId());
      newClient.save();
      model.put("stylist", stylist);
      model.put("client", newClient);
      model.put("clients", Client.allStylistClients(Integer.parseInt(request.params(":id"))));
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED CLIENTS ON STYLIST'S PAGE
    get("/stylists/:id/clients", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("clients", Client.allStylistClients(Integer.parseInt(request.params(":id"))));
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET/POST METHODS FOR UPDATING STYLIST'S NAME
    get("stylists/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("template", "templates/update-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("stylists/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      String stylist_name = request.queryParams("stylist_name");
      stylist.update(stylist_name);
      response.redirect("/");
      return null;
    });

    //HTTP GET/POST METHODS FOR UPDATING CLIENT'S NAME
    get("/stylists/:stylist_id/clients/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Client client = Client.find(Integer.parseInt(request.params(":id")));
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":stylist_id")));
      model.put("client", client);
      model.put("stylist", stylist);
      model.put("template", "templates/client-update-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:stylist_id/clients/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Client client = Client.find(Integer.parseInt(request.params(":id")));
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":stylist_id")));
      String client_name = request.queryParams("client_name");
      client.update(client_name);
      response.redirect("/stylists/" + stylist.getId() + "/clients");
      return null;
    });

    //HTTP POST DELETE METHOD FOR STYLIST
    post("/stylists/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("template", "templates/index.vtl");
      stylist.delete();
      model.put("stylists", Stylist.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST DELETE METHOD FOR CLIENT
    post("/stylists/:stylist_id/clients/:id/delete", (request, response) -> {
      Client client = Client.find(Integer.parseInt(request.params(":id")));
      client.delete();
      response.redirect("/stylists/" + request.params(":stylist_id") + "/clients");
      return null;
    });
  }
}