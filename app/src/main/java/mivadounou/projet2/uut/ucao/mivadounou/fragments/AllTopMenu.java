package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;

/**
 * Created by leinad on 7/12/17.
 */

public class AllTopMenu extends AllMenuListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference.child(FirebaseRef.MENU)
                .orderByChild("starCount")
                .startAt(1);
    }
}
