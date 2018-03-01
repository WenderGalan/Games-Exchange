package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.model.Ajuda;

public class SobreActivity extends AppCompatActivity {


    private Ajuda ajuda;
    private TextView release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        ajuda = (Ajuda) getIntent().getSerializableExtra("ajuda");
        release = findViewById(R.id.textViewRelease);
        release.setText("Games Exchange " + ajuda.getRelease());



    }


    public void abrirWebSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://wendergalan.com"));
        startActivity(intent);
    }
}
