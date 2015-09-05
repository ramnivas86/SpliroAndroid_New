package arya.spliro.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import arya.spliro.interfaces.ClickOnPreImeInterFace;

public class CustomEditText extends EditText {
	private ClickOnPreImeInterFace OnPreImeInterFace;

	/*
	 * Must use this constructor in order for the layout files to instantiate
	 * the class properly
	 */
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		OnPreImeInterFace.clickOnBackPressed(true);
		System.out.println("onKeyPreIme " + event);
		return false;
	}
	public void setOnPreImeListener(ClickOnPreImeInterFace OnPreImeInterFace) {
		this.OnPreImeInterFace = OnPreImeInterFace;
	}
}
