package com.example.jmartin5229.stockideatracker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private int mSubtitleVisible = 0;
    private static String TAG = "MainError";
    private Context mContext;
    private LocationListener locationListener;
    private LocationManager locationManager;
    public String  mGPSCoordinates;
    private static final int LOCATIONS_PERMISSION_REQUEST_CODE = 10;
    private static final int INTERNET_PERMISSION_REQUEST_CODE = 20;
    private String mStockSymbol;
    private boolean response = false;

        /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                //Log.d("Test", "99999999999999999999999999 getting = " + mGPSCoordinates );
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

        //Find out if the API build being used is API 23 or larger.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Check to see if app has NOT been given permission from the user to to use the services.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){
                // As permission has NOT been given, ask the user for permission, this permission granted request code will be using
                //LOCATIONS_PERMISSION_REQUEST_CODE that was set to a constant integer value. Can be any integer value I chose 10.
                //If multiple permissions are asked, each one needs a different integer value.
                requestPermissions( new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, LOCATIONS_PERMISSION_REQUEST_CODE);


              //  return;
            } else {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                Log.e("Test", "99999999999999999999999999 getting gps ");
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Check to see if app has NOT been given permission from the user to to use the services.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ){
                // As permission has NOT been given, ask the user for permission, this permission granted request code will be using
                //LOCATIONS_PERMISSION_REQUEST_CODE that was set to a constant integer value. Can be any integer value I chose 10.
                //If multiple permissions are asked, each one needs a different integer value.
                requestPermissions( new String[]{ Manifest.permission.INTERNET
                }, INTERNET_PERMISSION_REQUEST_CODE);


                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATIONS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                            ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat
                            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    }
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
        Log.d("TEST", "YYYYYYYYYYYYYYYYYYYYYYYYYY id = " + id + " R.id.sort_by_ticker = " + R.id.sort_by_ticker );

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.sort_by_ticker:
                mSubtitleVisible = 1;
                updateSubtitle();
                break;
            case R.id.sort_by_name:
                mSubtitleVisible = 2;
                updateSubtitle();
                break;
            case R.id.sort_by_date:
                mSubtitleVisible = 3;
                updateSubtitle();
                break;
            case R.id.sort_by_gps:
                mSubtitleVisible = 4;
                updateSubtitle();
                break;
            case R.id.add_new_stock_idea:
                Stock stock = new Stock();
                if (stock.getCoordinates() == null) {
                    stock.setCoordinates(mGPSCoordinates);
                }
                StockApi.get(this).AddStock(stock);
                Intent intent = StockActivity.newIntent(this,stock.getUUID());
                startActivity(intent);
                break;
            case R.id.portfolio_summary:
                showMessage("Summary Report", StockApi.get(this).getReport(this));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {

        String subtitle = "";
        switch (mSubtitleVisible) {
            case 1:
                subtitle = getString(R.string.sort_by_ticker);
                break;
            case 2:
                subtitle = getString(R.string.sort_by_name);

                break;
            case 3:
                subtitle = getString(R.string.sort_by_date);
                break;
            case 4:
                subtitle = getString(R.string.sort_by_gps);
                break;
            default:
                subtitle = "";
                break;
        }
        AppCompatActivity activity = (AppCompatActivity) this;
        activity.getSupportActionBar().setSubtitle(subtitle);


        StockIdeaListFragment frag = ((StockIdeaListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container));

        Bundle arg = new Bundle();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frag);
        ft.commit();

        // >Get the fragment manager from the activity.
        FragmentManager fm = getSupportFragmentManager();
        arg.putString(StockIdeaListFragment.STOCK_IDEA_LIST_FRAGMENT_KEY,subtitle);


        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        fragment = new StockIdeaListFragment();
        fragment.setArguments(arg);

        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();


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

    public void showMessage(String title, String message){  //Generic method to create a dialog and display it
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

}
