package gamesexchange.com.gamesexchange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.activities.AnuncioDetalhesActivity;
import gamesexchange.com.gamesexchange.activities.EditarPerfilActivity;
import gamesexchange.com.gamesexchange.activities.LoginActivity;
import gamesexchange.com.gamesexchange.activities.SobreActivity;
import gamesexchange.com.gamesexchange.adapter.AnunciosAdapter;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.model.Ajuda;
import gamesexchange.com.gamesexchange.model.Anuncio;
import gamesexchange.com.gamesexchange.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuarioFragment extends Fragment{

    private Toolbar toolbarUsuario;
    private TextView email;
    private TextView nome;
    private CircleImageView imagem;
    private ListView listaMeusAnuncios;
    private FirebaseUser usuarioFirebase;
    private DatabaseReference firebase;
    private String idUsuario;
    private Usuario usuario;
    private Ajuda ajuda;
    private ImageView editarPerfil;
    private ArrayList<Anuncio> meusAnuncios;
    private String[] idAnuncios;
    private AnunciosAdapter adapter;

    public UsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        //Configurando a toolbar
        toolbarUsuario = view.findViewById(R.id.toolbar_usuario);
        //toolbarUsuario.setLogo(R.drawable.logo_texto);
        toolbarUsuario.setTitle("Minha Conta");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarUsuario);
        setHasOptionsMenu(true);

        meusAnuncios = new ArrayList<Anuncio>();

        //configurando o layout
        nome = view.findViewById(R.id.textViewNomePerfil);
        email = view.findViewById(R.id.textViewEmailPerfil);
        imagem = view.findViewById(R.id.imageCircleViewAdapter);
        listaMeusAnuncios = view.findViewById(R.id.listViewMeusAnuncios);
        editarPerfil = view.findViewById(R.id.imageButtonEditarPerfil);

        //Recuperando o usuario
        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        idUsuario = usuarioFirebase.getUid();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuario);

        //recuperar objeto ajuda
        recuperarAjuda();

        //listener para o usuario
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                usuario.setId(usuarioFirebase.getUid());
                //Verifica se os atributos não são nulos para não gerar excessoes
                if (usuario.getNome() != null){
                    nome.setText(usuario.getNome());
                }
                if (usuario.getEmail() != null){
                    email.setText(usuario.getEmail());
                }
                if (usuario.getMeusAnuncios() != null){
                    if (usuario.getMeusAnuncios().isEmpty()){

                    }else{
                        Log.i("DEBUG", "O Usuario tem anuncios");
                        idAnuncios = usuario.getMeusAnuncios().split(",");
                        meusAnuncios.clear();
                        for (int i = 0 ; i < idAnuncios.length ; i++){
                            buscarAnuncio(idAnuncios[i]);
                        }

                    }
                }
                if (usuario.getFoto()!= null){
                    try {
                        Picasso.with(getContext()).load(usuario.getFoto()).into(imagem);
                    }catch (Exception e){
                        Log.i("DEBUG", "Não foi possível recuperar a imagem");
                        e.printStackTrace();
                    }
                }else{
                    Log.i("DEBUG", "Não possui imagem");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirEditarPerfil();
            }
        });

        //Setar o list view aqui
        adapter = new AnunciosAdapter(getContext(), 0, meusAnuncios);
        listaMeusAnuncios.setAdapter(adapter);

        listaMeusAnuncios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //chamar a activity de detalhes do anuncio com o botao flutuante editar.
                Intent intent = new Intent(getContext(), AnuncioDetalhesActivity.class);
                //passa o anuncio escolhido como parametro extra
                intent.putExtra("anuncio", meusAnuncios.get(i));
                intent.putExtra("tipo", "meuAnuncio");
                startActivity(intent);
            }
        });
        return view;
    }

    private void buscarAnuncio(String anuncioId){
        DatabaseReference reference = ConfiguracaoFirebase.getFirebase().child("anuncios").child(anuncioId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Log.i("DEBUG", "Carregou o anuncio");
                    Anuncio anuncio = dataSnapshot.getValue(Anuncio.class);
                    meusAnuncios.add(anuncio);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.i("DEBUG", "Não carregou o anuncio");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DEBUG", "Não encontrou nenhum anúncio com este ID");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch (item.getItemId()){
           case R.id.action_sair:
               deslogarUsuario();
               return true;
           /*case R.id.action_configuracoes:
               abrirConfiguracoes();
               return true;*/
           case R.id.action_compartilhar:
               compartilharApp();
               return true;
           case R.id.action_editar_perfil:
               abrirEditarPerfil();
               return true;
           case R.id.action_sobre:
               abrirSobre();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void abrirSobre() {
        Intent intent = new Intent(getActivity(), SobreActivity.class);
        intent.putExtra("ajuda", ajuda);
        startActivity(intent);
    }

    private void compartilharApp() {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SUBJECT, "Games Exchange - Convite para conhecer");

        if (usuario.getNome() != null && ajuda != null){
            intent.putExtra(Intent.EXTRA_TEXT, usuario.getNome() + ajuda.getMensagemCompartilhar() + ajuda.getLinkApp() );
        }else{
            if (ajuda.getMensagemCompartilhar() != null && ajuda.getLinkApp() != null){
                intent.putExtra(Intent.EXTRA_TEXT,ajuda.getMensagemCompartilhar() + ajuda.getLinkApp());
            }
        }
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        startActivity(intent);
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

    /*private void abrirConfiguracoes() {
        //Abrir a tela de configuracoes
        Intent intent = new Intent(getActivity(), ConfiguracoesActivity.class);
        startActivity(intent);
    }*/

    private void deslogarUsuario() {
        //desloga o usuario e volta para a tela incial de Login
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void abrirEditarPerfil(){
        //chamar a activity Editar Perfil
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }




}
