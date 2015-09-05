package arya.spliro.viewController.browse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.model.DataSyncModel;
import arya.spliro.utility.Util;
import arya.spliro.viewController.AddLocationActivity;
import arya.spliro.viewController.SpliroApp;

public class BrowseFragment extends AbstractFragment implements View.OnClickListener {
    private Button btnLocationB;
    private TextView txtTitleAppHB;
    private ImageView imgSearchBrowseB;
    View v;
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_browser_main,container,false);
        init();
        return v;
    }
    @Override
    protected BasicModel getModel() {
        return null;
    }

    private void init()
    {

        btnLocationB=(Button)v.findViewById(R.id.btnLocationB);
        txtTitleAppHB =(TextView)v.findViewById(R.id.txtTitleAppHB);
        imgSearchBrowseB=(ImageView)v.findViewById(R.id.imgSearchBrowseB);
        btnLocationB.setOnClickListener(this);
        imgSearchBrowseB.setOnClickListener(this);
        btnLocationB.setTypeface(SpliroApp.getFontRegular());
        txtTitleAppHB.setTypeface(SpliroApp.getAppHeaderTypeface());
        btnLocationB.setText(SpliroApp.defaultLocation.zipcode);

    }

    @Override
    public void onClick(View view) {
int vId=view.getId();
        if(vId==R.id. imgSearchBrowseB)
        {

        }
        else if(vId==R.id. imgSearchBrowseB)
        {

        }
        else if(vId==R.id.btnLocationB)
        {
            Intent i=new Intent(getActivity(), AddLocationActivity.class);
            startActivity(i);
            Util.startActAnimation(getActivity());
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
