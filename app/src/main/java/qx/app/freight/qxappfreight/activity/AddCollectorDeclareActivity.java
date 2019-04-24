package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 新增品名
 *
 * ！！！！！未使用！！！！
 */
public class AddCollectorDeclareActivity extends BaseActivity {

    @BindView(R.id.tv_product)
    EditText tvProduct;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.edt_volume)
    EditText edtVolume;
    @BindView(R.id.edt_type)
    EditText edtType;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private DeclareItem mData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_collector_declare;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        mData = (DeclareItem) getIntent().getSerializableExtra("DeclareItemBean");
        if (mData!=null){
            tvProduct.setText(mData.getCargoCn());
            edtNumber.setText(mData.getCargoCn());
            edtNumber.setText(mData.getCargoCn());
            edtNumber.setText(mData.getCargoCn());
            edtNumber.setText(mData.getCargoCn());
        }
    }

    @OnClick({R.id.tv_product, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.tv_product:
//                break;
            case R.id.btn_commit:
                commitInfo();
                break;
        }
    }

    private void commitInfo() {
        if (TextUtils.isEmpty(tvProduct.getText().toString().trim())){
            ToastUtil.showToast("请输入品名");
            return;
        }
        if (TextUtils.isEmpty(edtNumber.getText().toString().trim())){
            ToastUtil.showToast("请输入件数");
            return;
        }
        if (TextUtils.isEmpty(edtWeight.getText().toString().trim())){
            ToastUtil.showToast("请输入重量");
            return;
        }
        if (TextUtils.isEmpty(edtVolume.getText().toString().trim())){
            ToastUtil.showToast("请输入体积");
            return;
        }
        if (TextUtils.isEmpty(edtType.getText().toString().trim())){
            ToastUtil.showToast("请输入包装类型");
            return;
        }
        try {
            int number = Integer.parseInt(edtNumber.getText().toString().trim());
            int weight = Integer.parseInt(edtNumber.getText().toString().trim());
            int volume = Integer.parseInt(edtNumber.getText().toString().trim());

            DeclareItem item =  new DeclareItem();
            item.setCargoCn(tvProduct.getText().toString().trim());
            item.setNumber(number);
            item.setWeight(weight);
            item.setVolume(volume);

            EventBus.getDefault().post(item);
            finish();
        }catch (Exception e){
            ToastUtil.showToast("2222222");
        }
    }
}
