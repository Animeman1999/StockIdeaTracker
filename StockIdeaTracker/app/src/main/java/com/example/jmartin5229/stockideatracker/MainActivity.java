package com.example.jmartin5229.stockideatracker;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private int mSubtitleVisible = 1;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem subtitleItem =
//                menu.findItem(R.id.menu_item_show_subtitle);
//        switch (mSubtitleVisible)
//        {
//            case 1:
//            subtitleItem.setTitle(R.string.list_of_stock_ideas);
//                return true;
//            case 2:
//            subtitleItem.setTitle(R.string.add_new_stock_idea);
//                return true;
//            case 3:
//                subtitleItem.setTitle(R.string.portfolio_summary);
//                return true;
//        }
        updateSubtitle();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case  R.id.list_of_stock_ideas:
                mSubtitleVisible = 1;
                break;
            case R.id.add_new_stock_idea:
                mSubtitleVisible = 2;
                break;
            case R.id.portfolio_summary:
                mSubtitleVisible = 3;
                break;
        }
        updateSubtitle();

        return super.onOptionsItemSelected(item);
    }
    private void updateSubtitle() {

        String subtitle = getString(R.string.list_of_stock_ideas);
        switch (mSubtitleVisible) {
            case 1:
                subtitle = getString(R.string.list_of_stock_ideas);
                break;
            case 2:
                subtitle = getString(R.string.add_new_stock_idea);
                break;
            case 3:
                subtitle = getString(R.string.portfolio_summary);
                break;
        }
        AppCompatActivity activity = (AppCompatActivity) this;
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


}
