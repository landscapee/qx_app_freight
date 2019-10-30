package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
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
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.utils.Tools;

public class MsgDialogAct extends Activity {

    @BindView(R.id.btn_go)
    Button btnGo;

    private Context mContext;

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, MsgDialogAct.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_lock_msg);
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
            if (Tools.isLocked(MyApplication.getContext()))
                Tools.unLock(MyApplication.getContext());
            MainActivity.startActivity(mContext);
        });
    }


    private void setView() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
