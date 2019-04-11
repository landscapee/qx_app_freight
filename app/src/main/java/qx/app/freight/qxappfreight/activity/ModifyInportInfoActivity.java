package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InPortTallyListEntity;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog;
import qx.app.freight.qxappfreight.dialog.PopTestDialog;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 修改理货信息页面
 */
public class ModifyInportInfoActivity extends BaseActivity {
    private InPortTallyListEntity mData;
    @BindView(R.id.tv_way_bill)
    TextView mTvWaybill;//运单号
    @BindView(R.id.tv_transport_number)
    TextView mTvTransportNumber;//运单件数
    @BindView(R.id.tv_transport_weight)
    TextView mTvTransportWeight;//运单重量
    @BindView(R.id.tv_bill_number)
    TextView mTvBillNumber;//舱单件数
    @BindView(R.id.tv_bill_weight)
    TextView mTvBillWeight;//舱单重量
    @BindView(R.id.et_tally_number)
    EditText mEtTallyNumber;//理货件数输入框
    @BindView(R.id.et_tally_weight)
    EditText mEtTallyWeight;//理货重量输入框
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;//库区
    @BindView(R.id.tv_store_number)
    TextView mTvStoreNumber;//库位
    @BindView(R.id.cb_number_error)
    CheckBox mCbNumberError;//件数异常checkBox
    @BindView(R.id.tv_number_error)
    TextView mTvNumberError;//件数异常
    @BindView(R.id.et_error_number)
    EditText mEtNumberError;//件数异常输入框
    @BindView(R.id.cb_no_goods)
    CheckBox mCbNoGoods;//有单无货checkBox
    @BindView(R.id.tv_no_goods)
    TextView mTvNoGoods;//有单无货
    @BindView(R.id.cb_no_bills)
    CheckBox mCbNoBills;//有货无单checkBox
    @BindView(R.id.tv_no_bills)
    TextView mTvNoBills;//有货无单
    @BindView(R.id.cb_broken)
    CheckBox mCbBroken;//破损checkBox
    @BindView(R.id.tv_broken)
    TextView mTvBroken;//破损
    @BindView(R.id.cb_burke)
    CheckBox mCbBurke;//扣货checkBox
    @BindView(R.id.tv_burke)
    TextView mTvBurke;//扣货
    @BindView(R.id.cb_wrong_transport)
    CheckBox mCbWrongTransport;//错运checkBox
    @BindView(R.id.tv_wrong_transport)
    TextView mTvWrongTransport;//错运
    @BindView(R.id.cb_decompose)
    CheckBox mCbDecompose;//腐烂checkBox
    @BindView(R.id.tv_decompose)
    TextView mTvDecompose;//腐烂
    @BindView(R.id.cb_leakage)
    CheckBox mCbLeakage;//泄露checkBox
    @BindView(R.id.tv_leakage)
    TextView mTvLeakage;//泄露
    @BindView(R.id.tv_commit_inport_info)
    TextView mTvCommitInfo;//提交
    private List<Integer> mErrorCodeList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_inport_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "修改");
        mData = (InPortTallyListEntity) getIntent().getSerializableExtra("data");
        mTvWaybill.setText(mData.getWaybill());
        mTvTransportNumber.setText(String.valueOf(mData.getDocNumber()));
        mTvTransportWeight.setText(mData.getDocWeight() + "kg");
        mTvBillNumber.setText(String.valueOf(mData.getManifestNumber()));
        mTvBillWeight.setText(mData.getManifestWeight() + "kg");
        mEtTallyNumber.setText(String.valueOf(mData.getTallyNumber()));
        mEtTallyWeight.setText(mData.getTallyWeight() + "kg");
        mTvStoreName.setText("".equals(mData.getStoreName()) ? "请选择库区" : mData.getStoreName());
        mTvStoreNumber.setText((mData.getStoreNumber() == 0) ? "请选择库位" : mData.getStoreNumber() + "号库位");
        mEtTallyWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (mEtTallyWeight.getText().toString().length() != 0) {
                    if (!mEtTallyWeight.getText().toString().contains("kg")) {
                        mEtTallyWeight.setText(mEtTallyWeight.getText().toString() + "kg");
                    }
                }
            }
        });
        CheckBox[] checkBoxes = {mCbNumberError, mCbNoGoods, mCbBroken, mCbDecompose, mCbNoBills, mCbLeakage, mCbWrongTransport, mCbBurke};
        TextView[] textViews = {mTvNumberError, mTvNoGoods, mTvBroken, mTvDecompose, mTvNoBills, mTvLeakage, mTvWrongTransport, mTvBurke};
        for (int i = 0; i < checkBoxes.length; i++) {
            CheckBox checkBox = checkBoxes[i];
            TextView textView = textViews[i];
            int finalI = i;
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mErrorCodeList.add(finalI + 1);
                    textView.setTextColor(Color.RED);
                } else {
                    mErrorCodeList = mErrorCodeList.stream().filter(o -> o != finalI + 1).collect(Collectors.toList());
                    textView.setTextColor(Color.parseColor("#738598"));
                }
            });
        }
        mTvStoreName.setOnClickListener(v -> {
            List<TestBean> list22 = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                list22.add(new TestBean("库房" + i, false));
            }
            ChooseStoreroomDialog dialog = new ChooseStoreroomDialog();
            dialog.setChooseDialogInterface(position -> mTvStoreName.setText(list22.get(position).getName()));
            dialog.setData(list22, ModifyInportInfoActivity.this);
            dialog.show(getSupportFragmentManager(), "123");
        });
        mTvStoreNumber.setOnClickListener(v -> {
            List<TestBean> list22 = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                if (i == 20) {
                    list22.add(new TestBean(1, i));
                } else if (i == 25) {
                    list22.add(new TestBean(1, i));
                } else {
                    list22.add(new TestBean(0, i));
                }
            }
            PopTestDialog dialog = new PopTestDialog();
            dialog.setChooseDialogInterface(position -> mTvStoreNumber.setText(list22.get(position).getNumber() + "号库位"));
            dialog.setData(list22, ModifyInportInfoActivity.this);
            dialog.show(getSupportFragmentManager(), "123");
        });
        mTvCommitInfo.setOnClickListener(v -> {
            if (!mTvStoreName.getText().toString().equals("请选择库区") && !mTvStoreNumber.getText().toString().equals("请选择库位")) {
                if ("".equals(mEtTallyNumber.getText().toString())) {
                    ToastUtil.showToast("请输入理货件数");
                    return;
                }
                mData.setTallyNumber(Integer.valueOf(mEtTallyNumber.getText().toString()));
                String text1 = mEtTallyWeight.getText().toString();
                double weight;
                if (text1.contains("kg")) {
                    weight = Double.valueOf(text1.substring(0, text1.length() - 2));
                } else if (text1.equals("")) {
                    ToastUtil.showToast("请输入理货重量");
                    return;
                } else {
                    weight = Double.valueOf(text1);
                }
                mData.setTallyWeight(weight);
                mData.setStoreName(mTvStoreName.getText().toString());
                String text = mTvStoreNumber.getText().toString();
                mData.setStoreNumber(Integer.valueOf(text.substring(0, text.indexOf("号"))));
                mData.setErrorList(mErrorCodeList);
                mData.setNumberError(("".equals(mEtNumberError.getText().toString())) ? 0 : Integer.valueOf(mEtNumberError.getText().toString()));
                Intent intent = new Intent();
                intent.putExtra("data", mData);
                setResult(234, intent);
                finish();
            } else {
                ToastUtil.showToast("请设置库区库位等信息再提交");
            }
        });
    }
}
