package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InventoryInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.contract.ListInventoryDetailContract;
import qx.app.freight.qxappfreight.dialog.ExceptionDetailDialog;
import qx.app.freight.qxappfreight.presenter.ListInventoryDetailPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 清库-完成-详情
 * Created by swd
 */
public class ClearStorageDetailActivity extends BaseActivity implements ListInventoryDetailContract.listInventoryDetailView {

    @BindView(R.id.recycler_view_1)
    RecyclerView recyclerView1;

    InventoryInfoAdapter infoAdapter; //异常列表数据适配器
    private String taskId;  //任务id
    private String taskTitle; //标题
    List<InventoryDetailEntity> inventoryDetailEntityList = new ArrayList<>();//提交的异常数组

    @Override
    public int getLayoutId() {
        return R.layout.activity_clear_storage_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        taskId = getIntent().getStringExtra("taskId");
        taskTitle = getIntent().getStringExtra("taskTitle");
        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        mPresenter = new ListInventoryDetailPresenter(this);
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, taskTitle);
    }

    private void initView() {
        //第二个列表的初始化
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        infoAdapter = new InventoryInfoAdapter(inventoryDetailEntityList, 2);
        infoAdapter.setInventoryInfoListener(new InventoryInfoAdapter.InventoryInfoListener() {
            @Override
            public void onLook(int position) {
                ExceptionDetailDialog detailDialog = new ExceptionDetailDialog.Builder()
                        .inventoryDetailEntity(inventoryDetailEntityList
                                .get(position)).context(ClearStorageDetailActivity.this).build();
                detailDialog.show(getSupportFragmentManager(), "222");
            }

            @Override
            public void onDelete(int position) {

            }
        });
        recyclerView1.setAdapter(infoAdapter);
    }

    private void initData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setInventoryTaskId(taskId);
        ((ListInventoryDetailPresenter) mPresenter).listInventoryDetail(entity);
    }

    @Override
    public void listInventoryDetailResult(List<InventoryDetailEntity> entityList) {
        inventoryDetailEntityList.clear();
        inventoryDetailEntityList.addAll(entityList);
        infoAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastView(String error) {
        Log.d("toastView:", error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
