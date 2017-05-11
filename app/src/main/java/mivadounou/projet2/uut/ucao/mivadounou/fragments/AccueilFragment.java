package mivadounou.projet2.uut.ucao.mivadounou.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.adapters.RVAdapter;
import mivadounou.projet2.uut.ucao.mivadounou.entities.Person;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment {

    private List<Person> persons;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData() {
        persons = new ArrayList<>();
        persons.add(new Person("Le Beluga", "23 years old", R.drawable.ic_beluga2));
        persons.add(new Person("Le Patio", "25 years old", R.drawable.ic_patio));
        persons.add(new Person("Hotel du Golf", "35 years old", R.drawable.ic_golf));
    }


    public AccueilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_accueil, container, false);
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        initializeData();

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;

    }

}
