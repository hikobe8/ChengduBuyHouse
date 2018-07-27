package com.ray.chengdubuyhouse;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 下午5:53
 * Description :
 */
public class BaseActivity extends AppCompatActivity {

    protected void setupToolBar(int id){
        Toolbar toolbar = findViewById(id);
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
}
