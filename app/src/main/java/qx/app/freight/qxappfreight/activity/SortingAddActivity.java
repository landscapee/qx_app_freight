package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SortingAddAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.dialog.ErrorTypeChooseDialog;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialog;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.presenter.ReservoirPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 进港分拣 新增
 * <p>
 * create by guohap - 2019/4/26
 */
public class SortingAddActivity extends BaseActivity implements ReservoirContract.reservoirView, UploadsContract.uploadsView, ListReservoirInfoContract.listReservoirInfoView {

    CustomToolbar customToolbar;

    SortingAddAdapter mAdapter;//适配器

    int CURRENT_PHOTO_INDEX;

    InWaybillRecord mInWaybillRecord;//本页面的数据,这是最终生成的数据哦， 很关键
    int INDEX;
    String TYPE = "";
    String newCode; //传过来的运单号
    String flightNum; //传过来的航班号
    List<CounterUbnormalGoods> counterUbnormalGoodsList;//异常数组
    //是否转关 0 否 1 是
    int isTransit;//是否转关

    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表

    @BindView(R.id.edt_id)
    EditText idEdt;//运单号
    @BindView(R.id.edt_id_1)
    EditText idEdt2;//运单号
    @BindView(R.id.edt_real_sort_num)
    EditText sortingNumEdt;//实际分拣数
    @BindView(R.id.tv_reservoir)
    TextView reservoirTv;//库区
    @BindView(R.id.tv_location)
    TextView locationTv;//库位
    @BindView(R.id.et_location)
    EditText locationEt;//库位  暂修改为 输入框

