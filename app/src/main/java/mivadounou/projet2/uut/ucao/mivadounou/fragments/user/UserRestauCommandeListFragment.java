package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;
import mivadounou.projet2.uut.ucao.mivadounou.viewholder.UserRestauCommandeViewHolder;

/**
 * Created by leinad on 7/13/17.
 */

public abstract class UserRestauCommandeListFragment extends Fragment {

    private static final String TAG = "RestauMenuListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<CommandeMenu, UserRestauCommandeViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    protected Activity mActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_restau_menu, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (MainActivity.isNetworkAvailable(mActivity)) {

                    mAdapter.cleanup();

                    updateAdapter();

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

        updateAdapter();
    }

    public void updateAdapter() {

        Query commandeQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<CommandeMenu, UserRestauCommandeViewHolder>(CommandeMenu.class,
                R.layout.item_restau_commande, UserRestauCommandeViewHolder.class, commandeQuery) {
            @Override
            protected void populateViewHolder(UserRestauCommandeViewHolder userRestauCommandeViewHolder, final CommandeMenu commandeMenu, int i) {
                final DatabaseReference commandeRef = getRef(i);

                final String commandeKey = commandeRef.getKey();

                userRestauCommandeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        toDetails(menuRestauKey, menuRestau);
                    }
                });

                userRestauCommandeViewHolder.bindToPost(commandeMenu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Commande")
                                .setContentText("Voulez vous vraiment rejetter la commande " + commandeMenu.getMenuTitle() + " de : " + commandeMenu.getUserName())
                                .setConfirmText("oui")
                                .setCancelText("non")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.hide();

                                        MainActivity.mProgressDialog.setMessage("Modification encoure...");
                                        MainActivity.showProgressDialog();

                                        commandeMenu.setStatus(CommandeMenu.COMMANDE_REJECT);
                                        updateCommande(commandeMenu, commandeKey);
                                    }
                                })
                                .setCancelClickListener(null)
                                .show();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MainActivity.mProgressDialog.setMessage("Modification encoure...");
                        MainActivity.showProgressDialog();

                        commandeMenu.setStatus(CommandeMenu.COMMANDE_ACCEPT);
                        updateCommande(commandeMenu, commandeKey);


                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MainActivity.mProgressDialog.setMessage("Modification encoure...");
                        MainActivity.showProgressDialog();

                        commandeMenu.setStatus(CommandeMenu.COMMANDE_DONE);
                        updateCommande(commandeMenu, commandeKey);

                    }
                });
            }
        };

        mRecycler.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    private void updateCommande(final CommandeMenu commandeMenu, String commandeKey) {

        Map<String, Object> commandeMenuValues = commandeMenu.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/commande/" + commandeKey, commandeMenuValues);
        childUpdates.put("/user-commandes/" + commandeMenu.getUserKey() + "/" + commandeKey, commandeMenuValues);
        childUpdates.put("/restau-commandes/" + commandeMenu.getRestauKey() + "/" + commandeKey, commandeMenuValues);

        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        MainActivity.hideProgressDialog();

                        switch (commandeMenu.getStatus()) {

                            case CommandeMenu.COMMANDE_ACCEPT:
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Commande acceptée !")
                                        .setConfirmClickListener(null)
                                        .show();
                                break;

                            case CommandeMenu.COMMANDE_DONE:
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Commande effectuée !")
                                        .setConfirmClickListener(null)
                                        .show();
                                break;

                            case CommandeMenu.COMMANDE_REJECT:
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Commande rejettée !")
                                        .setConfirmClickListener(null)
                                        .show();
                                break;
                        }

                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        MainActivity.hideProgressDialog();

                        Toast.makeText(getActivity(), "Failed to update children " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
