package gamesexchange.com.gamesexchange.task;

import android.os.AsyncTask;
import android.util.Log;

import com.github.gilbertotorrezan.viacep.se.ViaCEPClient;
import com.github.gilbertotorrezan.viacep.shared.ViaCEPEndereco;
import java.io.IOException;
import gamesexchange.com.gamesexchange.model.CEP;

/**
 * Created by Wender Galan Gamer on 03/02/2018.
 */

public class BuscarCEP extends AsyncTask<String, Void, CEP> {

    private String cep;
    private CEP cepObj;

    public BuscarCEP(String cep) {
        this.cep = cep;
    }

    @Override
    protected CEP doInBackground(String... strings) {
        cepObj = new CEP();
        ViaCEPClient client = new ViaCEPClient();
        try {
            ViaCEPEndereco endereco = client.getEndereco(cep);

            try {
                cepObj.setCep(endereco.getCep());
                cepObj.setLogradouro(endereco.getLogradouro());
                cepObj.setComplemento(endereco.getComplemento());
                cepObj.setBairro(endereco.getBairro());
                cepObj.setLocalidade(endereco.getLocalidade());
                cepObj.setUf(endereco.getUf());
                cepObj.setIbge(endereco.getIbge());
            }catch (Exception e){
                Log.i("DEBUG", "Erro com o CEP: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cepObj;
    }



}
