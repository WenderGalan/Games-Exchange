package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.util.Validator;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.helper.Base64Custom;
import gamesexchange.com.gamesexchange.helper.Preferencias;
import gamesexchange.com.gamesexchange.model.Usuario;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText senhaNovamente;
    private EditText telefone;
    private Button cadastrar;
    private Usuario usuario = new Usuario();;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = findViewById(R.id.editTextNome);
        email = findViewById(R.id.editTextEmailCadastro);
        senha = findViewById(R.id.editTextSenhaCadastro);
        senhaNovamente = findViewById(R.id.editTextSenhaNovamente);
        telefone = findViewById(R.id.editTextTelefone);
        cadastrar = findViewById(R.id.buttonCadastrar);

        /**FORMATAÇÃO DO TELEFONE**/
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskFormatter);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    String primeiraSenha = senha.getText().toString();
                    String segundaSenha = senhaNovamente.getText().toString();
                    if (primeiraSenha.equals(segundaSenha)){
                        usuario.setNome(nome.getText().toString());
                        usuario.setEmail(email.getText().toString());
                        usuario.setSenha(primeiraSenha);
                        usuario.setTelefone(telefone.getText().toString());
                        cadastrarUsuario();
                    }else{
                        //Solicita foco para a senha errada
                        senhaNovamente.setError("Senhas diferentes");
                        senhaNovamente.setFocusable(true);
                        senhaNovamente.requestFocus();
                    }
                }


            }
        });

    }

    private boolean validarCampos() {
        Validator validator = new Validator();
        boolean retorno = true;
        if (!validator.validateNotNull(nome)){
            nome.setError("Nome vazio");
            nome.setFocusable(true);
            nome.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(email)){
            email.setError("Email vazio");
            email.setFocusable(true);
            email.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(senha)){
            senha.setError("Senha vazia");
            senha.setFocusable(true);
            senha.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(senhaNovamente)){
            senhaNovamente.setError("Email vazio");
            senhaNovamente.setFocusable(true);
            senhaNovamente.requestFocus();
            retorno = false;
        }
        if (!validator.validateNotNull(telefone)){
            telefone.setError("Telefone vazio");
            telefone.setFocusable(true);
            telefone.requestFocus();
            retorno = false;
        }

        return retorno;
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                email.getText().toString(),
                senha.getText().toString()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG ).show();

                    //cadastra no banco de dados
                    String identificadorUsuario = autenticacao.getCurrentUser().getUid();
                    usuario.setId(identificadorUsuario);
                    //Salva o usuario no Firebase
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarDados(identificadorUsuario, usuario.getNome());

                    FirebaseUser firebaseUser = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
                    Log.i("DEBUG", "firebaseUser: " + firebaseUser);
                    //Email de confirmação de usuário
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Toast.makeText(CadastroActivity.this, "Email de confirmação enviado", Toast.LENGTH_LONG).show();
                                Log.i("DEBUG", "Email de confirmção enviado com sucesso. Erro: " + task.getException());
                                abrirTelaPrincipal();
                            }
                        }
                    });
                    /**retorna true se foi verificado e false caso contrario
                     * firebaseUser.isEmailVerified();
                     * **/
                }else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";
                        email.setFocusable(true);
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já esá em uso no APP!";
                        email.setFocusable(true);
                    }catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, "" + erroExcecao, Toast.LENGTH_LONG ).show();

                }
            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirTelaDosTermos(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://docs.google.com/document/d/1jTi7wk_K1SIhbdpK-yPWgK-g8IVbIPgFa3B20iFXm4M/edit?usp=sharing"));
        startActivity(intent);
    }
}
