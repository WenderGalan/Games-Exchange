package gamesexchange.com.gamesexchange.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;

/**
 * Created by Wender Galan Gamer on 01/03/2018.
 */

public class Ajuda implements Serializable, Cloneable{

    private String release;
    private String mensagemCompartilhar;
    private String linkApp;

    public Ajuda(){

    }

    public String getRelease() {
        return release;
    }
    public void setRelease(String release) {
        this.release = release;
    }
    public String getMensagemCompartilhar() {
        return mensagemCompartilhar;
    }
    public void setMensagemCompartilhar(String mensagemCompartilhar) {
        this.mensagemCompartilhar = mensagemCompartilhar;
    }
    public String getLinkApp() {
        return linkApp;
    }
    public void setLinkApp(String linkApp) {
        this.linkApp = linkApp;
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("ajuda").setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("DEBUG", "Ajuda salva com sucesso");
            }
        });
    }

    public Ajuda clone() throws CloneNotSupportedException {
        return (Ajuda) super.clone();
    }

}
