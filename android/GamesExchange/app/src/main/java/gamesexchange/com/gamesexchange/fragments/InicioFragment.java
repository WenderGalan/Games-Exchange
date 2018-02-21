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

import gamesexchange.com.gamesexchange.R;
import gamesexchange.com.gamesexchange.activities.NovoAnuncioActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewAnuncios;
    private ImageButton floatingButton;

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

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





        return view;
    }


    private void novoAnuncio(){
        Intent intent = new Intent(getActivity(), NovoAnuncioActivity.class);
        startActivity(intent);
    }

}
