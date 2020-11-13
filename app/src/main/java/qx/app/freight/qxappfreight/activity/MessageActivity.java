package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.MessageAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.contract.MessageContract;
import qx.app.freight.qxappfreight.presenter.MessagePresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 消息列表页面
 * Created by swd
 */
public class MessageActivity extends BaseActivity implements MessageContract.messageView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {

    @BindView(R.id.mfrv_message)
    MultiFunctionRecylerView mfrvMessage;

    @BindView(R.id.tv_storage_user)
    TextView mTvStorageUser;
    @BindView(R.id.ll_storage_user)
    LinearLayout mLlStorageUser;

    private CustomToolbar toolbar;
    private MessageAdapter mAdapter;
    private List<PageListBean.RecordsBean> list;
    private int pageCurrent = 1;
    private int nowPosition;
    private List<String> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserInfoSingle.getInstance().getRoleRS().size()> 0) {
            requestData(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        }
    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "消息提醒");
    }

    private void initView() {
        mPresenter = new MessagePresenter(this);
        list = new ArrayList<>();
        mAdapter = new MessageAdapter(list);
        mfrvMessage.setLayoutManager(new LinearLayoutManager(this));
        mfrvMessage.setRefreshListener(this);
        mfrvMessage.setOnRetryLisenter(this);
        for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
            switch (UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode()) {
                case "securityCheck":
                    mList.add("安检");
                    break;
                case "collection":
                    mList.add("收运");
                    break;
                case "inventoryKeeper":
                    mList.add("货运");
                    break;
                case "cargoAgency":
                    mList.add("货代");
                    break;
                case "receive":
                    mList.add("收验");
                    break;
                case "preplaner":
                    mList.add("预配-组板");
                    break;
                case "beforehand":
                    mList.add("理货");
                    break;
                case "weighter":
                    mList.add("复重员");
                    break;
                case "infieldDriver":
                    mList.add("内场司机");
                    break;
                case "offSiteEscort":
                    mList.add("外场司机");
                    break;
                case "supervision":
                    mList.add("装卸机");
                    break;
                case "clipping":
                    mList.add("结载");
                    break;
                case "international_goods":
                    mList.add("国际货物");
                    break;
                case "delivery_in":
                    mList.add("进港提货");
                    break;
                case "beforehand_in":
                    mList.add("进港分拣");
                    break;
                case "porter":
                    mList.add("行李员");
                    break;
                case "stevedores":
                    mList.add("装卸员");
                    break;

            }

        }
        mTvStorageUser.setText(mList.get(0));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (list.get(position).getReadingStatus() == 0) {
                readMessage(position);
                nowPosition = position;
            }

        });

        mfrvMessage.setAdapter(mAdapter);
        mLlStorageUser.setOnClickListener(v -> {
            showStoragePickView();
        });
    }

    //
    private void requestData(String roleCode) {
        switch (roleCode) {
            case "安检":
                roleCode = "securityCheck";
                break;
            case "收运":
                roleCode = "collection";
                break;
            case "货运":
                roleCode = "inventoryKeeper";
                break;
            case "货代":
                roleCode = "cargoAgency";
                break;
            case "收验":
                roleCode = "receive";
                break;
            case "预配-组板":
                roleCode = "preplaner";
                break;
            case "理货":
                roleCode = "beforehand";
                break;
            case "复重员":
                roleCode = "weighter";
                break;
            case "内场司机":
                roleCode = "infieldDriver";
                break;
            case "外场司机":
                roleCode = "offSiteEscort";
                break;
            case "装卸机":
                roleCode = "supervision";
                break;
            case "结载":
                roleCode = "clipping";
                break;
            case "国际货物":
                roleCode = "international_goods";
                break;
            case "进港提货":
                roleCode = "delivery_in";
                break;
            case "进港分拣":
                roleCode = "beforehand_in";
                break;
            case "行李员":
                roleCode = "porter";
                break;
            case "装卸员":
                mList.add("stevedores");
                break;
        }
        BaseFilterEntity bean = new BaseFilterEntity();
        PageListEntity listBean = new PageListEntity();
        List<String> requestList = new ArrayList();
        requestList.add("create_time");
        listBean.setUserId(UserInfoSingle.getInstance().getUserId());
        listBean.setRole(roleCode);
        bean.setSize(20);
        bean.setCurrent(pageCurrent);
        bean.setDesc(requestList);
        bean.setFilter(listBean);
        ((MessagePresenter) mPresenter).pageList(bean);
    }

    private void readMessage(int position) {
        BaseFilterEntity bean = new BaseFilterEntity();
        bean.setMessageId(list.get(position).getId());
        bean.setUserId(UserInfoSingle.getInstance().getUserId());
        ((MessagePresenter) mPresenter).msMessageView(bean);
    }

    @Override
    public void onRefresh() {
        mTvStorageUser.setText(mList.get(0));
        pageCurrent = 1;
        requestData(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
    }

    @Override
    public void onLoadMore() {
        requestData(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            requestData(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void pageListResult(PageListBean pageListBean) {

        if (pageCurrent == 1) {
            list.clear();
            mfrvMessage.finishRefresh();
        } else {
            mfrvMessage.finishLoadMore();
        }
        if (pageListBean.getPages() >= pageCurrent) {
            pageCurrent++;
        } else {
            ToastUtil.showToast("没有更多数据！");
            return;
        }

        list.addAll(pageListBean.getRecords());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void msMessageViewResult(MsMessageViewBean msMessageViewBean) {
        list.get(nowPosition).setReadingStatus(1);
        mAdapter.notifyItemChanged(nowPosition);
    }

    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvStorageUser.setText(mList.get(options1));
                pageCurrent =1;
                requestData(mList.get(options1));
            }
        }).build();
        pickerView.setPicker(mList);
        pickerView.setTitleText("角色选择");
        pickerView.show();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
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
