package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import android.os.Bundle;
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

/**
 * Created by leinad on 7/14/17.
 */

public class CommandeFragment extends Fragment {

    private FragmentPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;

    private TabLayout tabLayout;

    public CommandeFragment() {
        // Required empty public constructor
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

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CommandeSentFragment());
        fragments.add(new CommandeAcceptedFragment());
        fragments.add(new CommandeDoneFragment());

        List<String> titles = new ArrayList<>();
        titles.add("Envoyée.");
        titles.add("Acceptées.");
        titles.add("Effectuée.");

        mPagerAdapter = new mFragmentAdapter(getChildFragmentManager(), fragments, titles);

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        return viewRoot;
    }
}
