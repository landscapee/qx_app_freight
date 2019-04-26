package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class SortingAddActivity extends BaseActivity {

    CustomToolbar customToolbar;
    String TYPE;

    InWaybillRecord mInWaybillRecord;//本页面的数据；

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting_add;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setMainTitle(R.color.white, "新增");
        customToolbar.setLeftTextView(View.VISIBLE, R.color.white, "返回", null);
        //新增页面的逻辑， 是修改还是新增？ TYPE == ADD / UPDATE
        TYPE = getIntent().getStringExtra("TYPE");
        if(TYPE == "ADD")
            typeAdd();
        else
            mInWaybillRecord = new Gson().fromJson(getIntent().getStringExtra("DATA"), new TypeToken<InWaybillRecord>(){}.getType());
            typeUpdate();
    }

    /**
     * 新增 逻辑
     */
    private void typeAdd(){

    }

    /**
     * x修改 逻辑
     */
    private void typeUpdate(){
        //将前一个页面传过来的数据渲染的页面上
    }
}
