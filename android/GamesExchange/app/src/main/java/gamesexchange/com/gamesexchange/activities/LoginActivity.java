package gamesexchange.com.gamesexchange.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.util.Validator;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.helper.Preferencias;
import gamesexchange.com.gamesexchange.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botao_logar;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private String identificadorUsuarioLogado;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = findViewById(R.id.editTextEmail);
        senha = findViewById(R.id.editTextSenha);
        botao_logar = findViewById(R.id.button_logar);
        loginButton = findViewById(R.id.login_button);

        botao_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    validarLogin();
                }
            }
        });

        //ler as permissoes dos usuarios - FACEBOOK
        loginButton.setReadPermissions("public_profile, email");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("DEBUG", "Metodo OnSuccess botao loginButton");
                handleFacebookAcessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("DEBUG", "Metodo OnCancel botao loginButton");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("DEBUG", "Metodo OnError botao loginButton");
            }
        });
    }

    private boolean validarCampos() {
        Validator validator = new Validator();
        boolean retorno = true;
        if (!validator.validateNotNull(email)){
            email.setError("Email vazio");
            email.setFocusable(true);
            email.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(senha)) {
            senha.setError("Senha vazia");
            senha.setFocusable(true);
            senha.requestFocus();
            retorno = false;
        }
        return retorno;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    private void handleFacebookAcessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "O login com o facebook falhou", Toast.LENGTH_LONG).show();
                    Log.i("DEBUG", "Login com o facebook falhou. Erro: " + task.getException());
                }
                recuperarDadosFacebook();
            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    identificadorUsuarioLogado = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorUsuarioLogado);
                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //monta o usuario com a classe model
                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("DEBUG", "Foi cancelado a inserção no Firebase, Erro: " + databaseError.getMessage());
                        }
                    };
                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);
                    abrirTelaPrincipal();
                }else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException e){
                        erroExcecao = "Email inexistente ou desativado";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Usuário ou senha inválidos";
                    } catch (FirebaseAuthEmailException e){
                        erroExcecao = "Email inválido";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "" + erroExcecao, Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    public void esqueciMinhaSenha(View view) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        //definindo um titulo
        alertDialog.setTitle("Redefinir senha");
        //definindo uma mensagem
        alertDialog.setMessage("Digite seu email");

        //Edit text dentro do Alert Dialog
        final EditText input = new EditText(LoginActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setGravity(1);
        input.setLayoutParams(lp);
        //passando ao Alert Dialog o edit text
        alertDialog.setView(input);

        //botao positivo - enviar
        alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //logica positiva - enviar
                FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                if (input.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email não enviado", Toast.LENGTH_LONG).show();
                    Log.i("DEBUG", "Campo Vazio");
                }else{
                    autenticacao.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();
                                Log.i("DEBUG", "Erro: "+task.getException());
                            }else{
                                Toast.makeText(LoginActivity.this, "Email inválido", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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

    public void recuperarDadosFacebook(){
        //inserir no banco de dados
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null){
             GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Usuario usuario = new Usuario();
                    try {
                        usuario.setNome(object.getString("name"));
                        usuario.setEmail(object.getString("email"));
                        usuario.setFoto("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                        usuario.setAnunciosRemovidos(0);

                        //inserir no firebase o usuario do facebook
                        firebase = ConfiguracaoFirebase.getFirebase();

                        String identificadorUsuario = autenticacao.getCurrentUser().getUid();
                        usuario.setId(identificadorUsuario);
                        //Salva o usuario no Firebase
                        usuario.salvar();

                        Preferencias preferencias = new Preferencias(LoginActivity.this);
                        preferencias.salvarDados(identificadorUsuario, usuario.getNome());

                    }catch (JSONException e){
                        Log.i("DEBUG", "Erro ao pegar os objetos do facebook. Erro: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            Bundle parameters = new Bundle();
            Log.i("DEBUG", "Pode dar erro aqui, verificar todos os parametros");
            parameters.putString("fields", "name, email, address");
            request.setParameters(parameters);
            request.executeAsync();

        }else{
            Log.i("DEBUG", "Acess Token Nulo. AccessToken: " + accessToken);
        }

        //abre a tela principal do app
        abrirTelaPrincipal();

    }
}