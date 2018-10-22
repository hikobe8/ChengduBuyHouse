package com.ray.chengdubuyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ray.chengdubuyhouse.BuildConfig;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.fragment.PreSellHouseFragment;
import com.ray.lib.base.BaseActivity;
import com.ray.lib.network.DownloadPackage;
import com.ray.lib.network.RxDownloader;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CONTENT_FRAGMENT_TAG = "PreSell";
    private static final String SHARE_URL = "http://android.myapp.com/myapp/detail.htm?apkName="+ BuildConfig.APPLICATION_ID;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment preSellHouseFragment = getSupportFragmentManager().findFragmentByTag(CONTENT_FRAGMENT_TAG);
        if (preSellHouseFragment == null) {
            preSellHouseFragment = new PreSellHouseFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, preSellHouseFragment, CONTENT_FRAGMENT_TAG)
                .commit();
        //fixme delete
        RxDownloader.getInstance().download("https://www.cdfangxie.com/Public/uploadfile/file/20181020/20181020144327_72771.rar")
                .subscribe(new Observer<DownloadPackage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(DownloadPackage downloadPackage) {
                        Log.e("download", Math.floor(downloadPackage.finishedRate*100) + "%");
                        if (downloadPackage.downloadFinished) {
                            Log.e("download", "download finished! " + downloadPackage.filePath);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_query) {
            DistrictQueryActivity.launch(this);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("text/plain");//设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_SUBJECT, "成都买房宝");//添加分享内容标题
            share_intent.putExtra(Intent.EXTRA_TEXT, "成都买房宝-房源信息查询神器,点击下载" + SHARE_URL);//添加分享内容
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "摇号必中");
            startActivity(share_intent);
        } else if (id == R.id.nav_send) {
            Intent data=new Intent(Intent.ACTION_SENDTO);
            if (data.resolveActivity(getPackageManager()) != null) {
                data.setData(Uri.parse("mailto:hikobe8@163.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "成都买房宝");
                data.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(data);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
