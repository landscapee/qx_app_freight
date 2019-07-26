package qx.app.freight.qxappfreight.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;
import qx.app.freight.qxappfreight.R;

/**
 * Fragment基类
 * By zyy
 * 2019-1-2
 */
public class BaseFragment extends Fragment {

    public BasePresenter mPresenter;

    public Unbinder unbinder; //用于Fragment的 黄油刀 解绑

    public Context mContext;

    // 加载框
    public MaterialDialog mProgessbarDialog;
    public View mView = null;
    public TextView mTextView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        initDialog();
    }

    public void initDialog() {
        mProgessbarDialog = new MaterialDialog(mContext);
//        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_progressbar, null);
            mTextView = mView.findViewById(R.id.progressbar_tv);
//        }
        mProgessbarDialog.setCanceledOnTouchOutside(true);
        mProgessbarDialog.setContentView(mView);
    }

    public void showProgessDialog(String message) {
        if (!TextUtils.isEmpty(message))
            mTextView.setText(message);
        mProgessbarDialog.show();
    }

    public void dismissProgessDialog() {
        mProgessbarDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter.interruptHttp();
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        MyApplication.currentView = getClass().getSimpleName();
//        Log.e("========="+getClass().getSimpleName(),"onResume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.e("========="+getClass().getSimpleName(),"onPause");
//    }
}
