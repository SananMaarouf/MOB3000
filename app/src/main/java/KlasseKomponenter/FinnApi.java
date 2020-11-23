package KlasseKomponenter;

import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

public class FinnApi {
    String apiUrl ="https://api.themoviedb.org/3/movie/";
    String apiSearch = "popular?"+"api_key=";
    String apiKey = "93e7133aa45445f8651ca9eda8a953b5";
    String moreInf ="&language=en-US&page="; // legg til side tall på slutten, hvis du ikkje vet skriv 1


    // Kjører http requesten og retunerer svaret
    public JSONObject httpEtterFilm(int sideTall) throws Exception {
        System.out.println("------------------------------Here we go!-------------------------");
        System.out.println(apiUrl+apiSearch+apiKey+moreInf);
        URL urlen = new URL(apiUrl+apiSearch+apiKey+moreInf+sideTall);

        Scanner scanner = new Scanner(urlen.openStream());
        String respons = scanner.useDelimiter("\\>").next();
        JSONObject objektet = new JSONObject(respons);



        //JSONArray jArray = objektet.getJSONArray("results");

        return objektet;

        // printer ut alle filmene i rekkefølge



    }
}
