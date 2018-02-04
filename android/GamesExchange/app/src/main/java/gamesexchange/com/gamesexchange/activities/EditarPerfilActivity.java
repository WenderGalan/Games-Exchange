package gamesexchange.com.gamesexchange.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.model.CEP;
import gamesexchange.com.gamesexchange.model.Usuario;
import gamesexchange.com.gamesexchange.task.BuscarCEP;
import gamesexchange.com.gamesexchange.util.Validator;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText cep;
    private TextView cidadeEstado;
    private Button salvarAlteracoes;
    private Button alterarSenha;
    private CircleImageView imagem;
    private Usuario usuario;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        nome = findViewById(R.id.editTextEditarPerfilNome);
        telefone = findViewById(R.id.editTextEditarPerfilTelefone);
        cep = findViewById(R.id.editTextEditarPerfilCep);
        cidadeEstado = findViewById(R.id.textViewCidadeEstado);
        alterarSenha = findViewById(R.id.buttonAlterarSenha);
        imagem = findViewById(R.id.imageViewCircleEditarPerfil);
        salvarAlteracoes = findViewById(R.id.buttonSalvarAlteracao);

        progressDialog = new ProgressDialog(this);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (usuario != null){
            //seta o nome
            nome.setText(usuario.getNome());
            //seta a foto
            if (usuario.getFoto() != null){
                try {
                    Picasso.with(getApplicationContext()).load(usuario.getFoto()).into(imagem);
                }catch(Exception e){
                    Log.i("DEBUG", "Não foi possivel setar a imagem");
                    e.printStackTrace();
                }
            }
            //seta o telefone
            if (usuario.getTelefone() != null){
                /**FORMATAÇÃO DO TELEFONE**/
                SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
                MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskFormatter);
                telefone.addTextChangedListener(maskTelefone);
                telefone.setText(usuario.getTelefone());
            }else{
                SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
                MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskFormatter);
                telefone.addTextChangedListener(maskTelefone);
            }
            //seta o cep
            if (usuario.getCep() != null){
                /**FORMATAÇÃO DO CEP**/
                SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNNN-NNN");
                MaskTextWatcher maskTelefone = new MaskTextWatcher(cep, simpleMaskFormatter);
                cep.addTextChangedListener(maskTelefone);
                cep.setText(usuario.getCep());
            }else{
                SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNNN-NNN");
                MaskTextWatcher maskTelefone = new MaskTextWatcher(cep, simpleMaskFormatter);
                cep.addTextChangedListener(maskTelefone);
            }
            //seta a localização
            if (usuario.getCidade() != null && usuario.getEstado() != null){
                String localizacao = usuario.getCidade() + " - " + usuario.getEstado();
                cidadeEstado.setText(localizacao);
            }

        }


        alterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditarPerfilActivity.this);
                //definindo um titulo
                alertDialog.setTitle("Alterar Senha");
                //definindo uma mensagem
                alertDialog.setMessage("Digite seu email para que seja enviado a redefinição de senha");

                //Edit text dentro do Alert Dialog
                final EditText input = new EditText(EditarPerfilActivity.this);
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
                            Toast.makeText(EditarPerfilActivity.this, "Email não enviado", Toast.LENGTH_LONG).show();
                            Log.i("DEBUG", "Campo Vazio");
                        }else{
                            autenticacao.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(EditarPerfilActivity.this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();
                                        Log.i("DEBUG", "Erro: "+task.getException());
                                    }else{
                                        Toast.makeText(EditarPerfilActivity.this, "Email inválido", Toast.LENGTH_LONG).show();
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
        });


        salvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remover formatações
                String cepSemFormatacao = cep.getText().toString().replaceAll("[^0-9]", "");
                String telefoneSemFormatacao = telefone.getText().toString().replaceAll("[^0-9]", "");

                //Verifica se o cep tem 8 numeros corretamente e salva as informaçoes
                if (cepSemFormatacao != null && cepSemFormatacao.length() == 8) {
                    try {
                        //recebe um objeto CEP
                        CEP objCEP = new BuscarCEP(cepSemFormatacao).execute().get();
                        //seta o objeto CEP no objeto USUARIO
                        if (objCEP.getCep() != null) {
                            usuario.setCep(objCEP.getCep());
                            usuario.setCidade(objCEP.getLocalidade());
                            usuario.setEstado(objCEP.getUf());
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Validator validator = new Validator();
                    if (validator.validateNotNull(cep)){
                        cep.setError("CEP Inválido");
                        cep.setFocusable(true);
                        cep.requestFocus();
                    }
                }

                //verifica se a formatacao do telefone esta correta
                if (telefoneSemFormatacao != null && telefoneSemFormatacao.length() == 11){
                    usuario.setTelefone(telefoneSemFormatacao);
                }else{
                    Validator validator = new Validator();
                    if (validator.validateNotNull(telefone)){
                        telefone.setError("Telefone Inválido");
                        telefone.setFocusable(true);
                        telefone.requestFocus();
                    }
                }

                //seta o nome do usuario
                usuario.setNome(nome.getText().toString());
                //salva no banco de dados
                usuario.salvar();
                //
                Toast.makeText(EditarPerfilActivity.this, "Usuário alterado com sucesso", Toast.LENGTH_LONG).show();
                finish();

            }
        });

        storageReference = ConfiguracaoFirebase.getStorage();

    }


    public void abrirSelecaoImagem(View view) {
        //abrir o selecionador de imagem
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);










        /**ESTA DANDO PROBLEMA NO RESGATE DA IMAGEM**/








        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setMessage("Mudando a foto de perfil...");
            progressDialog.show();

            Uri uri = data.getData();

            //cria a estrutura de pastas => UID USUARIO => perfil => IMAGEM
            storageReference.child(usuario.getId()).child("perfil").child(uri.getLastPathSegment());

            //upa a imagem ao Storage
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("DEBUG", "Entrou no onSucess");

                    //Pega a URL da imagem
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String urlImagem = downloadUrl.toString();

                    //pega a instancia do Firebase
                    databaseReference = ConfiguracaoFirebase.getFirebase();

                    //Guarda no banco de dados do usuario a URL da foto
                    databaseReference.child("usuarios").child(usuario.getId()).child("foto").setValue(urlImagem);

                    databaseReference.child("usuarios").child(usuario.getId()).child("foto").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("DEBUG", "Entrou no onDataChange");
                            String urlImagem = (String) dataSnapshot.getValue();
                            Picasso.with(EditarPerfilActivity.this).load(urlImagem).into(imagem);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("DEBUG", "Entrou no Cancelled");
                        }
                    });

                    progressDialog.dismiss();
                    Toast.makeText(EditarPerfilActivity.this, "Foto de perfil alterada com sucesso", Toast.LENGTH_LONG).show();



                }
            });




        }

    }
}
