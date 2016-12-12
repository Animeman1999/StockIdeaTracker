package com.example.jmartin5229.stockideatracker;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.Date;
import java.io.File;
import java.util.UUID;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.location.Location;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Jeff on 12/2/2016.
 */

public class StockFragment extends Fragment {
    private static final String ARG_STOCK_ID = "stock_id";
    private static final String DIALOG_DATE = "DialogDate";
    private StockApi mStockApi;

    final Intent pickStock = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private String mGPSCoordinates;
    StockFetcher stockFetcher = new StockFetcher();

    private Stock mStock;

    private TextView mIdeaTitle;
    private TextView mIdeaTitleLable;
    private EditText mTicker;
    private TextView mDateAddedLabel;
    private TextView mDateAdded;
    private TextView mDescriptionLable;
    private EditText mDescription;
    private ImageView mPhotoView;
    private TextView mGPSLabel;
    private TextView mGPS;
    private TextView mPurchasePriceLabel;
    private TextView mPurchasePrice;
    private TextView mNumberStockLabel;
    private TextView mNumberStock;
    private TextView mTotalStockPriceLabel;
    private TextView mTotalStockPrice;
    private TextView mCurrentStockPriceLabel;
    private TextView mCurrentStockPrice;
    private TextView mCurrentStockValueLabel;
    private TextView mCurrentStockValue;
    private Button mPurchaseButton;
    private String mPurchaseText = "";
    private TextView mPurchaseDateLabel;
    private TextView mPurchaseDate;
    private Button mValidateSymbolButton;
    private String mUserInput;

    private GoogleApiClient googleApiClient;


    private Context mContext;

    private long mCreationDateLong;
    private File mPhotoFile;

    public static StockFragment newInstance(UUID stockID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STOCK_ID, stockID);

        StockFragment stockFragment = new StockFragment();
        stockFragment.setArguments(args);

