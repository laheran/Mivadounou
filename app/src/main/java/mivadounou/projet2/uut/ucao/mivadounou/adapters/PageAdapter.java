package mivadounou.projet2.uut.ucao.mivadounou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mivadounou.projet2.uut.ucao.mivadounou.fragments.FfoodFragment;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.RestoFragment;

/**
 * Created by LAHERAN on 06/04/2017.
 */
public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RestoFragment tab1 = new RestoFragment();
                return tab1;
            case 1:
                FfoodFragment tab2 = new FfoodFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
