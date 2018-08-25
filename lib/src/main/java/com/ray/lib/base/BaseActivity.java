package com.ray.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ray.lib.util.RxDisposableManager;

import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 下午5:53
 * Description :
 */
public abstract class BaseActivity extends AppCompatActivity {

    private RxDisposableManager mRxDisposableManager;
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDisposableManager = new RxDisposableManager();
        mActivity = this;
    }

    protected void addDisposable(Disposable disposable) {
        mRxDisposableManager.addDisposable(disposable);
    }

    protected void setupToolBar(int id) {
        Toolbar toolbar = findViewById(id);
        setSupportActionBar(toolbar);
        setAsHomeUp();
    }

    protected void setupToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        setAsHomeUp();
    }

    protected void setAsHomeUp() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxDisposableManager.dispose();
    }
}
