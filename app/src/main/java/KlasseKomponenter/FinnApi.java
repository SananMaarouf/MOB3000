package KlasseKomponenter;

import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

public class FinnApi {
    String apiUrl ="https://api.themoviedb.org/3/movie/";
    String apiSearch = "?api_key=";
    String apiKey = "93e7133aa45445f8651ca9eda8a953b5";
    String moreInf ="&language=en-US&page="; // legg til side tall på slutten, hvis du ikkje vet skriv 1


    // Kjører http requesten og retunerer svaret
    public JSONObject httpEtterFilm(int sideTall, String sokEtter) throws Exception {
        // Sjekker om sidetall er tomt
        // hvis tomt ikke ta det med i urlen
        URL urlen = null;
        
        if (sideTall==0){
            urlen = new URL(apiUrl+sokEtter+apiSearch+apiKey);
            //System.out.println(apiUrl+sokEtter+apiSearch+apiKey);
        } else {
            urlen = new URL(apiUrl+sokEtter+apiSearch+apiKey+moreInf+sideTall);
           // System.out.println(apiUrl+sokEtter+apiSearch+apiKey+moreInf+sideTall);
        }


        Scanner scanner = new Scanner(urlen.openStream());
        String respons = scanner.useDelimiter("\\>").next();
        JSONObject objektet = new JSONObject(respons);

        // printer ut alle filmene i rekkefølge
        return objektet;
    }
}
