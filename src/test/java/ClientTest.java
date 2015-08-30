import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;


public class ClientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Client.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfClientNamesAretheSame() {
    Client firstClient = new Client("Michelle", 1);
    Client secondClient = new Client("Michelle", 1);
    assertTrue(firstClient.equals(secondClient));
  }

  @Test
  public void saves_clientNameIntoDatabase_true() {
    Client myClient = new Client("Michelle", 1);
    myClient.save();
    assertTrue(Client.all().get(0).equals(myClient));
  }

  @Test
  public void save_assignsIdToObject() {
    Client myClient = new Client("Michelle", 1);
    myClient.save();
    Client savedClient = Client.all().get(0);
    assertEquals(myClient.getId(), savedClient.getId());
  }

  @Test
  public void find_ClientInDatabase_true() {
    Client myClient = new Client("Michelle", 1);
    myClient.save();
    Client savedClient = Client.find(myClient.getId());
    assertTrue(myClient.equals(savedClient));
  }

  @Test
  public void save_StylistIdIntoDB() {
    Stylist myStylist = new Stylist ("Sasha");
    myStylist.save();
    Client myClient = new Client ("Michelle", myStylist.getId());
    myClient.save();
    Client savedClient = Client.find(myClient.getId());
    assertEquals(savedClient.getStylistId(), myStylist.getId());
  }
}
