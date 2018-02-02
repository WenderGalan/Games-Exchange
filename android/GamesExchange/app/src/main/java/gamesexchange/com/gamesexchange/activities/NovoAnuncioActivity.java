package gamesexchange.com.gamesexchange.activities;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.model.Anuncio;

public class NovoAnuncioActivity extends AppCompatActivity{

    Anuncio anuncio = new Anuncio();
    private CircleImageView imagem1;
    private CircleImageView imagem2;
    private CircleImageView imagem3;
    private CircleImageView imagem4;
    private CircleImageView imagem5;
    private EditText titulo;
    private EditText descricao;
    private Spinner spinner;
    private List<String> imagesEncodedList;
    private String imageEncoded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_anuncio);

        //Imagens resgatadas
        imagem1 = findViewById(R.id.imageViewCircle1);
        imagem2 = findViewById(R.id.imageViewCircle2);
        imagem3 = findViewById(R.id.imageViewCircle3);
        imagem4 = findViewById(R.id.imageViewCircle4);
        imagem5 = findViewById(R.id.imageViewCircle5);

        imagem2.setEnabled(false);
        imagem3.setEnabled(false);
        imagem4.setEnabled(false);
        imagem5.setEnabled(false);

        //atributos resgatados
        titulo = findViewById(R.id.textTitulo);
        descricao = findViewById(R.id.textDescricao);
        //desabilita o campo
        //descricao.setEnabled(false);
        spinner = findViewById(R.id.spinner);

       /* List<String> list = new ArrayAdapter<String>();
        list.add("Venda");
        list.add("Troca");
        list.add("Venda ou Troca");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);*/





    }

    /**DAQUI PARA BAIXO NÃO FOI TESTADO, TEM QUE TESTAR!**/

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
