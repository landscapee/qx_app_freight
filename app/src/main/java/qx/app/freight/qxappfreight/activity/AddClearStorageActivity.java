package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.dialog.ChooseStorehouseDialog;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.presenter.ReservoirPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 清库-新增
 * Created by swd
 */
public class AddClearStorageActivity extends BaseActivity implements ReservoirContract.reservoirView{

    @BindView(R.id.et_way_bill)
    EditText etWayBill;
    @BindView(R.id.tv_store_type)
    TextView tvStoreType;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_number)
    TextView tvStoreNumber;
    @BindView(R.id.tv_store_cold)
    TextView tvStoreCold;
    @BindView(R.id.btn_commit)
    Button btnCommit;


    private List<String> storageTypeList; //货邮类型   货物，邮件
    private List<String> coldTypeList; //是否冷藏   是否冷藏
    private List<TestBean> kuQuList; //库区
    private List<TestBean> kuWeiList; //库位

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_clear_storage;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initTitle();
        initData();
    }

    private void initTitle() {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "新增");
    }

    private void initData() {
        storageTypeList = new ArrayList<>();
        storageTypeList.add("货物");
        storageTypeList.add("邮件");

        coldTypeList = new ArrayList<>();
        coldTypeList.add("冷藏");
        coldTypeList.add("不冷藏");

        kuQuList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            kuQuList.add(new TestBean("库房" + i, false));
        }

        kuWeiList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            if (i == 20) {
                kuWeiList.add(new TestBean(1, i));
            } else if (i == 25) {
                kuWeiList.add(new TestBean(1, i));
            } else {
                kuWeiList.add(new TestBean(0, i));
            }
        }
        getReservoirAll();
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
     *点击事件
     */
    @OnClick({R.id.tv_store_type, R.id.tv_store_name, R.id.tv_store_number, R.id.tv_store_cold, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //货邮类型
            case R.id.tv_store_type:
                showStorageTypePickView();
                break;
            //库区
            case R.id.tv_store_name:
                chooseStoreroom();
                break;
            //库位
            case R.id.tv_store_number:
                chooseStorehouse();
                break;
            //是否冷藏
            case R.id.tv_store_cold:
                showColdTypePickView();
                break;
            //提交
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    /**
     * 货邮方式选择器
     */
    private void showStorageTypePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvStoreType.setText(storageTypeList.get(options1));
            }
        }).build();
        pickerView.setPicker(storageTypeList);
        pickerView.setTitleText("货邮");
        pickerView.show();
    }

    /**
     * 是否冷藏选择器
     */
    private void showColdTypePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvStoreCold.setText(coldTypeList.get(options1));
            }
        }).build();
        pickerView.setPicker(coldTypeList);
        pickerView.setTitleText("冷藏");
        pickerView.show();
    }

    /**
     * 选择库区
     */
    private void chooseStoreroom(){
        if (kuQuList==null||kuQuList.size()==0){
            ToastUtil.showToast("获取库区数据失败，请退出重试");
            return;
        }

        ChooseStoreroomDialog dialog = new ChooseStoreroomDialog();
        dialog.setChooseDialogInterface(new ChooseDialogInterface() {
            @Override
            public void confirm(int position) {
                Log.e("dime", "选择了库区：" + position);
                tvStoreName.setText(kuQuList.get(position).getName());
            }
        });
        dialog.setData(kuQuList, this);
        dialog.show(getSupportFragmentManager(), "123");
    }

    /**
     * 选择库位
     */
    private void chooseStorehouse(){
        ChooseStorehouseDialog dialog = new ChooseStorehouseDialog();
        dialog.setChooseDialogInterface(position -> tvStoreNumber.setText(kuWeiList.get(position).getNumber() + "号库位"));
        dialog.setData(kuWeiList, this);
        dialog.show(getSupportFragmentManager(), "123");
    }

    private void commit(){
        ToastUtil.showToast("点击了提交按钮");
    }

    @Override
    public void reservoirResult(ReservoirBean acceptTerminalTodoBeanList) {
        Log.e("dime", "库区信息\n" + acceptTerminalTodoBeanList.toString());
        //显示库区选择面板
        kuQuList = new ArrayList<>();
        for (ReservoirBean.RecordsBean item : acceptTerminalTodoBeanList.getRecords()) {
            TestBean testBean = new TestBean(item.getReservoirType(), 0);
            testBean.setName(item.getReservoirName());
            kuQuList.add(testBean);
        }
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
