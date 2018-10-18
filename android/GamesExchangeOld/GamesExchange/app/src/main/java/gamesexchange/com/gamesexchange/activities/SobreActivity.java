package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.model.Ajuda;

public class SobreActivity extends AppCompatActivity {


    private Ajuda ajuda;
    private TextView release;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        ajuda = (Ajuda) getIntent().getSerializableExtra("ajuda");
        release = findViewById(R.id.textViewRelease);
        release.setText("Games Exchange " + ajuda.getRelease());

        toolbar = findViewById(R.id.toolbarSobre);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Sobre");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                onBackPressed();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }


    public void abrirWebSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://wendergalan.com"));
        startActivity(intent);
    }

    public void abrirTermosDeUso(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://docs.google.com/document/d/1jTi7wk_K1SIhbdpK-yPWgK-g8IVbIPgFa3B20iFXm4M/edit?usp=sharing"));
        startActivity(intent);
    }
}
