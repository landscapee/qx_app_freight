package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * TODO : 新增退货页面
 * Created by pr
 */
public class AddReturnActivity extends BaseActivity {
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_scooter)
    TextView tvScooter;
    @BindView(R.id.sp_reason)
    Spinner spReason;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    public static void startActivity(Activity context, String waybillId, String mScooterCode, String waybillCode, List<DeclareItem> declareItemBean) {
        Intent starter = new Intent(context, AddReturnActivity.class);
        starter.putExtra("waybillId", waybillId);
        starter.putExtra("mScooterCode", mScooterCode);
        starter.putExtra("waybillCode", waybillCode);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("transportListBeans", (Serializable) declareItemBean);
        starter.putExtras(mBundle);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_return;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "新增");
        initView();
        //提交
        btnCommit.setOnClickListener(v -> {

        });
    }

    private void initView() {

    }
}
