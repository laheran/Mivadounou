package mivadounou.projet2.uut.ucao.mivadounou.fragments;


import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;
import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommandeSentFragment extends AllCommandeListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        assert currentUser != null;
        return databaseReference.child(FirebaseRef.USER_COMMANDES)
                .child(currentUser.getUid())
                .orderByChild("status")
                .equalTo(CommandeMenu.COMMANDE_SENT);
    }
}
