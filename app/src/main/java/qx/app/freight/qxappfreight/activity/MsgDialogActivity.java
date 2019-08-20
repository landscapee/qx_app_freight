package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.RecLockAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.PushBaseBean;
import qx.app.freight.qxappfreight.utils.SoundConfigUtils;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.VibrationUtils;
import qx.app.freight.qxappfreight.widget.CustomDividerItemDecoration;

public class MsgDialogActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_callback)
    Button mBtnCallback;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.rec_push)
    RecyclerView mRecyclerView;
    private Context mContext;
    private static final String KEY = "key";

    private RecLockAdapter mRecLockAdapter;

    private List<PushBaseBean> pushList = new ArrayList<>();

    public static void startActivity(Context context, PushBaseBean push) {
        Intent starter = new Intent(context, MsgDialogActivity.class);
        starter.putExtra(KEY, push);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_msg_dialog;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
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
        mTvTitle.setText("地服保障");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(mContext, CustomDividerItemDecoration.VERTICAL_LIST));
        mRecLockAdapter = new RecLockAdapter(mContext, pushList);
        mRecyclerView.setAdapter(mRecLockAdapter);
        PushBaseBean push = (PushBaseBean) getIntent().getSerializableExtra(KEY);
        if (push != null) {
            pushList.add(push);
            mRecLockAdapter.notifyDataSetChanged();
            setView();
        }
        mBtnCallback.setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, MainAct.class);
//                startActivity(intent);
            finish();
        });
    }


    private void setView() {
        if (pushList.size() > 1)
            mTvNum.setVisibility(View.VISIBLE);
        else
            mTvNum.setVisibility(View.GONE);
        mTvNum.setText(pushList.size() + "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushBaseBean push) {
        SoundConfigUtils.getInstance(mContext.getApplicationContext()).play();
        pushList.add(push);
        mRecLockAdapter.notifyDataSetChanged();
        setView();
    }

}
