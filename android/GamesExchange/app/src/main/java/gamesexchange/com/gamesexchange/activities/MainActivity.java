package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;


public class MainActivity extends AppCompatActivity {

    private Button sair;
    private Button trocar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sair = findViewById(R.id.button_sair);
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
                //deslogar do firebaase
                auth.signOut();
                //deslogar do facebook tambem
                LoginManager.getInstance().logOut();
                abrirTelaLogin();
            }
        });

        trocar = findViewById(R.id.button2);
        trocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnuncioDetalheActivity.class);
                startActivity(intent);
            }
        });

    }

    private void abrirTelaLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
