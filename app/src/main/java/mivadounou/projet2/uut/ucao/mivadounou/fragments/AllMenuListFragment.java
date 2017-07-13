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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewMenuFragment;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;
import mivadounou.projet2.uut.ucao.mivadounou.models.MenuRestau;
import mivadounou.projet2.uut.ucao.mivadounou.viewholder.AllMenuViewHolder;

/**
 * Created by leinad on 7/12/17.
 */

public abstract class AllMenuListFragment extends Fragment {

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

    public AllMenuListFragment() {
    }

    @Override
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

    protected void updateAdapter() {
        if (getUid() != null) {
            Query restauMenusQuery = getQuery(mDatabase);
            mAdapter = new FirebaseRecyclerAdapter<MenuRestau, AllMenuViewHolder>(MenuRestau.class,
                    R.layout.item_all_menu, AllMenuViewHolder.class, restauMenusQuery) {

                @Override
                protected void populateViewHolder(final AllMenuViewHolder allMenuViewHolder, final MenuRestau menuRestau, int i) {
                    final DatabaseReference menuRestauRef = getRef(i);

                    final String menuRestauKey = menuRestauRef.getKey();

                    allMenuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toDetails(menuRestauKey, menuRestau);
                        }
                    });

                    if (menuRestau.stars.containsKey(getUid())) {
                        allMenuViewHolder.getStarView().setImageResource(R.drawable.ic_star_black_24dp);
                    } else {
                        allMenuViewHolder.getStarView().setImageResource(R.drawable.ic_star_border_black_24dp);
                    }

                    switch (menuRestau.getType()) {
                        case "Plat Africain":
                            allMenuViewHolder.getMenuIconTypeImageView().setImageResource(R.drawable.ic_restaurant);
                            break;
                        case "Pizza":
                            allMenuViewHolder.getMenuIconTypeImageView().setImageResource(R.drawable.ic_pie);
                            break;
                        case "Burger":
                            allMenuViewHolder.getMenuIconTypeImageView().setImageResource(R.drawable.ic_fast_food);
                            break;
                    }

                    allMenuViewHolder.startAnim();

                    StorageReference imageRef = mStorage.child("images").child("menu").child(menuRestauKey);

                    Glide.with(mActivity)
                            .using(new FirebaseImageLoader())
                            .load(imageRef)
                            .crossFade()
                            .signature(new StringSignature(menuRestau.getMd5Hash()))
                            .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, StorageReference ref,
                                                           Target<GlideDrawable> target,
                                                           boolean isFirstResource) {
                                    allMenuViewHolder.stopAnim();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource,
                                                               StorageReference ref, Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache,
                                                               boolean isFirstResource) {
                                    allMenuViewHolder.stopAnim();
                                    return false;
                                }
                            })
                            .into(allMenuViewHolder.getMenuImageView());

                    allMenuViewHolder.bindToPost(menuRestau, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference globalmenuRestauRef = mDatabase.child("menu").child(menuRestauKey);
                            DatabaseReference restauMenuRef = mDatabase.child("restau-menus").child(menuRestau.getRestauId()).child(menuRestauKey);

                            // Run two transactions
                            onStarClicked(globalmenuRestauRef);
                            onStarClicked(restauMenuRef);
                        }
                    }, new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            CommandeMenu commandeMenu = new CommandeMenu(
                                    menuRestau.getTitle(),
                                    menuRestau.getType(),
                                    menuRestauKey,
                                    menuRestau.getPrice(),
                                    currentUser.getDisplayName().isEmpty() ? currentUser.getPhoneNumber() : currentUser.getDisplayName(),
                                    currentUser.getUid(),
                                    menuRestau.getRestauName(),
                                    menuRestau.getRestauId()
                            );

                            showDialog(commandeMenu);
                        }
                    });
                }
            };

            mRecycler.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    void showDialog(CommandeMenu commandeMenu) {

        CommandeDialogFragment commandeDialogFragment = CommandeDialogFragment
                .newInstance(commandeMenu);

        commandeDialogFragment.setCancelable(false);
        commandeDialogFragment.show(getFragmentManager(), "Commande");
    }

    protected void toDetails(String menuRestauKey, MenuRestau menuRestau) {
        Bundle args = new Bundle();

        args.putString("currentKey", menuRestauKey);
        args.putString("currentTitle", menuRestau.getTitle());
        args.putString("currentPrice", menuRestau.getPrice() + "");
        args.putString("currentDesc", menuRestau.getDesc());
        args.putString("currentType", menuRestau.getType());
        args.putString("currentMd5Hash", menuRestau.getMd5Hash());

        NewMenuFragment newMenuFragment = new NewMenuFragment();
        newMenuFragment.setArguments(args);

        MainActivity.isNewMenu = true;

//        if (getParentFragment().getParentFragment() instanceof UserRestauFragment) {
//
//            ((UserRestauFragment) getParentFragment().getParentFragment()).toDetail(newMenuFragment);
//        }
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference menuRef) {
        menuRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MenuRestau menuRestau = mutableData.getValue(MenuRestau.class);
                if (menuRestau == null) {
                    return Transaction.success(mutableData);
                }

                if (menuRestau.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    menuRestau.starCount = menuRestau.starCount - 1;
                    menuRestau.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    menuRestau.starCount = menuRestau.starCount + 1;
                    menuRestau.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(menuRestau);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(mActivity, "On resume RestauList", Toast.LENGTH_SHORT).show();
    }

    public String getUid() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            return firebaseUser.getUid();
        }
        return null;
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
