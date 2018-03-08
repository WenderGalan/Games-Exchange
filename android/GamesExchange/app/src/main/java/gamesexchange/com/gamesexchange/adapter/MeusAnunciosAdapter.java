package gamesexchange.com.gamesexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.model.Anuncio;

/**
 * Created by Wender on 26/02/2018.
 */

public class MeusAnunciosAdapter extends ArrayAdapter<Anuncio> {

    private ArrayList<Anuncio> anuncios;
    private Context context;


    public MeusAnunciosAdapter(Context c, int resource, ArrayList<Anuncio> objects) {
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
            view = inflater.inflate(R.layout.lista_meus_anuncios, parent, false);

            TextView tituloAnuncio = view.findViewById(R.id.textViewTituloAdapter);
            TextView cidadeDate = view.findViewById(R.id.textViewCidadeDateAdapter);
            TextView valorTroca = view.findViewById(R.id.textViewValorTrocaAdapter);
            CircleImageView imagemPrincipal = view.findViewById(R.id.imageCircleViewAdapter);

            //pega o item da lista
            Anuncio anuncio = anuncios.get(position);
            if (anuncio != null){
                tituloAnuncio.setText(anuncio.getTitulo());
                String cidadeData = anuncio.getCidade() + " - " + anuncio.getDataDaInsercao() + " às " + anuncio.getHorarioDaInsercao();
                cidadeDate.setText(cidadeData);
                String valorOuTroca = null;
                if (anuncio.getTipoAnuncio() != null){
                    if (anuncio.getTipoAnuncio().equals("Venda")){
                        valorOuTroca = "R$ " + anuncio.getValor();
                    }else if (anuncio.getTipoAnuncio().equals("Troca")){
                        valorOuTroca = "TROCA";
                    }else if (anuncio.getTipoAnuncio().equals("Troca & Venda")){
                        valorOuTroca = "R$ " + anuncio.getValor() + " - " + "TROCA";
                    }
                    valorTroca.setText(valorOuTroca);
                }
                
                /**TEM QUE SETAR A IMAGEM**/
                if (anuncio.getImagens() != null){
                    String[] imagens = anuncio.getImagens().split(",");
                    if (imagens[0] != null){
                        Picasso.with(getContext()).load(imagens[0]).into(imagemPrincipal);
                    }
                }
            }

        }
        return view;
    }
}
