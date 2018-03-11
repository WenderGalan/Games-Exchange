package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import gamesexchange.com.gamesexchange.R;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //INICIALIZA UMA THREAD PARA DEIXAR O SPLASH POR 1 SEGUNDO NA TELA DO USUARIO
        Thread timerThread = new Thread() {

            public void run() {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                   startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
