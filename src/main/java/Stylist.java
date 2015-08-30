import java.util.List;
import org.sql2o.*;


public class Stylist {
  private int id;
  private String stylist_name;

  public int getId() {
    return id;
  }

  public String getStylistName() {
    return stylist_name;
  }

  public Stylist (String stylist_name) {
    this.stylist_name = stylist_name;
  }

  public static List<Stylist> all() {
    String sql = "SELECT id, stylist_name FROM stylists";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  @Override
  public boolean equals(Object otherStylist) {
    if (!(otherStylist instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylist = (Stylist) otherStylist;
      return this.getStylistName().equals(newStylist.getStylistName()) &&
             this.getId() == newStylist.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stylists (stylist_name) VALUES (:stylist_name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("stylist_name", this.stylist_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Stylist find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE id=:id";
      Stylist stylist = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
        return stylist;
    }
  }

  public List<Client> getClients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE stylist_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Client.class);
    }
  }

//   public void delete() {
//     try(Connection con = DB.sql2o.open()) {
//       String sql = "DELETE FROM cuisines WHERE id = :id";
//       con.createQuery(sql)
//         .addParameter("id", id)
//         .executeUpdate();
//     }
//   }


//     public void update(String description) {
//       try(Connection con = DB.sql2o.open()) {
//         String sql = "UPDATE cuisines SET description = :description WHERE id = :id";
//         con.createQuery(sql)
//           .addParameter("description", description)
//           .addParameter("id", id)
//           .executeUpdate();
//       }
//     }

}
