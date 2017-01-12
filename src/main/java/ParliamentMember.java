import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by Dawid Tomasiewicz on 11.01.17.
 */
public class ParliamentMember {
    private String id = "";
    private String name = "";
    private JSONObject data;
    private JSONObject payments;
    private List<Voyage> voyages = new ArrayList<Voyage>();

    public JSONObject getPayments() {
        return payments;
    }

    public void setPayments(JSONObject payments) {
        this.payments = payments;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Voyage> getVoyages() {
        return voyages;
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }

    public ParliamentMember(JSONObject member) {
        try {
            this.id = member.getString("id");
            this.data = member.getJSONObject("data");
            this.name = this.data.getString("ludzie.nazwa");

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

    }
    public void setId(String id) {
        this.id = id;
    }






    public String getId() {

        return id;
    }
    public void setData(JSONObject data) {
        this.data = data;
    }
    public JSONObject getData() {

        return data;
    }

    public Double getDrobneNaprawy() {
        String json = "";
        Double result = 0.0;
        try {
            json = ParliamentSeeker.readUrl("https://api-v3.mojepanstwo.pl/dane/poslowie/" + this.getId()
                    + ".json?layers[]=wydatki");
            JSONObject obj = new JSONObject(json);
            this.setPayments(obj.getJSONObject("layers").getJSONObject("wydatki"));
        }
        catch (Exception e){
            ParliamentSeeker.handleException(e);
        }

        try {
            result = Double.parseDouble(this.getPayments()
                    .getJSONArray("roczniki")
                    .getJSONObject(0).getJSONArray("pola").getString(12));
        } catch (JSONException e) {
            ParliamentSeeker.handleJSONException(e);
        }
        return  result;
    }


    public Double getSumOfPayments() throws JSONException {
        Double sumOfPayments = 0.0;
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_biuro");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_ekspertyzy");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_inne");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_materialy_biurowe");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_podroze_pracownikow");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_przejazdy");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_spotkania");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_srodki_trwale");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_taksowki");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_telekomunikacja");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_wynagrodzenia_pracownikow");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_biuro_zlecenia");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_refundacja_kwater_pln");
        sumOfPayments += this.getData().getDouble("poslowie.wartosc_wyjazdow");

        return sumOfPayments;


    }}

