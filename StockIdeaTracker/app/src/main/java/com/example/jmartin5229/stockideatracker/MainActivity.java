package com.example.jmartin5229.stockideatracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private int mSubtitleVisible = 1;
    private static String TAG = "MainError";
    private Context mContext;
    private LocationListener locationListener;
    private LocationManager locationManager;
    public String  mGPSCoordinates;
    private static int PERMISSION_ACCESS_COARSE_LOCATION = 10;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient googleApiClient;


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
        googleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mGPSCoordinates = location.getLongitude() + ", " + location.getLatitude();
                Log.e("Test", "99999999999999999999999999 getting = " + mGPSCoordinates );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET

                }, 10);

                return;
            } else {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                Log.e("Test", "99999999999999999999999999 getting gps ");
            }
        }
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
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
                Stock stock = new Stock();
                if (stock.getCoordinates() == null) {
                    stock.setCoordinates(mGPSCoordinates);
                }
                Log.e("Test", "00000000000000000000000000000000000 mGPSCoordinates = " + mGPSCoordinates + " stock.getCoordinates =" +
                        stock.getCoordinates());
                StockApi.get(this).AddStock(stock);

                Intent intent = StockActivity.newIntent(this,stock.getUUID());

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
        if (googleApiClient != null){
            googleApiClient.connect();
        }

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
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    @Override
    public void onStop() {
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
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
        googleApiClient.disconnect();
        super.onStop();
    }
}
