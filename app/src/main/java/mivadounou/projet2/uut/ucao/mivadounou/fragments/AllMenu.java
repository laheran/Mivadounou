package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by leinad on 7/12/17.
 */

public class AllMenu extends AllMenuListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference.child("menu");
    }
}
