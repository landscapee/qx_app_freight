package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qxkj.positionapp.LocationEntity;
import com.qxkj.positionapp.observer.LocationObservable;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.fragment.TaskDoneFragment;
import qx.app.freight.qxappfreight.utils.ToastUtil;

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

