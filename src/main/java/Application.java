import java.io.*;
import java.net.URL;

public class Application {
    public static void main(String[] args) {
        Options options = new OptionsParser().parseOptions(args);
        ParliamentDisplayer parliamentDisplayer = new ParliamentDisplayer();
        parliamentDisplayer.display(options);

    }
}
