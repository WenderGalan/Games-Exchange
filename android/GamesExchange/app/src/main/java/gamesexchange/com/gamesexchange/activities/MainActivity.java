package gamesexchange.com.gamesexchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toolbar;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.adapter.TabsAdapter;
import gamesexchange.com.gamesexchange.helper.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {

    private Button sair;
    private Button trocar;
    private SearchView search;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbarPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configura as abas
        viewPager = findViewById(R.id.view_pager_main);
        slidingTabLayout = findViewById(R.id.sliding_tab_main);

        //configuracao adapter
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter( tabsAdapter );
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.text_item_tab);
        slidingTabLayout.setDistributeEvenly(true);
        //muda a cor da tab
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.branco));
        slidingTabLayout.setViewPager(viewPager);




    }

    private void abrirTelaLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
