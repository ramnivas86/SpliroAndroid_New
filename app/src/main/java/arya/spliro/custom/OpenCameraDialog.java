package arya.spliro.custom;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import arya.spliro.R;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

public class OpenCameraDialog extends DialogFragment implements OnClickListener {

	private Dialog dialog;
	private TextView txtUseCamera;
	private TextView txtUseExistPhoto;
	private TextView txtUploadPhotoCancel;
	private TextView txtTitleOCamera;
	private String titleTxtForD;
	private String fromCamera;
	private String fromGallery;
	private Bundle b;
	private File imagePath;
	private File temp1FolderPath;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.ot_open_camera_dialog);
		b = this.getArguments();


		init();
		Window window = dialog.getWindow();

//		window.getAttributes().windowAnimations = R.style.AppDialogPullAnimation;
		window.setGravity(Gravity.CENTER);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.setCancelable(true);
		return dialog;
	}

	private void init() {
		Constants.imageUri=null;
		txtUseCamera = (TextView) dialog.findViewById(R.id.txtUseCamera);
		txtUseExistPhoto = (TextView) dialog
				.findViewById(R.id.txtUseExistPhoto);
		txtUploadPhotoCancel = (TextView) dialog
				.findViewById(R.id.txtUploadPhotoCancel);
		txtTitleOCamera = (TextView) dialog.findViewById(R.id.txtTitleOCamera);
		txtUseCamera.setTypeface(SpliroApp.getFontSemiBold());
		txtUseExistPhoto.setTypeface(SpliroApp.getFontSemiBold());
		txtUploadPhotoCancel.setTypeface(SpliroApp.getFontSemiBold());
		txtTitleOCamera.setTypeface(SpliroApp.getFontLight());
//		titleTxtForD = b.getString(Constants.titleTxtForD);
//		fromCamera = b.getString(Constants.fromCamera);
//		fromGallery = b.getString(Constants.fromGallery);
//		txtTitleOCamera.setText(titleTxtForD);
//		txtUseCamera.setText(fromCamera);
//		txtUseExistPhoto.setText(fromGallery);

		txtUseCamera.setOnClickListener(this);
		txtUseExistPhoto.setOnClickListener(this);
		txtUploadPhotoCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.txtUseCamera) {
			goToImageView(txtUseCamera.getContext(), true);
			dismiss();
		} else if (v.getId() == R.id.txtUseExistPhoto) {
			goToImageView(txtUseExistPhoto.getContext(), false);
			dismiss();
		} else if (v.getId() == R.id.txtUploadPhotoCancel) {
			dismiss();
		}
	}

	private void goToImageView(Context ctx, boolean fromCamera) {
		Intent intent = new Intent();
		int pickImage = 0;

		try {
			if (fromCamera) {
					temp1FolderPath= new File(Environment.getExternalStorageDirectory()
							.getPath()+"/" + Constants.AppFolderName+"/"+Constants.tempFolderName);
                if(!temp1FolderPath.exists())
                {
                    temp1FolderPath.mkdirs();
                }
                File createFolder = new File(Environment.getExternalStorageDirectory()
                        .getPath()+"/" + Constants.AppFolderName+File.separator+ Constants.createFolder);
                if(!createFolder.exists())
                {
                    createFolder.mkdirs();
                }
					imagePath = new File(Environment.getExternalStorageDirectory()
							.getPath() +"/"+ Constants.AppFolderName+"/"+Constants.tempFolderName+"/"+Constants.profileImge+Constants.imageFormat);
					if(imagePath.exists())
					{
						Util.deleteFolderFromSDCard(imagePath, false);
					}

//					ContentValues values = new ContentValues();
//					values.put(MediaStore.Images.Media.TITLE, "OnlyOneImage");
					
//					Uri mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//					
//					intent.setData(mCapturedImageURI);
//					
					Constants.imageUri=Uri. fromFile(imagePath);
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore. EXTRA_OUTPUT,Constants.imageUri);

					pickImage = Constants.PICK_IMAGE_FROM_CAMERA;
					getActivity().startActivityForResult(intent, pickImage);




			} else {

					pickImage = Constants.PICK_IMAGE_FROM_GALLERY;
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_PICK);
					getActivity().startActivityForResult(intent, pickImage);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
