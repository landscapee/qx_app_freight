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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InventoryInfoAdapter;
import qx.app.freight.qxappfreight.adapter.SortingAddAdapter;
import qx.app.freight.qxappfreight.adapter.SortingAddAdapter2;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryUbnormalGoods;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog;
import qx.app.freight.qxappfreight.dialog.ExceptionDetailDialog;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.presenter.AddInventoryDetailPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;


/**
 * 清库-新增
 * Created by swd
 */
public class AddClearStorageActivity extends BaseActivity implements AddInventoryDetailContract.addInventoryDetailView {
    @BindView(R.id.edt_id)
    EditText edtId;
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

    SortingAddAdapter2 mAdapter;//异常情况选择列表适配器
    InventoryInfoAdapter infoAdapter; //异常列表数据适配器
    List<InventoryUbnormalGoods> counterUbnormalGoodsList;//异常数组
    List<InventoryDetailEntity> inventoryDetailEntityList;//提交的异常数组

    int CURRENT_PHOTO_INDEX;

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
        toolbar.setMainTitle(Color.WHITE, "鲜活清库");
    }

    private void initData() {
        counterUbnormalGoodsList = new ArrayList<>();
        inventoryDetailEntityList = new ArrayList<>();
        mPresenter = new AddInventoryDetailPresenter(this);
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SortingAddAdapter2(this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(new SortingAddAdapter2.OnSlectPicListener() {
            @Override
            public void onSelectPic(int position) {
                Log.e("dime", "选择图片：" + position);
                CURRENT_PHOTO_INDEX = position;
                //将数组转为List
                List<String> originList = new ArrayList<>();
                if (counterUbnormalGoodsList.get(position).getUploadPath() != null) {
                    for (String url : counterUbnormalGoodsList.get(position).getUploadPath()) {
                        originList.add(url);
                    }
                }
                MultiImageSelector.create(AddClearStorageActivity.this)
                        .showCamera(true) // 是否显示相机. 默认为显示
                        .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                        .multi() // 多选模式, 默认模式;
                        .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(AddClearStorageActivity.this, 1002);
            }
        });
        //设置异常类型
        mAdapter.setOnExceptionTypeListener(new SortingAddAdapter2.OnExceptionChooseListener() {
            @Override
            public void onExceptionChoose(int posstion) {
                ChooseStoreroomDialog chooseExcetionDialog = new ChooseStoreroomDialog();
                chooseExcetionDialog.setData(ExceptionUtils.testBeanList, AddClearStorageActivity.this);
                chooseExcetionDialog.setChooseDialogInterface(new ChooseDialogInterface() {
                    @Override
                    public void confirm(int position2) {
                        position2++;
                        counterUbnormalGoodsList.get(posstion).setUbnormalType(position2);
                        mAdapter.notifyItemChanged(posstion);
                    }
                });
                chooseExcetionDialog.show(getSupportFragmentManager(), "exception");
            }
        });
        //第二个列表的初始化
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        infoAdapter = new InventoryInfoAdapter(inventoryDetailEntityList);
        infoAdapter.setInventoryInfoListener(new InventoryInfoAdapter.InventoryInfoListener() {
            @Override
            public void onLook(int position) {
                ToastUtil.showToast("查看了第"+position+"位置的数据");
                ExceptionDetailDialog detailDialog = new ExceptionDetailDialog.Builder()
                        .inventoryDetailEntity(inventoryDetailEntityList
                                .get(position)).context(AddClearStorageActivity.this).build();
                detailDialog.show(getSupportFragmentManager(),"222");
            }

            @Override
            public void onDelete(int position) {
                inventoryDetailEntityList.remove(position);
                infoAdapter.notifyDataSetChanged();
            }
        });
        recyclerView1.setAdapter(infoAdapter);
    }

    @OnClick({R.id.btn_add_item, R.id.btn_info_commit, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_item:

                InventoryUbnormalGoods inventoryUbnormalGoods = new InventoryUbnormalGoods();
                counterUbnormalGoodsList.add(inventoryUbnormalGoods);
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_info_commit:
                commitInfo();
                break;
            case R.id.btn_commit:
                //提交异常列表
                submit();
                break;
        }
    }

    /**
     *  提交填好的异常情况
     */
    private void commitInfo() {
        //封装数据
        InventoryDetailEntity detailEntity = new InventoryDetailEntity();
        detailEntity.setWaybillCode(edtId.getText().toString().trim());
        detailEntity.setInventoryNumber(edtRealSortNum.getText().toString().trim());
        List<InventoryUbnormalGoods> goodsList = new ArrayList<>(counterUbnormalGoodsList);
        detailEntity.setInventoryUbnormalGoods(goodsList);
        //刷新异常上报列表
        inventoryDetailEntityList.add(detailEntity);
        infoAdapter.notifyDataSetChanged();
        //清空添加列表
        counterUbnormalGoodsList.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 总提交
     */
    private void submit() {
        //将组装好的数据返回给前一个页面
        if (TextUtils.isEmpty(edtId.getText().toString().trim())) {
//            mInWaybillRecord.setWaybillCode("");
        } else {
//            mInWaybillRecord.setWaybillCode(edtId.getText().toString().trim());
        }
        if (TextUtils.isEmpty(edtRealSortNum.getText().toString().trim())) {
            ToastUtil.showToast("请输入实际分拣数！");
            return;
        }

        ((AddInventoryDetailPresenter) mPresenter).addInventoryDetail(inventoryDetailEntityList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1002) {//异常上报相机事件
                List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//选择好的图片
                counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).setUploadPath(photoList);
                //开始上传图片
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
                        showProgessDialog("正在上传，请稍候...");
                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                        ((AddInventoryDetailPresenter) mPresenter).uploads(upFiles);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        }
    }

    @Override
    public void addInventoryDetailResult(String result) {
        ToastUtil.showToast("提交成功");
        finish();
    }

    @Override
    public void uploadsResult(Object result) {
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUploadPath().add(entry.getKey());
        }
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();
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
