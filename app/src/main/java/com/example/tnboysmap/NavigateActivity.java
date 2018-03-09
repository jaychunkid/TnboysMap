package com.example.tnboysmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.example.tnboysmap.utils.NavigationVoiceController;

public class NavigateActivity extends AppCompatActivity implements AMapNaviViewListener {

    private AMapNaviView aMapNaviView=null;
    private AMapNavi aMapNavi=null;
    private NavigationVoiceController controller=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        aMapNaviView=(AMapNaviView)findViewById(R.id.navigate_view);
        aMapNaviView.onCreate(savedInstanceState);
        aMapNaviView.setAMapNaviViewListener(this);
        AMapNaviViewOptions options=new AMapNaviViewOptions();
        options.setReCalculateRouteForYaw(true);
        options.setScreenAlwaysBright(true);
        aMapNaviView.setViewOptions(options);
        controller=NavigationVoiceController.getInstance(getApplicationContext());
        aMapNavi=AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(controller);
        AMapNavi.setTtsPlaying(false);
        aMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapNaviView.onDestroy();
        aMapNavi.stopNavi();
        aMapNavi.removeAMapNaviListener(controller);
        controller.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        aMapNaviView.onPause();
        aMapNavi.pauseNavi();
        controller.stopSpeaking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        aMapNaviView.onResume();
        aMapNavi.resumeNavi();
    }

    @Override
    public void onLockMap(boolean b) {

    }

    //导航组件返回按钮点击事件处理
    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    //点击返回按钮后弹出对话框选择“确定”事件处理
    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onNaviViewLoaded() {

    }
}
