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

        

        return v;
    }
}
