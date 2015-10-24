package com.shine.niceapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.shine.niceapp.R;
import com.shine.niceapp.ui.fragment.CardViewPagerFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardViewPagerFragment fragment = CardViewPagerFragment.getInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();

    }
}
