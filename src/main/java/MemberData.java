import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawid Tomasiewicz on 12.01.17.
 */
public class MemberData {
    private List<Double> paymentsList = new LinkedList<Double>();
    private String ludzieNazwa;

    public List<Double> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Double> paymentsList) {
        this.paymentsList = paymentsList;
    }

    public String getLudzieNazwa() {
        return ludzieNazwa;
    }

    public void setLudzieNazwa(String ludzieNazwa) {
        this.ludzieNazwa = ludzieNazwa;
    }

    public MemberData(JSONObject data) {

        try{
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_biuro"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_ekspertyzy"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_inne"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_materialy_biurowe"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_podroze_pracownikow"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_przejazdy"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_spotkania"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_srodki_trwale"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_taksowki"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_telekomunikacja"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_wynagrodzenia_pracownikow"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_biuro_zlecenia"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_refundacja_kwater_pln"));
            this.paymentsList.add(data.getDouble("poslowie.wartosc_wyjazdow"));

            this.ludzieNazwa = data.getString("ludzie.nazwa");

        }catch(JSONException e){
            ParliamentSeeker.handleJSONException(e);
        }



    }
}
