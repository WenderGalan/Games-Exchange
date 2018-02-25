package gamesexchange.com.gamesexchange.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wender Galan Gamer on 25/02/2018.
 */

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){
        //verifica o sdk do celular
        if (Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissao = new ArrayList<String>();

            //percorrer as permissoes
            for (String permissao : permissoes){
                //verifica se tenho a permissao para essa activity
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                //não tem essa permissao do usuario
                if (!validaPermissao){
                    listaPermissao.add(permissao);
                }

                //caso a lista esteja vazia não vai verificar
                if (listaPermissao.isEmpty()){
                    return true;
                }

                //convertendo a lista em um array
                String[] novasPermissoes = new String[listaPermissao.size()];
                listaPermissao.toArray(novasPermissoes);

                //Solicita a permissao
                ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

            }
        }

        return true;
    }
}
