package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.service.WebSocketSTOMPManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements LoginContract.loginView {
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.et_password)
    EditText mEtPassWord;
    @BindView(R.id.et_username)
    EditText mEtUserName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.GONE, R.mipmap.icon_back, v -> showDialog());
//        toolbar.setLeftTextView(View.VISIBLE, Color.RED, "左边文字", v -> Toast.makeText(LoginActivity.this, "点击了左边的文字", Toast.LENGTH_LONG).show());
        toolbar.setMainTitle(Color.WHITE, "登录");
//        toolbar.setRightIconView(View.VISIBLE, R.mipmap.icon_query, v -> Toast.makeText(LoginActivity.this, "右边图标", Toast.LENGTH_LONG).show());
//        toolbar.setRightTextView(View.VISIBLE, Color.GREEN, "右边文字", v -> Toast.makeText(LoginActivity.this, "点击了右边的文字", Toast.LENGTH_LONG).show());
        mEtPassWord.setText("111111");
        mEtUserName.setText(UserInfoSingle.getInstance().getLoginName());
        mEtUserName.setText("zhuangxieji");
        mPresenter = new LoginPresenter(this);
        mBtnLogin.setOnClickListener(v -> {
            login();
        });
        mEtPassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE ==actionId){
                    login();
                    return true;
                }
                return false;
            }
        });
        checkPermissions();
        checkPermissionsForWindow();
    }

    /**
     * 登录方法
     */
    private void login(){
//            WebSocketSTOMPManager webSocketSTOMPManager = new WebSocketSTOMPManager(this);
//            //创建连接
//            webSocketSTOMPManager.connect();
//            //创建订阅
//            webSocketSTOMPManager.registerStompTopic("/taskTodoUser/ua1a81dd438b748dc9ddf76896b6a11fb/taskTodo/taskTodoList");
        if(TextUtils.isEmpty(mEtUserName.getText().toString()) || TextUtils.isEmpty(mEtPassWord.getText().toString())){
            ToastUtil.showToast("账号或者密码不能为空");
        }else{
            ((LoginPresenter) mPresenter).login(getLoginEntity());
        }
    }

    /**
     * CommonDialog 的用法
     */
    private void showDialog(){
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("这个是标题")
                .setMessage("这个是提示内容")
                .setPositiveButton("左边按钮")
                .setNegativeButton("右边按钮")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm){
                            ToastUtil.showToast("点击了左边的按钮");
                        }else {
                            ToastUtil.showToast("点击了右边的按钮");
                        }
                    }
                })
                .show();
    }

    private LoginEntity getLoginEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername(mEtUserName.getText().toString().trim());
        mLoginEntity.setPassword(mEtPassWord.getText().toString().trim());
        return mLoginEntity;
    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        if (loginBean != null) {
            Tools.setLoginUserBean(loginBean);
            if ("zhuangxieji".equals(loginBean.getLoginName())){
                loginBean.setUserId(loginBean.getLoginid());
            }
            UserInfoSingle.setUser(loginBean);
            MainActivity.startActivity(this);
        } else {
            ToastUtil.showToast(this, "数据错误");
        }
    }

    @Override
    public void toastView(String error) {
        MainActivity.startActivity(this);
        Toast.makeText(LoginActivity.this, "错误" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("正在登录……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
