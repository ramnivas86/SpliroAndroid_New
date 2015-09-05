package arya.spliro.slidingTabBar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Ramnivas Singh on 11/08/2015.
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
ArrayList<Fragment> fragmentArrayList ;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb,ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this. fragmentArrayList=fragmentArrayList;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
//Fragment fragment=null;
//        if(position == 0)
//        {
//                    fragment = new BrowseFragment();
//        }
//        else  if(position == 1)
//        {
//            fragment = new NotificationsFragment();
//        }
//        else  if(position == 2)
//        {
//            fragment = new CreatePostFragment();
//        }
//
//        else  if(position == 3)
//        {
//            fragment = new MYSharesFragment();
//        }
//        else  if(position == 4)
//        {
//            fragment = new MoreFragment();
//        }

return  fragmentArrayList.get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}