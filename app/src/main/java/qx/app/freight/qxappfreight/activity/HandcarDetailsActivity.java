package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.HandcarDetailsAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 板车详情页面
 */
public class HandcarDetailsActivity extends BaseActivity {
    @BindView(R.id.rv_handcar)
    RecyclerView mRecyclerView;

    private FtRuntimeFlightScooter mFtRuntimeFlightScooter;
    private List <FtGroupScooter> list = new ArrayList <>();
    private HandcarDetailsAdapter mHandcarDetailsAdapter;

    private List <FtGroupScooter> listDelete = new ArrayList <>();

    private List <FtGroupScooter> listPullDown = new ArrayList <>();

    public static void startActivity(Context context, FtRuntimeFlightScooter mFtRuntimeFlightScooter) {
        Intent intent = new Intent(context, HandcarDetailsActivity.class);
        intent.putExtra("FtRuntimeFlightScooter", mFtRuntimeFlightScooter);
        ((Activity) context).startActivityForResult(intent, 0);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_handcar_details;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "板车详情");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mHandcarDetailsAdapter = new HandcarDetailsAdapter(list);
        mRecyclerView.setAdapter(mHandcarDetailsAdapter);
        mHandcarDetailsAdapter.setOnDeleteClickListener((view, position) ->
        {

            if (mFtRuntimeFlightScooter.isLock()){
                ToastUtil.showToast("被锁定的板车不能操作");
                return;
            }

            //本航班的 拉下
            if (list.get(position).getInFlight() == 0) {
                if (2 != list.get(position).getUpdateStatus())
                {
                    list.get(position).setUpdateStatus((short)1);
                }

                listPullDown.add(list.get(position));
                list.remove(position);
            } else {//删除
                list.get(position).setUpdateStatus((short)3);
                listDelete.add(list.get(position));
                list.remove(position);
            }
            mHandcarDetailsAdapter.notifyDataSetChanged();
        });

        setIsBack(true, () ->
                finishReturnList()
        );
    }

    /**
     * 关闭页面返回 list
     */
    private void finishReturnList() {
        Intent mIntent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("listPullDown", (Serializable) listPullDown);
        mBundle.putSerializable("listDelete", (Serializable) listDelete);
        mIntent.putExtras(mBundle);

        mFtRuntimeFlightScooter.setInFlight((short) 0);
        mFtRuntimeFlightScooter.setWeight((double)0);
        mFtRuntimeFlightScooter.setVolume((double)0);
        for (FtGroupScooter rcInfo : list) {
            mFtRuntimeFlightScooter.setWeight(mFtRuntimeFlightScooter.getWeight()+rcInfo.getWeight());
            mFtRuntimeFlightScooter.setVolume(mFtRuntimeFlightScooter.getVolume()+rcInfo.getVolume());
            if (rcInfo.getInFlight() == 1) {
                mFtRuntimeFlightScooter.setInFlight((short) 1);
            }
        }
        mFtRuntimeFlightScooter.setGroupScooters(list);
        mIntent.putExtra("FtRuntimeFlightScooter", mFtRuntimeFlightScooter);
        setResult(Constants.FINISH_HANDCAR_DETAILS, mIntent);
        finish();

    }

    private void initData() {
        mFtRuntimeFlightScooter = (FtRuntimeFlightScooter) getIntent().getSerializableExtra("FtRuntimeFlightScooter");
        list.addAll(mFtRuntimeFlightScooter.getGroupScooters());
        mHandcarDetailsAdapter.notifyDataSetChanged();
    }
}
