package com.example.jmartin5229.stockideatracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ccunn on 03-Dec-16.
 */

public class StockIdeaListFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private StockIdeaAdapter mAdapter;
    private File mPhotoFile;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // >Inflate the view from the layout file.
        View view = inflater.inflate(R.layout.fragment_stock_idea_list, container, false);
        // >Get the Recycler View.
        mRecyclerView = (RecyclerView) view.findViewById(R.id.stock_idea_recycler_view);
        // >Set the recycler view to use a liner layout.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private class StockIdeaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mTickerTextView;
        private TextView mDateTimeTextView;
        private TextView mCoordinatesTextView;
        private ImageView mThumbnailImageView;

        private Stock mStockIdea;// >The stock idea


        public StockIdeaHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_stock_idea_title);
            mTickerTextView = (TextView) itemView.findViewById(R.id.list_item_stock_idea_ticker_symbol);
            mDateTimeTextView = (TextView) itemView.findViewById(R.id.list_item_stock_idea_date_time);
            mCoordinatesTextView = (TextView) itemView.findViewById(R.id.list_item_stock_idea_coordinates);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.list_item_stock_idea_thumbnail);
        }


        public void bindStockIdea(Stock stockIdea) {
            mStockIdea = stockIdea;
            mPhotoFile = StockApi.get(getActivity()).getPhotoFile(stockIdea);
            // >Set the widgets, using the passed in stock idea.
            mTitleTextView.setText(stockIdea.getName());
            mTickerTextView.setText(stockIdea.getTicker());
            mDateTimeTextView.setText(stockIdea.getCreationDate());
            mCoordinatesTextView.setText(stockIdea.getCoordinates());
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mThumbnailImageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), 75, 75);
                mThumbnailImageView.setImageBitmap(bitmap);
            }
    }

        @Override
        public void onClick(View v) {
            // >Start a new activity with the intent of the stock idea with the passed in UUID.
            startActivity(StockActivity.newIntent(getActivity(), mStockIdea.getUUID()) );
        }
    }

    private class StockIdeaAdapter extends RecyclerView.Adapter<StockIdeaHolder> {
        private List<Stock> mStockIdeaList;


        public void setStockIdeaList(List<Stock> stockIdeaList) {
            mStockIdeaList = stockIdeaList;
        }


        // >Constructor
        public StockIdeaAdapter(List<Stock> stockIdeaList) {
            mStockIdeaList = stockIdeaList;
        }


        @Override
        public StockIdeaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // >Get a layout inflater to inflate the view.
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // >Use the inflater to inflate the default android list view.
            View view = layoutInflater.inflate(R.layout.list_item_stock_idea, parent, false);

            return new StockIdeaHolder(view);
        }

        @Override
        public void onBindViewHolder(StockIdeaHolder holder, int position) {
            // >Get an idea out of the list.
            Stock stockIdea = mStockIdeaList.get(position);
            // >Set the view holder's widgets.
            holder.bindStockIdea(stockIdea);
        }

        @Override
        public int getItemCount() {
            return mStockIdeaList.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }


    private void updateUI() {
        StockApi stockApi = StockApi.get(getActivity());
        List<Stock> stockIdeaList = stockApi.GetStocks();

        if (mAdapter == null) {
            // >Create a new adapter and give it the list of ideas.
            mAdapter = new StockIdeaAdapter(stockIdeaList);
            // >Set the adapter for the recycler.
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setStockIdeaList(stockIdeaList);
            // >Notify the adapter that the data has changed and should be reloaded.
            mAdapter.notifyDataSetChanged();
        }
    }


}
