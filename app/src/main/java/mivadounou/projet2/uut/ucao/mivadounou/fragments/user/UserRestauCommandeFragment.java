package mivadounou.projet2.uut.ucao.mivadounou.fragments.user;

import android.content.Context;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserRestauCommandeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserRestauCommandeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRestauCommandeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FragmentPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;

    private TabLayout tabLayout;

    public UserRestauCommandeFragment() {
        // Required empty public constructor
    }

    public static UserRestauCommandeFragment newInstance(String param1, String param2) {
        UserRestauCommandeFragment fragment = new UserRestauCommandeFragment();
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

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UserRestauCommandeReceivedFragment());
        fragments.add(new UserRestauCommandeAccepted());
        fragments.add(new UserRestauCommandeDoneFragment());

        List<String> titles = new ArrayList<>();
        titles.add("Reçu.");
        titles.add("Acceptées.");
        titles.add("Effectuée.");

        mPagerAdapter = new mFragmentAdapter(getChildFragmentManager(), fragments, titles);

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        return viewRoot;
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
