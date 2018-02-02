package gamesexchange.com.gamesexchange.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gamesexchange.com.gamesexchange.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewAnuncios;


    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        //searchView = view.findViewById(R.id.search);
        listViewAnuncios = view.findViewById(R.id.list_view_anuncio);





        return view;
    }

}
