package com.example.tnboysmap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.example.tnboysmap.R;
import com.example.tnboysmap.utils.MapUtils;

import java.util.List;

/**
 * Created by miku on 2017/6/11 0011.
 */

public class BusPathAdapter extends RecyclerView.Adapter<BusPathAdapter.ViewHolder> {

    private List<BusPath> busPathList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView pathTitle;
        TextView pathInfo;
        LinearLayout expandContent;

        public ViewHolder(View view) {
            super(view);
            pathTitle=(TextView)view.findViewById(R.id.title);
            pathInfo=(TextView)view.findViewById(R.id.info);
            expandContent=(LinearLayout)view.findViewById(R.id.expand_content);
        }
    }

    public BusPathAdapter(Context context, List<BusPath> busPathList){
        this.context=context;
        this.busPathList=busPathList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path,
                parent, false);
        final ViewHolder holder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                BusPath path=busPathList.get(position);
                if(holder.expandContent.getVisibility()==View.GONE){
                    List<BusStep> busStepList=path.getSteps();
                    if(busStepList!=null){
                        for(BusStep busStep:busStepList){
                            if(busStep.getWalk()!=null&&busStep.getWalk().getDistance()>0){
                                TextView detail=(TextView)View.inflate(context,
                                        R.layout.path_detail, null);
                                detail.setText(String.format("%s", "步行"+MapUtils.
                                        getLengthStr(busStep.getWalk().getDistance())));
                                holder.expandContent.addView(detail);
                            }
                            if(busStep.getBusLines()!=null&&busStep.getBusLines().size()>0){
                                RouteBusLineItem busLineItem=busStep.getBusLines().get(0);
                                TextView detail=(TextView)View.inflate(context,
                                        R.layout.path_detail, null);
                                detail.setText(String.format("%s", "从 "+
                                        busLineItem.getDepartureBusStation().getBusStationName()+
                                        " 乘坐 "+busLineItem.getBusLineName()+" 到 "+
                                        busLineItem.getArrivalBusStation().getBusStationName())+
                                        " 共 "+(busLineItem.getPassStationNum()+1)+" 站");
                                holder.expandContent.addView(detail);
                            }
                            if(busStep.getRailway()!=null){
                                TextView detail=(TextView)View.inflate(context,
                                        R.layout.path_detail, null);
                                detail.setText(String.format("%s", "从 "+
                                        busStep.getRailway().getDeparturestop().getName()+
                                        " 乘坐 "+busStep.getRailway().getTrip()+" 到 "+
                                        busStep.getRailway().getArrivalstop().getName()));
                                holder.expandContent.addView(detail);
                            }
                            if(busStep.getTaxi()!=null){
                                TextView detail=(TextView)View.inflate(context,
                                        R.layout.path_detail, null);
                                detail.setText(String.format("%s", "从 "+
                                        busStep.getTaxi().getmSname()+ " 打车到 "+
                                        busStep.getTaxi().getmTname()));
                                holder.expandContent.addView(detail);
                            }
                        }
                        holder.expandContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.expandContent.removeAllViews();
                    holder.expandContent.setVisibility(View.GONE);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BusPath busPath=busPathList.get(position);
        holder.pathTitle.setText(MapUtils.getBusPathTitle(busPath));
        holder.pathInfo.setText(MapUtils.getBusPathInfo(busPath));
    }

    @Override
    public int getItemCount() {
        return busPathList.size();
    }
}
