import static org.junit.Assert.*;

/**
 * Created by Dawid Tomasiewicz on 03.01.17.
 */
public class OptionsParserTest {




    @org.junit.Test
    public void parseOptions() throws Exception {
        String[] args = {"8", "1","Ewa","Kopacz"};
        Options options = new OptionsParser().parseOptions(args);
        assertEquals(options.getMemberOfParliamentFirstName(),"Ewa");

        String[] args2 = {"8", "1","Jarosław","Kaczyński"};
        options = new OptionsParser().parseOptions(args2);
        assertEquals(options.getMemberOfParliamentLastName(),"Kaczyński");
    }

}