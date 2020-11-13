package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.SecurityCheckResult;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 新增退货信息页面
 */
public class AddNotTransportRecordActivity extends BaseActivity {
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.sp_return_info)
    Spinner mSpReturnInfo;
    @BindView(R.id.et_return_goods_number)
    EditText mEtReturnNumber;
    @BindView(R.id.sp_choose_return_reason)
    Spinner mSpReason;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private String mGoodsName;
    private String mChoseReason;
    private String mChoseReturn;
    private int i = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_not_transport_record;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "新增");
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.icon_clear_data, v -> {
            mEtReturnNumber.setText("");
        });
        mGoodsName = getIntent().getStringExtra("goods_name");
        mTvGoodsName.setText(mGoodsName);
        String hint = "带 <font color='red'>*</font> 为必填项";
        mTvHint.setText(StringUtil.transformHtmlFromhtml(hint));
        String[] array1 = {"退货", "扣货", "移交公安"};

        String[] array = {"不明性质的粉末", "不明性质的液体", "发现锂电池但材料不足", "易燃易爆炸", "毒品", "危险品", "其他"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_general);

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array1);
        spinnerAdapter1.setDropDownViewResource(R.layout.item_spinner_general);

        mSpReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChoseReason = array[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpReason.setAdapter(spinnerAdapter);

        mSpReturnInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChoseReturn = array1[position];
                switchChoseInfo(mChoseReturn);
                Log.e("switchChoseInfo", i+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpReturnInfo.setAdapter(spinnerAdapter1);
        mBtnCommit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mEtReturnNumber.getText().toString())) {
                int returnNumber;
                try {
                    returnNumber = Integer.valueOf(mEtReturnNumber.getText().toString());
                    SecurityCheckResult entity = new SecurityCheckResult();
                    entity.setCommodity(mGoodsName);
                    entity.setPiece(returnNumber);
                    entity.setProcessMode(i);
                    entity.setReason(mChoseReason);
                    Intent intent = new Intent();
                    intent.putExtra("single_item", entity);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    ToastUtil.showToast("输入不合法，请检查");
                }
            } else {
                ToastUtil.showToast("信息不完整，请检查");
            }
        });
    }

    public int switchChoseInfo(String str) {

        switch (str) {
            case "退货":
                i = 0;
                break;
            case "扣货":
                i = 1;
                break;
            case "移交公安":
                i = 2;
                break;
        }
        return i;
    }
}
