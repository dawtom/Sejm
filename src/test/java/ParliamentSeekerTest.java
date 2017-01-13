import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dawid Tomasiewicz on 03.01.17.
 */
public class ParliamentSeekerTest {


    @Test
    public void display() throws Exception {
        String[] args = {"8", "2","Ewa","Kopacz"};
        Options options = new OptionsParser().parseOptions(args);
        assertTrue(new ParliamentDisplayer().getStringToDisplay(options).endsWith("170,50"));
    }

}