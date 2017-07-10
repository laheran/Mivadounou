package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.adapters.mFragmentAdapter;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewMenuFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserRestauHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserRestauHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRestauHomeFragment extends Fragment {

    private UserRestauFragment.OnFragmentInteractionListener mListener;

    private FragmentPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;

    private FloatingActionButton floatingActionButton;

    private Activity mActivity;

    private TabLayout tabLayout;

    public UserRestauHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        View viewRoot = inflater.inflate(R.layout.fragment_restau, container, false);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) viewRoot.findViewById(R.id.container);
        tabLayout = (TabLayout) viewRoot.findViewById(R.id.tabs);

//        floatingActionButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab_new_post);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UserRestauMenusFragment());
        fragments.add(new UserRestauTopMenusFragment());

        List<String> titles = new ArrayList<>();
        titles.add("Menus du Restau.");
        titles.add("Les plus appréciés.");

        mPagerAdapter = new mFragmentAdapter(getChildFragmentManager(), fragments, titles);

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
//        // Button launches NewPostActivity
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MainActivity.isNewMenu = true;
//
//                if (mActivity instanceof MainActivity) {
//
//                    ((MainActivity) mActivity).hideAndShow(MainActivity.TAG_NEW_MENU_FRAGMENT, new NewMenuFragment());
//
//                }
//            }
//        });
        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
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
