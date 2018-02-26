package gamesexchange.com.gamesexchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.model.Anuncio;

/**
 * Created by Wender on 26/02/2018.
 */

public class AnuncioAdapter extends ArrayAdapter<Anuncio> {

    private ArrayList<Anuncio> anuncios;
    private Context context;


    public AnuncioAdapter(Context c, ArrayList<Anuncio> objects) {
        super(c,0, objects);
        this.anuncios = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (anuncios != null){
            //inicializa o objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar a view a partir do XML
            view = inflater.inflate(R.layout.lista_anuncio, parent, false);

            TextView tituloAnuncio = view.findViewById(R.id.textViewTituloAdapter);
            TextView cidadeDate = view.findViewById(R.id.textViewCidadeDateAdapter);
            TextView valorTroca = view.findViewById(R.id.textViewValorTrocaAdapter);
            CircleImageView imagemPrincipal = view.findViewById(R.id.imageCircleViewAdapter);

            //pega o item da lista
            Anuncio anuncio = anuncios.get(position);
            tituloAnuncio.setText(anuncio.getTitulo());
            String cidadeData = anuncio.getCidade() + " - " + anuncio.getDataDaInsercao() + " às " + anuncio.getHorarioDaInsercao();
            cidadeDate.setText(cidadeData);
            String valorOuTroca = null;
            if (anuncio.getTipo().equals("Venda")){
                valorOuTroca = "R$ " + anuncio.getValor();
            }else if (anuncio.getTipo().equals("Troca")){
                valorOuTroca = "Troca";
            }else if (anuncio.getTipo().equals("Troca & Venda")){
                valorOuTroca = "R$ " + anuncio.getValor() + " - " + "TROCA";
            }
            valorTroca.setText(valorOuTroca);

            /**TEM QUE SETAR A IMAGEM**/

        }
        return view;
    }
}
