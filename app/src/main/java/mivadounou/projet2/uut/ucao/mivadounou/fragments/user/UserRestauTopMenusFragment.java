package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewRestauFragment;

/**
 * Created by leinad on 7/5/17.
 */

public class UserRestauTopMenusFragment extends RestauMenuListFragment {

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        SharedPreferences user = mActivity.getPreferences(Context.MODE_PRIVATE);

        String restauKey = user.getString(NewRestauFragment.RESTAU_KEY, "");

        return databaseReference.child("restau-menus")
                .child(restauKey)
                .orderByChild("starCount");
    }
}
