package gamesexchange.com.gamesexchange.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import gamesexchange.com.gamesexchange.R;

public class AnuncioDetalhesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private SliderLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalhes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        slider = findViewById(R.id.sliderMeuAnuncio);
        toolbar.setTitle("HD SAMSUNG 1 TERÁ");
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        HashMap<String,String> url_maps = new HashMap<String, String>();
        //passar as imagens para carregar
        url_maps.put("1", "http://papeldeparede.blog.br/wp-content/uploads/2017/02/imagens-para-deixar-o-smartphone-foda-2.jpg");
        url_maps.put("2", "http://www.mulherebeleza.net/wp-content/uploads/2014/04/Paisagens-naturais-do-Brasil.jpg");
        url_maps.put("3", "https://content.skyscnr.com/cd70a3cdc378ecba92ad43fe574358f7/galeria-as-mais-belas-paisagens-da-costa-rica-04.jpg?resize=800px:99999px&quality=75");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(AnuncioDetalhesActivity.this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
        //animacoa
        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //onde fica o indicador de imagens
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(3000);


    }

    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }



}



