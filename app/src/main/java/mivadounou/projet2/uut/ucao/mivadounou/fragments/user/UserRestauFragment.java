package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.app.Activity;
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
import android.widget.Toast;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.BottomNavigationViewHelper;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewMenuFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserRestauFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserRestauFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRestauFragment extends Fragment {

    public static final String TAG_USER_RESTAU_HOME_FRAGMENT = "TAG_USER_RESTAU_HOME_FRAGMENT";

    private final String TAG_USER_RESTAU_NEW_MENU_FRAGMENT = "TAG_USER_RESTAU_NEW_MENU_FRAGMENT";

    private final String TAG_USER_RESTAU_COMMANDE_FRAGMENT = "TAG_USER_RESTAU_COMMANDE_FRAGMENT";

    private final String TAG_USER_RESTAU_SETTING_FRAGMENT = "TAG_USER_RESTAU_SETTING_FRAGMENT";

    private OnFragmentInteractionListener mListener;

    private Activity mActivity;

    private ActionBar actionBar;

    private BottomNavigationView mBottomNav;

    private UserRestauHomeFragment userRestauHomeFragment = new UserRestauHomeFragment();

    private Fragment oldFragment;

    public UserRestauFragment() {
        // Required empty public constructor
    }

    public static UserRestauFragment newInstance() {
        UserRestauFragment fragment = new UserRestauFragment();
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
        View viewRoot = inflater.inflate(R.layout.fragment_user_restau, container, false);

        mBottomNav = (BottomNavigationView) viewRoot.findViewById(R.id.user_restau_navigation);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.user_restau_container, userRestauHomeFragment, TAG_USER_RESTAU_HOME_FRAGMENT)
                .commit();

        hideAndShow(TAG_USER_RESTAU_HOME_FRAGMENT);
    }

    public void hideAndShow(String tag) {

        switch (tag) {

            case TAG_USER_RESTAU_HOME_FRAGMENT:

                mBottomNav.setSelectedItemId(R.id.home_user_restau_menu);

                if (oldFragment != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .hide(oldFragment)
                            .commit();
                }

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(userRestauHomeFragment)
                        .commit();

                break;

            case TAG_USER_RESTAU_NEW_MENU_FRAGMENT:

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(userRestauHomeFragment)
                        .commit();

                if (oldFragment != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .hide(oldFragment)
                            .commit();
                }

                oldFragment = new NewMenuFragment();

                getChildFragmentManager()
                        .beginTransaction()
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.user_restau_container, oldFragment)
                        .commit();

                break;

            case TAG_USER_RESTAU_COMMANDE_FRAGMENT:

                break;

            case TAG_USER_RESTAU_SETTING_FRAGMENT:

                break;
        }
    }

    public void toDetail(NewMenuFragment newMenuFragment) {

        oldFragment = newMenuFragment;

        getChildFragmentManager()
                .beginTransaction()
                .hide(userRestauHomeFragment)
                .commit();

        getChildFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.user_restau_container, oldFragment)
                .commit();

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void selectFragment(MenuItem item) {
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.home_user_restau_menu:

                updateToolbarText(item.getTitle());

                hideAndShow(TAG_USER_RESTAU_HOME_FRAGMENT);

                break;

            case R.id.new_menu_user_restau_menu:

                updateToolbarText(item.getTitle());

                hideAndShow(TAG_USER_RESTAU_NEW_MENU_FRAGMENT);

                break;

            case R.id.commande_user_restau_menu:

                updateToolbarText(item.getTitle());

                break;

            case R.id.setting_user_restau_menu:

                updateToolbarText(item.getTitle());

                break;
        }


        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }
    }

    public void updateToolbarText(CharSequence text) {
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
