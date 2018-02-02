package gamesexchange.com.gamesexchange.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.fragments.InicioFragment;
import gamesexchange.com.gamesexchange.fragments.UsuarioFragment;

/**
 * Created by Wender Galan Gamer on 02/02/2018.
 */

public class TabsAdapter extends FragmentStatePagerAdapter{

    private Context context;
    //private String [] abas = new String[]{"INÍCIO", "USUÁRIO"};
    private int[] icones = new int[]{R.drawable.ic_action_home, R.drawable.ic_person};
    private int tamanhoIcone;

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        double escala = this.context.getResources().getDisplayMetrics().density;
        tamanhoIcone = (int) (25 * escala);
    }

    //retorna a pagina conforme a posicao
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new InicioFragment();
                break;
            case 1:
                fragment = new UsuarioFragment();
                break;
        }
        return fragment;
    }

    //retorna uma imagem como charsequence
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //Recuperar icones de acordo com a posicao
        Drawable drawable = ContextCompat.getDrawable(this.context, icones[position]);
        drawable.setBounds(0,0,tamanhoIcone,tamanhoIcone);

        //Permite colocar uma imagem dentro de um texto
        ImageSpan imageSpan = new ImageSpan(drawable);

        //classe utilizada para retornar um charsequence
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    @Override
    public int getCount() {
        return icones.length;
    }
}
