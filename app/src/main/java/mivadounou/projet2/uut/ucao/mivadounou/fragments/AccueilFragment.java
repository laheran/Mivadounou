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
import mivadounou.projet2.uut.ucao.mivadounou.models.Person;

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
        persons.add(new Person("Les Noces","Avédji Limousine, non loin des feux tricolores - 11 BP 421\n" +
                "Lomé - Togo ",R.drawable.noces));
        persons.add(new Person("Yogi's Fast-Food", "YOGIS - Le vrai goût Américain. Pour tous les appétits, pour tous les budgets℠. " +
                "La restauration rapide dans son sens réel. Rapide, abordable et savoureux bien sûr. ", R.drawable.yogi));
        persons.add(new Person("Akif", "Restauration rapide, Libanaise, Méditerranéenne, Moyen-Orien", R.drawable.akif));
        persons.add(new Person("BPC ( BURGER ET PIZZA CITY)", "Fast food Face Agence UTB, " +
                "Adidogomé Avé-Maria - Adidogomé Lomé - Togo ", R.drawable.bpc));
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
