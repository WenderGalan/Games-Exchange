package gamesexchange.com.gamesexchange.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Wender Galan Gamer on 28/12/2017.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static StorageReference referenciaStorage;
    private static StorageReference referenciaStorageFromUrl;


    public static DatabaseReference getFirebase(){

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }

    public static StorageReference getStorage(){

        if (referenciaStorage == null){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

    public static StorageReference getStorage(String url){

        if (referenciaStorageFromUrl == null){
            referenciaStorageFromUrl = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        }
        return referenciaStorageFromUrl;
    }

}
