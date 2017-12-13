package com.heshiqi.widget;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.heshiqi.widget.adapter.BaseHeaderFooterAdapter;
import com.heshiqi.widget.adapter.LoadMoreAdapter;
import com.heshiqi.widget.core.contract.MainContract;
import com.heshiqi.widget.core.presenter.MainPresenter;
import com.heshiqi.widget.core.repository.MainRepository;
import com.heshiqi.widget.entity.MainEntity;
import com.heshiqi.widget.loadmore.LoadMoreRecyclerView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainContract.View {

    /**
     * 实现了加载更多功能
     */
    private LoadMoreRecyclerView recyclerView;

    private LoadMoreAdapter adapter;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, new MainRepository());
        initView();
        initData();
        initListener();

    }

    private void initView() {

        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);

        //设置item之间的分隔线
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider_1dip));
        recyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        adapter = new LoadMoreAdapter(this, presenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.triggerLoadMore();
        recyclerView.setAdapter(adapter);
        //开始加载数据
        presenter.loadDatas(false);

    }

    private void initListener() {
        recyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                //加载更多
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.loadDatas(true);
                    }
                },3000);

            }
        });

        //话题推荐点击事件
        adapter.setOnItemClickListener(new BaseHeaderFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {

            }
        });

    }

    @Override
    public void onResultData(List<MainEntity> datas, boolean isSuccess, boolean isLoadMore) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS status) {
        recyclerView.setLoadMoreStatus(status);
    }
}
