package gamesexchange.com.gamesexchange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
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

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.activities.ConfiguracoesActivity;
import gamesexchange.com.gamesexchange.activities.EditarPerfilActivity;
import gamesexchange.com.gamesexchange.activities.LoginActivity;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
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

        //configurando o layout
        nome = view.findViewById(R.id.textViewNomePerfil);
        email = view.findViewById(R.id.textViewEmailPerfil);
        imagem = view.findViewById(R.id.imageCircleViewAdapter);
        listaMeusAnuncios = view.findViewById(R.id.listViewMeusAnuncios);

        //Recuperando o usuario
        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        idUsuario = usuarioFirebase.getUid();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuario);

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

        //Setar o list view aqui








        return view;
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
           case R.id.action_configuracoes:
               abrirConfiguracoes();
               return true;
           case R.id.action_editar_perfil:
               abrirEditarPerfil();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void abrirConfiguracoes() {
        //Abrir a tela de configuracoes
        Intent intent = new Intent(getActivity(), ConfiguracoesActivity.class);
        startActivity(intent);
    }

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
