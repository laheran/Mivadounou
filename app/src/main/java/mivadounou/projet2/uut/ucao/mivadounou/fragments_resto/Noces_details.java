package mivadounou.projet2.uut.ucao.mivadounou.fragments_resto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mivadounou.projet2.uut.ucao.mivadounou.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Noces_details extends Fragment {


    public Noces_details(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noces_details, container, false);
    }

}
