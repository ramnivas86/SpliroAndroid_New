package arya.spliro.fragments;

        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.arya.lib.model.BasicModel;
        import com.arya.lib.view.AbstractFragment;

        import java.util.Observable;

        import arya.spliro.R;
        import arya.spliro.utility.AppHeaderView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class NotificationsFragment extends AbstractFragment implements View.OnClickListener {
    private AppHeaderView appHeaderView;
    View v;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.notification_main,container,false);
//        init();
        return v;
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    private void init()
    {
        appHeaderView=(AppHeaderView)v.findViewById(R.id.appHeaderView);
        appHeaderView.setHeaderContent(getResources().getDrawable(R.drawable.spl_ic_back_w_l), getResources().getString(R.string.title_create_post_low), getResources().getString(R.string.preview), getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
