package mivadounou.projet2.uut.ucao.mivadounou.fragments_resto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mivadounou.projet2.uut.ucao.mivadounou.R;


public class Noces_menu extends Fragment {


    public Noces_menu(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         return inflater.inflate(R.layout.fragment_noces_menu, container, false);
    }

}