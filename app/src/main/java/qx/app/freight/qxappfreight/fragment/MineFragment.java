package qx.app.freight.qxappfreight.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.MessageActivity;
import qx.app.freight.qxappfreight.activity.NoticeActivity;
import qx.app.freight.qxappfreight.activity.TaskDoneActivity;
import qx.app.freight.qxappfreight.activity.UpdateInfoActivity;
import qx.app.freight.qxappfreight.activity.UpdatePWDActivity;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.UserBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.NoReadCountContract;
import qx.app.freight.qxappfreight.presenter.NoReadCountPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * ๆ็้กต้ข
 */
public class MineFragment extends BaseFragment implements NoReadCountContract.noReadCountView {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_role)
    TextView userRole;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.iv_mind_ms)
    ImageView ivMindMs;
    @BindView(R.id.iv_message_point)
    ImageView ivMessagePoint;
    @BindView(R.id.ll_mind)
    LinearLayout llMind;
    @BindView(R.id.tv_message_num)
    TextView tvMessageNum;
    @BindView(R.id.iv_notice)
    ImageView ivNotice;
    @BindView(R.id.iv_notice_point)
    ImageView ivNoticePoint;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.rl_message_change)
    RelativeLayout llPsd;
    @BindView(R.id.rl_today_done)
    RelativeLayout llTodayDone;

    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setMainTitle(Color.WHITE, "ๆ็");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestNoReadMessage();
    }

    private void initView() {
        mPresenter = new NoReadCountPresenter(this);
        userName.setText(UserInfoSingle.getInstance().getLoginName());
        userRole.setText(UserInfoSingle.getInstance().getUsername());
        for (LoginResponseBean.RoleRSBean roleRSBean :UserInfoSingle.getInstance().getRoleRS()){
            if (Constants.INSTALL_UNLOAD_EQUIP.equals(roleRSBean.getRoleCode())||
                    Constants.JUNCTION_LOAD.equals(roleRSBean.getRoleCode())||
                    Constants.DRIVEROUT.equals(roleRSBean.getRoleCode())||
                    Constants.PORTER.equals(roleRSBean.getRoleCode())||
                    Constants.INTERNATIONAL_GOODS.equals(roleRSBean.getRoleCode())||
                    Constants.INSTALL_EQUIP_LEADER.equals(roleRSBean.getRoleCode())) {
                llTodayDone.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * setUserVisibleHint(boolean isVisibleToUser)ๆฏFragmentไธญ็ไธไธชๅ่ฐๅฝๆฐใ
     * ๅฝๅFragmentๅฏ่งๆถ๏ผsetUserVisibleHint()ๅ่ฐ๏ผๅถไธญisVisibleToUser=trueใ
     * ๅฝๅFragment็ฑๅฏ่งๅฐไธๅฏ่งๆๅฎไพๅๆถ๏ผsetUserVisibleHint()ๅ่ฐ๏ผๅถไธญisVisibleToUser=falseใ
     *
     *๏ผ๏ผ๏ผ๏ผ๏ผ๏ผ๏ผ๏ผ ไฝๆฏ็จ่ตทๆฅๆฒกๅพๆๆๅพๅ๏ผ๏ผ๏ผ๏ผ
     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        Log.e("22222", "setUserVisibleHint:1 " + isVisibleToUser);
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.e("22222", "setUserVisibleHint: " + isVisibleToUser);
//        if (isVisibleToUser) {
//            //็ธๅฝไบFragment็onResume๏ผไธบtrueๆถ๏ผFragmentๅทฒ็ปๅฏ่ง
//            requestNoReadMessage();
//        } else {
//            //็ธๅฝไบFragment็onPause๏ผไธบfalseๆถ๏ผFragmentไธๅฏ่ง
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        requestNoReadMessage();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //็ธๅฝไบFragment็onResume๏ผไธบtrueๆถ๏ผFragmentๅทฒ็ปๅฏ่ง
        } else {
            //็ธๅฝไบFragment็onPause๏ผไธบfalseๆถ๏ผFragmentไธๅฏ่ง
            requestNoReadMessage();
        }
    }

    private void requestNoReadMessage() {
        PageListEntity bean = new PageListEntity();

        bean.setRole((UserInfoSingle.getInstance().getRoleRS()!=null&&UserInfoSingle.getInstance().getRoleRS().size()!=0)?UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode():"");
        bean.setUserId(UserInfoSingle.getInstance().getUserId());
        ((NoReadCountPresenter) mPresenter).noReadCount(bean);

        ((NoReadCountPresenter) mPresenter).noReadNoticeCount(UserInfoSingle.getInstance().getUserId());
    }

    @OnClick({R.id.user_image, R.id.btn_login_out, R.id.rl_message, R.id.rl_notice,R.id.rl_message_change,R.id.rl_today_done,R.id.rl_update_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_image:
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
            case R.id.rl_message:
                //่ทณ่ฝฌๅฐๆถๆฏๆ้
                startActivity(new Intent(getContext(), MessageActivity.class));
                break;
            case R.id.rl_notice:
                //่ทณ่ฝฌๅฐ้็ฅๅฌๅ
                startActivity(new Intent(getContext(), NoticeActivity.class));
                break;
            case R.id.rl_message_change:
                //ๅฏ็?ไฟฎๆน
                UpdatePWDActivity.startActivity(getActivity());
                break;
            case R.id.rl_today_done:
                //ๆ็ๅทฒๅ
                startActivity(new Intent(getActivity(), TaskDoneActivity.class));
                break;
            case R.id.rl_update_info:
                startActivity(new Intent(getActivity(), UpdateInfoActivity.class));
                break;
        }
    }

    private void loginOut() {
        CommonDialog dialog = new CommonDialog(getContext());
        dialog.setTitle("้ๅบ็ปๅฝ")
                .setMessage("ๆฏๅฆ้ๅบ็ปๅฝ")
                .setPositiveButton("็กฎๅฎ")
                .setNegativeButton("ๅๆถ")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            Tools.saveLoginNameAndPassword("","");
                            if (MyApplication.isNeedIm&& Tools.isProduct())
                                ((NoReadCountPresenter) mPresenter).loginOut(new UserBean(UserInfoSingle.getInstance().getUserId()));
                            else {
                                Tools.loginOut(getContext());
                            }
                        } else {
//                            ToastUtil.showToast("็นๅปไบๅณ่พน็ๆ้ฎ");
                        }
                    }
                })
                .show();

    }

    @Override
    public void noReadCountResult(String result) {
        tvMessageNum.setText(result);
        try {
            int i = Integer.parseInt(result);
            if (i>0){
                tvMessageNum.setText(result);
                ivMessagePoint.setVisibility(View.VISIBLE);
            }else {
                tvMessageNum.setText("");
                ivMessagePoint.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            tvMessageNum.setText("");
            ivMessagePoint.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void noReadNoticeCountResult(String result) {
        try {
            int i = Integer.parseInt( result );
            if (i>0){
                tvNoticeNum.setText(result);
                ivNoticePoint.setVisibility(View.VISIBLE);
            }else {
                tvNoticeNum.setText("");
                ivNoticePoint.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            tvNoticeNum.setText("");
            ivNoticePoint.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void loginOutResult(RespBean respBean) {
        if (respBean.isSucc()){
            ToastUtil.showToast("็ญพ้ๆๅ");
        }
        else {
            ToastUtil.showToast("็ญพ้ๅคฑ่ดฅ");
        }
        Tools.loginOut(getContext());

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
