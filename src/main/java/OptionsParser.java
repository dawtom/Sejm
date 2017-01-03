import static java.lang.System.exit;
import static java.lang.System.setOut;

/**
 * Created by Dawid Tomasiewicz on 23.12.16.
 */
public class OptionsParser {


    public Options parseOptions(String [] args){
        Options tmpOptions = new Options();
        String message = "\nCorrect arguments are: <numberOfTerm> <numberOfOption> <FirstName> <LastName> \n" +
                "If numberOfOption =1 or 2 FirstName and LastName are obligatory, otherwise will be ignored\n"
                +"numberOfTerm should be 7 or 8 (Sejmometr API does not provide us with information about any other terms\n"
                +"numberOfOption should be between 1 and 7 (both inclusive). Options are:\n"
                +"1) Lorem ipsum pan doktor wie o co chodzi\n"
                +"2) Dolor\n"
                +"3) I tu byłby opis wszystkich opcji\n";

        if (args.length == 0){
            System.out.println("No arguments" + message);
            exit(1);
        }

        if (args.length < 2){
            System.out.println("Too few arguments." + message);
            exit(1);
        }


        if (args.length > 4){
            System.out.println("Too many arguments. Maximum four will be used (depends on the second argument)." + message);
        }

        try {
            Integer args0 = Integer.parseInt(args[0]);
            tmpOptions.setParliamentTerm(args0);
            if (args0 < 7 || args0 > 8 ){
                System.out.println("Invalid term number. Only information about 7th and 8th term is provided" + message);
                exit(1);
            }
        }
        catch (NumberFormatException e){
            System.out.println("The first argument should be an integer." + message);
            exit(1);
        }

        try {
            Integer args1 = Integer.parseInt(args[1]);
            tmpOptions.setOptionNumber(args1);
            if (args1 < 1 || args1 > 7){
                System.out.println("Invalid option number. It should be between 1 and 7 inclusive" + message);
                exit(1);
            }

        }
        catch (NumberFormatException e){
            System.out.println("The second argument should be an integer." + message);
            exit(1);
        }

        if (tmpOptions.getOptionNumber() >= 3 && args.length >= 3){
            System.out.println("Some arguments are redundant and will not be used");
        }
        if (tmpOptions.getOptionNumber() <= 2){
            try {
                tmpOptions.setMemberOfParliamentFirstName(args[2]);
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Third argument necessary" + message);
                exit(1);
            }
            try {
                tmpOptions.setMemberOfParliamentLastName(args[3]);
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Fourth argument necessary" + message);
                exit(1);
            }
        }

        return tmpOptions;
    }
}
