package arya.spliro.viewController.myShares;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.model.MyShareSearchModel;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 9/7/2015.
 */
public class MyShareSearchFragment extends AbstractFragment implements View.OnClickListener {
    private MyShareSearchModel myShareSearchModel = new MyShareSearchModel();
    private View view;

    private ImageView imgBackAppHS;
    private TextView txtTitleAppHS;
    private Button btnLocationS;
    private ImageView imgClearSearch;
    private SeekBar seekLocation;
    private TextView txtSeekMin;
    private TextView txtSeekMax;
    private TextView txtSeekTitle;
    private TextView txtCatTab;
    private TextView txtDisplayNameTab;
    private ListView lvS;
    private EditText eTexSearch;
    private TextView txtApplyS;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_my_share_search, container, false);
        init();
        return view;
    }

    private void init() {
        imgBackAppHS = (ImageView) view.findViewById(R.id.imgBackAppHS);
        txtTitleAppHS = (TextView) view.findViewById(R.id.txtTitleAppHS);
        btnLocationS = (Button) view.findViewById(R.id.btnLocationS);
        imgClearSearch = (ImageView) view.findViewById(R.id.imgClearSearch);
        seekLocation = (SeekBar) view.findViewById(R.id.seekLocation);
        txtSeekMin = (TextView) view.findViewById(R.id.txtSeekMin);
        txtSeekMax = (TextView) view.findViewById(R.id.txtSeekMax);
        txtSeekTitle = (TextView) view.findViewById(R.id.txtSeekTitle);
        txtCatTab = (TextView) view.findViewById(R.id.txtCatTab);
        txtDisplayNameTab = (TextView) view.findViewById(R.id.txtDisplayNameTab);
        lvS = (ListView) view.findViewById(R.id.lvS);
        txtApplyS = (TextView) view.findViewById(R.id.txtApplyS);
        eTexSearch=(EditText)view.findViewById(R.id.eTexSearch);
        imgBackAppHS.setOnClickListener(this);
        btnLocationS.setOnClickListener(this);
        imgClearSearch.setOnClickListener(this);
        txtCatTab.setOnClickListener(this);
        txtDisplayNameTab.setOnClickListener(this);
        txtApplyS.setOnClickListener(this);
         setTypeFaceToAllFields();
    }

    private void setTypeFaceToAllFields()
    {
        txtTitleAppHS.setTypeface(SpliroApp.getAppHeaderTypeface());
        btnLocationS.setTypeface(SpliroApp.getFontLight());
        txtSeekMin.setTypeface(SpliroApp.getFontLight());
        txtSeekMax.setTypeface(SpliroApp.getFontLight());
        txtSeekTitle.setTypeface(SpliroApp.getFontLight());
        txtCatTab.setTypeface(SpliroApp.getFontLight());
        txtDisplayNameTab.setTypeface(SpliroApp.getFontLight());
        eTexSearch.setTypeface(SpliroApp.getFontLight());
        txtApplyS.setTypeface(SpliroApp.getFontSemiBold());
    }

    @Override
    protected BasicModel getModel() {
        return myShareSearchModel;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if(vId==R.id.imgBackAppHS)
        {

        }
        else if(vId==R.id.btnLocationS)
        {

        }
        else if(vId==R.id.imgClearSearch)
        {

        }
        else if(vId==R.id.txtCatTab)
        {

        }
        else if(vId==R.id.txtDisplayNameTab)
        {

        }
        else if(vId==R.id.txtApplyS)
        {

        }

    }
}
