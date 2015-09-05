package arya.spliro.viewController.createPost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.custom.OpenCameraDialog;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Phoosaram on 8/19/2015.
 */
public class PriceCalculateFragment extends AbstractFragment implements View.OnClickListener {
    private View rootView;
    private ImageView imgBackPCalc;
    private TextView txtTitleAppHB;
    private TextView txt_total_price;
    private TextView txt_per_share_price;
    private EditText etxt_enter_price;
    private TextView lbl_per_share_price;
    private TextView txt_upload_receipt;
    private  OpenCameraDialog pickImageDialog;
    private static PriceCalculateFragment instance;
    private static double total_price=0;
    private static double noOfShares=1;
    public String savedReceivedImgPath;
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.create_post_price_card,container,false);

        init();
        return rootView;
    }
     public static PriceCalculateFragment getInstance(double total_price_value,double no_of_shares)
     {
         total_price=total_price_value;
         noOfShares=no_of_shares;
         instance=new PriceCalculateFragment();

         return instance;
     }

    private void init()
    {
        imgBackPCalc=(ImageView)rootView.findViewById(R.id.imgBackPCalc);
        txtTitleAppHB=(TextView)rootView.findViewById(R.id.txtTitleAppHB);
        txt_total_price=(TextView)rootView.findViewById(R.id.txt_total_price);
        etxt_enter_price=(EditText)rootView.findViewById(R.id.etxt_enter_price);
        txt_per_share_price=(TextView)rootView.findViewById(R.id.txt_per_share_price);
        lbl_per_share_price=(TextView)rootView.findViewById(R.id.lbl_per_share_price);
        txt_upload_receipt=(TextView)rootView.findViewById(R.id.txt_upload_receipt);
        setTypeFaceToAllFields();
        setValuesToFields();
        imgBackPCalc.setOnClickListener(this);
        txt_upload_receipt.setOnClickListener(this);

    }

    private void setTypeFaceToAllFields() {
        Typeface lite= SpliroApp.getFontLight();
        Typeface semibold= SpliroApp.getFontSemiBold();
        txtTitleAppHB.setTypeface(semibold);
        txt_total_price.setTypeface(lite);
        txt_per_share_price.setTypeface(lite);
        etxt_enter_price.setTypeface(lite);
        lbl_per_share_price.setTypeface(lite);
        txt_upload_receipt.setTypeface(semibold);
        etxt_enter_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String s="0";
                if(!charSequence.toString().isEmpty())
                {
                    double d=Util.toDouble(charSequence.toString())/noOfShares;
                    s=Util.formatTwoDecimalPlaces(d);
                }
                txt_per_share_price.setText("$"+s+" per share");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view)
    {
        int vId=view.getId();
        if(vId==R.id.imgBackPCalc)
        {
            getActivity().onBackPressed();
            Util.hideKeyBoardMethod(getActivity(),imgBackPCalc);
        }
        else if(vId==R.id.txt_upload_receipt)
        {
            if(pickImageDialog==null) {
                pickImageDialog = new OpenCameraDialog();
            }
            if(!pickImageDialog.isVisible())
                pickImageDialog.show(getActivity().getSupportFragmentManager(), null);

        }
    }
    private void setValuesToFields()
    {
        if(total_price>0)
        {
            etxt_enter_price.setText(""+total_price);
            double perShare_price=total_price/noOfShares;
            txt_per_share_price.setText("$"+Util.formatTwoDecimalPlaces(perShare_price)+" per share");

        }
        else
        {
            total_price=0;
            etxt_enter_price.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.PICK_IMAGE_FROM_GALLERY){
            Constants.imageUri=data.getData();
        }
        if(requestCode == Constants.PICK_IMAGE_FROM_CAMERA||requestCode == Constants.PICK_IMAGE_FROM_GALLERY)
        {
            if(resultCode==getActivity().RESULT_OK)
            {
                LoadImagesFromSDCard loadImagesFromSDCard=new LoadImagesFromSDCard();
                loadImagesFromSDCard.execute();

            }
        }
    }
    public class LoadImagesFromSDCard extends AsyncTask<Void, Bitmap, Bitmap> {
        Bitmap mBitmap;
        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/
            Util.showProDialog(null);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            if (Constants.imageUri != null)
            {
                 savedReceivedImgPath=Util.compressImage(getActivity(),Constants.imageUri.toString());
                mBitmap = BitmapFactory.decodeFile(savedReceivedImgPath);
                SystemClock.sleep(2000);
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap bm)
        {
            Util.dimissProDialog();
            if (bm != null) {
//                createDataObj. profile_image_path=Constants.createdImagePath.getPath();
//                if (bm != null && !bm.isRecycled()) {
//                    bm.recycle();
//                    bm = null;
//                }
//                createJSOnArray(Constants.FileType_image, bm);
//                if(createAdapter!=null)
//                {
//                    createAdapter.notifyDataSetChanged();
//                }
            }

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }
    }


    public double getPrice()
    {
        double price = 0;
        if(!etxt_enter_price.getText().toString().isEmpty())
        {
            price=Double.valueOf(etxt_enter_price.getText().toString());
        }
        return price;
    }

}
