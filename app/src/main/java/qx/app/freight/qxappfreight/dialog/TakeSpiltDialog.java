package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * created by zyy
 * 2019/10/26 17:01
 */
public class TakeSpiltDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private String positiveBtn;
    private String negativeBtn;
    private String title;
    private OnClickListener listener;
    private TextView titleTxt;
    private TextView positiveTv;
    private TextView negativeTv;

    private Spinner spBerth;
    private List<String> cargos;
    SpinnerAdapter apsAdapter1;

    private Spinner spGoods;
    private List<String> goods;
    SpinnerAdapter apsAdapter2;

    private EditText etWeight;

    private TextView tvOne,tvTwo;

    private String strBerth="",strGoods="";

    private String oldBerth, oldGoods;
    private double oldWeight;

    private int widthAirFlag = 1;//0是宽体机，1是窄体机


    public TakeSpiltDialog(Context context, List<String> cargos, List<String> goods,String cargo,String good,double weight,int widthAirFlag) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        this.cargos = cargos;
        this.goods = goods;
        this.oldBerth = cargo;
        this.oldGoods = good;
        this.oldWeight = weight;
        this.widthAirFlag = widthAirFlag;
        setCanceledOnTouchOutside(false);
    }

    public TakeSpiltDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public TakeSpiltDialog setPositiveButton(String name) {
        this.positiveBtn = name;
        return this;
    }

    public TakeSpiltDialog setNegativeButton(String name) {
        this.negativeBtn = name;
        return this;
    }

    public TakeSpiltDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public TakeSpiltDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public TakeSpiltDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_take_split);
        initView();
    }

    private void initView() {
        titleTxt = findViewById(R.id.titleTv);
        positiveTv = findViewById(R.id.positiveTv);
        negativeTv = findViewById(R.id.negativeTv);

        spBerth = findViewById(R.id.sp_berth);
        spGoods = findViewById(R.id.sp_goods_position);

        tvOne = findViewById(R.id.tv_one);
        tvTwo = findViewById(R.id.tv_two);

        etWeight = findViewById(R.id.et_weight);

        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);

        if (!TextUtils.isEmpty(positiveBtn)) {
            View positiveLine = findViewById(R.id.positiveLine);
            positiveLine.setVisibility(View.VISIBLE);

            positiveTv.setVisibility(View.VISIBLE);
            positiveTv.setText(positiveBtn);
        }

        if (!TextUtils.isEmpty(negativeBtn)) {
            negativeTv.setText(negativeBtn);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

        apsAdapter1 = new ArrayAdapter <>(mContext, R.layout.item_spinner_loading_list_normal,cargos);
        spBerth.setAdapter(apsAdapter1);
        for (int i = 0; i < apsAdapter1.getCount(); i++) {
            if (Tools.compareFist(oldBerth,apsAdapter1.getItem(i).toString())) {
                spBerth.setSelection(i, true);// 默认选中项
                strBerth = oldBerth;
                break;
            }
        }
        spBerth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBerth = cargos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });
        if (widthAirFlag == 1){
            spGoods.setVisibility(View.INVISIBLE);
            tvTwo.setVisibility(View.INVISIBLE);
        }
        else {
            spGoods.setVisibility(View.VISIBLE);
            tvTwo.setVisibility(View.VISIBLE);

            apsAdapter2 = new ArrayAdapter <>(mContext, R.layout.item_spinner_loading_list_normal,goods);
            spGoods.setAdapter(apsAdapter2);
            for (int i = 0; i < apsAdapter2.getCount(); i++) {
                if (Tools.compareFist(oldGoods,apsAdapter2.getItem(i).toString())) {
                    spGoods.setSelection(i, true);// 默认选中项
                    strGoods = oldGoods;
                    break;
                }
            }
            spGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strGoods = goods.get(position);
                }
                @Override
                public void onNothingSelected(AdapterView <?> parent) {

                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeTv:
                this.dismiss();
                break;
            case R.id.positiveTv:
                if (listener != null) {
                    if (widthAirFlag == 1){
                        if (Tools.compareFist(strBerth,oldBerth))
                            ToastUtil.showToast("不能拆分到相同的舱位/货位");
                        else if(!StringUtil.isEmpty(etWeight.getText().toString()) &&Double.valueOf(etWeight.getText().toString())> 0&&Double.valueOf(etWeight.getText().toString())<oldWeight){
                            listener.onClick(this, true,strBerth,strGoods,Double.valueOf(etWeight.getText().toString()));
                            this.dismiss();
                        }
                        else {
                            ToastUtil.showToast("拆分重量必须大于0,小于被拆分重量");
                        }
                    }
                    else {
                        if (Tools.compareFist(strGoods,oldGoods))
                            ToastUtil.showToast("不能拆分到相同的舱位/货位");
                        else if(!StringUtil.isEmpty(etWeight.getText().toString())&&Double.valueOf(etWeight.getText().toString())> 0&&Double.valueOf(etWeight.getText().toString())<oldWeight){
                            listener.onClick(this, true,strBerth,strGoods,Double.valueOf(etWeight.getText().toString()));
                            this.dismiss();
                        }
                        else {
                            ToastUtil.showToast("拆分重量必须大于0,小于被拆分重量");
                        }
                    }

                }
                else
                    this.dismiss();
                break;
        }
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, boolean confirm,String strBerth,String strGoos,Double weight);
    }
}
