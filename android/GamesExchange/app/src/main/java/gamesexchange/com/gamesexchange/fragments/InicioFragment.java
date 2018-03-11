package gamesexchange.com.gamesexchange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.activities.NovoAnuncioActivity;
import gamesexchange.com.gamesexchange.adapter.AnunciosAdapter;
import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;
import gamesexchange.com.gamesexchange.model.Anuncio;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewAnuncios;
    private ImageButton floatingButton;
    private AnunciosAdapter adapter;
    private ArrayList<Anuncio> anuncios;

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

       // buscarAnuncios();

        //searchView = view.findViewById(R.id.search);
        listViewAnuncios = view.findViewById(R.id.list_view_anuncio);
        floatingButton = view.findViewById(R.id.floatingButton);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoAnuncio();
            }
        });

        //tratar o list view
        /*adapter = new AnunciosAdapter(getContext(), 0, anuncios);
        listViewAnuncios.setAdapter(adapter);

        listViewAnuncios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //chamar a activity de detalhes do anuncio com o botao flutuante editar.
                Intent intent = new Intent(getContext(), AnuncioDetalhesActivity.class);
                //passa o anuncio escolhido como parametro extra
                intent.putExtra("anuncio", anuncios.get(i));

                Toast.makeText(getContext(), "Clicou no item " + i, Toast.LENGTH_LONG).show();
            }
        });*/






        return view;
    }

    private void buscarAnuncios() {
        DatabaseReference reference = ConfiguracaoFirebase.getFirebase().child("anuncios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anuncio anuncio = dataSnapshot.getValue(Anuncio.class);
                anuncios.add(anuncio);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void novoAnuncio(){
        Intent intent = new Intent(getActivity(), NovoAnuncioActivity.class);
        startActivity(intent);
    }

}
