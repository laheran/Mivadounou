package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.BottomNavigationViewHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomNavFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomNavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomNavFragment extends Fragment {

    private final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";

    private final String TAG_LOCATION_FRAGMENT = "TAG_LOCATION_FRAGMENT";

    private final String TAG_SEARCH_FRAGMENT = "TAG_SEARCH_FRAGMENT";

    private final String TAG_COMMANDE_FRAGMENT = "TAG_COMMANDE_FRAGMENT";

    private OnFragmentInteractionListener mListener;

    private BottomNavigationView mBottomNav;

    private ActionBar actionBar;

    private AccueilFragment accueilFragment = new AccueilFragment();
    private LocationFragment locationFragment = new LocationFragment();
    private RechercheFragment rechercheFragment = new RechercheFragment();
    private CommandeFragment commandeFragment = new CommandeFragment();

    private List<Object> objects = new ArrayList<>();

    public BottomNavFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BottomNavFragment newInstance() {
        BottomNavFragment fragment = new BottomNavFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_main_activity, container, false);

        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, locationFragment, TAG_LOCATION_FRAGMENT)
                .commit();

        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, rechercheFragment, TAG_SEARCH_FRAGMENT)
                .commit();

        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, commandeFragment, TAG_COMMANDE_FRAGMENT)
                .commit();

        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, accueilFragment, TAG_HOME_FRAGMENT)
                .commit();

        hideAndShow(TAG_HOME_FRAGMENT);

        mBottomNav = (BottomNavigationView) viewRoot.findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        return viewRoot;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void hideAndShow(String tag) {

        switch (tag){

            case TAG_HOME_FRAGMENT:

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(locationFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(rechercheFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(commandeFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(accueilFragment)
                        .commit();

                break;

            case TAG_LOCATION_FRAGMENT:

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(accueilFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(rechercheFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(commandeFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(locationFragment)
                        .commit();

                break;

            case TAG_SEARCH_FRAGMENT:

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(locationFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(accueilFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(commandeFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(rechercheFragment)
                        .commit();

                break;

            case TAG_COMMANDE_FRAGMENT:

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(locationFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(rechercheFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(accueilFragment)
                        .commit();

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(commandeFragment)
                        .commit();

                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void selectFragment(MenuItem item) {
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.navigation_home:

                hideAndShow(TAG_HOME_FRAGMENT);

                break;

            case R.id.navigation_location:

                hideAndShow(TAG_LOCATION_FRAGMENT);

                break;

            case R.id.navigation_search:

                hideAndShow(TAG_SEARCH_FRAGMENT);

                break;

            case R.id.navigation_cart:

                hideAndShow(TAG_COMMANDE_FRAGMENT);

                break;
        }

        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        updateToolbarText(item.getTitle());
    }

    public void updateToolbarText(CharSequence text) {
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
}
