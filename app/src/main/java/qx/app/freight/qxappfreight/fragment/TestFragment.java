package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.activity.TestActivity;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.dialog.TpPushDialog;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class TestFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.button_select_warehouse_position)
    TextView btnTest;
    @BindView(R.id.tv_text)
    TextView mTvText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setMainTitle(Color.WHITE, "消息");
        initView();
        return view;
    }

    private void initView() {
        setData();
    }

    private void setData() {

        String content = "<font color='#FF0000'>" + "傻逼" + "</font>";
        btnTest.setText(Html.fromHtml(content));

    }

    @OnClick({R.id.button_input_check, R.id.button_check_person, R.id.button_check_file
            , R.id.button_check_cargo, R.id.button_input_cargo, R.id.button_add_cargo
            , R.id.button_input_warehouse, R.id.button_select_warehouse_position})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_input_check: //收验列表
//                ToastUtil.showToast(mContext, "收验列表");
                ViewHelper.setScaleX(getView(), 1.5f);// x方向上缩放
                ViewHelper.setScaleY(getView(), 1.5f);// y方向上缩放
                //发送消息
                ///taskTodoUser/ua1a81dd438b748dc9ddf76896b6a11fb/taskTodo/taskTodoList
//                webSocketSTOMPManager.sendMassage("/todoCenter/autoSend");
                break;
            case R.id.button_check_person: //检查报检人身份
//                ToastUtil.showToast(mContext, "检查报检人身份");
//                VerifyStaffActivity.startActivity(mContext);
                break;
            case R.id.button_check_file: //检查证明文件
                ToastUtil.showToast(mContext, "检查证明文件");
                break;
            case R.id.button_check_cargo: //检查货物
                ToastUtil.showToast(mContext, "检查货物");
                break;
            case R.id.button_input_cargo: //收货 ReceivingGoods
                Intent intent = new Intent(getContext(), ReceiveGoodsActivity.class);
                startActivity(intent);
                ToastUtil.showToast(mContext, "收货");
                break;
            case R.id.button_add_cargo: //新增
                //全屏dialog
                TpPushDialog tpPushDialog = new TpPushDialog(getContext(), R.style.custom_dialog, null, taskId -> {

                });
                tpPushDialog.show();
                break;
            case R.id.button_input_warehouse: //入库
                TestActivity.startActivity(mContext);
                break;
            case R.id.button_select_warehouse_position: //选择仓位e

                UpdatePushDialog updatePushDialog = new UpdatePushDialog(getContext(), R.style.custom_dialog, "", () -> {

                });
                updatePushDialog.show();

//                Intent intent1 = new Intent(getContext(), AllocateVehiclesFragment.class);
//                startActivity(intent1);
//                ToastUtil.showToast(mContext,"选择仓位");
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TestFragment===========", "onDestroy");
    }
}
