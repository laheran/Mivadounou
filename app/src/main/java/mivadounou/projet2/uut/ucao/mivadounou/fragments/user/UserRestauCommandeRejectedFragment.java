package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;
import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;

/**
 * Created by leinad on 7/14/17.
 */

public class UserRestauCommandeRejectedFragment extends UserRestauCommandeListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        SharedPreferences userSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        return databaseReference.child(FirebaseRef.RESTAU_COMMANDES)
                .child(userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, ""))
                .orderByChild("status")
                .equalTo(CommandeMenu.COMMANDE_REJECT);
    }
}
