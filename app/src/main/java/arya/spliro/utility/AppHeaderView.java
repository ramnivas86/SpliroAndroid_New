package arya.spliro.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import arya.spliro.R;
import arya.spliro.viewController.SpliroApp;

public class AppHeaderView extends RelativeLayout {

	public TextView txtTitleAppH;
    public ImageView imgAppTitle;
	public ImageView imgBackAppH;
	public ImageView imgforward;
    public RelativeLayout relforward;
    public TextView txtForward;

	public RelativeLayout relCommonHeadAppH;
	public Context context;

	public AppHeaderView(Context context) {
		super(context);
		this.context=context;
		init(context);
	}
	public AppHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init(context);
	}

	public AppHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init(context);
	}

	private void init(final Context context) 
	{
		if (!isInEditMode())
		{
			super.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			LayoutInflater.from(context).inflate(R.layout.common_app_header, this, true);
			this.relCommonHeadAppH = (RelativeLayout) findViewById(R.id.relCommonHeadAppH);
			this.txtTitleAppH = (TextView) findViewById(R.id.txtTitleAppH);
            this.imgAppTitle=(ImageView)findViewById(R.id.imgAppTitle);
			this.imgBackAppH = (ImageView) findViewById(R.id.imgBackAppH);
            this.relforward=(RelativeLayout)findViewById(R.id.relforward);
            this.imgforward = (ImageView) findViewById(R.id.imgforward);
            this.txtForward = (TextView) findViewById(R.id.txtForward);
            imgBackAppH.setOnClickListener((OnClickListener) context);
            imgforward.setOnClickListener((OnClickListener) context);
		}
	}
	
	public void setHeaderContent(Object icHLeft, Object title,Object icHRight1, int headerBackgrounColor) {
		this.relCommonHeadAppH.setBackgroundColor(headerBackgrounColor);
		if (title != null) 
		{
            if(title instanceof  String) {
                imgAppTitle.setVisibility(View.GONE);
                txtTitleAppH.setVisibility(View.VISIBLE);
                txtTitleAppH.setText((String) title);
            }
            else {
                imgAppTitle.setVisibility(View.VISIBLE);
                txtTitleAppH.setVisibility(View.GONE);
                imgAppTitle.setImageDrawable((Drawable) title);

            }
		}
		if (icHLeft != null)
		{

			if (icHLeft instanceof Drawable)
			{
                imgBackAppH.setImageDrawable((Drawable) icHLeft);
			}
		}
		if (icHRight1 != null) {
			if (icHRight1 instanceof String) 
			{
                imgforward.setVisibility(View.INVISIBLE);
                txtForward.setVisibility(View.VISIBLE);
                txtForward.setText((String)icHRight1);
			} 
			else if (icHRight1 instanceof Drawable)
			{
                txtForward.setVisibility(View.INVISIBLE);
                imgforward.setVisibility(View.VISIBLE);
                imgforward.setImageDrawable((Drawable) icHRight1);
			}
		}
        txtTitleAppH.setTypeface(SpliroApp.getAppHeaderTypeface());
	}

}
