import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by Dawid Tomasiewicz on 11.01.17.
 */
public class ParliamentMember {

    private String id = "";
    private String name = "";
    private MemberData data;
    private JSONObject paymentsLayer;
    private List<Voyage> voyages = new ArrayList<Voyage>();
    private boolean hasVisitedItaly;

    public ParliamentMember(JSONObject member) {
        try {
            this.id = member.getString("id");
            this.data = new MemberData(member.getJSONObject("data"));
            this.name = this.data.getLudzieNazwa();

        } catch (JSONException e) {
            System.out.println("Caught exception while initializing parliament member\n" + e.toString());
            exit(1);
        }
        JSONObject tmp;
        JSONArray tmpArray;
        try {
            tmp = member.getJSONObject("layers").getJSONObject("wyjazdy");
        }catch (JSONException e){
            try{
                tmpArray = member.getJSONObject("layers").getJSONArray("wyjazdy");
                for (int i = 0; i < tmpArray.length(); i++) {
                    this.voyages.add(new Voyage(tmpArray.getJSONObject(i).getString("country_code"),
                            tmpArray.getJSONObject(i).getString("liczba_dni"),
                            tmpArray.getJSONObject(i).getString("koszt_suma")));
                }
            }catch (JSONException ex){

            }
        }
        this.hasVisitedItaly = haveIVisitedItaly(this.getVoyages());


    }

    //GETTERS
    public String getId() {

        return id;
    }
    public String getName() {
        return name;
    }
    public boolean getHasVisitedItaly() {
        return hasVisitedItaly;
    }
    private JSONObject getPaymentsLayer() {
        return paymentsLayer;
    }
    public List<Voyage> getVoyages() {
        return voyages;
    }

    //SETTERS
    private void setPaymentsLayer(JSONObject paymentsLayer) {
        this.paymentsLayer = paymentsLayer;
    }

    //check if this has visited Italy
    private boolean haveIVisitedItaly(List<Voyage> voyages){
        boolean result = false;
        Iterator<Voyage> i = voyages.iterator();
        while (i.hasNext() && !result){
            Voyage voyage = i.next();
            if (voyage.getCountryCode().equals("IT")){
                result = true;
            }
        }
        return result;
    }

    //check how much this member has paid for "drobne naprawy"
    public Double getDrobneNaprawy() {
        String json = "";
        Double result = 0.0;
        try {
            json = ParliamentSeeker.readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/" + this.getId()
                    + ".json?layers[]=wydatki");
            JSONObject obj = new JSONObject(json);
            this.setPaymentsLayer(obj.getJSONObject("layers").getJSONObject("wydatki"));
        }
        catch (Exception e){
            ParliamentSeeker.handleException(e);
        }

        try {
            result = Double.parseDouble(this.getPaymentsLayer()
                    .getJSONArray("roczniki")
                    .getJSONObject(0).getJSONArray("pola").getString(12));
        } catch (JSONException e) {
            ParliamentSeeker.handleJSONException(e);
        }
        return  result;
    }

    //check the sum of payments of this member
    public Double getSumOfPayments() throws JSONException {
        Double sumOfPayments = 0.0;
        for (Double payment :
                this.data.getPaymentsList()) {
            sumOfPayments+=payment;
        }
        return sumOfPayments;


    }

    @Override
    public  String toString(){
        return "id: " + this.getId().toString() + "\n" +
                "name: " + this.getName();
    }

}