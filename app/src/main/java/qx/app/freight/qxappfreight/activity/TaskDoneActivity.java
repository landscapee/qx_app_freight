package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidouapp.imlibapi.activity.ImLibSpecialHomeFragment;
import com.qxkj.positionapp.GPSService;
import com.qxkj.positionapp.LocationEntity;
import com.qxkj.positionapp.observer.LocationObservable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.SeatChangeEntity;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.fragment.CargoManifestFragment;
import qx.app.freight.qxappfreight.fragment.ClearStorageFragment;
import qx.app.freight.qxappfreight.fragment.DynamicFragment;
import qx.app.freight.qxappfreight.fragment.LnstallationFragment;
import qx.app.freight.qxappfreight.fragment.MineFragment;
import qx.app.freight.qxappfreight.fragment.TaskDoneFragment;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TestFragment;
import qx.app.freight.qxappfreight.reciver.MessageReciver;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 持有已办fragment的activity
 */
public class TaskDoneActivity extends BaseActivity implements LocationObservable {

    private Fragment fragment1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TaskDoneActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_task_done;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.GONE);
        initFragment();
    }


    private void initFragment() {
        fragment1 = new TaskDoneFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment1)
                .show(fragment1)
                .commit();
    }


    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void receiveLocationUpdate(LocationEntity locationEntity) {
        if (locationEntity != null)
            ToastUtil.showToast("经度:" + locationEntity.getLongitude() + "纬度:" + locationEntity.getLatitude());
    }
}

