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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InventoryInfoAdapter;
import qx.app.freight.qxappfreight.adapter.SortingAddAdapter2;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.WayBillQueryBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryUbnormalGoods;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.contract.ListInventoryDetailContract;
import qx.app.freight.qxappfreight.dialog.ErrorTypeChooseDialog;
import qx.app.freight.qxappfreight.dialog.ExceptionDetailDialog;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.presenter.AddInventoryDetailPresenter;
import qx.app.freight.qxappfreight.presenter.ListInventoryDetailPresenter;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;


/**
 * ??????-??????
 * Created by swd
 */
public class AddClearStorageActivity extends BaseActivity implements AddInventoryDetailContract.addInventoryDetailView, ListInventoryDetailContract.listInventoryDetailView {
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.edt_real_sort_num)
    EditText edtRealSortNum;
    @BindView(R.id.btn_add_item)
    Button btnAddItem;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_info_commit)
    Button btnInfoCommit;
    @BindView(R.id.recycler_view_1)
    RecyclerView recyclerView1;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_clear)
    Button btnClear;

    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    SortingAddAdapter2 mAdapter;//?????????????????????????????????
    InventoryInfoAdapter infoAdapter; //???????????????????????????
    ArrayList <InventoryUbnormalGoods> counterUbnormalGoodsList;//????????????
    List <InventoryDetailEntity> inventoryDetailEntityList;//?????????????????????

    int CURRENT_PHOTO_INDEX;

    private String taskId;  //??????id
    private String taskTitle; //??????
    private String waybillId;//?????????id

    private boolean isFinish;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_clear_storage;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        taskId = getIntent().getStringExtra("taskId");
        taskTitle = getIntent().getStringExtra("taskTitle");
        initTitle();
        initData();
    }

    private void initTitle() {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, taskTitle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WayBillQueryBean data) {
        tvId.setText(data.getWayBillCode());
        waybillId = data.getId();
//        if (waybillId != null)
//            getWayBillInfo();
//        else {
//            ToastUtil.showToast("??????????????????");
//            tvNum.setText("??????: - -");
//            tvWeight.setText("??????: - -");
//            tvStatus.setText("??????: - -");
//        }
    }

    private void getWayBillInfo() {
        mPresenter = new AddInventoryDetailPresenter(this);
        if (!StringUtil.isEmpty(waybillId))
            ((AddInventoryDetailPresenter) mPresenter).getWaybillInfoByWaybillCode(waybillId);
    }

    private void initData() {
        tvId.setOnClickListener(v -> {
            startActivity(new Intent(this, WayBillQueryActivity.class).putExtra("taskId", taskId));
        });
        counterUbnormalGoodsList = new ArrayList <>();
        inventoryDetailEntityList = new ArrayList <>();
        //?????????recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SortingAddAdapter2(this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(new SortingAddAdapter2.OnSlectPicListener() {
            @Override
            public void onSelectPic(int position) {
                Log.e("dime", "???????????????" + position);
                CURRENT_PHOTO_INDEX = position;
                //???????????????List
                List <String> originList = new ArrayList <>();
                if (counterUbnormalGoodsList.get(position).getUploadFilePath() != null) {
                    for (String url : counterUbnormalGoodsList.get(position).getUploadFilePath()) {
                        originList.add(url);
                    }
                }
                MultiImageSelector.create(AddClearStorageActivity.this)
                        .showCamera(true) // ??????????????????. ???????????????
                        .count(9) // ????????????????????????, ?????????9. ???????????????????????????????????????
                        .multi() // ????????????, ????????????;
                        .origin((ArrayList <String>) originList) // ?????????????????????. ???????????????????????????????????????
                        .start(AddClearStorageActivity.this, 1002);
            }
        });
        //??????????????????
        mAdapter.setOnExceptionTypeListener(new SortingAddAdapter2.OnExceptionChooseListener() {
            @Override
            public void onExceptionChoose(int posstion) {
                ErrorTypeChooseDialog chooseExcetionDialog = new ErrorTypeChooseDialog();
                chooseExcetionDialog.setData(ExceptionUtils.testBeanList, AddClearStorageActivity.this);
                chooseExcetionDialog.setChooseDialogInterface(new ChooseDialogInterface() {
                    @Override
                    public void confirm(int position2) {
//                        int[] intTypes = {2, 4, 10, 19, 16};
                        counterUbnormalGoodsList.get(posstion).setUbnormalType(ExceptionUtils.testBeanList.get(position2).getType());
                        mAdapter.notifyDataSetChanged();
                    }
                });
                chooseExcetionDialog.show(getSupportFragmentManager(), "exception");
            }
        });
        //???????????????????????????
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        infoAdapter = new InventoryInfoAdapter(inventoryDetailEntityList, 1);
        infoAdapter.setInventoryInfoListener(new InventoryInfoAdapter.InventoryInfoListener() {
            @Override
            public void onLook(int position) {
                ExceptionDetailDialog detailDialog = new ExceptionDetailDialog.Builder()
                        .inventoryDetailEntity(inventoryDetailEntityList
                                .get(position)).context(AddClearStorageActivity.this).build();
                detailDialog.show(getSupportFragmentManager(), "222");
            }

            @Override
            public void onDelete(int position) {
                inventoryDetailEntityList.remove(position);
                infoAdapter.notifyDataSetChanged();
            }
        });
        recyclerView1.setAdapter(infoAdapter);

        loadData();

        setIsBack(true, () -> submitBack());
    }

    private void submitBack() {
        if (inventoryDetailEntityList.size() == 0) {
            finish();
            return;
        }
        isFinish = true;
        mPresenter = new AddInventoryDetailPresenter(this);
        ((AddInventoryDetailPresenter) mPresenter).addInventoryDetail(inventoryDetailEntityList);

    }


    private void loadData() {
        mPresenter = new ListInventoryDetailPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        baseFilterEntity.setInventoryTaskId(taskId);
        baseFilterEntity.setHandler(UserInfoSingle.getInstance().getUserId());
        ((ListInventoryDetailPresenter) mPresenter).listInventoryDetail(baseFilterEntity);
    }

    @OnClick({R.id.btn_add_item, R.id.btn_info_commit, R.id.btn_commit, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_item:
                addItem();
                break;
            case R.id.btn_info_commit:
                commitInfo();
                break;
            case R.id.btn_commit:
                //??????????????????
                submit();
                break;
            case R.id.btn_clear:
                //???????????????
                tvId.setText("");
                waybillId = null;
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void addItem() {

        InventoryUbnormalGoods inventoryUbnormalGoods = new InventoryUbnormalGoods();
        inventoryUbnormalGoods.setUbnormalSource("????????????");
        inventoryUbnormalGoods.setCreateTime(System.currentTimeMillis());
        inventoryUbnormalGoods.setCreateUser(UserInfoSingle.getInstance().getUserId());
        inventoryUbnormalGoods.setCreateUserName(UserInfoSingle.getInstance().getUsername());
        inventoryUbnormalGoods.setUploadFilePath(new ArrayList <>());
        //????????????
        if (TextUtils.isEmpty(tvId.getText().toString().trim())) {
            inventoryUbnormalGoods.setWaybillCode(null);
        } else {
            inventoryUbnormalGoods.setWaybillCode(tvId.getText().toString().trim());
        }
        counterUbnormalGoodsList.add(inventoryUbnormalGoods);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * ???????????????????????????
     */
    private void commitInfo() {
        if (!TextUtils.isEmpty(tvId.getText())) {
            for (InventoryDetailEntity item : inventoryDetailEntityList) {
                if (item.getWaybillCode().equals(tvId.getText())) {
                    ToastUtil.showToast("????????????????????????");
                    return;
                }
            }
        }

        for (InventoryUbnormalGoods bean : counterUbnormalGoodsList) {
            if (bean.getUbnormalNumber() == 0) {
                ToastUtil.showToast("????????????????????????");
                return;
            }
        }
        if (TextUtils.isEmpty(edtRealSortNum.getText().toString().trim())) {
            ToastUtil.showToast("???????????????????????????");
            return;
        }
        //????????????
        InventoryDetailEntity detailEntity = new InventoryDetailEntity();
        detailEntity.setDealTime(System.currentTimeMillis());
        detailEntity.setWaybillCode(tvId.getText().toString().trim());
        detailEntity.setWaybillId(waybillId);
        detailEntity.setInventoryTaskId(taskId);
        detailEntity.setHandler(UserInfoSingle.getInstance().getUserId());
        detailEntity.setHandlerName(UserInfoSingle.getInstance().getUsername());
        detailEntity.setInventoryNumber(edtRealSortNum.getText().toString().trim());
        try {
            List <InventoryUbnormalGoods> goodsList = Tools.deepCopy(counterUbnormalGoodsList);
            detailEntity.setSmInventoryUbnormalGoods(goodsList);
        } catch (Exception e) {

        }

        //????????????????????????
        inventoryDetailEntityList.add(detailEntity);
        infoAdapter.notifyDataSetChanged();
        //??????????????????
        counterUbnormalGoodsList.clear();
        mAdapter.notifyDataSetChanged();

        tvId.setText("");
        edtRealSortNum.setText("");
        tvNum.setText("??????: - -");
        tvWeight.setText("??????: - -");
        tvStatus.setText("??????: - -");
        waybillId = null;
    }

    /**
     * ?????????
     */
    private void submit() {
        if (inventoryDetailEntityList.size() == 0) {
            ToastUtil.showToast("????????????????????????");
            return;
        }
        isFinish = false;
        mPresenter = new AddInventoryDetailPresenter(this);
        ((AddInventoryDetailPresenter) mPresenter).addInventoryDetail(inventoryDetailEntityList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1002) {//????????????????????????
                List <String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//??????????????????
//                counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).setUploadPath(photoList);
                //??????????????????
                List <File> files = new ArrayList <>();
                for (String str : photoList) {
                    if (str.contains("storage"))//?????????????????? ??????
                        files.add(new File(str));
                }
                Luban.get(this).load(files)
                        .setMaxSize(150)
                        .setMaxHeight(1920)
                        .setMaxWidth(1080)
                        .putGear(Luban.CUSTOM_GEAR).launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(List <File> fileList) {
                        List <MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                        mPresenter = new AddInventoryDetailPresenter(AddClearStorageActivity.this);
                        ((AddInventoryDetailPresenter) mPresenter).uploads(upFiles);
                        showProgessDialog("????????????????????????...");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        }
    }

    @Override
    public void addInventoryDetailResult(BaseEntity result) {
        if ("318".equals(result.getStatus())) {
            ToastUtil.showToast(result.getMessage());
            EventBus.getDefault().post("ClearStorageFragment_refresh");
        } else {
            ToastUtil.showToast("????????????");
        }
        if (isFinish)
            finish();
    }

    @Override
    public void uploadsResult(Object result) {
//        Log.e("dime", "??????????????????" + counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUploadPath().size());
        Map <String, String> map = (Map <String, String>) result;
        Set <Map.Entry <String, String>> entries = map.entrySet();
        for (Map.Entry <String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUploadFilePath().add(entry.getKey());
        }

//        Log.e("dime", "??????????????????" + counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUploadPath().size());
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();
    }

    @Override
    public void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean) {

    }

    @Override
    public void getWaybillCodeResult(String result) {

    }

    /**
     * ?????? ????????????
     *
     * @param result
     */
    @Override
    public void getWaybillInfoByWaybillCodeResult(WaybillsBean result) {
        if (result != null) {
            //todo
            tvNum.setText("??????: " + result.getTotalNumber());
            tvWeight.setText("??????: " + result.getTotalWeight());
            tvStatus.setText("??????: " + Tools.getWaybillStatus(result.getWaybillStatus()));
            waybillId = result.getId();
        }
    }

    @Override
    public void getWaybillInfoByWaybillCodeResultFail() {
        ToastUtil.showToast("?????????????????????");
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

    /**
     * ???????????????????????????
     *
     * @param
     */
    @Override
    public void listInventoryDetailResult(List <InventoryDetailEntity> entityList) {
        if (entityList != null) {
            inventoryDetailEntityList.clear();
            inventoryDetailEntityList.addAll(entityList);
        }
        infoAdapter.notifyDataSetChanged();
    }
}
