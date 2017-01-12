import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawid Tomasiewicz on 12.01.17.
 */
public class Voyage {
    String countryCode;
    String liczbaDni;
    String kosztSuma;


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLiczbaDni() {
        return liczbaDni;
    }

    public void setLiczbaDni(String liczbaDni) {
        this.liczbaDni = liczbaDni;
    }

    public String getKosztSuma() {
        return kosztSuma;
    }

    public void setKosztSuma(String kosztSuma) {
        this.kosztSuma = kosztSuma;
    }

    public Voyage(String countryCode, String liczbaDni, String kosztSuma) {

        this.countryCode = countryCode;
        this.liczbaDni = liczbaDni;
        this.kosztSuma = kosztSuma;
    }
}
