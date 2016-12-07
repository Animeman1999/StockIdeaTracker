package com.example.jmartin5229.stockideatracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.UUID;

/**
 * Created by jmartin5229 on 12/5/2016.
 */
public class StockActivity extends SingleFragmentActivity {

    private static final String EXTRA_STOCK_ID = "com.example.jmartin5229.stockideatracker.stock.id";

    public static Intent newIntent(Context packageContext, UUID stockId){
        Intent intent = new Intent(packageContext, StockActivity.class);
        intent.putExtra(EXTRA_STOCK_ID, stockId);
        return intent;
    }

    @Override
        protected Fragment createFragment() {
        UUID stockId = (UUID) getIntent().getSerializableExtra(EXTRA_STOCK_ID);
        return StockFragment.newInstance(stockId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
