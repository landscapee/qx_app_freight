package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.LockEventbusEntity;
import qx.app.freight.qxappfreight.utils.Tools;

public class MsgDialogVisibleAct extends BaseActivity {

    @BindView(R.id.btn_go)
    Button btnGo;
    private Context mContext;

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, MsgDialogVisibleAct.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_msg_visible;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tools.startVibrator(getApplicationContext(),true,R.raw.ring);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.closeVibrator(getApplicationContext());
    }

    private void initView() {
        btnGo.setOnClickListener(v->{
//            if (Tools.isLocked(MyApplication.getContext()))
//                Tools.unLock(MyApplication.getContext());
//            MainActivity.startActivity(mContext);
            finish();
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LockEventbusEntity result) {


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if ("MsgDialogAct_finish".equals(result)){
            if (Tools.isLocked(MyApplication.getContext()))
                Tools.unLock(MyApplication.getContext());
            finish();
        }

    }
}
