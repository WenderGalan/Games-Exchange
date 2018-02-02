package gamesexchange.com.gamesexchange.activities;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;


public class MainActivity extends AppCompatActivity {

    private Button sair;
    private Button trocar;
    private Button galeria;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        galeria = findViewById(R.id.button3);
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarImagem(view);
            }
        });

        sair = findViewById(R.id.button_sair);
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
                //deslogar do firebaase
                auth.signOut();
                //deslogar do facebook tambem
                LoginManager.getInstance().logOut();
                abrirTelaLogin();
            }
        });

        trocar = findViewById(R.id.button2);
        trocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnuncioDetalheActivity.class);
                startActivity(intent);
            }
        });


        search.setSubmitButtonEnabled(true);


    }

    private void abrirTelaLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    private List<String> imagesEncodedList;
    private String imageEncoded;

    public void adicionarImagem(View view) {
        //adiciona imagem no novo anuncio
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData() != null){

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "Você não escolheu a(s) imagem(ns)",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);

        for (String s : imagesEncodedList){
            Log.i("DEBUG", "Caminho da Imagem: " + s.toUpperCase());
        }
    }


}
