package com.bignerdranch.android.photogallery;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lfs-ios on 2017/4/10.
 */

public abstract class SignleFragmentActivity extends AppCompatActivity {
    //用来创建fragment对象
    protected  abstract Fragment createFragment();

    //返回布局，@LayoutRes任何时候都应该返回有效的布局资源id
    @LayoutRes
    protected int getLayoutResId() {

        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());

        //获取Fragment管理者对象
        FragmentManager fm = getSupportFragmentManager();
        //从FragmentManager获取fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //add(R.id.fragment_container, fragment),参数1为fragment视图应该出现在活动的什么位置，用作
        //FragmentManager队列中fragment的唯一标识符
        if (fragment == null) {

            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


    }
}
