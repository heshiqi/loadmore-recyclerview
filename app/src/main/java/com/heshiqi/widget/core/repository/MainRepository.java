package com.heshiqi.widget.core.repository;

import com.heshiqi.widget.entity.MainEntity;
import com.heshiqi.widget.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshiqi on 17/12/13.
 */

public class MainRepository implements IBaseRepository {

    static final String[] imgs = new String[]{
            "http://p2.gexing.com/G1/M00/92/EC/rBACE1PbWkXTtBKuAADv9jhW78s625_600x.jpg",
            "http://img1.3lian.com/2015/a2/222/237.jpg",
            "http://img5.duitang.com/uploads/item/201507/26/20150726164007_ZkfYS.jpeg",
            "http://img5.duitang.com/uploads/item/201511/01/20151101110436_zWiZN.thumb.700_0.jpeg",
            "http://img3.duitang.com/uploads/item/201512/17/20151217122216_rfGHJ.jpeg",
            "http://img5.duitang.com/uploads/item/201511/13/20151113171711_Pd4tG.thumb.700_0.jpeg",
            "http://cdn.duitang.com/uploads/item/201508/09/20150809010902_QjynH.jpeg",
            "http://img4.duitang.com/uploads/item/201511/20/20151120212438_KXrhZ.jpeg",
            "http://img1.imgtn.bdimg.com/it/u=2368026536,1512658262&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2712718160,391073185&fm=214&gp=0.jpg"
    };

    public void getDatas(RequestListener<List<MainEntity>> requestListener) {
       List<MainEntity> datas=new ArrayList<>();
       final int size=imgs.length;
        for (int i=0;i<size;i++) {
            MainEntity entity = new MainEntity();
            entity.setId(i);
            entity.setContent("文本内容 "+i);
            entity.setImg(imgs[i]);
            datas.add(entity);
        }
        requestListener.onSuccess(datas,200,null);
    }
}
