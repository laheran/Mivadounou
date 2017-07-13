package mivadounou.projet2.uut.ucao.mivadounou.fragments;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.adapters.mFragmentAdapter;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.user.UserRestauFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment {

    private UserRestauFragment.OnFragmentInteractionListener mListener;

    private FragmentPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;

    private Activity mActivity;

    private TabLayout tabLayout;

    public AccueilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_restau, container, false);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) viewRoot.findViewById(R.id.container);
        tabLayout = (TabLayout) viewRoot.findViewById(R.id.tabs);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllMenu());
        fragments.add(new AllTopMenu());

        List<String> titles = new ArrayList<>();
        titles.add("Menus.");
        titles.add("Les Menus plus appréciés.");

        mPagerAdapter = new mFragmentAdapter(getChildFragmentManager(), fragments, titles);

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

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
}
