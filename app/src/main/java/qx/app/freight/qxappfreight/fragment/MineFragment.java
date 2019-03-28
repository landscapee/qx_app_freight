package qx.app.freight.qxappfreight.fragment;

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
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 我的页面
 */
public class MineFragment extends BaseFragment {
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
    }

    private void initView() {
        userName.setText(UserInfoSingle.getInstance().getLoginName());
        userRole.setText(UserInfoSingle.getInstance().getUsername());
    }

    @OnClick({R.id.user_image, R.id.btn_login_out, R.id.rl_message,R.id.rl_notice})
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
}
