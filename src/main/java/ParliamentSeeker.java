import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
            case 6:{
                result = getMostExpensiveVoyageOfMemberOpNo6(options);
                break;
            }
            case 7:{
                result = getMembersHavingVisitedItalyOpNo7(options);
                break;
            }
        }

        return result;
    }


    private String getMembersHavingVisitedItalyOpNo7(Options options){
        List<ParliamentMember> idList = memberList(options);
        List<String> resultList = new LinkedList<String>();

        ParliamentMember parliamentMember;
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
                parliamentMember = new ParliamentMember(obj);

                System.out.println(parliamentMember.getHasVisitedItaly() + "     processing...");
                if (parliamentMember.getHasVisitedItaly()){
                    resultList.add(parliamentMember.getName());
                }
            }catch (JSONException e){
                handleJSONException(e);
            }
        }



        return resultList.toString();
    }

    private String getMostExpensiveVoyageOfMemberOpNo6(Options options){
        String result = "";
        Double tmpMostExpensiveVoyage;
        Double maxMostExpensiveVoyage = 0.0;
        List<ParliamentMember> idList = memberList(options);
        ParliamentMember parliamentMember;
        ParliamentMember mostExpensiveVoyageMember = null;

        for (ParliamentMember  member :
                idList) {
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/"+ member.getId() + ".json?layers[]=wyjazdy");
            }
            catch (Exception e){
                handleException(e);
            }

            try {
                JSONObject obj = new JSONObject(json);
                tmpMostExpensiveVoyage = 0.0;

                parliamentMember = new ParliamentMember(obj);
                for (Voyage voyage :
                        parliamentMember.getVoyages()) {

                    Double kosztSuma =  Double.parseDouble(voyage.getKosztSuma());
                    if (kosztSuma > tmpMostExpensiveVoyage){
                        tmpMostExpensiveVoyage = kosztSuma;
                    }
                }
                if (tmpMostExpensiveVoyage >= maxMostExpensiveVoyage){
                    maxMostExpensiveVoyage = tmpMostExpensiveVoyage;
                    mostExpensiveVoyageMember = parliamentMember;
                }
                System.out.println(tmpMostExpensiveVoyage + "  ::  " + maxMostExpensiveVoyage  + "      processing...");
            }catch (JSONException e){
                handleJSONException(e);
            }
        }

        result = "id: " + mostExpensiveVoyageMember.getId() +
                "\n name: " + mostExpensiveVoyageMember.getName() +
                "\n Most expensive voyage cost was : " +  String.format("%.2f",maxMostExpensiveVoyage);


        return result;
    }

    private String getMaxNumberOfDaysAbroadOpNo5(Options options){
        String result;
        Integer tmpDaysAbroad;
        Integer maxDaysAbroad = 0;
        List<ParliamentMember> idList = memberList(options);
        ParliamentMember parliamentMember;
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
                "\nname: " + maxDaysAbroadMember.getName() +
                "\nDaysAbroad: " +  maxDaysAbroad;

        return result;
    }

    private String getMaxNumberOfVoyagesOpNo4(Options options){
        String result;
        Integer tmpNumberOfVoyages;
        Integer maxNumberOfVoyages = -1 ;
        ParliamentMember parliamentMember;
        ParliamentMember maxNumberOfVoyagesMember = null;

        List<ParliamentMember> members = memberList(options);
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

        String result;
        Double sumOfPayments = 0.0;
        Double averagePayments;
        Integer count = 0;
        ParliamentMember parliamentMember;

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
            String next;

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


    //Create list of all members from specified term
    private List<ParliamentMember> memberList(Options options){
        ParliamentMember parliamentMember;
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