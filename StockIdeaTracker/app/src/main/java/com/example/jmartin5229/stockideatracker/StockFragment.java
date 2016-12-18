package com.example.jmartin5229.stockideatracker;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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

import java.text.NumberFormat;
import java.util.Date;
import java.io.File;
import java.util.UUID;

import com.google.android.gms.common.api.GoogleApiClient;



/**
 * Created by Jeff on 12/2/2016.
 */

public class StockFragment extends Fragment {
    private static final String ARG_STOCK_ID = "stock_id";
    private static final String DIALOG_DATE = "DialogDate";

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
    private TextView mTickerDisplay;
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
    private TextView mProfitLoss;
    private Button mDeleteButton;
    private Button mDeleteConfirmButton;
    private Button mDeleteCancelButton;
    private Boolean mDeleteThisStock = false;
    private Button mSellStockButton;
    private Button mSellConfimrButton;
    private Button mSellCancelButton;
    private TextView mStockSoldDateLabel;
    private TextView mStockSoldDate;


    private GoogleApiClient googleApiClient;

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

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

        mProfitLoss = (TextView)v.findViewById(R.id.profit_loss);
        mStockSoldDate = (TextView)v.findViewById(R.id.stock_sold_date);
        mStockSoldDateLabel = (TextView)v.findViewById(R.id.stock_sold_date_label);

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
                mUserInput = s.toString().trim();
            }
        });
        mDeleteConfirmButton = (Button)v.findViewById(R.id.stock_delete_confirm_button);
        mDeleteConfirmButton.setVisibility(View.GONE);
        mDeleteConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StockApi.get(getActivity()).DeleteStock(String.valueOf(mStock.getUUID()));
                //********************************************************************
                mDeleteThisStock = true;
                getActivity().finish();
            }
        });

        mDeleteCancelButton = (Button)v.findViewById(R.id.stock_cancel_button);
        mDeleteCancelButton.setVisibility(View.GONE);
        mDeleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteCancelButton.setVisibility(View.GONE);
                mDeleteConfirmButton.setVisibility(View.GONE);
                mDeleteButton.setVisibility(View.VISIBLE);
            }
        });

        mDeleteButton = (Button)v.findViewById(R.id.stock_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mDeleteCancelButton.setVisibility(View.VISIBLE);
                mDeleteConfirmButton.setVisibility(View.VISIBLE);
                mDeleteButton.setVisibility(View.GONE);
            }
        });

        mSellConfimrButton = (Button)v.findViewById(R.id.stock_sell_confirm_button);
        mSellConfimrButton.setVisibility(View.GONE);
        mSellConfimrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStock.setSoldPrice(mStock.getCurrentPrice());
                Log.d("Test", "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmmmmmmmmmmmmmmmmm  mStock.getSoldPrice() = " + mStock.getSoldPrice());
                Date date = new Date();
                //Declare a date format to use on the date
                String dateFormat = "yyyy MMM dd hh:mm";
                //Get a string version of the data formated by the date format
                String soldDate = DateFormat.format(dateFormat, date)
                        .toString();
                mStock.setSoldDate(soldDate);
                mSellConfimrButton.setVisibility(View.GONE);
                mSellCancelButton.setVisibility(View.GONE);
                Integer numberStock = mStock.getNumberStock();
                mCurrentStockPriceLabel.setText("Price per stock was sold for: ");
                double soldPrice = mStock.getSoldPrice();
                mCurrentStockPrice.setText(String.valueOf(soldPrice ));
                mCurrentStockValueLabel.setText("Total of all stocks was sold for: ");
                double soldTotalValue = soldPrice * numberStock;
                mCurrentStockValue.setText(String.valueOf(soldTotalValue));
                double purchasePrice = mStock.getPurchasePrice();
                double totalPurchasePrice = purchasePrice * numberStock;
                if (soldTotalValue - totalPurchasePrice >= 0)
                {
                    mProfitLoss.setText("This has made a profit of " +formatter.format(soldTotalValue - totalPurchasePrice));
                }
                else {
                    mProfitLoss.setText("This has made a loss of " +formatter.format(soldTotalValue - totalPurchasePrice));
                }
            }
        });

        mSellCancelButton = (Button)v.findViewById(R.id.stock_sell_cancel_button);
        mSellCancelButton.setVisibility(View.GONE);
        mSellCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSellStockButton.setVisibility(View.VISIBLE);
                mSellCancelButton.setVisibility(View.GONE);
                mSellConfimrButton.setVisibility(View.GONE);
            }
        });

        mSellStockButton = (Button)v.findViewById(R.id.stock_sell_button);
        mSellStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSellStockButton.setVisibility(View.GONE);
                mSellCancelButton.setVisibility(View.VISIBLE);
                mSellConfimrButton.setVisibility(View.VISIBLE);
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
                        mStock.setTicker(mUserInput.toUpperCase());
                        mTickerDisplay.setVisibility(View.VISIBLE);
                        mTickerDisplay.setText(mStock.getTicker());
                        mTicker.setVisibility(View.GONE);
                        mStock.setName(stock.getName());
                        mStock.setCurrentPrice(stock.getCurrentPrice());
                        mCurrentStockPrice.setText(formatter.format(stock.getCurrentPrice()));
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


        if (canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
                updatePhotoView();
            }

        });

        mGPSLabel = (TextView)v.findViewById(R.id.stock_idea_coordinates_label);
        mGPS = (TextView)v.findViewById(R.id.stock_idea_coordinates);
        mGPS.setText(mStock.getCoordinates());

        mPurchasePriceLabel = (TextView) v.findViewById(R.id.stock_idea_purchase_price_label);
        mPurchasePrice = (TextView)v.findViewById(R.id.stock_idea_purchase_price);
        final Double purchasePrice = mStock.getPurchasePrice();
        mPurchasePrice.setText(formatter.format( purchasePrice));

        mNumberStock = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased);
        int numberStock = mStock.getNumberStock();
        mNumberStock.setText(String.valueOf(numberStock));

        mTotalStockPrice = (TextView) v.findViewById(R.id.stock_idea_total_purchase_price);
        mCurrentStockPrice = (TextView) v.findViewById(R.id.stock_idea_current_price);
        mCurrentStockPrice.setText(formatter.format(mStock.getCurrentPrice()));

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
                                mStock.setPurchasePrice(mStock.getCurrentPrice());
                                mStock.setNumberStock(numberStock);
                                Date date = new Date();
                                //Declare a date format to use on the date
                                String dateFormat = "yyyy MMM dd hh:mm";
                                //Get a string version of the data formated by the date format
                                String purchaseDate = DateFormat.format(dateFormat, date)
                                        .toString();
                                mStock.setPurchaseDate(purchaseDate);
                                mTotalStockPrice.setText(formatter.format(numberStock * mStock.getCurrentPrice()));
                                mCurrentStockValue.setText(formatter.format(numberStock * mStock.getCurrentPrice()));
                                mCurrentStockPrice.setText(formatter.format(mStock.getCurrentPrice()));
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
                             //   mDeleteButton.setVisibility(View.VISIBLE);
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
            if(mStock.getTicker() != null){
                Stock stock = stockFetcher.fetchStockPriceName(mStock.getTicker(), getContext());
                mCurrentStockPrice.setText(formatter.format( stock.getCurrentPrice()));
                mStock.setCurrentPrice(stock.getCurrentPrice());
                mCurrentStockPriceLabel.setVisibility(View.VISIBLE);
                mCurrentStockPrice.setVisibility(View.VISIBLE);
            } else {
                mCurrentStockPriceLabel.setVisibility(View.GONE);
                mCurrentStockPrice.setVisibility(View.GONE);
            }
            mProfitLoss.setVisibility(View.GONE);
            mNumberStockLabel.setVisibility(View.GONE);
            mNumberStock.setVisibility(View.GONE);
            mTotalStockPriceLabel.setVisibility(View.GONE);
            mTotalStockPrice.setVisibility(View.GONE);
            mPurchaseDateLabel.setVisibility(View.GONE);
            mPurchaseDate.setVisibility(View.GONE);
            mCurrentStockValueLabel.setVisibility(View.GONE);
            mCurrentStockValue.setVisibility(View.GONE);
            mPurchasePrice.setVisibility(View.GONE);
            mPurchasePriceLabel.setVisibility(View.GONE);
            mPurchaseButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);
            mSellStockButton.setVisibility(View.GONE);

        }
        else
        {
            mPurchaseButton.setVisibility(View.GONE);
            Double totalPurchasePrice = purchasePrice * numberStock;
            mTotalStockPrice.setText(formatter.format(totalPurchasePrice));
            mDeleteButton.setVisibility(View.GONE);
            mSellStockButton.setVisibility(View.VISIBLE);

            if(mStock.getTicker() != null)
            {
                Stock stock = stockFetcher.fetchStockPriceName(mStock.getTicker(), getContext());
                mCurrentStockPrice.setText(formatter.format( stock.getCurrentPrice()));
                mStock.setCurrentPrice(stock.getCurrentPrice());
                Double currentStockValue = stock.getCurrentPrice() * numberStock;
                mCurrentStockValue.setText(formatter.format(currentStockValue));
                mProfitLoss.setVisibility(View.VISIBLE);
                if (currentStockValue - totalPurchasePrice >= 0)
                {
                    mProfitLoss.setText("This could make a profit of " +formatter.format(currentStockValue - totalPurchasePrice) + " if sold now.");
                }
                else {
                    mProfitLoss.setText("This could make a loss of " +formatter.format(currentStockValue - totalPurchasePrice) + " if sold now.");
                }
            }
        }
        Log.d("Text", "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm mStock.getSoldPrice()" + mStock.getSoldPrice());
        if(mStock.getSoldPrice() > 0 ){
            mStockSoldDate.setText(mStock.getSoldDate());
            mStockSoldDate.setVisibility(View.VISIBLE);
            mStockSoldDateLabel.setVisibility(View.VISIBLE);
            Double totalPurchasePrice = purchasePrice * numberStock;
            mSellStockButton.setVisibility(View.GONE);
            mCurrentStockPriceLabel.setText("Price per stock was sold for: ");
            double soldPrice = mStock.getSoldPrice();
            mCurrentStockPrice.setText(String.valueOf(soldPrice ));
            mCurrentStockValueLabel.setText("Total of all stocks was sold for: ");
            double soldTotalValue = soldPrice * numberStock;
            mCurrentStockValue.setText(String.valueOf(soldTotalValue));
            if (soldTotalValue - totalPurchasePrice >= 0)
            {
                mProfitLoss.setText("This has made a profit of " +formatter.format(soldTotalValue - totalPurchasePrice));
            }
            else {
                mProfitLoss.setText("This has made a loss of " +formatter.format(soldTotalValue - totalPurchasePrice));
            }
        }else {
            mStockSoldDate.setVisibility(View.GONE);
            mStockSoldDateLabel.setVisibility(View.GONE);
        }

        mTickerDisplay = (TextView)v.findViewById(R.id.stock_idea_ticker_display);
        mTickerDisplay.setText(mStock.getTicker());
        if(mStock.getTicker() == null){
            mTickerDisplay.setVisibility(View.GONE);
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
        } else {
            mValidateSymbolButton.setVisibility(View.GONE);
            mTicker.setVisibility(View.GONE);
            mTickerDisplay.setVisibility(View.VISIBLE);
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
        if (!mDeleteThisStock){
            StockApi.get(getActivity()).UpdateStock(mStock);
        }
        Log.d("TEST", "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV Stock was NOT updated");

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

}
