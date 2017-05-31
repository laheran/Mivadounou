package mivadounou.projet2.uut.ucao.mivadounou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Noces_details;
import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Noces_menu;

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
                Noces_menu tab1 = new Noces_menu();
                return tab1;
            case 1:
                Noces_details tab2 = new Noces_details();
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
