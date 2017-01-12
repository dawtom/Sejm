import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by Dawid Tomasiewicz on 23.12.16.
 */
public class ParliamentSeeker {

    private String json = "";

    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {

            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

    }

    public void display(Options options) {
        System.out.println(getStringToDisplay(options));
    }

    public String getStringToDisplay(Options options) {
        String result = "";
        switch (options.getOptionNumber()){
            case 1:{
                result = getSumOfPaymentsWithGivenMemberNameOpNo1(options);
                break;
            }
            case 2:{
                result = getDrobneNaprawyPaymentsWithGivenNameOpNo2(options);
                break;
            }
            case 3:{
                result = getAverageSumOfPaymentsOpNo3(options);
                break;
            }
            case 4:{
                result = getMaxNumberOfVoyagesOpNo4(options);
                break;
            }
            case 5:{
                result = getMaxNumberOfDaysAbroadOpNo5(options);
                break;
            }
         /*   case 6:{
                result = executeOptionNumber6(options);
                break;
            }
            case 7:{
                result = executeOptionNumber7(options);
                break;
            }*/
        }

        return result;
    }

/*
    private String executeOptionNumber7(Options options){
        String result = "";
        String countryCodeTemplate = "IT";
        List<String> idList = idList(options);
        List<String> resultList = new LinkedList<String>();
        Boolean hasVisitedItaly;
        String hasVisitedItalyMemberId = "";
        String hasVisitedItalyMemberFullName = "";
        for (String id :
                idList) {
            hasVisitedItaly = false;
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/"+ id + ".json?layers[]=wyjazdy");
            }
            catch (Exception e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }

            try {
                JSONObject obj = new JSONObject(json);
                JSONObject tmp = obj.getJSONObject("layers");
                try {
                    JSONObject tmp2 = tmp.getJSONObject("wyjazdy");        //exception driven developement
                }catch (JSONException e){
                    try {
                        for (int i = 0; i < tmp.getJSONArray("wyjazdy").length() && !hasVisitedItaly; i++) {
                            if (tmp.getJSONArray("wyjazdy").getJSONObject(i).getString("country_code")
                                    .equals(countryCodeTemplate)){
                                hasVisitedItaly = true;
                                resultList.add("\nname: " +
                                        obj.getJSONObject("data").getString("ludzie.nazwa"));
                            }
                         }
                    }
                    catch (JSONException e2){
                        System.out.println("Internal application error: \n" + e.toString());
                        exit(1);
                    }
                }

                System.out.println(hasVisitedItaly.toString() + "     processing...");
            }catch (JSONException e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }
        }

        result = resultList.toString();




        return result;
    }

    private String executeOptionNumber6(Options options){
        String result = "";
        Double tmpMostExpensiveVoyage = 0.0;
        Double maxMostExpensiveVoyage = 0.0;
        Double tmpTmpMostExpensiveJourney = 0.0;
        List<String> idList = idList(options);
        String mostExpensiveVoyageMemberId = "";
        String mostExpensiveVoyageMemberFullName = "";
        for (String id :
                idList) {
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/"+ id + ".json?layers[]=wyjazdy");
            }
            catch (Exception e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }

            try {
                JSONObject obj = new JSONObject(json);
                tmpMostExpensiveVoyage = 0.0;//obj.getJSONObject("layers").getJSONArray("wyjazdy").length();
                JSONObject tmp = obj.getJSONObject("layers");
                try {
                    JSONObject tmp2 = tmp.getJSONObject("wyjazdy");        //exception driven developement: FIXME
                    tmpMostExpensiveVoyage = 0.0;
                }catch (JSONException e){
                    try {
                        tmpTmpMostExpensiveJourney = 0.0;
                        for (int i = 0; i < tmp.getJSONArray("wyjazdy").length(); i++) {
                            tmpTmpMostExpensiveJourney += Double.parseDouble
                                    (tmp.getJSONArray("wyjazdy").getJSONObject(i).getString("koszt_suma"));
                        }
                        tmpMostExpensiveVoyage = tmpTmpMostExpensiveJourney;

                    }
                    catch (JSONException e2){
                        System.out.println("Internal application error: \n" + e.toString());
                        exit(1);
                    }
                }

                if (tmpMostExpensiveVoyage >= maxMostExpensiveVoyage){
                    maxMostExpensiveVoyage = tmpMostExpensiveVoyage;
                    mostExpensiveVoyageMemberId = obj.getString("id");
                    mostExpensiveVoyageMemberFullName = obj.getJSONObject("data").getString("ludzie.nazwa");
                }
                System.out.println(tmpMostExpensiveVoyage + "  ::  " + maxMostExpensiveVoyage  + "      processing...");
            }catch (JSONException e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }
        }

        result = "id: " + mostExpensiveVoyageMemberId +
                "\n name: " + mostExpensiveVoyageMemberFullName +
                "\n Most expensive voyage cost was : " +  String.format("%.2f",maxMostExpensiveVoyage);




        return result;
    }
*/
    private String getMaxNumberOfDaysAbroadOpNo5(Options options){
        String result = "";
        Integer tmpDaysAbroad = 0;
        Integer maxDaysAbroad = 0;
        List<ParliamentMember> idList = idList(options);
        ParliamentMember parliamentMember = null;
        ParliamentMember maxDaysAbroadMember=null;

        for (ParliamentMember member :
                idList) {
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/"+ member.getId() + ".json?layers[]=wyjazdy");
            }
            catch (Exception e){
                handleException(e);
            }

            try {
                JSONObject obj = new JSONObject(json);
                tmpDaysAbroad = 0;
                parliamentMember = new ParliamentMember(obj);
                JSONObject tmp = obj.getJSONObject("layers");
                for (Voyage voyage :
                        parliamentMember.getVoyages()) {
                    tmpDaysAbroad += Integer.parseInt(voyage.getLiczbaDni());
                }

                if (tmpDaysAbroad >= maxDaysAbroad){
                    maxDaysAbroad = tmpDaysAbroad;
                    maxDaysAbroadMember=parliamentMember;
                }
                System.out.println(tmpDaysAbroad + "  ::  " + maxDaysAbroad  + "      processing...");
            }catch (JSONException e){
                handleJSONException(e);
            }
        }

