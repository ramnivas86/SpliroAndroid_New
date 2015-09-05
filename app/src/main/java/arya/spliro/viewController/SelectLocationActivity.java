package arya.spliro.viewController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.adapters.SelectLocationListAdapter;
import arya.spliro.dao.LocationData;
import arya.spliro.model.SelectLocationListModel;
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Phoosaram on 8/17/2015.
 */
public class SelectLocationActivity extends AbstractFragmentActivity implements View.OnClickListener{

    private ImageView imgBackAppH;
    private TextView txt_cancel_SLoc;
    private TextView txt_apply_SLOC;
    private TextView txtaddLocation;
    private TextView txtTitleAppHB;
    public ListView listView_location;
    private LocationData loc_to_send_back;
    private SelectLocationListAdapter locationListAdapter;
    private SelectLocationListModel model=new SelectLocationListModel();
    @Override
    protected void onCreatePost(Bundle savedInstanceState)
    {
        setContentView(R.layout.brows_screen_location);
        init();

    }
    private void init()
    {
        imgBackAppH=(ImageView)findViewById(R.id.imgBackAppH);
        txt_cancel_SLoc=(TextView)findViewById(R.id.txt_cancel_SLoc);
        txt_apply_SLOC=(TextView)findViewById(R.id.txt_apply_SLOC);
        txtaddLocation=(TextView)findViewById(R.id.txtaddLocation);
        txtTitleAppHB=(TextView)findViewById(R.id.txtTitleAppHB);
        listView_location=(ListView)findViewById(R.id.listView_location);
        setTypeFaceToAllFields();
        imgBackAppH.setOnClickListener(this);
        txt_cancel_SLoc.setOnClickListener(this);
        txt_apply_SLOC.setOnClickListener(this);
        txtaddLocation.setOnClickListener(this);
        model.getLocationListFromServer();
        listView_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                for(LocationData loc:SpliroApp.default_locations)
                {
                    loc.is_default="0";
                }
                loc_to_send_back=SpliroApp.default_locations.get(position);
                SpliroApp.default_locations.get(position).is_default="1";
                locationListAdapter.notifyDataSetChanged();
            }
        });



    }

    private void setDataToList()
    {
        locationListAdapter=new SelectLocationListAdapter(SelectLocationActivity.this,SpliroApp.default_locations);
        listView_location.setAdapter(locationListAdapter);
    }

    @Override
    protected BasicModel getModel() {
        return model;
    }

    @Override
    public void update(Observable observable, Object o)
    {
        setDataToList();

    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.imgBackAppH)
        {
            onBackPressed();
        }
        else if(vId==R.id.txt_cancel_SLoc)
        {
            onBackPressed();
        }
        else if(vId==R.id.txtaddLocation)
        {
            Intent i=new Intent(SelectLocationActivity.this,AddLocationActivity.class);
            startActivity(i);
            Util.startActAnimation(SelectLocationActivity.this);

        }
        else if(vId==R.id.txt_apply_SLOC)
        {

            if(loc_to_send_back!=null)
            {
                SpliroApp.defaultLocation=loc_to_send_back;
                setResult(RESULT_OK,new Intent().putExtra(Constants.SELECTED_LOCATION,loc_to_send_back));
            }
            super.onBackPressed();
            Util.endActAnimation(this);


        }

    }

    private void setTypeFaceToAllFields()
    {
        txt_cancel_SLoc.setTypeface(SpliroApp.getFontLight());
        txt_apply_SLOC.setTypeface(SpliroApp.getFontLight());
        txtaddLocation.setTypeface(SpliroApp.getFontLight());
        txtTitleAppHB.setTypeface(SpliroApp.getAppHeaderTypeface());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED,new Intent().putExtra(Constants.SELECTED_LOCATION,loc_to_send_back));
        Util.endActAnimation(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDataToList();
    }
}
