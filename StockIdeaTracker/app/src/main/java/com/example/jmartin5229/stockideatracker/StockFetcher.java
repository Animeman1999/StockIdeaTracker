package com.example.jmartin5229.stockideatracker;

/**
 * Created by Jeff on 12/10/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockFetcher {
    private static final String TAG = "StockFetcher";


    private byte[] getUrlBytes(String urlSpec, Context context) throws IOException {

        Log.d("Test", "+++++++++++++++++++++++++++++++++++  Enter getUrlBytes passed in urlSpec = " + urlSpec);
        //Create a new URL object from the url string that was passed in
        URL url = new URL(urlSpec);

        //Create a new HTTP connection to the specified url.
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        int response = connection.getResponseCode();
        Log.d("TEST", "+++++++++++++++++++++++++++++++++++++++++++++++++++ connection.getResponseCode = "+ response);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
             Log.d("Test", "+++++++++++++++++++++++++++++++++++  Access granted for inputStream");
            try {

                //Create a output stream to hold that data that is read from
                //the url source
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Log.d("Test", "++++++++++++++++++++++++++++++++++++++++ ByteArrayOutputStream created");
                //Create a input stream from the http connection
                InputStream in = connection.getInputStream();

                Log.d("Test", "+++++++++++++++++++++++++++++++++++++++++++++InputStream connected");
                //Check to see that the response code from the http request
                //is 200, which is the same as http_ok.
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d("Test", "Did not get OK response code");
                    throw new IOException(connection.getResponseMessage() +
                            ": with" + urlSpec);

                }

                //Create an int to hold how many bytes we are going to read in.
                int bytesRead = 0;

                //Create a byte array to act as a buffer that will read in
                //up to 1024 bytes at a time.
                byte[] buffer = new byte[1024];

                //While we can read bytes from the input stream
                while ((bytesRead = in.read(buffer)) > 0) {
                    Log.d("Test", "+++++++++++++++++++++++++++++ reading bytes from buffer");
                    //write the bytes out to the output stream
                    out.write(buffer, 0, bytesRead);
                }

                //Once everything has been read and written, close the
                //output stream
                out.close();
                in.close();
                Log.d("Test", "++++++++++++++++++++++++++++++++++ closing in and out streams");

                //Convert the output stream to a byte array
                return out.toByteArray();
            } finally {
                //make sure the connection to the web is closed.
                connection.disconnect();
            }
        }else{
            Log.d("Test", "+++++++++++++++++++++++++++++++++++  Access NOT granted for inputStream");
        }
        return null ;
    }
    private String getUrlString(String urlSpec, Context context) throws IOException {
        Log.d("Test", "+++++++++++++++++++++++++++++++++++  Enter getUrlString urlSpec = " + urlSpec);

        return new String(getUrlBytes(urlSpec, context));
    }


    public String fetchStockPriceName(String stockSymbol, Context context) {
        //This is the method that will take the original URL and allow
        //us to add any parameters that might be required to it.
        //For the URL's on my server there are no additional parameters
        //needed. However many API's require extra parameters and this
        //is where they add them.
        Log.d("Test", "+++++++++++++++++++++++++++++++++++  Enter fetchStockPriceName stockSymbol passed in is + " + stockSymbol);
        String UUIDString;
        try {

            String url = Uri.parse("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20%3D'" +
                    stockSymbol + "'&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json")
                    .buildUpon()
                    //Add extra parameters here with the method
                    //.appendQueryParameter("param", "Value")
                    .build().toString();

            //Log.d("Test", "+++++++++++++++++++++++++++++++ String url = " + url);

            //This calls the above methods to use the URL to get the JSON from
            //the web service. After this call we will actually have the JSON
            //that we need to parse
            String jsonString = getUrlString(url, context);
            //Log.i(TAG, "+++++++++++++++++++++++++++++++Fetched contents of URL: " + jsonString);
            //This will take the jsonString that we got back and put it into
            //a JSONObject.
            JSONObject jsonObject = new JSONObject(jsonString);

            //Parse the stock out from the object.
            UUIDString = parseStock(jsonObject);

            Log.i(TAG, "++++++++++++++++++++++++++++++ParsedStock to UUIDString = " + UUIDString);
        } catch (JSONException jse) {
            Log.e(TAG, "+++++++++++++++++++++++++++++++++Failed to parse JSON", jse);
            return "";
        } catch (IOException ioe) {
            Log.e(TAG, "++++++++++++++++++++++++++++++++++++Failed to load", ioe);
            return "";
        }

        return UUIDString;
    }

    private String parseStock(JSONObject jsonObject)
            throws IOException, JSONException {
        //Log.d("Test", "+++++++++++++++++++++++++++++++++++  Enter parseStock jsonObjec = " + jsonObject);
        JSONObject query = jsonObject.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");

        JSONObject quote = results.getJSONObject("quote");
        //Log.d("Test", "+++++++++++++++++++++++++++++++++++++ get jsonObject quote = " + quote);
        Stock stock = new Stock();
        String name = (quote.getString("Name"));
        Log.d("Test", "+++++++++++++++++++++++++++++++++++++++++++++ Name = " + name );
        stock.setName(name);
        Double price = quote.getDouble("LastTradePriceOnly");
        Log.d("Test", "+++++++++++++++++++++++++++++++++++++++++++++ quote = " + price );
        stock.setPurchasePrice(price);

        Log.d("Test", "+++++++++++++++++++++++++++++++++++++++++++++ stock UUID = " + stock.getUUID() );
        return stock.getUUID().toString();
    }
}
