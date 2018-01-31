package gamesexchange.com.gamesexchange.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Wender Galan Gamer on 28/12/2017.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "gamesexchange.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias (Context contextParametro){
        contexto = contextParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario, String nome){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nome);
        editor.commit();
    }

   public String getIdentificador(){
       return preferences.getString(CHAVE_IDENTIFICADOR, null);
   }
   public String getNome(){
       return preferences.getString(CHAVE_NOME, null);
   }

}
