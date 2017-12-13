package com.heshiqi.widget.core.presenter;

import com.heshiqi.widget.core.contract.MainContract;
import com.heshiqi.widget.core.repository.MainRepository;
import com.heshiqi.widget.entity.MainEntity;
import com.heshiqi.widget.exception.AHHttpException;
import com.heshiqi.widget.listener.RequestListener;
import com.heshiqi.widget.loadmore.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshiqi on 17/12/13.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    private MainRepository mainRepository;

    private List<MainEntity> datas = new ArrayList<>();

    public MainPresenter(MainContract.View view, MainRepository repository) {
        this.mView = view;
        this.mainRepository = repository;
    }

    @Override
    public void loadDatas(final boolean isLoadMore) {
        mainRepository.getDatas(new RequestListener<List<MainEntity>>() {
            @Override
            public void onSuccess(List<MainEntity> data, int statusCode, Object tag) {
                if (!isLoadMore) {
                    //不是加载更多 清除掉旧数据
                    datas.clear();
                }
                datas.addAll(data);
                if (data.size()<10) {
                    mView.setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS.LOADING_HIDE);
                } else {
                    mView.setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS.LOADING);
                }
                mView.onResultData(datas, true, isLoadMore);
            }

            @Override
            public void onError(AHHttpException e, Object tag) {
                mView.onResultData(datas, false, isLoadMore);
            }
        });
    }

    @Override
    public MainEntity getItem(int position) {
        int size = datas.size();
        if (size == 0 || position < 0 || position >= size) {
            return null;
        }
        return datas.get(position);
    }

    @Override
    public List<MainEntity> getDatas() {
        return datas;
    }

}
