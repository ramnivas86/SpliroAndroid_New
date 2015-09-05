package arya.spliro.viewController;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.dao.CreateData;
import arya.spliro.utility.Util;
import arya.spliro.viewController.myShares.SharePreviewFragment;

/**
 * Created by phoosaram on 9/1/2015.
 */
public class SharePreviewActivity extends AbstractFragmentActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    CreateData createData;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.frg_share_preview);
        init();
    }

    private void init()
    {
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        SharePreviewFragment sharePreviewFragment=new SharePreviewFragment();
        sharePreviewFragment.setArguments(getIntent().getExtras());
        Util.addFragment(fm,sharePreviewFragment,R.id.sharePreviewContainer,false);

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Util.endActAnimation(this);

    }
}
