package arya.spliro.viewController.myShares;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.adapters.MSharesPastAdapter;
import arya.spliro.dao.CreateData;
import arya.spliro.model.MyShareModel;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SharePreviewActivity;

/**
 * Created by phoosaram on 26/08/2015.
 */
public class MYSharesFragment extends AbstractFragment implements View.OnClickListener {
    MyShareModel myShareModel=new MyShareModel();
    private ImageView imgRightMS;
    private ImageView imgSearchMS;
    private Button btnBackAppHMS;
    private Button btnPast;
    private Button btnCurrent;
    private Button btnSaved;
    private ListView listViewMyShare;
    private ArrayList<CreateData> sharesList;
    private MSharesPastAdapter adapter;
    private View rootView;
    private String selectedTabName;
    private int clickedPosition;
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.frg_my_share,container,false);
        init();
        return rootView;
    }

    @Override
    protected BasicModel getModel() {
        return myShareModel;
    }
    private void init() {
        imgRightMS = (ImageView) rootView.findViewById(R.id.imgRightMS);
        imgSearchMS = (ImageView) rootView.findViewById(R.id.imgSearchMS);
        btnBackAppHMS = (Button) rootView.findViewById(R.id.btnBackAppHMS);
        btnPast = (Button) rootView.findViewById(R.id.btnPast);
        btnCurrent = (Button)rootView. findViewById(R.id.btnCurrent);
        btnSaved = (Button) rootView.findViewById(R.id.btnSaved);
        listViewMyShare = (ListView) rootView.findViewById(R.id.listViewMyShare);
        imgRightMS.setOnClickListener(this);
        imgSearchMS.setOnClickListener(this);
        btnBackAppHMS.setOnClickListener(this);
        btnPast.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);
        btnSaved.setOnClickListener(this);
        onClick(btnPast);
        listViewMyShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clickedPosition=position;
               CreateData createData= (CreateData)adapter.getItem(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable(Constants.KEY_FOR_PREVIEW_DATA,createData);
                getActivity().startActivityForResult(new Intent(getActivity(), SharePreviewActivity.class).putExtras(bundle),Constants.REQ_FOR_SHARE_PREVIEW);


            }
        });
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();

        if (vId == R.id.imgRightMS) {

        } else if (vId == R.id.imgSearchMS) {
                Util.addFragment(getActivity().getSupportFragmentManager(),new MyShareSearchFragment(),R.id.createFrameContainer,false);
        }  else if (vId == R.id.btnPast) {
            setSelectedTab(btnPast);
        } else if (vId == R.id.btnCurrent) {
            setSelectedTab(btnCurrent);
        } else if (vId == R.id.btnSaved) {
            setSelectedTab(btnSaved);
        }
    }


    private void setSelectedTab(Button btn) {
        boolean b=true;
        setDisableBtn(false);
        btn.setSelected(b);
        btn.setEnabled(false);
         selectedTabName= btn.getText().toString();
        if(selectedTabName.equals(getResources().getString(R.string.past)))
        {
            myShareModel.getSharesList(Constants.CLOSED_STATUS);
            btnSaved.setEnabled(b);
            btnCurrent.setEnabled(b);
        }
        else  if(selectedTabName.equals(getResources().getString(R.string.current)))
        {
            myShareModel.getSharesList(Constants.POSTING_STATUS);
            btnSaved.setEnabled(b);
            btnPast.setEnabled(b);

        }
        else if(selectedTabName.equals(getResources().getString(R.string.saved)))
        {
            myShareModel.getSharesList(Constants.SAVING_STATUS);
            btnCurrent.setEnabled(b);
            btnPast.setEnabled(b);

        }

    }
    private void setDisableBtn(boolean b)
    {
        btnPast.setSelected(b);
        btnCurrent.setSelected(b);
        btnSaved.setSelected(b);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof ArrayList)
        {
            if(sharesList!=null)
            {
                sharesList.clear();
            }

            sharesList=(ArrayList<CreateData>)o;
            if(sharesList!=null)
            {adapter=new MSharesPastAdapter(getActivity(),sharesList, Util.getCurrentTimeInUTC());
                listViewMyShare.setAdapter(adapter);

            }

        }

    }

    public void refresListView(CreateData createData) {
        if(selectedTabName.equals(getResources().getString(R.string.past)))
        {
            sharesList.remove(clickedPosition);
            adapter.notifyDataSetChanged();
//            myShareModel.getSharesList(Constants.POSTING_STATUS);
        }
       else if(selectedTabName.equals(getResources().getString(R.string.current)))
        {
            sharesList.remove(clickedPosition);
            adapter.notifyDataSetChanged();
//            myShareModel.getSharesList(Constants.CLOSED_STATUS);
        }
        if(selectedTabName.equals(getResources().getString(R.string.saved)))
        {
            myShareModel.getSharesList(Constants.SAVING_STATUS);
        }

    }
    public void refreshList()// 4/9/2015 phoosaram
    {
        if(selectedTabName.equals(getResources().getString(R.string.past)))
        {
            myShareModel.getSharesList(Constants.CLOSED_STATUS);
        }
        else if(selectedTabName.equals(getResources().getString(R.string.current)))
        {
            myShareModel.getSharesList(Constants.POSTING_STATUS);
        }
        if(selectedTabName.equals(getResources().getString(R.string.saved)))
        {
            myShareModel.getSharesList(Constants.SAVING_STATUS);
        }

    }
}
