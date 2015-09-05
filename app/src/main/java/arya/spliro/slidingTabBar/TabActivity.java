package arya.spliro.slidingTabBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.dao.CreateData;
import arya.spliro.fragments.NotificationsFragment;
import arya.spliro.model.DataSyncModel;
import arya.spliro.model.TabModel;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.browse.BrowseFragment;
import arya.spliro.viewController.createPost.CreatePostFragment;
import arya.spliro.viewController.createPost.InviteAllFragment;
import arya.spliro.viewController.createPost.PreviewFragment;
import arya.spliro.viewController.createPost.PriceCalculateFragment;
import arya.spliro.viewController.more.MoreFragment;
import arya.spliro.viewController.myShares.MYSharesFragment;

/**
 * Created by Ramnivas Singh on 11/08/2015.
 */
public class TabActivity extends AbstractFragmentActivity {

    // Declaring Your View and Variables
    public ViewPager pager;
    TabViewPagerAdapter adapter;
    public static SlidingTabLayout tabs;
    CharSequence Titles[];
    int Numboftabs = 5;
    private String[] tabImgIdArray;
    public static ArrayList<Drawable> drawableList = new ArrayList<Drawable>();
    private String[] selectedTabImgIdArray;
    ArrayList<Fragment> fragmentArrayList;
    TabModel tabModel = new TabModel();
    public static boolean disablePagerScroll = false;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.tab_activity);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQ_FOR_SHARE_PREVIEW) {
                pager.setCurrentItem(2);
                fragmentArrayList.get(2).onActivityResult(requestCode, resultCode, data);
            } else if (fragmentArrayList.get(2).isVisible()) {
                Fragment f = fm.findFragmentById(R.id.createFrameContainer);
                if (fm.getBackStackEntryCount() > 0) {
                    f.onActivityResult(requestCode, resultCode, data);
                } else {
                    fragmentArrayList.get(2).onActivityResult(requestCode, resultCode, data);
                }
            }
            else if(fragmentArrayList.get(4).isVisible())
            {
                Fragment f = fm.findFragmentById(R.id.createFrameContainer);
                if (fm.getBackStackEntryCount()>0 ) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
                else {
                    fragmentArrayList.get(4).onActivityResult(requestCode, resultCode, data);
                }
            }

        }
        else if(resultCode==Constants.RESULT_CODE_REFRESHMYSHARES)
        {
//            fragmentArrayList.get(3).onActivityResult(requestCode, resultCode, data);
            ((MYSharesFragment)fragmentArrayList.get(3)).refresListView((CreateData)data.getSerializableExtra(Constants.CLOSED_STATUS));
        }
    }

    private void init() {
        Config.init(this);
        Util.checkAppFoledIsExist();
        tabModel.setCurrentLocation(TabActivity.this);
        disablePagerScroll = false;
        Titles = getResources().getStringArray(R.array.tab_array);
        tabImgIdArray = getResources().getStringArray(R.array.tab_imgages_id_array);
        selectedTabImgIdArray = getResources().getStringArray(R.array.tab_selected_imgages_id_array);
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new BrowseFragment());
        fragmentArrayList.add(new NotificationsFragment());
        fragmentArrayList.add(new CreatePostFragment());
        fragmentArrayList.add(new MYSharesFragment());
        fragmentArrayList.add(new MoreFragment());
        createTabViewList(this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        adapter = new TabViewPagerAdapter(fm, Titles, Numboftabs, fragmentArrayList);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        if (Util.isTablet(this)) {
            SlidingTabLayout.TAB_VIEW_TEXT_SIZE_SP = 18;
        } else {
            SlidingTabLayout.TAB_VIEW_TEXT_SIZE_SP = 10;
        }
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setCustomTabView(R.layout.tab_row_layout, R.id.main_tab_title);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(android.R.color.transparent);
            }
        });
        tabs.setViewPager(pager);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return disablePagerScroll;
            }
        });
        DataSyncModel synchModel = new DataSyncModel();
        synchModel.syncData();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() // 4/9/2015 phoosaram
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3)
                {
                    ((MYSharesFragment)fragmentArrayList.get(3) ).refreshList();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    public void createTabViewList(Context ctx) {
        for (int j = 0; j < 5; j++) {
            Drawable normalDrawableImg = getDrawableImgUsingId(ctx, tabImgIdArray[j]);
            Drawable selectedDrawableImg = getDrawableImgUsingId(ctx, selectedTabImgIdArray[j]);
            drawableList.add(setImageSelector(TabActivity.this, selectedDrawableImg, normalDrawableImg));
        }
    }

    private StateListDrawable setImageSelector(Context ctx, Drawable selectedDrawableImg, Drawable normalDrawableImg) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, selectedDrawableImg);
        states.addState(new int[]{android.R.attr.state_selected}, selectedDrawableImg);
        states.addState(new int[]{android.R.attr.state_focused}, selectedDrawableImg);
        states.addState(new int[]{}, normalDrawableImg);
//        states.setBounds(0,0,30,30);
        return states;
    }

    public Drawable getDrawableImgUsingId(Context ctx, String imgId) {
        return ctx.getResources().getDrawable(getResources()
                .getIdentifier(imgId, "drawable", getPackageName()));
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onBackPressed() {
        Fragment f = fm.findFragmentById(R.id.createFrameContainer);
        if (fm.getBackStackEntryCount() > 0) {
            if (f instanceof PriceCalculateFragment) {
                ((CreatePostFragment) fragmentArrayList.get(2)).setDataToPrice(((PriceCalculateFragment) f).getPrice(), ((PriceCalculateFragment) f).savedReceivedImgPath);
            } else if (f instanceof InviteAllFragment) {
                ((CreatePostFragment) fragmentArrayList.get(2)).setDataToInvitees(((InviteAllFragment) f).getSelectedInviteeList());
            } else if (f instanceof PreviewFragment && ((PreviewFragment) f).resetCreateScreen) {
                ((CreatePostFragment) fragmentArrayList.get(2)).createPostModel.notifyObservers(((PreviewFragment) f).resetCreateScreen);
            }
            fm.popBackStack();

        } else {
            Util.gotoHomeScreen(TabActivity.this);
        }
    }

    public static void disabledScrolling(boolean b) {
        disablePagerScroll = b;
        tabs.disabled(disablePagerScroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Env.currentActivity = this;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (disablePagerScroll) {
            if (fragmentArrayList.get(2).isVisible() && disablePagerScroll) {
                ((CreatePostFragment) fragmentArrayList.get(2)).clickOnBackPressed(true);
            }

        }

    }


}