package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.presenter.ReservoirPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 进港分拣 新增
 * <p>
 * create by guohap - 2019/4/26
 */
public class SortingAddActivity extends BaseActivity implements ReservoirContract.reservoirView, UploadsContract.uploadsView {

    CustomToolbar customToolbar;

    SortingAddAdapter mAdapter;//适配器

    int CURRENT_PHOTO_INDEX;
    int CURRENT_EXCEPTION_INDEX;

    InWaybillRecord mInWaybillRecord;//本页面的数据,这是最终生成的数据哦， 很关键
    int INDEX;
    String TYPE = "";
    List<CounterUbnormalGoods> counterUbnormalGoodsList;//异常数组
    //是否转关 0 否 1 是
    int isTransit;//是否转关

    @BindView(R.id.edt_id)
    EditText idEdt;//运单号
    @BindView(R.id.edt_real_sort_num)
    EditText sortingNumEdt;//实际分拣数
    @BindView(R.id.tv_reservoir)
    TextView reservoirTv;//库区
    @BindView(R.id.tv_location)
    TextView locationTv;//库位
    @BindView(R.id.tv_is_transit)
    TextView isTransitTv;//转关
    @BindView(R.id.btn_submit)
    Button submitBtn;//提交
    @BindView(R.id.btn_add_item)
    Button addItemBtn;//新增异常

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting_add;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", listener->{
            finish();
        });
        //新增页面的逻辑， 是修改还是新增？ TYPE == ADD / UPDATE
        TYPE = getIntent().getStringExtra("TYPE");
        INDEX = getIntent().getIntExtra("INDEX", 0);

        if (TYPE.equals(SortingActivity.TYPE_ADD)) {
            Log.e("dime", "进入了addd");
            //如果是新增数据， 直接初始化
            customToolbar.setMainTitle(Color.WHITE, "新增");
            mInWaybillRecord = new InWaybillRecord();
            counterUbnormalGoodsList = new ArrayList<>();
        } else if (TYPE.equals(SortingActivity.TYPE_UPDATE)) {
            Log.e("dime", "进入了UPDATE");
            //如果是修改，数据从前一个页面来
            customToolbar.setMainTitle(Color.WHITE, "修改");
            mInWaybillRecord = (InWaybillRecord) getIntent().getSerializableExtra("DATA");
            Log.e("dime", "我进来了：\n" + mInWaybillRecord.toString());
            counterUbnormalGoodsList = mInWaybillRecord.getCounterUbnormalGoodsList();
            //显示运单号， 实际分拣数，库区，库位，是否转关
            idEdt.setText(mInWaybillRecord.getId() + "");
            sortingNumEdt.setText(mInWaybillRecord.getTotalNumberPackages()== null?"": mInWaybillRecord.getTotalNumberPackages()+"");
            reservoirTv.setText(mInWaybillRecord.getWarehouseArea());
            locationTv.setText(mInWaybillRecord.getWarehouseLocation());
            if(mInWaybillRecord.getTransit() != null){
                locationTv.setText(mInWaybillRecord.getTransit() == 0 ? "否" : "是");
            }
        } else {
            Log.e("dime", "不知道进入了哪里");
        }
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SortingAddActivity.this));
        mAdapter = new SortingAddAdapter(getSupportFragmentManager(), SortingAddActivity.this, counterUbnormalGoodsList);
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
                CURRENT_EXCEPTION_INDEX = posstion;
                ChooseStoreroomDialog chooseExcetionDialog = new ChooseStoreroomDialog();
                chooseExcetionDialog.setData(ExceptionUtils.testBeanList, SortingAddActivity.this);
                chooseExcetionDialog.setChooseDialogInterface(new ChooseDialogInterface() {
                    @Override
                    public void confirm(int position2) {
                        position2++;
                        if (counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType() == null) {
                            List<Integer> ubnormalType = new ArrayList<>(1);
                            ubnormalType.set(0, 1);
                            counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).setUbnormalType(ubnormalType);
                        } else if (counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType().size() == 0) {
                            counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType().add(0, position2);
                        } else {
                            counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType().set(0, position2);
                        }
                        mAdapter.notifyItemChanged(CURRENT_EXCEPTION_INDEX);
                    }
                });
                chooseExcetionDialog.show(getSupportFragmentManager(), "exception");

