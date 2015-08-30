import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;


public class StylistTest {

    @Rule
    public DatabaseRule database = new DatabaseRule();


    @Test
    public void all_emptyAtFirst() {
      assertEquals(Stylist.all().size(), 0);
    }

    @Test
    public void equals_returnsTrueIfStylistNamesAreTheSame() {
      Stylist firstStylist = new Stylist("Sasha");
      Stylist secondStylist = new Stylist("Sasha");
      assertTrue(firstStylist.equals(secondStylist));
    }

    @Test
    public void save_stylistNameIntoDatabase_true() {
      Stylist myStylist = new Stylist("Sasha");
      myStylist.save();
      assertTrue(Stylist.all().get(0).equals(myStylist));
    }

    @Test
    public void find_StylistInDatabase_true() {
      Stylist myStylist = new Stylist("Sasha");
      myStylist.save();
      Stylist savedStylist = Stylist.find(myStylist.getId());
      assertTrue(myStylist.equals(savedStylist));
    }

    @Test
    public void getClients_retrievesAllClientsFromDatabase_true() {
      Stylist myStylist = new Stylist("Sasha");
      myStylist.save();
      Client firstClient = new Client("Michelle", myStylist.getId());
      firstClient.save();
      Client secondClient = new Client("Chelsea", myStylist.getId());
      secondClient.save();
      Client[] clients = new Client[] {firstClient, secondClient};
      assertTrue(myStylist.getClients().containsAll(Arrays.asList(clients)));
    }

}
