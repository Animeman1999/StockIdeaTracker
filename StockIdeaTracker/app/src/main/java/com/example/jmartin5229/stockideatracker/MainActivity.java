package com.example.jmartin5229.stockideatracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private int mSubtitleVisible = 1;
    private static String TAG = "MainError";
    private Context mContext;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single);


        // >Get the fragment manager from the activity.
        FragmentManager fm = getSupportFragmentManager();
        // >Get the current fragment from the frame layout. (null when first starting app)
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        // >Create a fragment and add it to the fragment manager.(only happens when first starting app)
        if (fragment == null) {
            fragment = new StockIdeaListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        switch (id) {
            case R.id.list_of_stock_ideas:
                mSubtitleVisible = 1;
                updateSubtitle();
                break;
            case R.id.add_new_stock_idea:
                mSubtitleVisible = 2;
                updateSubtitle();
                Log.d(TAG, "++++++++++++++++++++++++++++++++++++++ About to Added new stock++++++++++++++++++++++");
                Stock stock = new Stock();
                Log.d(TAG, "++++++++++++++++++++++++++++++++++++++ Added new stock++++++++++++++++++++++");
                StockApi.get(this).AddStock(stock);
                Log.d(TAG, "++++++++++++++++++++++++++++++++++++++ Added new stock to database++++++++++++++++++++++");
                Intent intent = StockActivity.newIntent(this,stock.getUUID());
                Log.d(TAG, "++++++++++++++++++++++++++++++++++++++ Added new new Intent and about to add it++++++++++++++++++++++");
                startActivity(intent);
                break;
            case R.id.portfolio_summary:
                mSubtitleVisible = 3;
                updateSubtitle();
                break;
        }


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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.jmartin5229.stockideatracker/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.jmartin5229.stockideatracker/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
