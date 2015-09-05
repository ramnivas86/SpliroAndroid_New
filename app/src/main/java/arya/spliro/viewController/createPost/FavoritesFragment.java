package arya.spliro.viewController.createPost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.model.FavoritesModel;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 8/20/2015.
 */
public class FavoritesFragment extends AbstractFragment implements View.OnClickListener {
private FavoritesModel favoritesModel=new FavoritesModel();
    private View favoriteView;
    private ImageView imgBackFav;
    private TextView txtTitleFav;
    private  ImageView imgSearchFav;
    private  TextView txtSharesFav;
    private TextView txtUserFav ;
    private ListView lvFav;
    private FragmentManager fm;
    private FragmentTransaction ft;
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        favoriteView=inflater.inflate(R.layout.frg_favorite,container,false);
        init();
        return favoriteView;
    }

    private void init() {
       imgBackFav= (ImageView) favoriteView.findViewById(R.id.imgBackFav);
         txtTitleFav= (TextView) favoriteView.findViewById(R.id.txtTitleFav);
         imgSearchFav= (ImageView) favoriteView.findViewById(R.id.imgSearchFav);
      txtSharesFav= (TextView) favoriteView.findViewById(R.id.txtSharesFav);
        txtUserFav = (TextView) favoriteView.findViewById(R.id.txtUserFav);
        lvFav= (ListView) favoriteView.findViewById(R.id.lvFav);
        imgBackFav.setOnClickListener(this);
        txtTitleFav.setOnClickListener(this);
        imgSearchFav.setOnClickListener(this);
        txtSharesFav.setOnClickListener(this);
        txtUserFav.setOnClickListener(this);
        txtTitleFav.setTypeface(SpliroApp.getFontSemiBold());
        txtSharesFav.setTypeface(SpliroApp.getFontSemiBold());
        txtUserFav .setTypeface(SpliroApp.getFontSemiBold());
        fm=getActivity().getSupportFragmentManager();
        ft=fm.beginTransaction();
        clickOnSharesTab();
    }

    @Override
    protected BasicModel getModel() {
        return favoritesModel;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
    int vId=view.getId();
        if(vId==R.id.imgBackFav)
        {
        getActivity().onBackPressed();
        }
        else  if(vId==R.id.imgSearchFav)
        {

        }
        else  if(vId==R.id.txtSharesFav)
        {
            clickOnSharesTab();
        }
        else  if(vId==R.id.txtUserFav)
        {
            clickOnUserTab();

        }
    }
    public void clickOnUserTab()
    {
        txtSharesFav.setSelected(false);
        txtUserFav.setSelected(true);
        txtUserFav.setEnabled(false);
        txtSharesFav.setEnabled(true);

    }
    public void clickOnSharesTab()
    {
        txtSharesFav.setSelected(true);
        txtUserFav.setSelected(false);
        txtUserFav.setEnabled(true);
        txtSharesFav.setEnabled(false);
    }
}