    @BindView(R.id.tv_is_transit)
    TextView isTransitTv;//转关
    @BindView(R.id.btn_submit)
    Button submitBtn;//提交
    @BindView(R.id.btn_add_item)
    Button addItemBtn;//新增异常
    @BindView(R.id.tv_remark)
    EditText remarkEdt;//备注
    @BindView(R.id.tv_overweight)
    TextView tvOverweight;//备注

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting_add;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", listener -> {
            finish();
        });
        //新增页面的逻辑， 是修改还是新增？ TYPE == ADD / UPDATE
        TYPE = getIntent().getStringExtra("TYPE");

        INDEX = getIntent().getIntExtra("INDEX", 0);

        if (TYPE.equals(SortingActivity.TYPE_ADD)) {
            Log.e("dime", "进入了addd");
            //如果是新增数据， 直接初始化
            newCode = getIntent().getStringExtra("newCode");
            flightNum = getIntent().getStringExtra("FLIGHTNo");

            //根据航班号查运单前缀
            if (!TextUtils.isEmpty(newCode)) {
                isEditChange =true;
                String[] parts = newCode.split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            //根据航班号航司码搜索运单号前三位
            if (!TextUtils.isEmpty(flightNum)){
                 mPresenter = new ReservoirPresenter(this);
                ((ReservoirPresenter)mPresenter).getAirWaybillPrefix(flightNum.substring(0,2));
            }
            customToolbar.setMainTitle(Color.WHITE, "新增");
            mInWaybillRecord = new InWaybillRecord();
            mInWaybillRecord.setDelFlag(0);
            rcInfoOverweight = new ArrayList <>();
            counterUbnormalGoodsList = new ArrayList<>();
        } else if (TYPE.equals(SortingActivity.TYPE_UPDATE)) {
            Log.e("dime", "进入了UPDATE");
            //如果是修改，数据从前一个页面来
            customToolbar.setMainTitle(Color.WHITE, "修改");
            mInWaybillRecord = (InWaybillRecord) getIntent().getSerializableExtra("DATA");
            counterUbnormalGoodsList = mInWaybillRecord.getCounterUbnormalGoodsList();

            //初始化超重重量
            rcInfoOverweight = mInWaybillRecord.getOverWeightList();
            int overweight = 0;
            for (RcInfoOverweight mRcInfoOverweight:rcInfoOverweight){
                overweight += mRcInfoOverweight.getOverWeight();
            }
            tvOverweight.setText(overweight+"kg");

            //显示运单号， 实际分拣数，库区，库位，是否转关，备注
            if (!TextUtils.isEmpty(mInWaybillRecord.getWaybillCode())){
                isEditChange =true;
                String[] parts = mInWaybillRecord.getWaybillCode().split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }

            sortingNumEdt.setText(mInWaybillRecord.getTallyingTotal() == null ? "" : mInWaybillRecord.getTallyingTotal() + "");
            reservoirTv.setText(mInWaybillRecord.getWarehouseArea());
            locationTv.setText(mInWaybillRecord.getWarehouseLocation());
            if (mInWaybillRecord.getTransit() != null) {
                locationTv.setText(mInWaybillRecord.getTransit() == 0 ? "否" : "是");
            }
            remarkEdt.setText("" + mInWaybillRecord.getRemark() == null?"":mInWaybillRecord.getRemark());
        } else {
            Log.e("dime", "不知道进入了哪里");
        }
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SortingAddActivity.this));
        mAdapter = new SortingAddAdapter(SortingAddActivity.this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(new SortingAddAdapter.OnSlectPicListener() {
            @Override
            public void onSelectPic(int position) {
                Log.e("dime", "选择图片：" + position);
                CURRENT_PHOTO_INDEX = position;
                //将数组转为List
                List<String> originList = new ArrayList<>();
                if (counterUbnormalGoodsList.get(position).getLocalPath() != null) {
                    for (String url : counterUbnormalGoodsList.get(position).getLocalPath()) {
                        originList.add(url);
                    }
                }
                MultiImageSelector.create(SortingAddActivity.this)
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .multi() // 多选模式, 默认模式;
                        .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(SortingAddActivity.this, 1002);
            }
        });
        //设置异常类型
        mAdapter.setOnExceptionTypeListener(new SortingAddAdapter.OnExceptionChooseListener() {
            @Override
            public void onExceptionChoose(int posstion) {
                Log.e("dime", "位置信息：" + posstion);
                ErrorTypeChooseDialog chooseExcetionDialog = new ErrorTypeChooseDialog();
                chooseExcetionDialog.setData(ExceptionUtils.testBeanList, SortingAddActivity.this);
                chooseExcetionDialog.setChooseDialogInterface(new ChooseDialogInterface() {
                    @Override
                    public void confirm(int position2) {
                        int[] intTypes = {2, 4, 10, 19, 16};
                        Log.e("dime", "异常：type=" + position2);
                        if (counterUbnormalGoodsList.get(posstion).getUbnormalType() == null) {
                            List<Integer> ubnormalType = new ArrayList<>(1);
                            ubnormalType.set(0, intTypes[position2]);
                            counterUbnormalGoodsList.get(posstion).setUbnormalType(ubnormalType);
                        } else if (counterUbnormalGoodsList.get(posstion).getUbnormalType().size() == 0) {
                            counterUbnormalGoodsList.get(posstion).getUbnormalType().add(0, intTypes[position2]);
                        } else {
                            counterUbnormalGoodsList.get(posstion).getUbnormalType().set(0, intTypes[position2]);
                        }
                        Log.e("dime", "组装好的数据呢：" + counterUbnormalGoodsList.get(posstion).getUbnormalType().toString());
                        Log.e("dime", "组装好的数据呢：位置信息：" + posstion);
                        mAdapter.notifyItemChanged(posstion);
                    }
                });
                chooseExcetionDialog.show(getSupportFragmentManager(), "exception");
            }
        });

        //库区按钮 点击事件
        reservoirTv.setOnClickListener(listener -> {
            getReservoirAll();
        });

        //库位按钮 点击事件
        locationTv.setOnClickListener(listener -> {
            ToastUtil.showToast("库位操作暂不支持， 统一为0");
        });

        //是否转关 点击事件
        isTransitTv.setOnClickListener(listener -> {
            if (isTransitTv.getText().toString() == "是") {
                isTransitTv.setText("否");
                mInWaybillRecord.setTransit(0);
            } else {
                isTransitTv.setText("是");
                mInWaybillRecord.setTransit(1);
            }
        });
        //新增 异常数据
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CounterUbnormalGoods newExceptionItem = new CounterUbnormalGoods();
                counterUbnormalGoodsList.add(newExceptionItem);
                mAdapter.notifyDataSetChanged();
            }
        });
        //提交按钮 点击事件
        submitBtn.setOnClickListener(listen -> {
            //将组装好的数据返回给前一个页面
            if (TextUtils.isEmpty(idEdt.getText().toString().trim())&&TextUtils.isEmpty(idEdt2.getText().toString().trim())) {
                mInWaybillRecord.setWaybillCode("");
            } else {
                if (!TextUtils.isEmpty(idEdt.getText().toString().trim())&&isEditChange){
                    mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim()+"-"+idEdt2.getText().toString().trim());
                }else {
                    ToastUtil.showToast("请输入正确的运单号");
                    return;
                }

            }
            mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim()+"-"+idEdt2.getText().toString().trim());
            if (TextUtils.isEmpty(sortingNumEdt.getText().toString().trim())) {
                ToastUtil.showToast("请输入实际分拣数！");
                return;
            }
            if (TextUtils.isEmpty(remarkEdt.getText().toString().trim())) {
                mInWaybillRecord.setRemark("");
            } else {
                mInWaybillRecord.setRemark(remarkEdt.getText().toString().trim());
            }
            mInWaybillRecord.setTallyingTotal(Integer.valueOf(sortingNumEdt.getText().toString().trim()));
            if (mInWaybillRecord.getWarehouseArea() == null || mInWaybillRecord.getWarehouseArea() == "") {
                ToastUtil.showToast("请选择库区");
                return;
            }
            if (!StringUtil.isEmpty(locationEt.getText().toString()))
                mInWaybillRecord.setWarehouseLocation(locationEt.getText().toString());

            mInWaybillRecord.setCounterUbnormalGoodsList(counterUbnormalGoodsList);
            mInWaybillRecord.setOverWeightList(rcInfoOverweight);
            Intent intent = new Intent(SortingAddActivity.this, SortingActivity.class);
            intent.putExtra("DATA", mInWaybillRecord);
            intent.putExtra("INDEX", INDEX);
            setResult(RESULT_OK, intent);
            finish();
        });

        //运单号后缀事件
        idEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("2222222", "afterTextChanged: "+s.toString().trim());
                editChange(s.toString().trim());
            }

        });
        //超重情况
        tvOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
    }

    //判断运单号后缀是否符合规则
    private boolean isEditChange=false;
    private void editChange(String ss) {
        if (!TextUtils.isEmpty(ss)&&ss.length() ==8){
            String s1 = ss.substring(0,7);
            String s2 = ss.substring(7,8);
            isEditChange = Integer.parseInt(s1)%7 == Integer.parseInt(s2);
        }else {
            isEditChange =false;
        }
        if (isEditChange){
            idEdt2.setTextColor(Color.parseColor("#888888"));
        }else {
            idEdt2.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    //超重情况的弹窗
    private void showPopWindowList() {
        SortingReturnGoodsDialog dialog = new SortingReturnGoodsDialog(this);
        dialog.setData(rcInfoOverweight)
                .setOnClickListener(new SortingReturnGoodsDialog.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        tvOverweight.setText(text);
                    }
                })
                .show();

    }

    /**
     * x修改 逻辑
     */
    private void typeUpdate() {
        //将前一个页面传过来的数据渲染的页面上

    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result){
        if (result.getFunctionFlag().equals("SortingAddActivity")){
            String code = result.getData();
            Log.e("22222", "运单号" + code);
            if (!TextUtils.isEmpty(code)&&code.length()>=10) {
                if (code.startsWith("DN")){
                    newCode = "DN-"+code.substring(2,10);
                }else {
                    if (code.length()>=11){
                        String s0=code.substring(0, 3); //前3位
                        String s00=code.substring(3, 11); //后8位
                        newCode = s0+"-"+s00;
                    }else {
                        ToastUtil.showToast("无效的运单号");
                        return;
                    }
                }
            }else {
                ToastUtil.showToast("无效的运单号");
                return;
            }

            checkCode(newCode);

        }
    }

    /**检查运单号是否符合规则，并填入控件
     *
     * @param code
     */
    private void checkCode(String code) {
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

        //向控件里面填入数据
        if (!TextUtils.isEmpty(code)) {
            String[] parts2 = code.split("-");
            idEdt.setText(parts2[0]);
            idEdt2.setText(parts2[1]);
        }

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

    /**
     * 提交方法
     */
    private void submit() {

    }

    @Override
    public void reservoirResult(ReservoirBean acceptTerminalTodoBeanList) {
        Log.e("dime", "库区信息\n" + acceptTerminalTodoBeanList.toString());
        //显示库区选择面板
        List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();
        for (ReservoirBean.RecordsBean item : acceptTerminalTodoBeanList.getRecords()) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            mTestBeanList.add(testBean);
        }
        ChooseStoreroomDialog2 chooseStoreroomDialog = new ChooseStoreroomDialog2();
        chooseStoreroomDialog.setData(mTestBeanList, SortingAddActivity.this);
        chooseStoreroomDialog.show(getSupportFragmentManager(), "guohao");
        chooseStoreroomDialog.setChooseDialogInterface(new ChooseDialogInterface() {
            @Override
            public void confirm(int position) {
                Log.e("dime", "选择了库区：" + position);
                reservoirTv.setText(acceptTerminalTodoBeanList.getRecords().get(position).getReservoirName());
                //设置库区的id
                mInWaybillRecord.setWarehouseArea(acceptTerminalTodoBeanList.getRecords().get(position).getReservoirName());
                mInWaybillRecord.setWarehouseAreaDisplay(acceptTerminalTodoBeanList.getRecords().get(position).getReservoirName());
                //设置库区type
                mInWaybillRecord.setWarehouseAreaType(acceptTerminalTodoBeanList.getRecords().get(position).getReservoirType());
                mInWaybillRecord.setWarehouseLocation("");
            }
        });
    }

    @Override
    public void getAirWaybillPrefixResult(String getAirWaybillPrefixBean) {
        idEdt.setText(getAirWaybillPrefixBean);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1002) {//异常上报相机事件
                List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//选择好的图片
                counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).setLocalPath(photoList);
                //开始上传图片
                mPresenter = new UploadsPresenter(this);
                List<File> files = new ArrayList<>();
                for (String str : photoList) {
                    files.add(new File(str));
                }
                Luban.get(this).load(files)
                        .setMaxSize(150)
                        .setMaxHeight(1920)
                        .setMaxWidth(1080)
                        .putGear(Luban.CUSTOM_GEAR).launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {
                        showProgessDialog("正在上传，请稍候。。。。。。");
                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                        mPresenter = new UploadsPresenter(SortingAddActivity.this);
                        ((UploadsPresenter) mPresenter).uploads(upFiles);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        }
    }

    @Override
    public void uploadsResult(Object result) {
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic().add(entry.getKey());
        }
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();

    }

    @Override
    public void listReservoirInfoResult(List<ReservoirArea> acceptTerminalTodoBeanList) {
        Log.e("dime", "库区信息\n" + acceptTerminalTodoBeanList.toString());
        //显示库区选择面板
        List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();
        for (ReservoirArea item : acceptTerminalTodoBeanList) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            mTestBeanList.add(testBean);
        }
        ChooseStoreroomDialog2 chooseStoreroomDialog = new ChooseStoreroomDialog2();
        chooseStoreroomDialog.setData(mTestBeanList, SortingAddActivity.this);
        chooseStoreroomDialog.show(getSupportFragmentManager(), "guohao");
        chooseStoreroomDialog.setChooseDialogInterface(new ChooseDialogInterface() {
            @Override
            public void confirm(int position) {
                Log.e("dime", "选择了库区：" + position);
                String reservoir = acceptTerminalTodoBeanList.get(position).getReservoirName();//库区已经选择
                reservoirTv.setText(acceptTerminalTodoBeanList.get(position).getReservoirName());
                mInWaybillRecord.setWarehouseArea(reservoir);//库区的id
                mInWaybillRecord.setWarehouseAreaDisplay(acceptTerminalTodoBeanList.get(position).getReservoirName());
                mInWaybillRecord.setWarehouseLocation("");
            }
        });
    }
}
