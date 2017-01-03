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

    private String readUrl(String urlString) throws Exception {
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

    private String getStringToDisplay(Options options) {
        String result = "";
        switch (options.getOptionNumber()){
            case 1:{
                result = executeOptionNumber1(options);
                break;
            }
            case 2:{
                result = executeOptionNumber2(options);
                break;
            }
            case 3:{
                result = executeOptionNumber3(options);
                break;
            }
            case 4:{
                result = executeOptionNumber4(options);
                break;
            }
            case 5:{
                result = executeOptionNumber5(options);
                break;
            }
            case 6:{
                result = executeOptionNumber6(options);
                break;
            }
            case 7:{
                result = executeOptionNumber7(options);
                break;
            }
        }

        return result;
    }

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
                    JSONObject tmp2 = tmp.getJSONObject("wyjazdy");        //exception driven developement: FIXME bo aż boli
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

    private String executeOptionNumber5(Options options){
        String result = "";
        Integer tmpDaysAbroad = 0;
        Integer maxDaysAbroad = 0;
        Integer tmpTmpDaysAbroad = 0;
        List<String> idList = idList(options);
        String maxDaysAbroadMemberId = "";
        String maxDaysAbroadMemberFullName = "";
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
                tmpDaysAbroad = 0;//obj.getJSONObject("layers").getJSONArray("wyjazdy").length();
                JSONObject tmp = obj.getJSONObject("layers");
                try {
                    JSONObject tmp2 = tmp.getJSONObject("wyjazdy");        //exception driven developement: FIXME
                    tmpDaysAbroad = 0;
                }catch (JSONException e){
                    try {
                        tmpTmpDaysAbroad = 0;
                        for (int i = 0; i < tmp.getJSONArray("wyjazdy").length(); i++) {
                            tmpTmpDaysAbroad += Integer.parseInt
                                    (tmp.getJSONArray("wyjazdy").getJSONObject(i).getString("liczba_dni"));
                        }
                        tmpDaysAbroad = tmpTmpDaysAbroad;

                    }
                    catch (JSONException e2){
                        System.out.println("Internal application error: \n" + e.toString());
                        exit(1);
                    }
                }

                if (tmpDaysAbroad >= maxDaysAbroad){
                    maxDaysAbroad = tmpDaysAbroad;
                    maxDaysAbroadMemberId = obj.getString("id");
                    maxDaysAbroadMemberFullName = obj.getJSONObject("data").getString("ludzie.nazwa");
                }
                System.out.println(tmpDaysAbroad + "  ::  " + maxDaysAbroad  + "      processing...");
            }catch (JSONException e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }
        }

        result = "id: " + maxDaysAbroadMemberId +
                "\n name: " + maxDaysAbroadMemberFullName +
                "\n DaysAbroad: " +  maxDaysAbroad;




        return result;
    }

    private String executeOptionNumber4(Options options){
        String result = "";
        Integer tmpNumberOfVoyages = 0;
        Integer maxNumberOfVoyages = -1 ;
        String maxNumberOfVoyagesMemberId = "";
        String maxNumberOfVoyagesMemberFullName = "";
        List<String> idList = idList(options);
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
                tmpNumberOfVoyages = 0;//obj.getJSONObject("layers").getJSONArray("wyjazdy").length();
                JSONObject tmp = obj.getJSONObject("layers");
                try {
                    JSONObject tmp2 = tmp.getJSONObject("wyjazdy");        //exception driven developement: FIXME
                    tmpNumberOfVoyages = 0;
                }catch (JSONException e){
                    try {
                        tmpNumberOfVoyages = tmp.getJSONArray("wyjazdy").length();
                    }
                    catch (JSONException e2){
                        System.out.println(e.toString());
                    }
                }

                if (tmpNumberOfVoyages >= maxNumberOfVoyages){
                    maxNumberOfVoyages = tmpNumberOfVoyages;
                    maxNumberOfVoyagesMemberId = obj.getString("id");
                    maxNumberOfVoyagesMemberFullName = obj.getJSONObject("data").getString("ludzie.nazwa");
                }
                System.out.println(tmpNumberOfVoyages + "  ::  " + maxNumberOfVoyages  + "      processing...");
            }catch (JSONException e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }
        }

        result = "id: " + maxNumberOfVoyagesMemberId +
                "\n name: " + maxNumberOfVoyagesMemberFullName +
                "\n Number of voyages: " +  maxNumberOfVoyages;

        return result;
    }

    private String executeOptionNumber3(Options options){
        String result = "";

        String searchID="";
        Double sumOfPayments = 0.0;
        Double averagePayments = 0.0;
        Integer count = 0;


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
                    JSONObject res = obj.getJSONArray("Dataobject").getJSONObject(j);
                    JSONObject data = res.getJSONObject("data");

                    try {
                        sumOfPayments += setSumOfPayments(data);
                    }
                    catch (JSONException e){
                        System.out.println("Something wrong with your JSON");
                        System.out.println(e.toString());
                        exit(1);
                    }

                    i++;
                }
                i--;
                try {
                    next = obj.getJSONObject("Links").getString("next");
                }catch (JSONException e){
                    System.out.println("Internal application error: \n" + e.toString());
                    exit(1);
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
            System.out.println("Internal application error: \n" + e.toString());
            exit(1);
        }

        averagePayments = sumOfPayments/count;
        result = String.format("%.2f",averagePayments);

        return result;
    }

    private String executeOptionNumber2(Options options) {
        String result = "";
        String searchID="";
        Double drobneNaprawy = 0.0;
        boolean findMember = false;
        String searchName = options.getMemberOfParliamentFirstName() + " " + options.getMemberOfParliamentLastName();

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

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count && !findMember; i++) {
                for (int j = 0; j < 50 && i < count && !findMember; j++) {
                    JSONObject res = obj.getJSONArray("Dataobject").getJSONObject(j);
                    JSONObject data = res.getJSONObject("data");
//                System.out.println(data.toString());
                    String tmp = data.getString("ludzie.nazwa");
                    if (tmp.equals(searchName)){
                        searchID = res.getString("id");
                        findMember = true;
                    }

                    i++;
                }
                i--;
                try {
                    next = obj.getJSONObject("Links").getString("next");
                }catch (JSONException e){
                    System.out.println("Internal application error: \n" + e.toString());
                    exit(1);
                }

                try {
                    json = readUrl(next);
                    obj = new JSONObject(json);
                }
                catch (Exception e){
                    System.out.println("Internal application error: \n" + e.toString());
                    exit(1);                }
            }
        }
        catch (Exception e){
            System.out.println("Caught exception:\n" + e.toString());
            exit(1);
        }

        if (findMember){
            try {
                json = readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/" + searchID + ".json?layers[]=wydatki");
            }
            catch (Exception e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }

            try {
                JSONObject obj = new JSONObject(json);
                drobneNaprawy += Double.parseDouble(obj.getJSONObject("layers").getJSONObject("wydatki")
                        .getJSONArray("roczniki")
                        .getJSONObject(0).getJSONArray("pola").getString(12));
                //System.out.println(drobneNaprawy.toString());
                result = "id: " + searchID + "\nname: " + searchName + "\ndrobne naprawy: " + drobneNaprawy.toString();
            }
            catch (JSONException e){
                System.out.println("Internal application error: \n" + e.toString());
                exit(1);
            }

        }
        else{
            result += "Member " + searchName + " not found in this parliament";
        }

        return result;
    }

    private String executeOptionNumber1(Options options) {
        String result = "";
        String searchID="";
        Double sumOfPayments = 0.0;
        boolean findMember = false;
        String searchName = options.getMemberOfParliamentFirstName() + " " + options.getMemberOfParliamentLastName();

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

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count && !findMember; i++) {
                for (int j = 0; j < 50 && i < count && !findMember; j++) {
                    JSONObject res = obj.getJSONArray("Dataobject").getJSONObject(j);
                    JSONObject data = res.getJSONObject("data");
//                System.out.println(data.toString());
                    String tmp = data.getString("ludzie.nazwa");
                    if (tmp.equals(searchName)){
                        //System.out.println(searchID);

                        try {
                            sumOfPayments = setSumOfPayments(data);
                        }
                        catch (JSONException e){
                            System.out.println("Something wrong with your JSON");
                            System.out.println(e.toString());
                            exit(1);
                        }

                        findMember = true;
                    }
                    i++;
                }
                i--;

                try {
                    next = obj.getJSONObject("Links").getString("next");
                }catch (JSONException e){
                    System.out.println("Internal application error: \n" + e.toString());
                    exit(1);
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
            System.out.println("Internal application error: \n" + e.toString());
            exit(1);
        }

        if (findMember){
            result += sumOfPayments.toString();
        }
        else{
            result += "Member " + searchName + " not found in this parliament";
        }

        return result;
    }

    private Double setSumOfPayments(JSONObject data) throws org.json.JSONException {
        Double sumOfPayments = 0.0;
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_biuro");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_ekspertyzy");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_inne");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_materialy_biurowe");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_podroze_pracownikow");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_przejazdy");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_spotkania");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_srodki_trwale");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_taksowki");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_telekomunikacja");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_wynagrodzenia_pracownikow");
        sumOfPayments += data.getDouble("poslowie.wartosc_biuro_zlecenia");
        sumOfPayments += data.getDouble("poslowie.wartosc_refundacja_kwater_pln");
        sumOfPayments += data.getDouble("poslowie.wartosc_wyjazdow");

        return sumOfPayments;

    }
    //Create list of all id's of members from specified term
    private List<String> idList(Options options){

        List<String> result = new ArrayList<String>();
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

            Integer count = obj.getInt("Count");

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 50 && i < count; j++) {
                    JSONObject res = obj.getJSONArray("Dataobject").getJSONObject(j);
                    result.add(res.getString("id"));
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
            System.out.println("Internal application error: \n" + e.toString());
            exit(1);
        }



        return result;
    }

}