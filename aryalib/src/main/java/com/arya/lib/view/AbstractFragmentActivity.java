package com.arya.lib.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.Util;

import java.io.File;
import java.util.Observer;


public abstract class AbstractFragmentActivity extends FragmentActivity implements Observer{
    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getModel();
        if (model != null)
            model.addObserver(this);
        Env.currentActivity=this;
        onCreatePost(savedInstanceState);
    }

    protected abstract void onCreatePost(Bundle savedInstanceState);
    protected abstract BasicModel getModel();

    @Override
    protected void onResume() {
        super.onResume();
        Env.currentActivity=this;
    }
}