        return stockFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID stockID = (UUID) getArguments().getSerializable(ARG_STOCK_ID);
        mStock = StockApi.get(getActivity()).GetStock(stockID);
        //Log.d("Test", "666666666666666666666666666 StockFragment.java OnCreate - mStock.getPurchaseDate() = " + mStock.getPurchaseDate() + "66666666666666666666666666666");
        mPhotoFile = StockApi.get(getActivity()).getPhotoFile(mStock);
        mContext = getContext();


       }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock_idea, container, false);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;



        if(packageManager.resolveActivity(pickStock,PackageManager.MATCH_DEFAULT_ONLY)== null){

        }
        mIdeaTitleLable = (TextView)v.findViewById(R.id.stock_idea_title_label);
        mIdeaTitle = (TextView) v.findViewById(R.id.stock_idea_title);
        mIdeaTitle.setText(mStock.getName());

        final String mSmbol = mStock.getTicker();
        mTicker = (EditText) v.findViewById(R.id.stock_idea_ticker);
        mTicker.setText(mStock.getTicker());
        mTicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUserInput = s.toString();

            }
        });

        mValidateSymbolButton = (Button)v.findViewById(R.id.validate_symbol_button);
        mValidateSymbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInput != ""){
                    Stock stock = stockFetcher.fetchStockPriceName(mUserInput, getContext());
                    if ( stock == null){
                        Toast.makeText(getContext(), mUserInput + "Not a valid stock symbol", Toast.LENGTH_LONG);
                        mValidateSymbolButton.setText(mUserInput + " is not a valid stock symbol. \n Validate stock Symbol");
                        mTicker.setText("");
                    }else {
                        mStock.setName(stock.getName());
                        mStock.setCurrentPrice(stock.getCurrentPrice());
                        mPurchasePrice.setText(String.valueOf(mStock.getCurrentPrice()));
                        mIdeaTitle.setText(mStock.getName());
                        mValidateSymbolButton.setVisibility(View.GONE);
                        mPurchaseButton.setVisibility(View.VISIBLE);
                        mIdeaTitle.setVisibility(View.VISIBLE);
                        mIdeaTitleLable.setVisibility(View.VISIBLE);
                        mIdeaTitleLable.setVisibility(View.VISIBLE);
                        mIdeaTitle.setVisibility(View.VISIBLE);
                        mPhotoView.setVisibility(View.VISIBLE);
                        mGPSLabel.setVisibility(View.VISIBLE);
                        mGPS.setVisibility(View.VISIBLE);
                        mDescriptionLable.setVisibility(View.VISIBLE);
                        mDescription.setVisibility(View.VISIBLE);
                        mCurrentStockPriceLabel.setVisibility(View.VISIBLE);
                        mCurrentStockPrice.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        String creationDate =  mStock.getCreationDate();
        //Log.e("Test", "666666666666666666666666666 OnCreateView - creationDate =" + creationDate + "6666666666666666666666666");
        //Log.e("Test", "666666666666666666666666666 OnCreateView - creationDate.toString() =" + creationDate + "6666666666666666666666666");
        mDateAddedLabel = (TextView)v.findViewById(R.id.stock_idea_creation_date_label);
        mDateAdded = (TextView)v.findViewById(R.id.stock_idea_creation_date);
        mDateAdded.setText( creationDate);
        mCurrentStockValue = (TextView) v.findViewById(R.id.stock_idea_total_current_value);

        mDescriptionLable = (TextView)v.findViewById(R.id.stock_idea_description_label);
        mDescription =(EditText)v.findViewById(R.id.stock_idea_description);
        mDescription.setText(mStock.getDescription());
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStock.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Needs to be initialized so not null when mPhotView is used in updatePhotoView();
        mPhotoView = (ImageView)v.findViewById(R.id.stock_idea_thumbnail);

        updatePhotoView();
        if (canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = new MainActivity();
                startActivityForResult(captureImage, REQUEST_PHOTO);




               // mStock.setCoordinates();
               // mGPS.setText();
                //******************************get a picture from the camera
                //                               save picture into drawable folder
                //                               save picture name to database
                //                               setImageResource to view of new picture
                //                               get GPS
                //                               save GPS to database
                //                               update mGPS.setText
                //mPhotoView = (ImageView)v.findViewById(R.id.stock_idea_thumbnail);

                updatePhotoView();
            }

        });

        mGPSLabel = (TextView)v.findViewById(R.id.stock_idea_coordinates_label);
        mGPS = (TextView)v.findViewById(R.id.stock_idea_coordinates);
        mGPS.setText(mStock.getCoordinates());

        mPurchasePriceLabel = (TextView) v.findViewById(R.id.stock_idea_purchase_price_label);
        mPurchasePrice = (TextView)v.findViewById(R.id.stock_idea_purchase_price);
        final Double purchasePrice = mStock.getPurchasePrice();
        mPurchasePrice.setText(String.valueOf( purchasePrice));

        mNumberStock = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased);
        int numberStock = mStock.getNumberStock();
        mNumberStock.setText(String.valueOf(numberStock));

        mTotalStockPrice = (TextView) v.findViewById(R.id.stock_idea_total_purchase_price);
        mCurrentStockPrice = (TextView) v.findViewById(R.id.stock_idea_current_price);

        mNumberStockLabel = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased_label);
        mTotalStockPriceLabel = (TextView)v.findViewById(R.id.stock_idea_total_purchase_price_label);
        mCurrentStockPriceLabel = (TextView)v.findViewById(R.id.stock_idea_current_price_label);
        mCurrentStockValueLabel = (TextView)v.findViewById(R.id.stock_idea_total_current_value_label);
        mPurchaseButton = (Button)v.findViewById(R.id.stock_purchase_button);
        mPurchaseDateLabel =(TextView)v.findViewById(R.id.stock_purchase_date_label);
        mPurchaseDate = (TextView)v.findViewById(R.id.stock_purchase_date);

        mPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter the number of Stocks to buy.");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_NUMBER );
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPurchaseText = input.getText().toString();
                        if(mPurchaseText != "") {
                            try {
                                int numberStock = Integer.valueOf(mPurchaseText);
                                mNumberStock.setText(mPurchaseText);
                                mStock.setNumberStock(numberStock);
                                Date date = new Date();
                                //Declare a date format to use on the date
                                String dateFormat = "yyyy MMM dd hh:mm";
                                //Get a string version of the data formated by the date format
                                String purchaseDate = DateFormat.format(dateFormat, date)
                                        .toString();
                                mStock.setPurchaseDate(purchaseDate);
                                mTotalStockPrice.setText(String.valueOf(numberStock * mStock.getCurrentPrice()));
                                mCurrentStockValue.setText(String.valueOf(numberStock * mStock.getCurrentPrice()));
                                mCurrentStockPrice.setText(String.valueOf(mStock.getCurrentPrice()));
                                mNumberStockLabel.setVisibility(View.VISIBLE);
                                mNumberStock.setVisibility(View.VISIBLE);
                                mTotalStockPriceLabel.setVisibility(View.VISIBLE);
                                mTotalStockPrice.setVisibility(View.VISIBLE);
                                mPurchaseDateLabel.setVisibility(View.VISIBLE);
                                mPurchaseDate.setVisibility(View.VISIBLE);
                                mCurrentStockPriceLabel.setVisibility(View.VISIBLE);
                                mCurrentStockPrice.setVisibility(View.VISIBLE);
                                mCurrentStockValueLabel.setVisibility(View.VISIBLE);
                                mCurrentStockValue.setVisibility(View.VISIBLE);
                                mPurchaseButton.setVisibility(View.GONE);
                            } catch (Exception e)
                            {
                                Toast.makeText(getActivity(), R.string.bad_input, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });


        if (numberStock <= 0)
        {
            mNumberStockLabel.setVisibility(View.GONE);
            mNumberStock.setVisibility(View.GONE);
            mTotalStockPriceLabel.setVisibility(View.GONE);
            mTotalStockPrice.setVisibility(View.GONE);
            mCurrentStockPriceLabel.setVisibility(View.GONE);
            mPurchaseDateLabel.setVisibility(View.GONE);
            mPurchaseDate.setVisibility(View.GONE);
            mCurrentStockPrice.setVisibility(View.GONE);
            mCurrentStockValueLabel.setVisibility(View.GONE);
            mCurrentStockValue.setVisibility(View.GONE);
            mPurchaseButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mPurchaseButton.setVisibility(View.GONE);

            mTotalStockPrice.setText(String.valueOf(purchasePrice * numberStock));

            Double currentPrice = 1.11; //88888888888888888888888888Needs to be a method here to get the price from yahoo
            mCurrentStockPrice.setText(String.valueOf(currentPrice));

            mCurrentStockValue.setText(String.valueOf(currentPrice * numberStock));
        }

        if(mStock.getTicker() == null){
            mPurchaseButton.setVisibility(View.GONE);
            mIdeaTitle.setVisibility(View.GONE);
            mIdeaTitleLable.setVisibility(View.GONE);
            mIdeaTitleLable.setVisibility(View.GONE);
            mIdeaTitle.setVisibility(View.GONE);
            mPhotoView.setVisibility(View.GONE);
            mGPSLabel.setVisibility(View.GONE);
            mGPS.setVisibility(View.GONE);
            mPurchasePriceLabel.setVisibility(View.GONE);
            mPurchasePrice.setVisibility(View.GONE);
            mDescriptionLable.setVisibility(View.GONE);
            mDescription.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StockApi.get(getActivity()).UpdateStock(mStock);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageResource(R.drawable.take_a_picture);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
//    public void GetStockSymbol()
//    {
//
//        Log.d("Test", "--------------------------Enter GetStockSymbol");
//        mStockSymbol = "";
//        final Context context = this;
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter the stock symbol.");
//
//        final EditText input = new EditText(this);
//        builder.setView(input);
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mStockSymbol = input.getText().toString();
//                response = true;
//                if(mStockSymbol != "") {
//                    String UUIDString = stockFetcher.fetchStockPriceName(mStockSymbol, context);
//                    if ( UUIDString == ""){
//                        Toast.makeText(context, mStockSymbol + "Not a valid stock symbol", Toast.LENGTH_LONG);
//                        input.setText("");
//                    }else {
//                        input.setText(UUIDString);
//                    };
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                response = true;
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//
//
//        Log.d("Test", "--------------------------Exiting GetStockSymbol string being returned = " + mStockSymbol);
//
//
//    }
}
