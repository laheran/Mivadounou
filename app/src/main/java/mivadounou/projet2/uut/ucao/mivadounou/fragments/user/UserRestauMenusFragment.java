package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;

/**
 * Created by leinad on 7/5/17.
 */

public class UserRestauMenusFragment extends RestauMenuListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        SharedPreferences user = mActivity.getPreferences(Context.MODE_PRIVATE);

        return databaseReference.child(FirebaseRef.RESTAU_MENUS)
                .child(user.getString(NewRestauFragment.RESTAU_KEY, ""));
    }

}