//                ChooseExceptionDialog chooseExceptionDialog = new ChooseExceptionDialog();
//                chooseExceptionDialog.setOnExceptionChooseListener(new ChooseExceptionDialog.OnExceptionChooseListener() {
//                    @Override
//                    public void onExceptionChoose(Integer[] exceptionTypes) {
//                        for (int typeItem : exceptionTypes) {
//                            if(!counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType().contains(typeItem)){
//                                counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).getUbnormalType().add(typeItem);
//                            }
//                        }
//                        Log.e("dime", "异常类型：" + counterUbnormalGoodsList.get(CURRENT_EXCEPTION_INDEX).toString());
//                    }
//                });
//                chooseExceptionDialog.show(getSupportFragmentManager(), "dime");
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
            if (TextUtils.isEmpty(idEdt.getText().toString().trim())) {
                mInWaybillRecord.setId("");
            } else {
                mInWaybillRecord.setId(idEdt.getText().toString().trim());
            }
            if (TextUtils.isEmpty(sortingNumEdt.getText().toString().trim())) {
                ToastUtil.showToast("请输入实际分拣数！");
                return;
            }
            mInWaybillRecord.setTotalNumberPackages(Integer.valueOf(sortingNumEdt.getText().toString().trim()));
            if (mInWaybillRecord.getWarehouseArea() == null || mInWaybillRecord.getWarehouseArea() == "") {
                ToastUtil.showToast("请选择库区");
                return;
            }
            mInWaybillRecord.setCounterUbnormalGoodsList(counterUbnormalGoodsList);
            Log.e("dime", "我要提交了：\n" + mInWaybillRecord.toString());
            Intent intent = new Intent(SortingAddActivity.this, SortingActivity.class);
            intent.putExtra("DATA", mInWaybillRecord);
            intent.putExtra("INDEX", INDEX);
            setResult(RESULT_OK, intent);
            finish();
        });

    }

    /**
     * x修改 逻辑
     */
    private void typeUpdate() {
        //将前一个页面传过来的数据渲染的页面上

    }

    /**
     * 选择库区方法
     */
    private void getReservoirAll() {
        mPresenter = new ReservoirPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setFilter("12");
        entity.setCurrent(1);
        entity.setSize(10);
        ((ReservoirPresenter) mPresenter).reservoir(entity);
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
        List<TestBean> mTestBeanList = new ArrayList<>();
        for (ReservoirBean.RecordsBean item : acceptTerminalTodoBeanList.getRecords()) {
            TestBean testBean = new TestBean(item.getReservoirType(), 0);
            testBean.setName(item.getReservoirName());
            mTestBeanList.add(testBean);
        }
        ChooseStoreroomDialog chooseStoreroomDialog = new ChooseStoreroomDialog();
        chooseStoreroomDialog.setData(mTestBeanList, SortingAddActivity.this);
        chooseStoreroomDialog.show(getSupportFragmentManager(), "guohao");
        chooseStoreroomDialog.setChooseDialogInterface(new ChooseDialogInterface() {
            @Override
            public void confirm(int position) {
                Log.e("dime", "选择了库区：" + position);
                String reservoir = acceptTerminalTodoBeanList.getRecords().get(position).getReservoirName();//库区已经选择
                reservoirTv.setText(acceptTerminalTodoBeanList.getRecords().get(position).getReservoirName());
                mInWaybillRecord.setWarehouseArea(reservoir);//库区的id
                mInWaybillRecord.setWarehouseLocation("库位未知");
            }
        });
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
                Log.e("dime", "相机选择Result" + photoList.toString());
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
        Log.e("dime", "添加前：当前的UbnormalPics\n" + counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic());
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic().add(entry.getKey());
        }
        Log.e("dime", "添加后：当前的UbnormalPics\n" + counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic().toString());
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();

    }
}
