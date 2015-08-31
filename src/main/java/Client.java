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

  public static List<Client> allStylistClients(int stylist_id) {
    String sql = "SELECT id, client_name, stylist_id FROM clients WHERE stylist_id=:stylist_id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("stylist_id", stylist_id)
        .executeAndFetch(Client.class);
    }
  }

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

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void deleteAllClients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE stylist_id=:id AND id>=1";
      con.createQuery(sql)
          .addParameter("id", id)
          .addParameter("stylist_id", stylist_id)
          .executeUpdate();
        }
      }

  public void update(String client_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET client_name = :client_name WHERE id=:id";
      con.createQuery(sql)
        .addParameter("client_name", client_name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}
