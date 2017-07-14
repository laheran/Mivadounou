package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;

/**
 * Created by leinad on 7/14/17.
 */

public class CommandeAcceptedFragment extends AllCommandeListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        return databaseReference.child("user-commandes")
                .child(currentUser.getUid())
                .orderByChild("status")
                .equalTo(CommandeMenu.COMMANDE_ACCEPT);
    }
}