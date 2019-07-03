package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SortingInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InWaybill;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.dialog.InputCodeDialog;
import qx.app.freight.qxappfreight.dialog.InputDialog;
import qx.app.freight.qxappfreight.presenter.InWaybillRecordPresenter;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 进港分拣 - 显示列表界面
 * <p>
 * create by guohao - 2019/4/26
 */
public class SortingActivity extends BaseActivity implements InWaybillRecordContract.inWaybillRecordView, ListReservoirInfoContract.listReservoirInfoView {

    String flightNo = "";
    String arriveTime = "";
    String handCarNum = "";
    String getHandCarNumTotal = "";

    //从上一个页面传递过来的数据
    TransportDataBase transportListBean;

    //需要显示的数据delFlag == 0
    List<InWaybillRecord> mList = new ArrayList<>();
    //删除的数据delFlag == 1
    List<InWaybillRecord> mListDel = new ArrayList<>();

    //最终提交请求的实体
    InWaybillRecordSubmitEntity submitEntity = new InWaybillRecordSubmitEntity();

    //网络请求返回的bean
    InWaybillRecordBean resultBean;

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

    //recycler 页脚
    TextView tvFoot;

    List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        transportListBean = (TransportDataBase) intent.getSerializableExtra("TASK_INFO");
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
            intentAdd.putExtra("FLIGHTNo", flightNo);
            Bundle bundle = new Bundle();
            if (mTestBeanList.size() > 0)
                bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
            else {
                ToastUtil.showToast("未获取到库区");
                return;
            }
            intentAdd.putExtras(bundle);
            startActivityForResult(intentAdd, 1);
//            showDialog();
        });
        //初始化presenter

        //暂存，提交请求
        tempBtn.setOnClickListener(listener -> {
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("提示")
                    .setMessage("确认暂存吗？")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消")
                    .isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener(new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
//                                ToastUtil.showToast("点击了左边的按钮");
                                if (mList == null || mList.size() == 0) {
                                    ToastUtil.showToast("请添加分拣信息");
                                    return;
                                }
                                List<InWaybillRecord> submitList = new ArrayList<>();
                                submitList.addAll(mList);
                                submitList.addAll(mListDel);
                                submitEntity.setFlag(0);
                                submitEntity.setList(mList);
                                ((InWaybillRecordPresenter) mPresenter).submit(submitEntity);
                            } else {
//                                ToastUtil.showToast("点击了右边的按钮");
                            }
                        }
                    })
                    .show();


        });
        //提交请求
        doneBtn.setOnClickListener(listener -> {
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("提示")
                    .setMessage("确认提交？")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消")
                    .isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener(new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
//                                ToastUtil.showToast("点击了左边的按钮");
                                if (mList == null || mList.size() == 0) {
                                    ToastUtil.showToast("请添加分拣信息");
                                    return;
                                }
                                List<InWaybillRecord> submitList = new ArrayList<>();
                                submitList.addAll(mList);
                                submitList.addAll(mListDel);
                                submitEntity.setFlag(1);
                                submitEntity.setList(mList);
                                ((InWaybillRecordPresenter) mPresenter).submit(submitEntity);
                            } else {
//                                ToastUtil.showToast("点击了右边的按钮");
                            }
                        }
                    })
                    .show();


        });
        //获取数据
        getData();

    }

    private void getData() {
        //初始化数据
        mPresenter = new InWaybillRecordPresenter(this);
        InWaybillRecordGetEntity entity = new InWaybillRecordGetEntity();
        entity.setTaskFlag(0);
        entity.setFlightInfoId(transportListBean.getFlightId());
        ((InWaybillRecordPresenter) mPresenter).getList(entity);
    }

    /**
     * 新增dialog
     */
    private void showDialog() {
        InputCodeDialog dialog1 = new InputCodeDialog(this);
        dialog1.setTitle("手动输入")
                .setPositiveButton("取消")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new InputCodeDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        } else {
                            if (TextUtils.isEmpty(dialog1.getMessage())){
                                ToastUtil.showToast("输入为空");
                            }else {
                                turnToAddActivity(dialog1.getMessage());
                            }

                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("dime", "onActivityResult：" + requestCode + "-" + requestCode);
        //来自新增页面的结果返回
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //新增
            InWaybillRecord mInWaybillRecord = (InWaybillRecord) data.getSerializableExtra("DATA");
            if (!TextUtils.isEmpty(mInWaybillRecord.getWaybillCode())){
                for (InWaybillRecord item:mList){
                    if (mInWaybillRecord.getWaybillCode().equals(item.getWaybillCode())){
                        ToastUtil.showToast("不能添加相同运单号");
                        return;
                    }
                }
            }

            mList.add(mInWaybillRecord);
            mAdapter.notifyDataSetChanged();
            //更新总运单数，总件数
            resultBean.setCount(resultBean.getCount() + 1);
            resultBean.setTotal(resultBean.getTotal() + mInWaybillRecord.getTallyingTotal());
            tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            //修改
            InWaybillRecord inWaybillRecord = (InWaybillRecord) data.getSerializableExtra("DATA");
            int index = data.getIntExtra("INDEX", -1);
            if (index != -1) {
                //更新总运单数，总件数
                resultBean.setTotal(resultBean.getTotal() - mList.get(index).getTallyingTotal() + inWaybillRecord.getTallyingTotal());
                tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
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
            resultBean.setList(mList);
        } else {
            for (InWaybillRecord item : resultBean.getList()) {
                if(item.getDelFlag() == null || item.getDelFlag() == 0){
                    mList.add(item);
                }else if(item.getDelFlag() == 1){
                    mListDel.add(item);
                }
            }
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

        //item点击事件，进入修改
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
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
            Bundle bundle = new Bundle();
            if (mTestBeanList.size() > 0)
                bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
            else {
                ToastUtil.showToast("未获取到库区");
                return;
            }
            intent.putExtras(bundle);
            //去修改
            SortingActivity.this.startActivityForResult(intent, 2);
        });
        //添加页脚
        tvFoot = new TextView(SortingActivity.this);
        tvFoot.setGravity(Gravity.CENTER);
        tvFoot.setTextColor(Color.parseColor("#FF2E81FD"));
        tvFoot.setTextSize(14);
        tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());

        mAdapter.setFooterView(tvFoot);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnInWaybillRecordDeleteListener(position -> {
            //数据被删除了
            CURRENT_DELETE_POSITION = position;
            //更新总运单数，总件数
            resultBean.setCount(resultBean.getCount()-1);
            resultBean.setTotal(resultBean.getTotal() - mList.get(CURRENT_DELETE_POSITION).getTallyingTotal());
            tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
            //操作数据源
            if (mList.get(CURRENT_DELETE_POSITION).getId() == null || mList.get(CURRENT_DELETE_POSITION).getId() == "" || mList.get(CURRENT_DELETE_POSITION).getId().equals("")) {
                mList.remove(CURRENT_DELETE_POSITION);

            } else {
                //将数据放到删除列表里，delFlag = 1
                mList.get(position).setDelFlag(1);
                mListDel.add(mList.get(position));
                mList.remove(position);

                /*
                不再调用删除接口
                ((InWaybillRecordPresenter) mPresenter).deleteById(mList.get(CURRENT_DELETE_POSITION).getId());
                */
            }
            //刷新列表
            mAdapter.notifyDataSetChanged();
            recyclerView.closeMenu();
        });
        recyclerView.closeMenu();
        getReservoirAll();
    }
    /**
     * 选择库区方法
     */
    private void getReservoirAll() {
//        mPresenter = new ReservoirPresenter(this);
//        BaseFilterEntity entity = new BaseFilterEntity();
////        entity.setFilter("12");
//        entity.setCurrent(1);
//        entity.setSize(10);
//        ((ReservoirPresenter) mPresenter).reservoir(entity);

        mPresenter = new ListReservoirInfoPresenter(this);
        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode(UserInfoSingle.getInstance().getDeptCode());
//        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode("wf_put_in");
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

    /**
     * 激光扫码回调
     */
    private String newCode;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("SortingActivity")){
            newCode = "";
            String code = result.getData();
            Log.e("22222", "运单号" + code);
            if (!TextUtils.isEmpty(code)&&code.length()>=10) {
                if (code.startsWith("DN")){
                    newCode = "DN-"+code.substring(2,10);
                }else {
                    if (code.length()>=11){
                        newCode =editChange(code);
                    }else {
                        ToastUtil.showToast("无效的运单号");
                        return;
                    }
                }
            }else {
                ToastUtil.showToast("无效的运单号");
                return;
            }
            turnToAddActivity(newCode);
        }
    }

    /**
     * 跳转到新增界面
     */
    private void turnToAddActivity(String code){

        recyclerView.closeMenu();
        boolean isEditChange;
        String[] parts = code.split("-");
        String ss = parts[1];
        String s1=""; //后8位的前7位
        String s2=""; //后8位的最后1位
        s1 = ss.substring(0,7);
        s2 = ss.substring(7,8);
        if (StringUtil.isDouble(s1)&&StringUtil.isDouble(s2)){
            isEditChange = Integer.parseInt(s1)%7 == Integer.parseInt(s2);
        }else {
            isEditChange=false;
        }

        if (!isEditChange){
            ToastUtil.showToast("无效的运单号");
            return;
        }
        //跳转到 ->新增页面
        if (resultBean == null) {
            ToastUtil.showToast("运单信息出错，请重新请求");
            return;
        }
        if (resultBean.getCloseFlag() == 1) {
            ToastUtil.showToast("运单已经关闭，无法编辑！");
            return;
        }
        Intent intentAdd = new Intent(SortingActivity.this, SortingAddActivity.class);
        intentAdd.putExtra("TYPE", TYPE_ADD);
        intentAdd.putExtra("newCode", code);
        Bundle bundle = new Bundle();
        if (mTestBeanList.size() > 0)
            bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
        else {
            ToastUtil.showToast("未获取到库区");
            return;
        }
        intentAdd.putExtras(bundle);
        startActivityForResult(intentAdd, 1);
    }

    //判断运单号后缀是否符合规则
    private String  editChange(String ss) {
        String s0=ss.substring(0, 3); //前3位
        String s00=ss.substring(3, 11); //后8位
        return s0+"-"+s00;
    }

    @Override
    public void listReservoirInfoResult(List<ReservoirArea> acceptTerminalTodoBeanList) {
        Log.e("dime", "库区信息\n" + acceptTerminalTodoBeanList.toString());
        //显示库区选择面板
        for (ReservoirArea item : acceptTerminalTodoBeanList) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            mTestBeanList.add(testBean);
        }

    }
}
