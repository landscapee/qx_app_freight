package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SelectTaskMemberRvAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.SelectTaskMemberContract;
import qx.app.freight.qxappfreight.presenter.SelectTaskMemberPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 装卸员小组长分配装卸机任务页面
 */
public class AssignInstallEquipMemberActivity extends BaseActivity implements SelectTaskMemberContract.SelectTaskMemberView {
    @BindView(R.id.rv_select_member)
    RecyclerView mRvSelectMember;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    private String mTaskId;
    private List<SelectTaskMemberEntity> mMemberList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_assign_install_equip_member;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "选择任务人员");
        initView();
    }

    private void initView() {
        mTaskId = getIntent().getStringExtra("task_id");
        mPresenter = new SelectTaskMemberPresenter(this);
        ((SelectTaskMemberPresenter) mPresenter).getLoadUnloadLeaderList(mTaskId);
        mTvConfirm.setOnClickListener(v -> commitSelectMember());
    }

    /**
     * 选择提交
     */
    private void commitSelectMember() {
        if (mMemberList != null && mMemberList.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (SelectTaskMemberEntity entity : mMemberList) {
                if (entity.isSelected()) {
                    sb.append(entity.getStaffId());
                    sb.append(",");
                }
            }
            if (sb.toString().length() > 0){
                String members = sb.toString().substring(0, sb.toString().length() - 1);
                if (TextUtils.isEmpty(members)) {
                    ToastUtil.showToast("至少得选择一个任务人");
                } else {
                    BaseFilterEntity entity = new BaseFilterEntity();
                    entity.setTaskId(mTaskId);
                    entity.setStaffIds(members);
                    ((SelectTaskMemberPresenter) mPresenter).selectMember(entity);
                }
            }
            else
                ToastUtil.showToast("至少得选择一个任务人");

        }
    }

    @Override
    public void getLoadUnloadLeaderListResult(List<SelectTaskMemberEntity> result) {
        mMemberList.clear();
        if (result != null && result.size() != 0) {
            mMemberList.addAll(result);
            SelectTaskMemberRvAdapter adapter = new SelectTaskMemberRvAdapter(result);
            mRvSelectMember.setLayoutManager(new GridLayoutManager(this, 3));
            mRvSelectMember.setAdapter(adapter);
        }
    }

    @Override
    public void selectMemberResult(String result) {
        ToastUtil.showToast("任务分配成功");
        EventBus.getDefault().post("refresh_data_update");
        finish();
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
