package com.example.jmartin5229.stockideatracker;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmartin5229.stockideatracker.StockApi;

import java.io.File;
import java.util.UUID;

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

    private Stock mStock;
    private EditText mIdeaTitle;
    private TextView mTicker;
    private TextView mDateAdded;
    private EditText mDescription;
    private ImageView mPicture;
    private TextView mGPS;
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
    private String m_Text = "";

    private File mPhotoFile;

    public static StockFragment newInstance(UUID stockID)
    {
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
        mStock = mStockApi.GetStock(stockID);
        mPhotoFile = StockApi.get(getActivity()).getPhotoFile(mStock);
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
        mIdeaTitle = (EditText)v.findViewById(R.id.stock_idea_title);
        mIdeaTitle.setText(mStock.getName());
        mIdeaTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStock.setName(s.toString());
                /// ************************************************************Need to add ticker from Yahoo.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTicker = (TextView)v.findViewById(R.id.stock_idea_ticker);
        mTicker.setText(mStock.getTicker());

        mDateAdded = (TextView)v.findViewById(R.id.stock_idea_creation_date);
        mDateAdded.setText(mStock.getCreationDate().toString());

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

        mPicture = (ImageView)v.findViewById(R.id.list_item_stock_idea_thumbnail);
        String imageName = mStock.getPicture();
        //This needs to be tested****************************************************************************************
        if (imageName != "") {
            int pictureId = getResources().getIdentifier(imageName, "drawable", "com.example.jmartin5229.stockideatracker");
            mPicture.setImageResource(pictureId);
        }
        else
        {
            mPicture.setImageResource(R.drawable.take_a_picture);
        }
        if (canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
                //******************************get a picture from the camera
                //                               save picture into drawable folder
                //                               save picture name to database
                //                               setImageResource to view of new picture
                //                               get GPS
                //                               save GPS to database
                //                               update mGPS.setText
            }
        });

        mGPS = (TextView)v.findViewById(R.id.stock_idea_coordinates);
        mGPS.setText(mStock.getCoordinates());

        mPurchasePrice = (TextView)v.findViewById(R.id.stock_idea_purchase_price);
        final Double purchasePrice = mStock.getPurchasePrice();
        mPurchasePrice.setText(String.valueOf( purchasePrice));

        mNumberStock = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased);
        int numberStock = mStock.getNumberStock();
        mNumberStock.setText(String.valueOf(numberStock));

        mNumberStockLabel = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased_label);
        mTotalStockPriceLabel = (TextView)v.findViewById(R.id.stock_idea_total_purchase_price_label);
        mCurrentStockPriceLabel = (TextView)v.findViewById(R.id.stock_idea_current_price_label);
        mCurrentStockValueLabel = (TextView)v.findViewById(R.id.stock_idea_total_current_value_label);
        mPurchaseButton = (Button)v.findViewById(R.id.stock_purchase_button);
        mPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Title");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL );
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                if(m_Text != "") {
                    try {
                        int numberStock = Integer.valueOf(m_Text);
                        mNumberStock.setText(m_Text);
                        mStock.setNumberStock(numberStock);
                        mTotalStockPrice.setText(String.valueOf(numberStock * purchasePrice));
                        mCurrentStockValue.setText(String.valueOf(numberStock * purchasePrice));
                        mNumberStockLabel.setVisibility(View.VISIBLE);
                        mTotalStockPriceLabel.setVisibility(View.VISIBLE);
                        mCurrentStockPriceLabel.setVisibility(View.VISIBLE);
                        mCurrentStockValueLabel.setVisibility(View.VISIBLE);
                        mPurchaseButton.setVisibility(View.GONE);
                    } catch (Exception e)
                    {
                        Toast.makeText(getActivity(), R.string.bad_input, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (numberStock <= 0)
        {
            mNumberStockLabel.setVisibility(View.GONE);
            mTotalStockPriceLabel.setVisibility(View.GONE);
            mCurrentStockPriceLabel.setVisibility(View.GONE);
            mCurrentStockValueLabel.setVisibility(View.GONE);
            mPurchaseButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mPurchaseButton.setVisibility(View.GONE);
            mTotalStockPrice = (TextView) v.findViewById(R.id.stock_idea_total_purchase_price);
            mTotalStockPrice.setText(String.valueOf(purchasePrice * numberStock));

            mCurrentStockPrice = (TextView) v.findViewById(R.id.stock_idea_current_price);
            Double currentPrice = 1.11; //88888888888888888888888888Needs to be a method here to get the price from yahoo
            mCurrentStockPrice.setText(String.valueOf(currentPrice));

            mCurrentStockValue = (TextView) v.findViewById(R.id.stock_idea_total_current_value);
            mCurrentStockValue.setText(String.valueOf(currentPrice * numberStock));
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mStockApi.UpdateStock(mStock);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

}
