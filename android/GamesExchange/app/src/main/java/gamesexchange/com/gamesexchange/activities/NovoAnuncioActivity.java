package gamesexchange.com.gamesexchange.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.adapter.HintAdapter;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.config.ListaItens;
import gamesexchange.com.gamesexchange.helper.Permissao;
import gamesexchange.com.gamesexchange.model.Ajuda;
import gamesexchange.com.gamesexchange.model.Anuncio;
import gamesexchange.com.gamesexchange.model.CEP;
import gamesexchange.com.gamesexchange.model.MeusAnuncios;
import gamesexchange.com.gamesexchange.model.Usuario;
import gamesexchange.com.gamesexchange.task.BuscarCEP;
import gamesexchange.com.gamesexchange.util.Base64Custom;
import gamesexchange.com.gamesexchange.util.Validator;

public class NovoAnuncioActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Anuncio anuncio;
    private Usuario usuario;
    private DatabaseReference firebase;
    private FirebaseUser usuarioFirebase;
    private CircleImageView imagem0;
    private CircleImageView imagem1;
    private CircleImageView imagem2;
    private EditText titulo;
    private EditText descricao;
    private EditText preco;
    private Button publicar;
    private TextView localizacao;
    private Spinner spinnerTipoDeAnuncio;
    private Spinner spinnerCategoria;
    private Spinner spinnerTipo;
    private List<Uri> imagens;
    private List<String> listaTipoAnuncio;
    private List<String> listaCategoria;
    private List<String> listaTipo;
    private String[] permissoesNecessarias = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleApiClient googleApiClient;
    private static final int GALLERY_INTENT = 2;
    private String imageEncoded;
    private ArrayList<Uri> imagesEncodedList = new ArrayList<Uri>();
    private Ajuda ajuda;
    private MeusAnuncios meusAnuncios;
    private ProgressDialog progressDialog;
    private String imagensURL;

    /**Se for falso terá que pedir ao usuario para escolher**/
    private boolean categoria = true;
    private boolean tipo = true;
    private boolean tipoDeAnuncio = true;
    private boolean valor = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_anuncio);

        //Imagens resgatadas
        imagem0 = findViewById(R.id.imageViewCircle1);
        imagem1 = findViewById(R.id.imageViewCircle2);
        imagem2 = findViewById(R.id.imageViewCircle3);

        //atributos resgatados
        titulo = findViewById(R.id.textTitulo);
        descricao = findViewById(R.id.textDescricao);
        preco = findViewById(R.id.textValor);
        localizacao = findViewById(R.id.textViewLocalizacao);
        publicar = findViewById(R.id.buttonPublicar);

        //inicializa o objeto
        anuncio = new Anuncio();
        imagens = new ArrayList<Uri>();
        ajuda = new Ajuda();
        meusAnuncios = new MeusAnuncios();

        //recupera a ajuda
        recuperarAjuda();

        //resgata o usuario ativo
        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        final String idUsuario = usuarioFirebase.getUid();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuario);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                usuario.setId(idUsuario);
                Log.i("DEBUG", "Usuário construido com sucesso!");

                //seta a localização do usuario
                if (usuario.getCidade() != null && usuario.getEstado() != null && usuario.getCep() != null) {
                    localizacao.setText(usuario.getCidade() + " - " + usuario.getEstado());
                    anuncio.setCep(usuario.getCep());
                    anuncio.setCidade(usuario.getCidade());
                    anuncio.setEstado(usuario.getEstado());
                } else {
                    //solicita a permissao
                    Permissao.validaPermissoes(1, NovoAnuncioActivity.this, permissoesNecessarias);
                    //acessa a localização do usuário

                    googleApiClient = new GoogleApiClient.Builder(NovoAnuncioActivity.this)
                            .addConnectionCallbacks(NovoAnuncioActivity.this)
                            .addOnConnectionFailedListener(NovoAnuncioActivity.this)
                            .addApi(LocationServices.API)
                            .build();

                    //Tentando conexão com o Google API. Se a tentativa for bem sucessidade, o método onConnected() será chamado, senão, o método onConnectionFailed() será chamado.
                    googleApiClient.connect();
                    //reconecta para ter certeza q salvou tudo
                    googleApiClient.reconnect();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DEBUG", "Erro ao construir o usuário!");
            }
        });

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
                if (resultado.equals(listaCategoria.get(0).toString())) {
                    //clicou em consoles e acessorios
                    listaTipo = ListaItens.getListaModeloVideoGames();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(0).toString());
                    categoria = true;

                } else if (resultado.equals(listaCategoria.get(1).toString())) {
                    //clicou em jogos
                    listaTipo = ListaItens.getListaModeloVideoGames();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(1).toString());
                    categoria = true;

                } else if (resultado.equals(listaCategoria.get(2).toString())) {
                    //clicou em computadores e acessorios
                    listaTipo = ListaItens.getListaComputadores();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(2).toString());
                    categoria = true;

                } else if (resultado.equals(listaCategoria.get(3).toString())) {
                    //clicou em celular e telefonia
                    listaTipo = ListaItens.getListaCelulares();
                    HintAdapter adapterCategoria = new HintAdapter(NovoAnuncioActivity.this, R.layout.spinner_item, listaTipo);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipo.setAdapter(adapterCategoria);
                    spinnerTipo.setSelection(adapterCategoria.getCount());
                    anuncio.setCategoria(listaCategoria.get(3).toString());
                    categoria = true;

                } else if (resultado.equals(listaCategoria.get(4).toString())) {
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

                if (resultado.equals("Tipo")) {
                    tipo = false;
                    Log.i("DEBUG", "O tipo está nulo");
                } else {
                    tipo = true;
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
                if (resultado.equals(listaTipoAnuncio.get(0).toString())) {
                    //selecionou Venda
                    preco.setVisibility(View.VISIBLE);
                    tipoDeAnuncio = true;
                    valor = true;
                    anuncio.setTipoAnuncio("Venda");
                    //ajustar o layout
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
                    lp.setMargins(0, 15, 0, 0);
                    lp.addRule(RelativeLayout.BELOW, preco.getId());
                    lp.addRule(RelativeLayout.ALIGN_START, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_END, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_LEFT, publicar.getId());
                    localizacao.setLayoutParams(lp);

                } else if (resultado.equals(listaTipoAnuncio.get(1).toString())) {
                    //selecionou Troca
                    preco.setVisibility(View.INVISIBLE);
                    tipoDeAnuncio = true;
                    valor = false;
                    anuncio.setTipoAnuncio("Troca");
                    //ajustar o layout
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
                    lp.setMargins(0, 15, 0, 0);
                    lp.addRule(RelativeLayout.BELOW, spinnerTipoDeAnuncio.getId());
                    lp.addRule(RelativeLayout.ALIGN_START, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_END, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_LEFT, publicar.getId());
                    localizacao.setLayoutParams(lp);

                } else if (resultado.equals(listaTipoAnuncio.get(2).toString())) {
                    //selecionou Troca e Venda
                    preco.setVisibility(View.VISIBLE);
                    tipoDeAnuncio = true;
                    valor = true;
                    anuncio.setTipoAnuncio("Troca & Venda");
                    //ajustar o layout
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
                    lp.setMargins(0, 15, 0, 0);
                    lp.addRule(RelativeLayout.BELOW, preco.getId());
                    lp.addRule(RelativeLayout.ALIGN_START, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_END, publicar.getId());
                    lp.addRule(RelativeLayout.ALIGN_LEFT, publicar.getId());
                    localizacao.setLayoutParams(lp);
                } else if (resultado.equals(listaTipoAnuncio.get(3).toString())) {
                    tipoDeAnuncio = false;
                    valor = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //valida todos os campos
                if (validarCampos()){
                    //seta os campos
                    anuncio.setTitulo(titulo.getText().toString());
                    anuncio.setDescricao(descricao.getText().toString());
                    anuncio.setContadorDenuncia(0);
                    anuncio.setPrioridade(0);
                    anuncio.setVisitas(0);

                    //divide a sring pelos espaços
                    String[] tags = titulo.getText().toString().split(" ");
                    String resultado = null;
                    //percorre o vetor de strings concatenando com virgulas
                    for (String tag : tags){
                        if (resultado == null){
                            resultado = tag + ",";
                        }else{
                            resultado += tag + ",";
                        }
                    }
                    //seta as tags
                    anuncio.setTags(resultado);

                    /**SETAR A HORA E A DATA ATUAL**/
                    Calendar c = Calendar.getInstance();
                    int ano = c.get(Calendar.YEAR);
                    int mes = c.get(Calendar.MONTH)+1;
                    int dia = c.get(Calendar.DAY_OF_MONTH);
                    int hora = c.get(Calendar.HOUR_OF_DAY);
                    int minuto = c.get(Calendar.MINUTE);

                    anuncio.setDataDaInsercao(dia + "/" + mes + "/" + ano);
                    anuncio.setHorarioDaInsercao(hora + ":" + minuto);

                    /**verifica se o tipo do anuncio possui valor**/
                    if (valor){
                        anuncio.setValor(preco.getText().toString());
                    }else{
                        anuncio.setValor("");
                    }

                    /**Tratar a inserção das imagens e também coloca-las**/

                    progressDialog.setMessage("Inserindo anúncio...");
                    progressDialog.show();

                    for (int i = 0 ; i < imagesEncodedList.size() ;  i++){
                        inserirImagem(i);
                    }

                    //seta a string com as urls das imagens inseridas no storage
                    anuncio.setImagens(imagensURL);

                    /**
                     * CONFIGURAR O ID DO ANUNCIO
                     * */
                    int idAnuncio = 0;
                    try {
                        String aux = ajuda.getContadorDeAnuncios();
                        idAnuncio = Integer.parseInt(aux);
                        idAnuncio += 1;
                        ajuda.setContadorDeAnuncios(Integer.toString(idAnuncio));
                        ajuda.salvar();

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    anuncio.setId(idAnuncio);

                    //finaliza o progress bar
                    progressDialog.dismiss();

                    Log.i("DEBUG", "ID: " + anuncio.getId());
                    Log.i("DEBUG", "TITULO: " + anuncio.getTitulo());
                    Log.i("DEBUG", "DESCRICAO: " + anuncio.getDescricao());
                    Log.i("DEBUG", "CATEGORIA: " + anuncio.getCategoria());
                    Log.i("DEBUG", "TIPO: " + anuncio.getTipo());
                    Log.i("DEBUG", "TIPO DE ANUNCIO: " + anuncio.getTipoAnuncio());
                    Log.i("DEBUG", "VALOR: " + anuncio.getValor());
                    Log.i("DEBUG", "DATA: " + anuncio.getDataDaInsercao());
                    Log.i("DEBUG", "HORA: " + anuncio.getHorarioDaInsercao());
                    Log.i("DEBUG", "TAGS: " + anuncio.getTags());
                    Log.i("DEBUG", "DENUNCIAS: " + anuncio.getContadorDenuncia());
                    Log.i("DEBUG", "VISITAS: " + anuncio.getVisitas());
                    Log.i("DEBUG", "PRIORIDADE: " + anuncio.getPrioridade());
                    Log.i("DEBUG", "CEP: " + anuncio.getCep());
                    Log.i("DEBUG", "CIDADE: " + anuncio.getCidade());
                    Log.i("DEBUG", "ESTADO: " + anuncio.getEstado());
                    Log.i("DEBUG", "IMAGENS: " + anuncio.getImagens());

                    /**
                     * INSERE O ANUNCIO NO BANCO MEUS ANUNCIOS PARA FACILITAR A PESQUISA DO MESMO
                     * **/

                    DatabaseReference reference = ConfiguracaoFirebase.getFirebase().child("meusanuncios").child(usuario.getId());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                //pega os ids existentes e adiciona o novo
                                Log.i("DEBUG", "Meus Anuncios recuperado com sucesso;");
                                meusAnuncios = dataSnapshot.getValue(MeusAnuncios.class);
                                String id += "," + anuncio.getId();





                            }else{
                                //insere um novo anuncio

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    /**
                     * PERGUNTAR SE O USUARIO DESEJA ASSISTIR
                     * UM ANUNCIO PARA AUMENTAR A PRIORIDADE DO ANUNCIO
                     * */
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(NovoAnuncioActivity.this);
                    builder.setTitle("Aumentar a Exposição");
                    builder.setMessage("Deseja aumentar a exposição do seu anúncio apenas assistindo a um vídeo?");

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //chamar

                        }
                    });

                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();*/













                }
            }
        });


    }

    private boolean validarCampos() {
        //Este metodo vai validar os campos da activity
        Validator validator = new Validator();
        boolean retorno = true;

        if (!validator.validateNotNull(titulo)) {
            titulo.setError("Título vazio");
            titulo.setFocusable(true);
            titulo.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(descricao)) {
            descricao.setError("Descrição vazia");
            descricao.setFocusable(true);
            descricao.requestFocus();
            retorno = false;
        }
        if (categoria == false) {
            TextView errorText = (TextView) spinnerCategoria.getSelectedView();
            errorText.setError("Categoria não selecionada");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Categoria não selecionada");//changes the selected item text to this
            retorno = false;
        }
        if (tipo == false) {
            TextView errorText2 = (TextView) spinnerTipo.getSelectedView();
            errorText2.setError("Tipo não selecionado");
            errorText2.setTextColor(Color.RED);//just to highlight that this is an error
            errorText2.setText("Tipo não selecionado");//changes the selected item text to this
            retorno = false;
        }
        if (tipoDeAnuncio == false) {
            TextView errorText3 = (TextView) spinnerTipoDeAnuncio.getSelectedView();
            errorText3.setError("Tipo do anúncio não selecionado");
            errorText3.setTextColor(Color.RED);//just to highlight that this is an error
            errorText3.setText("Tipo do anúncio não selecionado");//changes the selected item text to this
            retorno = false;
        }
        if (valor == true) {
            if (!validator.validateNotNull(preco)) {
                preco.setError("Valor vazio");
                preco.setFocusable(true);
                preco.requestFocus();
                retorno = false;
            }
        }
        return retorno;
    }

    public void adicionarImagem(View view) {
        //adiciona imagem no novo anuncio
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && null != data) {
                    if(data.getData()!=null){

                        Uri mImageUri = data.getData();
                        //somente 5 imagens selecionadas
                        if (imagesEncodedList.size() < 3){
                            imagesEncodedList.add(mImageUri);
                        }else{
                            //ja foi selecionada as 5 imagens
                            Toast.makeText(this, "Já foram selecionadas 3 imagens", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, "Alguma coisa deu errado :/", Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);

        if (imagesEncodedList != null){
            int qtd = imagesEncodedList.size();
            for (int i = 0 ; i < qtd ; i++){
                String imagem  = imagesEncodedList.get(i).toString();
                if (i == 0){
                    Picasso.with(NovoAnuncioActivity.this).load(imagem).into(imagem0);
                }else if (i == 1){
                    Picasso.with(NovoAnuncioActivity.this).load(imagem).into(imagem1);
                }else if (i == 2){
                    Picasso.with(NovoAnuncioActivity.this).load(imagem).into(imagem2);
                }

            }
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

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                if (cepSemFormatacao != null && cepSemFormatacao.length() == 8) {
                    try {
                        //recebe um objeto CEP
                        CEP objCEP = new BuscarCEP(cepSemFormatacao).execute().get();
                        //seta o objeto CEP no objeto USUARIO
                        if (objCEP.getCep() != null) {
                            usuario.setCep(objCEP.getCep());
                            usuario.setCidade(objCEP.getLocalidade());
                            usuario.setEstado(objCEP.getUf());
                            Toast.makeText(NovoAnuncioActivity.this, "Localização alterada com sucesso", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NovoAnuncioActivity.this, "CEP Inválido", Toast.LENGTH_LONG).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    //altera a interface
                    localizacao.setText(usuario.getCidade() + " - " + usuario.getEstado());
                    //altera o banco de dados
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();

                } else {
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

    //Caso o usuario não aceite a permissao para acessar a localização do mesmo
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado : grantResults) {
            //permissao foi negada
            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão Negada");
        builder.setMessage("Para inserir um anúncio é necessário aceitar a permissão de acesso a sua localização!");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conexão com o serviços do Google Service API foi estabelecida!
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //recupera a localizacao do usuario
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //recupera as informacoes da localizacao a partir da latitude e longitude
        Geocoder geocoder = new Geocoder(this);
        List<Address> endereco = null;
        try {
            endereco = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CEP objCEP = null;
        if (endereco != null){
            try {
               objCEP = new BuscarCEP(endereco.get(0).getPostalCode()).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (objCEP != null) {
                //altera a localização do usuário se ele não tiver nenhuma informação de localização
                if (usuario.getCep() == null){
                    usuario.setEstado(objCEP.getUf());
                    usuario.setCidade(objCEP.getLocalidade());
                    usuario.setCep(objCEP.getCep());
                    usuario.salvar();
                }

                //altera as informacoes do anuncio
                anuncio.setCep(objCEP.getCep());
                anuncio.setCidade(objCEP.getLocalidade());
                anuncio.setEstado(objCEP.getUf());



            }

            /**SETAR AS INFORMACOES DOS ANUNCIOS PARA A LOCALIZACAO DO ANUNCIO FICAR NELE E NAO NO USUARIO**/
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Aguardando o GoogleApiClient reestabelecer a conexão.

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //A conexão com o Google API falhou!
        pararConexaoComGoogleApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararConexaoComGoogleApi();
    }

    public void pararConexaoComGoogleApi() {
        //Verificando se está conectado para então cancelar a conexão!
        if (googleApiClient != null){
            if (googleApiClient.isConnected()) {
                googleApiClient.disconnect();
            }
        }
    }

    private void recuperarAjuda(){
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebase().child("ajuda");
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ajuda = dataSnapshot.getValue(Ajuda.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DEBUG", "Não carregou a ajuda");
            }
        });
    }

    private void inserirImagem(int position){
        StorageReference storageRef = ConfiguracaoFirebase.getStorage();
        //Cada usuario tem uma pasta com seu UID e dentro tem duas pastas com sua foto de perfil e a foto de seus anuncios
        StorageReference imagem = storageRef.child(usuario.getId() + "/" + "anuncios/" + imagesEncodedList.get(position).getLastPathSegment());
        //upa a imagem ao storage
        UploadTask uploadTask = imagem.putFile(imagesEncodedList.get(position));
        //Listener para verificar o estado da task de upload
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imagensURL = imagensURL + "," + downloadUrl.toString();
            }
        });
    }
}
