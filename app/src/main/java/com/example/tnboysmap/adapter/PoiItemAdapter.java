package com.example.tnboysmap.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.example.tnboysmap.R;
import com.example.tnboysmap.SearchActivity;
import com.example.tnboysmap.utils.Constants;

import java.util.List;

/**
 * Created by miku on 2017/6/1 0001.
 */

public class PoiItemAdapter extends RecyclerView.Adapter<PoiItemAdapter.ViewHolder>{

    private List<PoiItem> poiItemList;
    private SearchActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View poiItemView;
        TextView poiItemName;
        TextView poiItemAddress;

        public ViewHolder(View view){
            super(view);
            poiItemView=view;
            poiItemName=(TextView)view.findViewById(R.id.name);
            poiItemAddress=(TextView)view.findViewById(R.id.address);
        }
    }

    public PoiItemAdapter(List<PoiItem> poiItemList, SearchActivity activity){
        this.poiItemList=poiItemList;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip_poi,
                parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.poiItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                PoiItem poiItem=poiItemList.get(position);
                Intent intent=new Intent();
                intent.putExtra("resultType", Constants.RESULT_POIITEM);
                intent.putExtra("result", poiItem);
                activity.setResult(AppCompatActivity.RESULT_OK, intent);
                activity.finish();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoiItem poiItem=poiItemList.get(position);
        holder.poiItemName.setText(poiItem.getTitle());
        holder.poiItemAddress.setText(poiItem.getSnippet());
    }

    @Override
    public int getItemCount() {
        return poiItemList.size();
    }
}