        result = "id: " + maxDaysAbroadMember.getId() +
                "\n name: " + maxDaysAbroadMember.getName() +
                "\n DaysAbroad: " +  maxDaysAbroad;

        return result;
    }

    private String getMaxNumberOfVoyagesOpNo4(Options options){
        String result = "";
        Integer tmpNumberOfVoyages = 0;
        Integer maxNumberOfVoyages = -1 ;
        ParliamentMember parliamentMember = null;
        ParliamentMember maxNumberOfVoyagesMember = null;

        List<ParliamentMember> members = idList(options);
        for (ParliamentMember member :
                members) {
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/"+ member.getId() + ".json?layers[]=wyjazdy");
            }
            catch (Exception e){
                handleException(e);
            }

            try {
                JSONObject obj = new JSONObject(json);
                parliamentMember = new ParliamentMember(obj);
                tmpNumberOfVoyages = 0;
                JSONObject tmp = obj.getJSONObject("layers");
                tmpNumberOfVoyages = parliamentMember.getVoyages().size();

                if (tmpNumberOfVoyages >= maxNumberOfVoyages){
                    maxNumberOfVoyages = tmpNumberOfVoyages;
                    maxNumberOfVoyagesMember = parliamentMember;
                }
                System.out.println(tmpNumberOfVoyages + "  ::  " + maxNumberOfVoyages  + "      processing...");
            }catch (JSONException e){
                handleJSONException(e);
            }
        }

        result = "id: " + maxNumberOfVoyagesMember.getId() +
                "\n name: " + maxNumberOfVoyagesMember.getName() +
                "\n Number of voyages: " +  maxNumberOfVoyages;

        return result;
    }

    private String getAverageSumOfPaymentsOpNo3(Options options){
        String result = "";
        Double sumOfPayments = 0.0;
        Double averagePayments = 0.0;
        Integer count = 0;
        ParliamentMember parliamentMember = null;

        try {
            json = readUrl(
                    "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=" +
                            options.getParliamentTerm().toString());

        }
        catch (Exception e){
            System.out.println("Internal application error: \n" + e.toString());
            exit(1);
        }

        try{
            JSONObject obj = new JSONObject(json);
            String next = "";
            count = obj.getInt("Count");

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 50 && i < count; j++) {
                    parliamentMember = new ParliamentMember(obj.getJSONArray("Dataobject").getJSONObject(j));

                    try {
                        sumOfPayments += parliamentMember.getSumOfPayments();
                        System.out.println("Nr w API " + i + "           processing . . .");
                    }
                    catch (JSONException e){
                        handleJSONException(e);
                    }
                    i++;
                }
                i--;
                try {
                    next = obj.getJSONObject("Links").getString("next");

                }catch (JSONException e){
                    //System.out.println("Internal application error: \n" + e.toString());
                }

                try {
                    json = readUrl(next);
                    obj = new JSONObject(json);
                }
                catch (Exception e){
                    handleException(e);
                }
            }
        }
        catch (Exception e){
            handleException(e);
        }

        averagePayments = sumOfPayments/count;
        result = String.format("%.2f",averagePayments);

        return result;
    }

    private String getDrobneNaprawyPaymentsWithGivenNameOpNo2(Options options) {
        String result = "";
        String searchID="";
        Double drobneNaprawy = 0.0;
        boolean findMember = false;
        String searchName = options.getMemberOfParliamentFirstName() + " " + options.getMemberOfParliamentLastName();
        ParliamentMember parliamentMember = null;

        try {
            json = readUrl(
                    "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=" +
                            options.getParliamentTerm().toString());
        }
        catch (Exception e){
            handleException(e);
        }

        try{
            JSONObject obj = new JSONObject(json);
            String next = "";

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count && !findMember; i++) {
                for (int j = 0; j < 50 && i < count && !findMember; j++) {
                    parliamentMember = new ParliamentMember(obj.getJSONArray("Dataobject").getJSONObject(j));

                    if (parliamentMember.getName().equals(searchName)){
                        findMember = true;
                        searchID = parliamentMember.getId();
                    }
                    i++;
                }
                i--;

                try {
                    next = obj.getJSONObject("Links").getString("next");
                }
                catch (JSONException e){
                    //handleJSONException(e);
                }
                try {
                    json = readUrl(next);
                    obj = new JSONObject(json);
                } catch (Exception e) {
                    handleException(e);
                }

            }
        }
        catch (JSONException e){
            handleJSONException(e);
        }

        if (findMember){
            drobneNaprawy += parliamentMember.getDrobneNaprawy();
            result = "id: " + parliamentMember.getId() + "\nname: " + parliamentMember.getName() + "\ndrobne naprawy: "
                    + String.format("%.2f",drobneNaprawy);

        }
        else{
            result += "Member " + searchName + " not found in this parliament";
        }

        return result;
    }

    private String getSumOfPaymentsWithGivenMemberNameOpNo1(Options options) {
        String result = "";
        Double sumOfPayments = 0.0;
        boolean findMember = false;
        String searchName = options.getMemberOfParliamentFirstName() + " " + options.getMemberOfParliamentLastName();
        ParliamentMember parliamentMember = null;

        try {
            json = readUrl(
                    "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=" +
                            options.getParliamentTerm().toString());
        }
        catch (Exception e){
            handleException(e);
        }

        try{
            JSONObject obj = new JSONObject(json);
            String next = "";

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count && !findMember; i++) {
                for (int j = 0; j < 50 && i < count && !findMember; j++) {
                    parliamentMember = new ParliamentMember(obj.getJSONArray("Dataobject").getJSONObject(j));

                    if (parliamentMember.getName().equals(searchName)){
                        findMember = true;
                    }
                    i++;
                }
                i--;

                try {

                    next = obj.getJSONObject("Links").getString("next");
                    json = readUrl(next);
                    obj = new JSONObject(json);
                }
                catch (JSONException e){
                    handleJSONException(e);
                }
            }
            sumOfPayments = parliamentMember.getSumOfPayments();
        }
        catch (Exception e){
            handleException(e);
        }


        if (findMember){
            result += sumOfPayments.toString();
        }
        else{
            result += "Member " + searchName + " not found in this parliament";
        }

        return result;
    }


    //Create list of all id's of members from specified term
    private List<ParliamentMember> idList(Options options){
        ParliamentMember parliamentMember = null;
        List<ParliamentMember> result = new ArrayList<ParliamentMember>();
        try {
            json = readUrl(
                    "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=" +
                            options.getParliamentTerm().toString());

        }
        catch (Exception e){
            handleException(e);
        }

        try{
            JSONObject obj = new JSONObject(json);
            String next = "";

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 50 && i < count; j++) {
                    parliamentMember = new ParliamentMember(obj.getJSONArray("Dataobject").getJSONObject(j));
                    result.add(parliamentMember);
                    i++;
                }
                i--;
                try {
                    next = obj.getJSONObject("Links").getString("next");
                }catch (JSONException e){

                }

                try {
                    json = readUrl(next);
                    obj = new JSONObject(json);
                }
                catch (Exception e){
                    System.out.println("Internal application error: \n" + e.toString());
                    exit(1);
                }
            }

        }
        catch (Exception e){
            handleException(e);
        }

        return result;
    }





    public static void handleJSONException(JSONException e){
        System.out.println("Internal application error related to JSON: \n" + e.toString());
        exit(1);
    }

    public static void handleException(Exception e) {
        System.out.println("Internal application error: \n" + e.toString());
        exit(1);
    }

}