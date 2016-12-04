package com.example.jmartin5229.stockideatracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ccunn on 03-Dec-16.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    // >The method used by all inheriting activities to build their single fragment.
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single);

        // >Get the fragment manager from the activity.
        FragmentManager fm = getSupportFragmentManager();
        // >Get the current fragment from the frame layout. (null when first starting app)
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        // >Create a fragment and add it to the fragment manager.(only happens when first starting app)
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
