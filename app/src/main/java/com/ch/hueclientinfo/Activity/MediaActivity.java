package com.ch.hueclientinfo.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.hueclientinfo.Adapter.MyAdapter;
import com.ch.hueclientinfo.Bean.Info;
import com.ch.hueclientinfo.Bean.Page;
import com.ch.hueclientinfo.R;
import com.ch.hueclientinfo.Utils.DownloadUtil;
import com.ch.hueclientinfo.Utils.ItemDivider;
import com.ch.hueclientinfo.Utils.ReadJson;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    private TextView current_page;
    private TextView last_page;
    private Button pre, next;
    private List<String> list;
    private MyAdapter adapter;
    private static final int DownloadSuccess=0;
    private static final int DownloadFaied =1;
    private String page ="1";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switchMessage(msg);
            super.handleMessage(msg);
        }
    };

    private void switchMessage(Message msg) {
        switch (msg.what){
            case DownloadSuccess:
                String json = msg.getData().getString("json");
                json= ReadJson.get().convertJson(json);
                Gson gson = new Gson();
                final Page page =gson.fromJson(json,Page.class);
                final List<Info> infos =page.getData();
                //  Log.d("NewsActivity", "page.getTotal():" + page.getTotal());
                last_page.setText(String.valueOf(page.getTotal()));
                list.clear();
                for (Info info:infos){
                    list.add(info.getTitle());
                }
                adapter.setDataList(list);
                recyclerView.addItemDecoration(new ItemDivider().setDividerColor(getResources().getColor(R.color.colorPrimary)));
                //recyclerView.setAdapter(adapter);
                adapter.setItemClickListener(new MyAdapter.ItemClickListener() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Intent intent = new Intent();
                        intent.setClass(MediaActivity.this,ContentActivity.class);
                        intent.putExtra("title",page.getData().get(position).getTitle());
                        intent.putExtra("contents",page.getData().get(position).getContents());
                        intent.putExtra("source",page.getData().get(position).getSource());
                        startActivity(intent);
                    }
                });

                break;
            case DownloadFaied:
                Toast.makeText(getApplicationContext(),"没有网络",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        refreah();
        init();
    }

    private void init() {
        setTitle(R.string.media);
        recyclerView = findViewById(R.id.recyclerView);
        current_page = findViewById(R.id.current_page);
        last_page = findViewById(R.id.last_page);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        list = new ArrayList<>();
        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MediaActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        adapter = new MyAdapter();
        adapter.setDataList(list);
        recyclerView.setAdapter(adapter);
        downloadinfo(page);
    }
    private void downloadinfo(String page){
        final SweetAlertDialog dialog = new SweetAlertDialog(MediaActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitleText("提示");
        dialog.setContentText("数据正在加载中,请稍等！");
        dialog.show();
        DownloadUtil util = DownloadUtil.get();
        util.download("http://www.hue.edu.cn/webapp/public/cates/413&page=1?page="+page, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String json) {
                if (json!=null) {
                    Message message = Message.obtain();
                    message.what = DownloadSuccess;
                    Bundle bundle = new Bundle();
                    bundle.putString("json",json);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    dialog.dismiss();
                }
            }
            @Override
            public void onDownloadFailed(Exception e) {
                Message message = Message.obtain();
                message.what = DownloadFaied;
                handler.sendMessage(message);
            }
        });
    }
    private void refreah() {
        SmartRefreshLayout refreshLayout =findViewById(R.id.refreshLayout);
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){

            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                TastyToast.makeText(MediaActivity.this,"下拉刷新",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                super.onHeaderPulling(header, percent, offset, headerHeight, extendHeight);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {
                /**
                 * 重新加载当前页
                 */
                loadCurrent();
                super.onHeaderReleased(header, headerHeight, extendHeight);
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                TastyToast.makeText(MediaActivity.this,"刷新成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                super.onHeaderFinish(header, success);
            }
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                TastyToast.makeText(MediaActivity.this,"正在刷新",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                super.onRefresh(refreshLayout);
            }
        });
    }
    private void loadCurrent(){
        downloadinfo(current_page.getText().toString());
    }

    private void prePage(){
        int curr = Integer.parseInt(current_page.getText().toString());
        if (curr > 1) {
            current_page.setText(String.valueOf(curr - 1));
            downloadinfo(String.valueOf(curr - 1));

        }
    }
    private void  nextPage(){
        int curr = Integer.parseInt(current_page.getText().toString());
        int last = Integer.parseInt(last_page.getText().toString());
        if (curr < last) {
            current_page.setText(String.valueOf(curr + 1));
            downloadinfo(String.valueOf(curr + 1));
        }
    }
    @Override
    public void onClick(View view) {
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePage();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
    }
}
