package arya.spliro.viewController.myShares;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arya.lib.logger.Logger;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.io.File;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.LocationData;
import arya.spliro.model.SharePreviewModel;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by phoosaram on 9/1/2015.
 */
public class SharePreviewFragment extends AbstractFragment implements View.OnClickListener {
    View rootView;
    private int no_of_clicks = 0;
    CreateData createData;
    private TextView txtSharesNameMSPreview;
    private TextView txtTitleMSPreview;
    private TextView txtPriceMSPreview;
    private TextView txtPerShareMSPreview;
    private TextView txtViewReceiptMSPreview;
    private TextView txtDescriptionMSPreview;
    private TextView txtDistanceMSPreview;
    private TextView txtNoOfPeopleJoinedMSPreview;
    private TextView txtNoOfSharesMSPreview;
    private TextView txtInvitedMSPreview;
    private TextView txtSharesMSPreview;
    private TextView txtDateMSPreview;
    private TextView txtShareEndsMSPreview;
    private TextView txtEditMSPreview;
    private TextView txtRepostMSPreview;
    private TextView txtCloseMSPreview;
    private Button btnBackAppMSPreview;
    private Button btnLocationMSPreview;
    private Button btnMoreDescMSPreview;
    private ImageView cImgItemShareMSPreview;
    private ImageView imgCreatorMSPreview;
    private RatingBar rtShareOwnerMSPreview;
    private LinearLayout llinvitedMSPreview;
    private LinearLayout llViewReceiptMSPreview;
    private SharePreviewModel sharePreviewModelObj = new SharePreviewModel();
    private Dialog dialog;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frg_my_share_singleview, container, false);
        init();
        return rootView;
    }

    private void init() {
        if (getArguments().containsKey(Constants.KEY_FOR_PREVIEW_DATA)) {
            createData = (CreateData) getArguments().getSerializable(Constants.KEY_FOR_PREVIEW_DATA);
        }
        txtSharesNameMSPreview = (TextView) rootView.findViewById(R.id.txtSharesNameMSPreview);
        txtTitleMSPreview = (TextView) rootView.findViewById(R.id.txtTitleMSPreview);
        txtPriceMSPreview = (TextView) rootView.findViewById(R.id.txtPriceMSPreview);
        txtPerShareMSPreview = (TextView) rootView.findViewById(R.id.txtPerShareMSPreview);
        txtViewReceiptMSPreview = (TextView) rootView.findViewById(R.id.txtViewReceiptMSPreview);
        txtDescriptionMSPreview = (TextView) rootView.findViewById(R.id.txtDescriptionMSPreview);
        txtDistanceMSPreview = (TextView) rootView.findViewById(R.id.txtDistanceMSPreview);
        txtInvitedMSPreview = (TextView) rootView.findViewById(R.id.txtInvitedMSPreview);
        txtNoOfPeopleJoinedMSPreview = (TextView) rootView.findViewById(R.id.txtNoOfPeopleJoinedMSPreview);
        txtShareEndsMSPreview = (TextView) rootView.findViewById(R.id.txtShareEndsMSPreview);
        txtSharesMSPreview = (TextView) rootView.findViewById(R.id.txtSharesMSPreview);
        txtNoOfSharesMSPreview = (TextView) rootView.findViewById(R.id.txtNoOfSharesMSPreview);
        txtDateMSPreview = (TextView) rootView.findViewById(R.id.txtDateMSPreview);
        txtEditMSPreview = (TextView) rootView.findViewById(R.id.txtEditMSPreview);
        txtCloseMSPreview = (TextView) rootView.findViewById(R.id.txtCloseMSPreview);
        txtRepostMSPreview = (TextView) rootView.findViewById(R.id.txtRepostMSPreview);
        btnBackAppMSPreview = (Button) rootView.findViewById(R.id.btnBackAppMSPreview);
        btnLocationMSPreview = (Button) rootView.findViewById(R.id.btnLocationMSPreview);
        btnMoreDescMSPreview = (Button) rootView.findViewById(R.id.btnMoreDescMSPreview);
        cImgItemShareMSPreview = (ImageView) rootView.findViewById(R.id.cImgItemShareMSPreview);
        imgCreatorMSPreview = (ImageView) rootView.findViewById(R.id.imgCreatorMSPreview);
        rtShareOwnerMSPreview = (RatingBar) rootView.findViewById(R.id.rtShareOwnerMSPreview);
        llinvitedMSPreview = (LinearLayout) rootView.findViewById(R.id.llinvitedMSPreview);
        llViewReceiptMSPreview = (LinearLayout) rootView.findViewById(R.id.llViewReceiptMSPreview);
        setTypeFaceToAllFields();
        setDataToAllFields();
        btnBackAppMSPreview.setOnClickListener(this);
        btnMoreDescMSPreview.setOnClickListener(this);
        txtRepostMSPreview.setOnClickListener(this);
        cImgItemShareMSPreview.setOnClickListener(this);
        llViewReceiptMSPreview.setOnClickListener(this);
        txtCloseMSPreview.setOnClickListener(this);
        txtEditMSPreview.setOnClickListener(this);
    }

    private void setDataToAllFields() {
        try {
            if (createData != null) {
                txtSharesNameMSPreview.setText(createData.display_name);
                txtTitleMSPreview.setText(createData.title);
                txtPriceMSPreview.setText(Constants.CURRENCY + createData.invoice_price);
                txtPerShareMSPreview.setText(Constants.CURRENCY + createData.per_share_price + Constants.PER_SHARE);
                txtDescriptionMSPreview.setText(createData.categories_data.description);
                txtNoOfSharesMSPreview.setText(createData.no_of_shares);
                btnLocationMSPreview.setText(createData.location_data.zipcode);
                rtShareOwnerMSPreview.setRating(createData.rate);
                txtDateMSPreview.setText(Util.setShareEndDate(createData.post_expire_date));
                Util.loadImage(getActivity(), imgCreatorMSPreview, createData.profile_pic_url, 0);
                txtNoOfPeopleJoinedMSPreview.setText(Util.getSpannedString("0", (int) getActivity().getResources().getDimension(R.dimen.txt_price_size_2)).append(Constants.PEOPLE_JOINED));
                if (createData.user_id == Config.getUserId()) {
                    llinvitedMSPreview.setVisibility(View.GONE);
                    txtDistanceMSPreview.setVisibility(View.GONE);
                    txtDistanceMSPreview.setText(Util.getSpannedString("0", (int) getActivity().getResources().getDimension(R.dimen.txt_price_size_2)).append(Constants.DISTANCE_TEXT));
                } else {
                    txtDistanceMSPreview.setVisibility(View.VISIBLE);
                    txtDistanceMSPreview.setText(Util.getSpannedString(Util.getLocationDistance(createData.location_data.location_latitude, createData.location_data.location_longitude, SpliroApp.defaultLocation.location_latitude, SpliroApp.defaultLocation.location_longitude), (int) getActivity().getResources().getDimension(R.dimen.spannable_txt_size)).append(Constants.DISTANCE_TEXT));
                }
                if (new File(createData.categories_data.image_path).exists()) {
                    cImgItemShareMSPreview.setTag(createData.categories_data.image_path);
                    Util.loadImageFromSDcard(getActivity(), cImgItemShareMSPreview, createData.categories_data.image_path, R.drawable.ic_launcher);
                } else {
                    Util.loadImage(getActivity(), cImgItemShareMSPreview, createData.categories_data.pic_thumb_url, 0);
                }
                if (createData.status.equals(Constants.POSTING_STATUS)) {
                    txtRepostMSPreview.setVisibility(View.VISIBLE);
                    txtCloseMSPreview.setText(getActivity().getResources().getString(R.string.close));
                    txtEditMSPreview.setText(getActivity().getResources().getString(R.string.edit));
                } else if (createData.status.equals(Constants.CLOSED_STATUS)) {
                    txtCloseMSPreview.setText(getActivity().getResources().getString(R.string.delete));
                    txtEditMSPreview.setText(getActivity().getResources().getString(R.string.repost));
                    txtRepostMSPreview.setVisibility(View.INVISIBLE);
                } else if (createData.status.equals(Constants.SAVING_STATUS)) {
                    txtEditMSPreview.setText(getActivity().getResources().getString(R.string.repost));
                    txtCloseMSPreview.setText(getActivity().getResources().getString(R.string.delete));
                    txtRepostMSPreview.setVisibility(View.INVISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (Logger.IS_DEBUG) {
                Logger.error("SharePreviewFragment", "setDataToAllFields(),reason[" + e.getMessage() + "]");
            }
        }

    }

    private void setTypeFaceToAllFields() {
        try {
            Typeface lightTTf = SpliroApp.getFontLight();
            Typeface semiBoldTTF = SpliroApp.getFontSemiBold();
            txtSharesNameMSPreview.setTypeface(lightTTf);
            txtTitleMSPreview.setTypeface(semiBoldTTF);
            txtPriceMSPreview.setTypeface(semiBoldTTF);
            txtPerShareMSPreview.setTypeface(lightTTf);
            txtViewReceiptMSPreview.setTypeface(lightTTf);
            txtDescriptionMSPreview.setTypeface(lightTTf);
            txtDistanceMSPreview.setTypeface(semiBoldTTF);
            txtNoOfPeopleJoinedMSPreview.setTypeface(lightTTf);
            txtNoOfSharesMSPreview.setTypeface(semiBoldTTF);
            txtInvitedMSPreview.setTypeface(lightTTf);
            txtSharesMSPreview.setTypeface(lightTTf);
            txtDateMSPreview.setTypeface(lightTTf);
            txtShareEndsMSPreview.setTypeface(lightTTf);
            btnLocationMSPreview.setTypeface(lightTTf);
            txtEditMSPreview.setTypeface(lightTTf);
            txtRepostMSPreview.setTypeface(lightTTf);
            txtCloseMSPreview.setTypeface(lightTTf);
        } catch (Exception e) {
            e.printStackTrace();
            if (Logger.IS_DEBUG) {
                Logger.error("SharePreviewFragment", "setTypeFaceToAllFields(),reason[" + e.getMessage() + "]");
            }
        }


    }


    @Override
    protected BasicModel getModel() {
        return sharePreviewModelObj;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof CreateData) {
            CreateData createDataObj = (CreateData) o;
            if (createDataObj.status.equals(Constants.CLOSED_STATUS) && createDataObj.isSuccess) {
                getActivity().setResult(Constants.RESULT_CODE_REFRESHMYSHARES, new Intent().putExtra(Constants.CLOSED_STATUS, this.createData));
                getActivity().onBackPressed();
                Util.showCenteredToast(getActivity().getString(R.string.post_closed_successfully));
            } else if (createDataObj.status.equals(Constants.DELETE_STATUS)) {
                getActivity().setResult(Constants.RESULT_CODE_REFRESHMYSHARES, new Intent().putExtra(Constants.CLOSED_STATUS, this.createData));
                getActivity().onBackPressed();
                Util.showCenteredToast(createDataObj.errorMsg);
            } else {
                if (!createDataObj.errorMsg.isEmpty()) {
                    Util.showOkDialog(null, createDataObj.errorMsg);
                    createDataObj.errorMsg = "";
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.btnMoreDescMSPreview) {
            no_of_clicks++;
            if (no_of_clicks % 2 == 0) {
                txtDescriptionMSPreview.setMaxLines(Constants.MIN_LINES_TO_SHOW);
                if (txtDescriptionMSPreview.getLineCount() > Constants.MIN_LINES_TO_SHOW) {
                    txtDescriptionMSPreview.setBackgroundResource(R.drawable.spl_ic_more_arrow);
                }
            } else {
                txtDescriptionMSPreview.setMaxLines(Constants.MAX_LINES_TO_SHOW);
                if (txtDescriptionMSPreview.getLineCount() > Constants.MIN_LINES_TO_SHOW) {
                    txtDescriptionMSPreview.setBackgroundResource(R.drawable.spl_ic_more_arrow_up);
                }
            }
        } else if (vId == R.id.btnBackAppMSPreview) {
            getActivity().onBackPressed();
        } else if (vId == R.id.txtRepostMSPreview) {
            this.createData.post_guid = Util.getGUID(null);
            this.createData.post_id = 0;
            getActivity().setResult(getActivity().RESULT_OK, new Intent().putExtra(Constants.REPOST_SHARE, this.createData));
            getActivity().onBackPressed();
        } else if (vId == R.id.cImgItemShareMSPreview) {
            String imagePath = (String) view.getTag();
            Util.showImageInDefaultImageViewer(getActivity(), imagePath);
        } else if (vId == R.id.llViewReceiptMSPreview) {
            if (createData.receipt_image_path != null && !createData.receipt_image_path.isEmpty()) {
                Util.showImageInDefaultImageViewer(getActivity(), createData.receipt_image_path);
            }

        } else if (vId == R.id.txtCloseMSPreview) {
            if (createData.status.equals(Constants.POSTING_STATUS)) {
                dialog = Util.showYesNoMessageDialog(getActivity(), getString(R.string.do_you_want_to_close_post), getString(R.string.app_name), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        createData.status = Constants.CLOSED_STATUS;
                        Util.showProDialog(null);
                        createData.ctx = getActivity();
                        sharePreviewModelObj.changeShareStatus(createData);
                    }
                }, null, null, null);
            } else if (createData.status.equals(Constants.CLOSED_STATUS) || createData.status.equals(Constants.SAVING_STATUS)) {
                dialog = Util.showYesNoMessageDialog(getActivity(), getString(R.string.do_you_want_to_delete), getString(R.string.app_name), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Util.showProDialog(null);
                        createData.status = Constants.DELETE_STATUS;
                        createData.ctx = getActivity();
                        sharePreviewModelObj.changeShareStatus(createData);
                    }
                }, null, null, null);
            }
        } else if (vId == R.id.txtEditMSPreview) {
            getActivity().setResult(getActivity().RESULT_OK, new Intent().putExtra(Constants.EDIT_SHARE, this.createData));
            getActivity().onBackPressed();
        }

    }
}
