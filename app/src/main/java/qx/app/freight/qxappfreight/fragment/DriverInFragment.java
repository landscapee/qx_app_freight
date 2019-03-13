package qx.app.freight.qxappfreight.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.DriverInBacklogActivity;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.adapter.HandcarBacklogTPAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.TransportBeginPresenter;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

public class DriverInFragment extends BaseFragment implements TransportBeginContract.transportBeginView, ScanScooterContract.scanScooterView {

    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.slrv_car_doing)
    SlideRecyclerView doingSlideRecyclerView;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.btn_begin_end)
    Button btnBeginEnd;
    @BindView(R.id.tv_tp_status)
    TextView tvTpStatus;

    private List <TransportTodoListBean> list;
    private HandcarBacklogTPAdapter mHandcarBacklogTPAdapterDoing;
    private int tpStatus = 1; // 0 运输中 1 运输结束


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_driver_in, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);

        mToolBar.setMainTitle(Color.WHITE, "我的待办");
        mToolBar.setRightTextView(View.VISIBLE, Color.WHITE, "待办列表", v -> {

            DriverInBacklogActivity.startActivity(getContext());

        });
        doingSlideRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list = new ArrayList <>();
        mHandcarBacklogTPAdapterDoing = new HandcarBacklogTPAdapter(list);
        doingSlideRecyclerView.setAdapter(mHandcarBacklogTPAdapterDoing);
        mHandcarBacklogTPAdapterDoing.setOnDeleteClickListener((view, position) ->
                {
                    if (list.size() == 5) {
                        llAdd.setVisibility(View.VISIBLE);
                    }
                    list.remove(position);
                    doingSlideRecyclerView.closeMenu();
                    mHandcarBacklogTPAdapterDoing.notifyDataSetChanged();
                    upDataBtnStatus();
                }
        );
        mHandcarBacklogTPAdapterDoing.setOnItemClickListener((adapter, view, position) -> {
        });
        imageScan.setOnClickListener(v -> {
            ScanManagerActivity.startActivity(getActivity(), "DriverInFragment");
        });
        //删除
        mHandcarBacklogTPAdapterDoing.setOnDeleteClickListener((view, position) -> {
            doingSlideRecyclerView.closeMenu();
            mPresenter = new TransportBeginPresenter(this);
            TransportEndEntity transportEndEntity = new TransportEndEntity();
            transportEndEntity.setId(list.get(position).getId());
            ((TransportBeginPresenter) mPresenter).scanScooterDelete(transportEndEntity);
        });

        getData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("DriverInFragment".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(result.getData());
        }
    }

    private void addScooterInfo(String scooterCode) {
        Log.e("scooterCode", scooterCode);
        if (!"".equals(scooterCode)) {
            mPresenter = new ScanScooterPresenter(this);
            TransportTodoListBean mainIfos = new TransportTodoListBean();
            mainIfos.setTpScooterCode(scooterCode);
            mainIfos.setDtoType(1);// 内场运输标记
            mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
            ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
        } else
            ToastUtil.showToast(getActivity(), "扫描结果为空请重新扫描");
    }

    private void getData() {
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId());
    }
    /**
     * 开始按钮是否可以点击
     */
    private void upDataBtnStatus() {
        if (list.size() > 0) {
            btnBeginEnd.setVisibility(View.VISIBLE);
        } else {
            btnBeginEnd.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.ll_add, R.id.btn_begin_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                ScanManagerActivity.startActivity(getActivity(), "DriverInFragment");
                break;
            case R.id.btn_begin_end:
                if (tpStatus == 1) {
                    doStart();

                } else {
                    doEnd();

                }
                break;
        }
    }

    private void doStart() {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        for (TransportTodoListBean mTransportTodoListBean:list){
            mTransportTodoListBean.setBeginAreaType(mTransportTodoListBean.getTpStartLocate());
            mTransportTodoListBean.setEndAreaType(mTransportTodoListBean.getTpDestinationLocate());
        }
        transportEndEntity.setScooters(list);
        ((TransportBeginPresenter) mPresenter).transportBegin(transportEndEntity);
    }

    private void doEnd() {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        for (TransportTodoListBean mTransportTodoListBean:list){
            mTransportTodoListBean.setBeginAreaType(mTransportTodoListBean.getTpStartLocate());
            mTransportTodoListBean.setEndAreaType(mTransportTodoListBean.getTpDestinationLocate());
            //库区出来的板车 放到待运区时 默认 EndAreaId 为D1
            if ("warehouse".equals(mTransportTodoListBean.getTpStartLocate())){
                mTransportTodoListBean.setEndAreaId("D1");
            }
        }
        transportEndEntity.setScooters(list);
        ((TransportBeginPresenter) mPresenter).transportEnd(transportEndEntity);
    }

    @Override
    public void scanScooterResult(String result) {
        if (!"".equals(result)) {
            getData();

        }
    }

    @Override
    public void scooterWithUserResult(List <TransportTodoListBean> result) {
        if (result != null) {
            list.clear();
            list.addAll(result);
            mHandcarBacklogTPAdapterDoing.notifyDataSetChanged();
            for (TransportTodoListBean mTransportTodoListBean:result)
            {
                if (mTransportTodoListBean.getTpState() == 2){
                    setTpStatus(0);
                    break;
                }
            }
            if (list.size() >= 5) {
                ToastUtil.showToast(getContext(), "最多一次拉5板货");
                llAdd.setVisibility(View.GONE);
            }
            else {
                if (tpStatus != 0){
                    llAdd.setVisibility(View.VISIBLE);
                }

            }
            upDataBtnStatus();
        } else
            ToastUtil.showToast(getActivity(), "返回数据为空");
    }

    @Override
    public void scanScooterDeleteResult(String result) {
        if (!"".equals(result)) {
            getData();
            ToastUtil.showToast(getActivity(), "删除成功");
        } else
            Log.e("删除失败", "删除失败");
    }

    @Override
    public void transportBeginResult(String result) {
        if (!"".equals(result)) {
            setTpStatus(0);
        } else
            Log.e("开始失败", "开始失败");
    }

    @Override
    public void transportEndResult(String result) {
        if (!"".equals(result)) {
            setTpStatus(1);
        } else
            Log.e("结束失败", "结束失败");
    }
    private void setTpStatus(int flag) {
        tpStatus = flag;
        if (flag == 0){
            btnBeginEnd.setText("结束");
            llAdd.setVisibility(View.GONE);
            doingSlideRecyclerView.closeMenu();
            doingSlideRecyclerView.setIsmIsSlide(false);
            tvTpStatus.setVisibility(View.VISIBLE);
        }
        else {
            btnBeginEnd.setText("开始");
            mPresenter = new TransportBeginPresenter(this);
            doingSlideRecyclerView.setIsmIsSlide(true);
            list.clear();
            mHandcarBacklogTPAdapterDoing.notifyDataSetChanged();
            llAdd.setVisibility(View.VISIBLE);
            tvTpStatus.setVisibility(View.GONE);
        }
        upDataBtnStatus();
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
