package gamesexchange.com.gamesexchange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.activities.ConfiguracoesActivity;
import gamesexchange.com.gamesexchange.activities.EditarPerfilActivity;
import gamesexchange.com.gamesexchange.activities.LoginActivity;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuarioFragment extends Fragment {

    private Toolbar toolbarUsuario;


    public UsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

        toolbarUsuario = view.findViewById(R.id.toolbar_usuario);
        toolbarUsuario.setLogo(R.drawable.logo_texto);
        toolbarUsuario.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarUsuario);
        setHasOptionsMenu(true);
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

    private void abrirEditarPerfil() {
        //chamar a activity Editar Perfil
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
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
}
