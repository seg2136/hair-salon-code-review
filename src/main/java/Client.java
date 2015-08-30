import java.util.List;
import org.sql2o.*;


public class Client {
  private int id;
  private String client_name;
  private int stylist_id;

  public int getId() {
    return id;
  }

  public String getClientName() {
    return client_name;
  }

  public int getStylistId() {
    return stylist_id;
  }

  public Client(String client_name, int stylist_id) {
    this.client_name = client_name;
    this.stylist_id = stylist_id;
  }

  @Override
  public boolean equals(Object otherClient){
    if (!(otherClient instanceof Client)) {
      return false;
    } else {
      Client newClient = (Client) otherClient;
      return this.getClientName().equals(newClient.getClientName()) &&
             this.getId() == newClient.getId() &&
             this.getStylistId() == newClient.getStylistId();
    }
  }

  public static List<Client> all() {
    String sql = "SELECT id, client_name, stylist_id FROM clients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

//   public static List<Restaurant> allCuisineRestaurants(int cuisine_id) {
//     String sql = "SELECT id, name, cuisine_id FROM restaurants WHERE cuisine_id=:cuisine_id";
//     try(Connection con = DB.sql2o.open()) {
//       return con.createQuery(sql)
//         .addParameter("cuisine_id", cuisine_id)
//         .executeAndFetch(Restaurant.class);
//     }
//   }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO clients (client_name, stylist_id) VALUES (:client_name, :stylist_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("client_name", this.client_name)
        .addParameter("stylist_id", this.stylist_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Client find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients where id=:id";
      Client client = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Client.class);
          return client;
    }
  }

//   public void delete() {
//     try(Connection con = DB.sql2o.open()) {
//       String sql = "DELETE FROM restaurants WHERE id = :id;";
//       con.createQuery(sql)
//         .addParameter("id", id)
//         .executeUpdate();
//     }
//   }

//   public void deleteRestaurants() {
//     try(Connection con = DB.sql2o.open()) {
//       String sql = "DELETE FROM restaurants WHERE cuisine_id=:id AND id>=1";
//       con.createQuery(sql)
//           .addParameter("id", id)
//           .addParameter("cuisine_id", cuisine_id)
//           .executeUpdate();
//         }
//       }

//   public void update(String name) {
//     try(Connection con = DB.sql2o.open()) {
//       String sql = "UPDATE restaurants SET name = :name WHERE id=:id";
//       con.createQuery(sql)
//         .addParameter("name", name)
//         .addParameter("id", id)
//         .executeUpdate();
//     }
//   }
}
