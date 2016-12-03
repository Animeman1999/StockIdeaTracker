package com.example.jmartin5229.stockideatracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jmartin5229.stockideatracker.StockApi;

import java.util.UUID;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockFragment extends Fragment {
    private static final String ARG_STOCK_ID = "stock_id";
    private static final String DIALOG_DATE = "DialogDate";
    private StockApi mStockApi;

    private static final int REQUEST_DATE = 0;

    private Stock mStock;
    private EditText mIdeaTitle;
    private TextView mTicker;
    private TextView mDateAdded;
    private EditText mDescription;
    private ImageView mPicture;
    private TextView mGPS;
    private TextView mPurchasePrice;
    private TextView mNumberStock;
    private TextView mTotalStockPrice;
    private TextView mCurrentStockPrice;
    private TextView mCurrentStockValue;


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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock_idea, container, false);

        mIdeaTitle = (EditText)v.findViewById(R.id.stock_idea_title);
        mIdeaTitle.setText(mStock.getName());
        mIdeaTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStock.setName(s.toString());
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
        int pictureId =  getResources().getIdentifier(imageName, "drawable", "com.example.jmartin5229.stockideatracker");
        mPicture.setImageResource(pictureId);

        mGPS = (TextView)v.findViewById(R.id.stock_idea_coordinates);
        mGPS.setText(mStock.getCoordinates());

        mPurchasePrice = (TextView)v.findViewById(R.id.stock_idea_purchase_price);
        Double purchasePrice = mStock.getPurchasePrice();
        mPurchasePrice.setText(String.valueOf( purchasePrice));

        mNumberStock = (TextView)v.findViewById(R.id.stock_idea_number_of_stock_purchased);
        int numberStock = mStock.getNumberStock();
        mNumberStock.setText(String.valueOf(numberStock));

        mTotalStockPrice = (TextView)v.findViewById(R.id.stock_idea_total_purchase_price);
        mTotalStockPrice.setText(String.valueOf (purchasePrice * numberStock));

        mCurrentStockPrice = (TextView)v.findViewById(R.id.stock_idea_current_price);
        Double currentPrice = 1.11; //88888888888888888888888888Needs to be a method here to get the price from yahoo
        mCurrentStockPrice.setText(String.valueOf(currentPrice));

        mCurrentStockValue = (TextView)v.findViewById(R.id.stock_idea_total_current_value);
        mCurrentStockValue.setText(String.valueOf(currentPrice * numberStock));
        return v;
    }

}
