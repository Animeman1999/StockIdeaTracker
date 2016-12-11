package com.example.jmartin5229.stockideatracker;

/**
 * Created by Jeff on 12/10/2016.
 */

import android.net.Uri;
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

    private byte[] getUrlBytes(String urlSpec) throws IOException
    {
        //Create a new URL object from the url string that was passed in
        URL url = new URL(urlSpec);

        //Create a new HTTP connection to the specified url.
        HttpURLConnection connection =
                (HttpURLConnection)url.openConnection();

        try {
            //Create a output stream to hold that data that is read from
            //the url source
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //Create a input stream from the http connection
            InputStream in = connection.getInputStream();

            //Check to see that the response code from the http request
            //is 200, which is the same as http_ok.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
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
                //write the bytes out to the output stream
                out.write(buffer, 0, bytesRead);
            }

            //Once everything has been read and written, close the
            //output stream
            out.close();
            in.close();

            //Convert the output stream to a byte array
            return out.toByteArray();
        } finally {
            //make sure the connection to the web is closed.
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public boolean fetchStockPriceName(Stock stock) {
        //This is the method that will take the original URL and allow
        //us to add any parameters that might be required to it.
        //For the URL's on my server there are no additional parameters
        //needed. However many API's require extra parameters and this
        //is where they add them.

        try {

            String url = Uri.parse("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20%3D'" +
                    stock.getTicker() + "'&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json")
                    .buildUpon()
                    //Add extra parameters here with the method
                    //.appendQueryParameter("param", "Value")
                    .build().toString();

            //This calls the above methods to use the URL to get the JSON from
            //the web service. After this call we will actually have the JSON
            //that we need to parse
            String jsonString = getUrlString(url);

            //This will take the jsonString that we got back and put it into
            //a JSONObject.
            JSONObject jsonObject = new JSONObject(jsonString);

            //Parse the stock out from the object.
            parseStock(stock, jsonObject);

            Log.i(TAG, "Fetched contents of URL: " + jsonString);
        } catch (JSONException jse) {
            Log.e(TAG, "Failed to parse JSON", jse);
            return false;
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to load", ioe);
            return false;
        }

        return true;
    }

    private void parseStock(Stock stock, JSONObject jsonObject)
            throws IOException, JSONException {

        stock.setPurchasePrice(jsonObject.getDouble("LastTradePriceOnly"));
        stock.setName(jsonObject.getString("Name"));

    }
}
