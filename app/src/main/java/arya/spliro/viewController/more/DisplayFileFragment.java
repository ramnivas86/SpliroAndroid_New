package arya.spliro.viewController.more;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.Observable;

import arya.spliro.R;


public class DisplayFileFragment extends AbstractFragment implements View.OnClickListener
{
	WebView termWebView;
	ImageView imgBackAppH;
	String url ;
    View view;
    DisplayFileFragment(String url)
    {
        this.url=url;
    }
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.display_file,container,false);
        intit();
        return view;
    }

    private void intit()
    {
        imgBackAppH=(ImageView)view.findViewById(R.id.imgBackAppH);
        termWebView=(WebView)view.findViewById(R.id.term_webview);

        termWebView.loadUrl(url);
        imgBackAppH.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(DisplayFileFragment.this).commit();
            }
        });
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o)
    {
}

    @Override
    public void onClick(View view) {

    }

}

