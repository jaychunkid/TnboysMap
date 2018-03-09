package com.example.tnboysmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tnboysmap.adapter.PoiItemAdapter;
import com.example.tnboysmap.adapter.TipAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener,
        Inputtips.InputtipsListener, SearchView.OnQueryTextListener{

    private String city=null;

    private RecyclerView recyclerView=null;
    private List<Tip> tipList=null;
    private List<PoiItem> poiItemList=null;
    private TipAdapter tipAdapter=null;
    private PoiItemAdapter poiItemAdapter=null;

    private ProgressDialog loadingDialog=null;
    private SearchView searchView=null;
    private TextView noResult=null;

    //活动跳转函数
    public static void startActivity(AppCompatActivity activity, int REQUEST_CODE, String city){
        Intent intent=new Intent(activity, SearchActivity.class);
        intent.putExtra("city", city);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        city=getIntent().getStringExtra("city");

        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        noResult=(TextView)findViewById(R.id.text_no_result);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_tip);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    //设置搜索框的文本并直接开始搜索
    public void setQuery(String keyword){
        searchView.setQuery(keyword, true);
    }

    //开始地点搜索
    private void searchPOI(String keyword){
        showLoadingDialog();
        PoiSearch.Query query=new PoiSearch.Query(keyword, "", city);
        query.setPageSize(50);
        query.setPageNum(0);
        PoiSearch poiSearch=new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    //显示等待对话框
    private void showLoadingDialog(){
        if(loadingDialog==null){
            loadingDialog=new ProgressDialog(this);
            loadingDialog.setTitle("Please wait for a moment");
            loadingDialog.setMessage("Loading...");
            loadingDialog.setCancelable(true);
            loadingDialog.show();
        }
    }

    //关闭等待对话框
    private void dismissLoadingDialog(){
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog=null;
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    //搜索框开始搜索事件处理
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchPOI(query);
        return false;
    }

    //搜索框内文本改变事件处理
    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText!=null&&!newText.equals("")){
            InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, city);
            Inputtips inputTips = new Inputtips(SearchActivity.this.getApplicationContext(),
                    inputtipsQuery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (tipAdapter != null && tipList != null) {
                tipList.clear();
                tipAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    //获取关键词搜索结果
    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if(i==1000){
            tipList=list;
            tipAdapter=new TipAdapter(tipList, this);
            recyclerView.setAdapter(tipAdapter);
            tipAdapter.notifyDataSetChanged();
            if(tipList.size()!=0){
                noResult.setVisibility(View.GONE);
            } else {
                noResult.setVisibility(View.VISIBLE);
            }
        } /*else {
            Toast.makeText(this, "Get input tips failed. Please check your settings.",
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    //获取地点搜索结果
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dismissLoadingDialog();
        if(i==1000){
            if(poiResult!=null&&poiResult.getQuery()!=null&&poiResult.getPois()!=null){
                poiItemList=poiResult.getPois();
                poiItemAdapter=new PoiItemAdapter(poiItemList, this);
                recyclerView.setAdapter(poiItemAdapter);
                poiItemAdapter.notifyDataSetChanged();
                if(poiItemList.size()!=0) {
                    noResult.setVisibility(View.GONE);
                } else {
                    noResult.setVisibility(View.VISIBLE);
                }
            } else {
                noResult.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, "Poi searching failed. Error code "+i,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //toolbar菜单项被选中事件处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
