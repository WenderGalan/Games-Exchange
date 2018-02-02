package gamesexchange.com.gamesexchange.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Macal on 02/02/2018.
 */

public class BuscaCEP {

    /**Retorna um vetor com 4 posições, sendo:
     * 0-> lograoduro (Rua)
     * 1-> bairro
     * 2-> localidade (cidade)
     * 3-> uf
     * **/
    private String[] consultaCep(String cep){
        String retorno[] = new String[4];
        //monta a url
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            JSONObject obj = new JSONObject(this.get(url));
            retorno[0] = obj.getString("logradouro");
            retorno[1] = obj.getString("bairro");
            retorno[2] = obj.getString("localidade");
            retorno[3] = obj.getString("uf");

            Log.i("DEBUG", "RETORNO 0 - " + retorno[0]);
            Log.i("DEBUG", "RETORNO 1 - " + retorno[1]);
            Log.i("DEBUG", "RETORNO 2 - " + retorno[2]);
            Log.i("DEBUG", "RETORNO 3 - " + retorno[3]);

        } catch (JSONException e) {
            Log.i("DEBUG", "Retornou o erro do JSON. Erro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.i("DEBUG", "Retornou o erro do metodo GET. Erro: " + e.getMessage());
            e.printStackTrace();
        }

        return retorno;
    }

    //Obtem os dados via GET
    public final String get(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
