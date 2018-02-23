package gamesexchange.com.gamesexchange.activities;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.adapter.HintAdapter;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.config.ListaItens;
import gamesexchange.com.gamesexchange.model.Anuncio;
import gamesexchange.com.gamesexchange.model.CEP;
import gamesexchange.com.gamesexchange.model.Usuario;
import gamesexchange.com.gamesexchange.task.BuscarCEP;
import gamesexchange.com.gamesexchange.util.Validator;

public class NovoAnuncioActivity extends AppCompatActivity{

    private Anuncio anuncio;
    private Usuario usuario;
    private DatabaseReference firebase;
    private FirebaseUser usuarioFirebase;
    private CircleImageView imagem1;
    private CircleImageView imagem2;
    private CircleImageView imagem3;
    private CircleImageView imagem4;
    private CircleImageView imagem5;
    private EditText titulo;
    private EditText descricao;
    private EditText preco;
    private TextView localizaco;
    private Spinner spinnerTipoDeAnuncio;
    private Spinner spinnerCategoria;
    private Spinner spinnerTipo;
    private List<String> imagesEncodedList;
    private String imageEncoded;
    private List<String> listaTipoAnuncio;
    private List<String> listaCategoria;
    private List<String> listaTipo;


    /**Se for falso terá que pedir ao usuario para escolher**/
    private boolean categoria = true;
    private boolean tipo = true;
    private boolean tipoDeAnuncio = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_anuncio);

        //inicializa o objeto
        anuncio = new Anuncio();

        //resgata o usuario ativo
        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        String idUsuario = usuarioFirebase.getUid();
        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuario);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                Log.i("DEBUG", "Usuário construido com sucesso!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DEBUG", "Erro ao construir o usuário!");
            }
        });

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
        preco = findViewById(R.id.textValor);
        localizaco = findViewById(R.id.textViewLocalizacao);



        //desabilita o campo
        //descricao.setEnabled(false);

        /**configuracao do Spinner Categoria**/
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        listaCategoria = ListaItens.getListaCategoria();

        //seta o adapter para consumir toda a lista menos o último item porque o mesmo é o hint da caixa de seleção
        HintAdapter adapterCategoria = new HintAdapter(this, R.layout.spinner_item, listaCategoria);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);
        spinnerCategoria.setSelection(adapterCategoria.getCount());


        /**configuracao do Spinner tipo**/
        spinnerTipo = findViewById(R.id.spinnerTipo);


        //abrir outro spinner com o tipo desejado
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String resultado = listaCategoria.get(i).toString();
                if (resultado.equals(listaCategoria.get(0).toString())){
                    //clicou em consoles e acessorios
                    listaTipo = ListaItens.getListaModeloVideoGames();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(0).toString());

                }else if (resultado.equals(listaCategoria.get(1).toString())){
                    //clicou em jogos
                    listaTipo = ListaItens.getListaModeloVideoGames();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(1).toString());

                }else if (resultado.equals(listaCategoria.get(2).toString())){
                    //clicou em computadores e acessorios
                    listaTipo = ListaItens.getListaComputadores();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(2).toString());

                }else if (resultado.equals(listaCategoria.get(3).toString())){
                    //clicou em celular e telefonia
                    listaTipo = ListaItens.getListaCelulares();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(3).toString());

                }else if (resultado.equals(listaCategoria.get(4).toString())){
                    categoria = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nenhum item foi selecionado
            }
        });

        //recupera o q foi selecionado no spinner tipo
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String resultado = listaTipo.get(i).toString();
                anuncio.setTipo(resultado);

                if (resultado.equals("Tipo")){
                    tipo = false;
                    Log.i("DEBUG", "O tipo está nulo");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nenhum item foi selecionado
            }
        });

        /**configuracao do spinner Tipo de Anuncio**/
        spinnerTipoDeAnuncio = findViewById(R.id.spinnerTipoDeAnuncio);
        listaTipoAnuncio = ListaItens.getListaTipoAnuncio();

        //seta o adapter para consumir toda a lista menos o último item porque o mesmo é o hint da caixa de seleção
        HintAdapter adapterAnuncio = new HintAdapter(this, R.layout.spinner_item, listaTipoAnuncio);
        adapterAnuncio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDeAnuncio.setAdapter(adapterAnuncio);
        spinnerTipoDeAnuncio.setSelection(adapterAnuncio.getCount());

        //pega a selecao do spinner tipo de anuncio
        spinnerTipoDeAnuncio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //aqui ele sempre entrara no ambos ou seja será o fluxo padrao
                //listaSpinner.get(i) => pega o item que foi clicado
                String resultado = listaTipoAnuncio.get(i).toString();
                if (resultado.equals(listaTipoAnuncio.get(0).toString())){
                    //selecionou Venda
                    preco.setVisibility(View.VISIBLE);
                }else if (resultado.equals(listaTipoAnuncio.get(1).toString())){
                    //selecionou Troca
                    preco.setVisibility(View.INVISIBLE);
                }else if (resultado.equals(listaTipoAnuncio.get(2).toString())){
                    //selecionou Troca e Venda
                    preco.setVisibility(View.VISIBLE);
                }else if (resultado.equals(listaTipoAnuncio.get(3).toString())){
                    tipoDeAnuncio = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






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

    public void mudarLocalizacao(View view) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NovoAnuncioActivity.this);
        //definindo um titulo
        alertDialog.setTitle("Alterar Localização");
        //definindo uma mensagem
        alertDialog.setMessage("Digite seu CEP para alterar sua localização!");

        //Edit text dentro do Alert Dialog
        final EditText input = new EditText(NovoAnuncioActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        /**FORMATAÇÃO DO CEP**/
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(input, simpleMaskFormatter);
        input.addTextChangedListener(maskTelefone);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setGravity(1);
        input.setLayoutParams(lp);
        //passando ao Alert Dialog o edit text
        alertDialog.setView(input);

        //botao positivo - enviar
        alertDialog.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //logica positiva - enviar
                //retira a formatacao do cep
                String cep = input.getText().toString();
                String cepSemFormatacao = cep.replaceAll("[^0-9]", "");

                //resgata o cep que o usuario passou e altera a interface
                if (cepSemFormatacao != null && cepSemFormatacao.length() == 8){
                    try {
                        //recebe um objeto CEP
                        CEP objCEP = new BuscarCEP(cepSemFormatacao).execute().get();
                        //seta o objeto CEP no objeto USUARIO
                        if (objCEP.getCep() != null) {
                            usuario.setCep(objCEP.getCep());
                            usuario.setCidade(objCEP.getLocalidade());
                            usuario.setEstado(objCEP.getUf());
                            Toast.makeText(NovoAnuncioActivity.this, "Localização alterada com sucesso", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(NovoAnuncioActivity.this, "CEP Inválido", Toast.LENGTH_LONG).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    //altera a interface
                    localizaco.setText(usuario.getCidade() + " - " + usuario.getEstado());
                    //altera o banco de dados
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();

                }else{
                    //Toast.makeText(NovoAnuncioActivity.this, "CEP Inválido", Toast.LENGTH_LONG).show();
                }


            }
        });


        //botao negativo - cancelar
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //logica negativa - cancelar
                dialogInterface.cancel();
            }
        });
        //mostrar o alert dialog
        alertDialog.show();
    }


}
