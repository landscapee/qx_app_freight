package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

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
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

public class SortingActivity extends BaseActivity implements InWaybillRecordContract.inWaybillRecordView {

    String flightNo = "";
    String arriveTime = "";
    String handCarNum = "";
    String getHandCarNumTotal = "";
    TransportListBean transportListBean;
    List<InWaybillRecord> mList;
    InWaybillRecord deleteInWaybillRecord;
    InWaybillRecordSubmitEntity submitEntity = new InWaybillRecordSubmitEntity();//最终提交请求的实体

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
        transportListBean = (TransportListBean) intent.getSerializableExtra("TASK_INFO");
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
        CustomToolbar customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setMainTitle(R.color.white, "进港理货");
        customToolbar.setLeftTextView(View.VISIBLE, R.color.white, "上一步", null);
        //右侧添加按钮
        customToolbar.setRightIconView(View.VISIBLE, R.mipmap.add_bg, listener -> {
            //跳转到 ->新增页面
            Intent intentAdd = new Intent(SortingActivity.this, SortingAddActivity.class);
            intentAdd.putExtra("TYPE", "ADD");
            startActivityForResult(intentAdd, 1);
        });
        //初始化presenter
        mPresenter = new InWaybillRecordPresenter(this);
        //暂存，提交请求
        tempBtn.setOnClickListener(listener->{
            submitEntity.setFlag(0);
            submitEntity.setList(mList);
            ((InWaybillRecordPresenter)mPresenter).submit(submitEntity);
        });
        //提交请求
        doneBtn.setOnClickListener(listener->{
            submitEntity.setFlag(1);
            String xx = new Gson().toJson(submitEntity);
            Log.e("dime - json", xx);
            ((InWaybillRecordPresenter)mPresenter).submit(submitEntity);
        });
        //获取数据
        getData();

    }

    private void getData(){
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
        }
    }

    @Override
    public void resultGetList(InWaybillRecordBean bean) {
        if(bean.getList() == null){
            Log.e("dime", "没有item数据");
            mList = new ArrayList<>();
        }else{
            Log.e("dime", "有数据："+bean.getList().size());
            mList = bean.getList();
        }
        //初始化提交实体类
        submitEntity.setFlightId(transportListBean.getFlightId());
        submitEntity.setFlightYLId("");
        submitEntity.setFlightNum(transportListBean.getFlightNumber());
        submitEntity.setTaskId(transportListBean.getTaskId());
        submitEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        submitEntity.setUserName(UserInfoSingle.getInstance().getUsername());
        submitEntity.setList(mList);
        //列表 -- 初始化
        mAdapter = new SortingInfoAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnInWaybillRecordDeleteListener(new SortingInfoAdapter.OnInWaybillRecordDeleteListener() {
            @Override
            public void onDeleteListener(InWaybillRecord inWaybillRecord) {
                //数据被删除了
                deleteInWaybillRecord = inWaybillRecord;
                ((InWaybillRecordPresenter)mPresenter).deleteById(inWaybillRecord.getId());
            }
        });
    }

    @Override
    public void resultSubmit(Object o) {
        Log.e("dime", "提交/暂存，item长度：" + submitEntity.getList().size());
        Log.e("dime", "提交/暂存，返回值：" + o.toString());
        ToastUtil.showToast("成功");
    }

    @Override
    public void resultDeleteById(Object o) {
        //删除成功，刷新数据
        mList.remove(deleteInWaybillRecord);
        mAdapter.notifyDataSetChanged();
        deleteInWaybillRecord = null;
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("dime", "错误信息："+error);
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
