package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.models.MenuRestau;
import mivadounou.projet2.uut.ucao.mivadounou.viewholder.AllMenuViewHolder;

/**
 * Created by leinad on 7/13/17.
 */

public abstract class CommandeListFragment extends Fragment {

    private static final String TAG = "RestauMenuListFragment";


    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private StorageReference mStorage;

    private FirebaseRecyclerAdapter<MenuRestau, AllMenuViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    protected SwipeRefreshLayout swipeRefreshLayout;

    protected Activity mActivity;

    private SharedPreferences userSharedPreferences;

    private FirebaseAuth mAuth;

    public CommandeListFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_restau_menu, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setHasFixedSize(true);

        userSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (MainActivity.isNetworkAvailable(mActivity)) {

                    mAdapter.cleanup();

//                    updateAdapter();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(mActivity, "Verifier votre connexion internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 30000);
                } else {
                    Toast.makeText(mActivity, "Verifier votre connexion internet", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

//        updateAdapter();
    }
}
