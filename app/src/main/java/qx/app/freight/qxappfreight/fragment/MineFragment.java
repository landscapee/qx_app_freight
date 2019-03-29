package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.activity.MessageActivity;
import qx.app.freight.qxappfreight.activity.NoticeActivity;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.contract.NoReadCountContract;
import qx.app.freight.qxappfreight.presenter.NoReadCountPresenter;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 我的页面
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
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setMainTitle(Color.WHITE, "我的");
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
    }


    /**
     * setUserVisibleHint(boolean isVisibleToUser)是Fragment中的一个回调函数。
     * 当前Fragment可见时，setUserVisibleHint()回调，其中isVisibleToUser=true。
     * 当前Fragment由可见到不可见或实例化时，setUserVisibleHint()回调，其中isVisibleToUser=false。
     *
     *！！！！！！！！ 但是用起来没得效果得啊！！！！
     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        Log.e("22222", "setUserVisibleHint:1 " + isVisibleToUser);
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.e("22222", "setUserVisibleHint: " + isVisibleToUser);
//        if (isVisibleToUser) {
//            //相当于Fragment的onResume，为true时，Fragment已经可见
//            requestNoReadMessage();
//        } else {
//            //相当于Fragment的onPause，为false时，Fragment不可见
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
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
            requestNoReadMessage();
        }
    }

    private void requestNoReadMessage() {
        PageListEntity bean = new PageListEntity();
        bean.setRole(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        bean.setUserId(UserInfoSingle.getInstance().getUserId());
        ((NoReadCountPresenter) mPresenter).noReadCount(bean);

        ((NoReadCountPresenter) mPresenter).noReadNoticeCount(UserInfoSingle.getInstance().getUserId());
    }

    @OnClick({R.id.user_image, R.id.btn_login_out, R.id.rl_message, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_image:
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
            case R.id.rl_message:
                startActivity(new Intent(getContext(), MessageActivity.class));
                break;
            case R.id.rl_notice:
                startActivity(new Intent(getContext(), NoticeActivity.class));
                break;
        }
    }

    private void loginOut() {
        UserInfoSingle.setUserNil();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(getContext());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

}
