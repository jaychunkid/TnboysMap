package com.example.tnboysmap.utils;

import android.location.Location;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.enums.IconType;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteRailwayItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by miku on 2017/5/30 0030.
 */

public class MapUtils {

    public static LatLng convertToLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint){
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static NaviLatLng convertToNaviLatLng(LatLng latLng){
        return new NaviLatLng(latLng.latitude, latLng.longitude);
    }

    public static NaviLatLng convertToNaviLatLng(LatLonPoint latLonPoint){
        return new NaviLatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latLng){
        return new LatLonPoint(latLng.latitude, latLng.longitude);
    }

    public static LatLonPoint convertToLatLonPoint(Location location){
        return new LatLonPoint(location.getLatitude(), location.getLongitude());
    }

    public static LatLonPoint convertToLatLonPoint(NaviLatLng naviLatLng){
        return new LatLonPoint(naviLatLng.getLatitude(), naviLatLng.getLongitude());
    }

    public static String getLengthStr(float length){
        if(length < 1000){
            return (int)(length) +"米";
        } else if(length < 10000){
            DecimalFormat decimalFormat=new DecimalFormat(".0");
            return decimalFormat.format(length/1000)+"公里";
        } else {
            return (int)(length/1000)+"公里";
        }
    }

    public static String getTimeStr(long second){
        if (second > 3600) {
            long hour = second / 3600;
            long minute = (second % 3600) / 60;
            return hour + "小时" + minute + "分钟";
        }
        if (second >= 60) {
            long minute = second / 60;
            return minute + "分钟";
        }
        return second + "秒";
    }

    public static String getBusPathTitle(BusPath path){
        String empty="";
        if(path==null){
            return empty;
        }
        List<BusStep> busStepList=path.getSteps();
        if(busStepList==null){
            return empty;
        }

        StringBuilder title=new StringBuilder();
        for(BusStep step:busStepList){
            StringBuilder subTitle=new StringBuilder();
            if(step.getBusLines().size()>0){
                for(RouteBusLineItem busLineItem:step.getBusLines()){
                    if(busLineItem!=null){
                        String name=busLineItem.getBusLineName();
                        if(name!=null){
                            subTitle.append(name.replaceAll("\\(.*\\)", ""));
                            subTitle.append(" / ");
                        }
                    }
                }
                title.append(subTitle.substring(0, subTitle.length()-3));
                title.append(" > ");
            }
            if(step.getRailway()!=null){
                RouteRailwayItem routeRailwayItem=step.getRailway();
                title.append(routeRailwayItem.getTrip()+"("+
                        routeRailwayItem.getDeparturestop().getName()+" - "+
                        routeRailwayItem.getArrivalstop().getName());
                title.append(" > ");
            }
        }
        return title.substring(0, title.length()-3);
    }

    public static String getBusPathInfo(BusPath path){
        String empty="";
        if(path==null){
            return empty;
        }
        return MapUtils.getTimeStr(path.getDuration())+" | "+
                MapUtils.getLengthStr(path.getDistance())+" | 步行"+
                MapUtils.getLengthStr(path.getWalkDistance())+" | 花费"+
                path.getCost()+"元";
    }

    public static String getActionStr(int iconType){
        switch (iconType){
            case IconType.ARRIVED_DESTINATION:
                return "到达目的地";
            case IconType.ARRIVED_SERVICE_AREA:
                return "到达服务器";
            case IconType.ARRIVED_TOLLGATE:
                return "到达收费站";
            case IconType.ARRIVED_TUNNEL:
                return "到达隧道";
            case IconType.CABLEWAY:
                return "通过索道";
            case IconType.CHANNEL:
                return "通过通道";
            case IconType.CROSSWALK:
                return "通过人行横道";
            case IconType.CRUISE_ROUTE:
                return "通过游船路线";
            case IconType.ENTER_ROUNDABOUT:
                return "进入环岛";
            case IconType.LADDER:
                return "通过阶梯";
            case IconType.LEFT:
                return "左转";
            case IconType.LEFT_BACK:
                return "向左后方转";
            case IconType.LEFT_FRONT:
                return "向左前方转";
            case IconType.LEFT_TURN_AROUND:
                return "左转掉头";
            case IconType.LIFT:
                return "通过直梯";
            case IconType.OUT_ROUNDABOUT:
                return "驶出环岛";
            case IconType.OVERPASS:
                return "通过过街天桥";
            case IconType.PARK:
                return "通过公园";
            case IconType.RIGHT:
                return "右转";
            case IconType.RIGHT_BACK:
                return "向右后方转";
            case IconType.RIGHT_FRONT:
                return "向右前方转";
            case IconType.SIGHTSEEING_BUSLINE:
                return "通过观光车路线";
            case IconType.SKY_CHANNEL:
                return "通过空中通道";
            case IconType.SLIDEWAY:
                return "通过滑道";
            case IconType.SQUARE:
                return "通过广场";
            case IconType.STAIRCASE:
                return "通过扶梯";
            case IconType.STRAIGHT:
                return "直行";
            case IconType.UNDERPASS:
                return "通过地下通道";
            case IconType.WALK_ROAD:
                return "通过行人道路";
            default:
                return null;
        }
    }
}
