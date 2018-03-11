package gamesexchange.com.gamesexchange.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.model.Anuncio;
import gamesexchange.com.gamesexchange.model.Usuario;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class AnuncioDetalhesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private SliderLayout slider;
    private ImageButton excluir;
    private Anuncio anuncio;
    private String tipo;
    private FloatingTextButton ligar;
    private FloatingTextButton email;
    private Toolbar toolbar;
    private String[] imagens;
    private String UIDUsuario;
    private Usuario usuario;
    private TextView valor;
    private TextView categoria;
    private TextView tipoDeAnuncio;
    private TextView descricao;
    private TextView denunciar;
    private TextView usuarioText;
    private TextView localizacao;
    private TextView text;
    private TextView tipoText;
    private TextView data;
    private FirebaseUser usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalhes);

        excluir = findViewById(R.id.floatingButtonExcluir);
        ligar = findViewById(R.id.floatingButtonTextLigar);
        email = findViewById(R.id.floatingButtonTextEmail);
        slider = findViewById(R.id.sliderMeuAnuncio);
        toolbar = findViewById(R.id.toolbar);
        valor = findViewById(R.id.textViewValorLinha);
        categoria = findViewById(R.id.textViewCategoriaLinha);
        tipoDeAnuncio = findViewById(R.id.textViewTipoDeAnuncioLinha);
        descricao = findViewById(R.id.textViewDescricaoLinha);
        denunciar = findViewById(R.id.textViewDenunciar);
        usuarioText = findViewById(R.id.textViewUsuarioLinha);
        localizacao = findViewById(R.id.textViewLocalizacaoLinha);
        tipoText = findViewById(R.id.textViewTipoLinha);
        data = findViewById(R.id.textViewDataLinha);
        text = findViewById(R.id.textView23);


        setSupportActionBar(toolbar);

        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncio");
        tipo = getIntent().getStringExtra("tipo");

        consultarInformacoesUsuario();

        Log.i("DEBUG", "TIPO DO ANUNCIO: " + tipo);

        if (tipo.equals("meuAnuncio")){
            excluir.setVisibility(View.VISIBLE);
            email.setVisibility(View.INVISIBLE);
            ligar.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            denunciar.setVisibility(View.INVISIBLE);

        }else if (tipo.equals("anuncio")){
            excluir.setVisibility(View.INVISIBLE);
            email.setVisibility(View.VISIBLE);
            ligar.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            denunciar.setVisibility(View.VISIBLE);

        }

        //botao excluir anuncio
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AnuncioDetalhesActivity.this);
                alertDialog.setTitle("Excluir o Anúncio?");
                alertDialog.setMessage("Deseja mesmo excluir este anúncio? Após a exclusão deste anúncio não será mais possível visualizá-lo!");
                alertDialog.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = ConfiguracaoFirebase.getFirebase().child("anuncios").child(anuncio.getId());
                        if (anuncio.getImagens() != null){
                            String imagens[] = anuncio.getImagens().split(",");
                            Log.i("DEBUG", "Imagens: " + imagens[0]);
                            deletarImagens(imagens);
                        }
                        String meusAnuncios[] = usuario.getMeusAnuncios().split(",");
                        String novoMeusAnuncios[] = new String[meusAnuncios.length-1];
                        for (int j = 0; j < meusAnuncios.length; j++){
                            for (int h = 0 ; h < novoMeusAnuncios.length ; h++){
                                if (meusAnuncios[j].equals(anuncio.getId())){

                                }else{
                                    novoMeusAnuncios[h] = meusAnuncios[j];
                                }
                            }
                        }

                        String meusAnunciosString = null;
                        for (int k = 0; k < novoMeusAnuncios.length ; k++){
                            if (meusAnunciosString == null){
                                meusAnunciosString = novoMeusAnuncios[k];
                            }else{
                                meusAnunciosString = meusAnunciosString + "," + novoMeusAnuncios[k];
                            }
                        }

                        Log.i("DEBUG", "Novo meus anuncios: " + meusAnunciosString);
                        usuario.setMeusAnuncios(meusAnunciosString);

                        //deleta o anuncio
                        reference.removeValue();
                        dialogInterface.dismiss();
                        usuario.salvar();
                        finish();
                        Toast.makeText(AnuncioDetalhesActivity.this, "Anúncio excluido com sucesso", Toast.LENGTH_LONG).show();
                    }
                });

                alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (anuncio != null){
                    UIDUsuario = anuncio.getIdUsuario();

                    Intent email = new Intent(Intent.ACTION_SEND);
                    if (usuario.getEmail() != null){
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{usuario.getEmail()});
                    }else{
                        Toast.makeText(AnuncioDetalhesActivity.this, "O usuário não possui email cadastrado", Toast.LENGTH_LONG).show();
                    }
                    email.putExtra(Intent.EXTRA_SUBJECT, "Email referente ao anúncio: " + anuncio.getTitulo());
                    email.putExtra(Intent.EXTRA_TEXT, "Digite aqui sua mensagem ao " + usuario.getNome());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Selecione o email"));
                }
            }
        });

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usuario.getTelefone() != null){
                    String telefone = usuario.getTelefone();
                    Uri uri = Uri.parse("tel:" + telefone);
                    Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(AnuncioDetalhesActivity.this, "O usuário não possui telefone cadastrado", Toast.LENGTH_LONG).show();
                }

            }
        });

        denunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"gamesexchange@wendergalan.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Denúncia - UID: " + anuncio.getId());
                email.putExtra(Intent.EXTRA_TEXT, "ID do Anúncio: " + anuncio.getId() + "\nPor favor digite sua mensagem: ");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Selecione o email"));
            }
        });

        if (anuncio != null){
            //seta o titulo
            toolbar.setTitle(anuncio.getTitulo());
            //passa as imagens e configura o slider
            if (anuncio.getImagens()!= null){
                imagens = anuncio.getImagens().split(",");
                HashMap<String,String> url_maps = new HashMap<String, String>();

                for (int i = 0; i < imagens.length ; i++){
                    url_maps.put("", imagens[i]);
                }

                for(String name : url_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView.description(name).image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(AnuncioDetalhesActivity.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("extra",name);

                    slider.addSlider(textSliderView);
                }
                //animacao
                slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                //onde fica o indicador de imagens
                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
                slider.setCustomAnimation(new DescriptionAnimation());
                slider.setDuration(3000);
            }

            if (anuncio.getTipoAnuncio().equals("Troca")){
                valor.setText("");
                tipoDeAnuncio.setText("TROCA");
            }else if (anuncio.getTipoAnuncio().equals("Venda")){
                valor.setText("R$ " + anuncio.getValor());
                tipoDeAnuncio.setText("VENDA");
            }else if (anuncio.getTipoAnuncio().equals("Troca & Venda")){
                valor.setText("R$ " + anuncio.getValor());
                tipoDeAnuncio.setText("TROCA & VENDA");
            }
            categoria.setText(anuncio.getCategoria());
            descricao.setText(anuncio.getDescricao());
            //setar o usuario
            if (usuario != null){
                usuarioText.setText(usuario.getNome());
            }else{
                usuarioText.setText("");
            }
            tipoText.setText(anuncio.getTipo());
            data.setText(anuncio.getDataDaInsercao());
            localizacao.setText(anuncio.getCidade() + " - " + anuncio.getEstado());


        }


    }

    private void deletarImagens(String[] imagens) {

        for (int i = 0; i < imagens.length ; i++){
            //pega o nome da imagem para poder apagar a mesma
            String nome = FirebaseStorage.getInstance().getReferenceFromUrl(imagens[i]).getName();
            //recebe a referencia da imagem a ser excluida
            StorageReference imagemPerfil = ConfiguracaoFirebase.getStorage().child(usuario.getId()).child("anuncios/").child(nome);

            Log.i("DEBUG", "imagemPerfilReferencia: " + imagemPerfil);
            imagemPerfil.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("DEBUG", "Sucesso na deleção da imagem do usuario");
                    //salva a url da foto no banco + as informacoes do usuario

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("DEBUG", "Não foi possivel deletar a imagem de perfil do usuario do Storage. Erro: " + e.getMessage());
                }
            });
        }



    }

    private void consultarInformacoesUsuario() {
        //consultar dados do usuario
        DatabaseReference reference = ConfiguracaoFirebase.getFirebase().child("usuarios").child(anuncio.getIdUsuario());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Log.i("DEBUG", "Resgatou o usuário");
                    usuario = dataSnapshot.getValue(Usuario.class);
                    usuarioText.setText(usuario.getNome());

                    if (tipo.equals("meuAnuncio")){
                        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
                        usuario.setId(usuarioFirebase.getUid());
                    }else if (tipo.equals("anuncio")){
                        usuario.setId(anuncio.getIdUsuario());
                    }

                }else{
                    Log.i("DEBUG", "Usuário não encontrado");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }



    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }



}



