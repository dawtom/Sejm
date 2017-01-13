/**
 * Created by Dawid Tomasiewicz on 13.01.17.
 */
public class ParliamentDisplayer {

    ParliamentSeeker parliamentSeeker = new ParliamentSeeker();

    public void display(Options options) {
        System.out.println(getStringToDisplay(options));
    }

    public String getStringToDisplay(Options options) {
        String result = "";
        switch (options.getOptionNumber()){
            case 1:{
                result = parliamentSeeker.getSumOfPaymentsWithGivenMemberNameOpNo1(options);
                break;
            }
            case 2:{
                result = parliamentSeeker.getDrobneNaprawyPaymentsWithGivenNameOpNo2(options);
                break;
            }
            case 3:{
                result = parliamentSeeker.getAverageSumOfPaymentsOpNo3(options);
                break;
            }
            case 4:{
                result = parliamentSeeker.getMaxNumberOfVoyagesOpNo4(options);
                break;
            }
            case 5:{
                result = parliamentSeeker.getMaxNumberOfDaysAbroadOpNo5(options);
                break;
            }
            case 6:{
                result = parliamentSeeker.getMostExpensiveVoyageOfMemberOpNo6(options);
                break;
            }
            case 7:{
                result = parliamentSeeker.getMembersHavingVisitedItalyOpNo7(options);
                break;
            }
        }

        return result;
    }
}
