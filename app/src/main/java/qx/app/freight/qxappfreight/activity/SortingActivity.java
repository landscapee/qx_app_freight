package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SortingInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.presenter.InWaybillRecordPresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 进港分拣 - 显示列表界面
 * <p>
 * create by guohao - 2019/4/26
 */
public class SortingActivity extends BaseActivity implements InWaybillRecordContract.inWaybillRecordView {

    String flightNo = "";
    String arriveTime = "";
    String handCarNum = "";
    String getHandCarNumTotal = "";
    TransportListBean.TransportDataBean transportListBean;
    List<InWaybillRecord> mList;
    InWaybillRecordSubmitEntity submitEntity = new InWaybillRecordSubmitEntity();//最终提交请求的实体

    InWaybillRecordBean resultBean;//网络请求返回的bean

    int CURRENT_DELETE_POSITION = -1;

    static final String TYPE_ADD = "ADD";
    static final String TYPE_UPDATE = "UPDATE";

    SortingInfoAdapter mAdapter;

    @BindView(R.id.recycler_view)
    SlideRecyclerView recyclerView;

    @BindView(R.id.tv_flight_no)
    TextView flightNoTv;
    @BindView(R.id.tv_arrive_time)
    TextView arriveTimeTv;
    @BindView(R.id.tv_handcar_num)
    TextView handCarNumTv;
    @BindView(R.id.tv_handcar_total)
    TextView handCarTotalTv;

    @BindView(R.id.btn_temp)
    Button tempBtn;
    @BindView(R.id.btn_done)
    Button doneBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        Intent intent = getIntent();
        transportListBean = (TransportListBean.TransportDataBean) intent.getSerializableExtra("TASK_INFO");
        //从前一个页面传递过来的数据
        flightNo = transportListBean.getFlightNo();
        arriveTime = TimeUtils.getTime2(transportListBean.getEtd());
        handCarNum = transportListBean.getArriveWarehouseNum() + "";
        getHandCarNumTotal = transportListBean.getTotalScooterNum() + "";
        //显示传递的数据
        flightNoTv.setText(flightNo);
        arriveTimeTv.setText(arriveTime);
        handCarNumTv.setText(handCarNum);
        handCarTotalTv.setText("/" + getHandCarNumTotal);
        //toolbar
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "进港理货");
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "上一步", listener -> {
            finish();
        });
        //右侧添加按钮
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.add_bg, listener -> {
            //跳转到 ->新增页面
            if (resultBean == null) {
                return;
            }
            if (resultBean.getCloseFlag() == 1) {
                ToastUtil.showToast("运单已经关闭，无法编辑！");
                return;
            }
            Intent intentAdd = new Intent(SortingActivity.this, SortingAddActivity.class);
            intentAdd.putExtra("TYPE", TYPE_ADD);
            startActivityForResult(intentAdd, 1);
        });
        //初始化presenter
        mPresenter = new InWaybillRecordPresenter(this);
        //暂存，提交请求
        tempBtn.setOnClickListener(listener -> {
            if (mList == null || mList.size() == 0) {
                ToastUtil.showToast("请添加分拣信息");
                return;
            }
            submitEntity.setFlag(0);
            submitEntity.setList(mList);
            ((InWaybillRecordPresenter) mPresenter).submit(submitEntity);
        });
        //提交请求
        doneBtn.setOnClickListener(listener -> {
            if (mList == null || mList.size() == 0) {
                ToastUtil.showToast("请添加分拣信息");
                return;
            }
            submitEntity.setFlag(1);
            submitEntity.setList(mList);
            ((InWaybillRecordPresenter) mPresenter).submit(submitEntity);
        });
        //获取数据
        getData();

    }

    private void getData() {
        //初始化数据
        InWaybillRecordGetEntity entity = new InWaybillRecordGetEntity();
        entity.setFlightId(transportListBean.getFlightId());
        ((InWaybillRecordPresenter) mPresenter).getList(entity);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("dime", "onActivityResult：" + requestCode + "-" + requestCode);
        //来自新增页面的结果返回
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {//新增
            InWaybillRecord mInWaybillRecord = (InWaybillRecord) data.getSerializableExtra("DATA");
            mList.add(mInWaybillRecord);
            mAdapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {//修改
            InWaybillRecord inWaybillRecord = (InWaybillRecord) data.getSerializableExtra("DATA");
            int index = data.getIntExtra("INDEX", -1);
            if (index != -1) {
                mList.set(index, inWaybillRecord);
                mAdapter.notifyItemChanged(index);
            }
        }
    }

    @Override
    public void resultGetList(InWaybillRecordBean bean) {
        if (bean == null) {
            resultBean = new InWaybillRecordBean();
        } else {
            resultBean = bean;
        }
        if (resultBean.getList() == null) {
            mList = new ArrayList<>();
            resultBean.setList(mList);
        } else {
            mList = resultBean.getList();
            Log.e("dime", "服务器的数据，分拣信息长度 = " + mList.size());
        }
        //初始化提交实体类
        submitEntity.setFlightId(transportListBean.getFlightId());
        submitEntity.setFlightYLId(transportListBean.getFlightYLId());
        submitEntity.setFlightNum(transportListBean.getFlightNumber());
        submitEntity.setTaskId(transportListBean.getTaskId());
        submitEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        submitEntity.setUserName(UserInfoSingle.getInstance().getUsername());
        submitEntity.setList(mList);

        //列表 -- 初始化
        mAdapter = new SortingInfoAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {//item点击事件，进入修改
            if (resultBean.getCloseFlag() == 1) {
                ToastUtil.showToast("运单已经关闭，无法编辑！");
                return;
            }
            Intent intent = new Intent(SortingActivity.this, SortingAddActivity.class);
            intent.putExtra("TYPE", "UPDATE");
            InWaybillRecord updateInWaybillRecord = null;
            try {
                updateInWaybillRecord = Tools.IOclone(mList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dime", "updateInWaybillRecord clone error：" + e.getMessage());
            }
            intent.putExtra("DATA", updateInWaybillRecord);
            intent.putExtra("INDEX", position);
            SortingActivity.this.startActivityForResult(intent, 2);//去修改
        });
        //添加页脚
        TextView textView = new TextView(SortingActivity.this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#FF2E81FD"));
        textView.setTextSize(14);
        textView.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());

        mAdapter.setFooterView(textView);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnInWaybillRecordDeleteListener(position -> {
            //数据被删除了
            CURRENT_DELETE_POSITION = position;
            if (mList.get(CURRENT_DELETE_POSITION).getId() == null || mList.get(CURRENT_DELETE_POSITION).getId() == "" || mList.get(CURRENT_DELETE_POSITION).getId().equals("")) {
                mList.remove(CURRENT_DELETE_POSITION);
                mAdapter.notifyDataSetChanged();
            } else {
                ((InWaybillRecordPresenter) mPresenter).deleteById(mList.get(CURRENT_DELETE_POSITION).getId());
            }
        });
    }

    @Override
    public void resultSubmit(Object o) {
        Log.e("dime", "提交/暂存，返回值：" + o.toString());
        ToastUtil.showToast("成功");
        EventBus.getDefault().post("InPortTallyFragment_refresh");//发送消息让前一个页面刷新
        finish();
    }

    @Override
    public void resultDeleteById(Object o) {
        //删除成功，刷新数据
        Log.e("dime", "删除成功：" + o.toString());
        mList.remove(CURRENT_DELETE_POSITION);
        mAdapter.notifyDataSetChanged();
        CURRENT_DELETE_POSITION = -1;
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("dime", "错误信息：" + error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中.......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
