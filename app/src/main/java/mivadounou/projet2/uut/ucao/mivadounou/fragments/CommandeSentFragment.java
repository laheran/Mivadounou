package mivadounou.projet2.uut.ucao.mivadounou.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommandeSentFragment extends AllCommandeListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        return databaseReference.child("user-commandes")
                .child(currentUser.getUid())
                .orderByChild("status")
                .equalTo(CommandeMenu.COMMANDE_SENT);
    }
}
