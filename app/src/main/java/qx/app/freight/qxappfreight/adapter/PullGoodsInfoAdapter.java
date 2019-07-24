package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * 拉货上报信息列表适配器
 */
public class PullGoodsInfoAdapter extends BaseMultiItemQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private OnDeleteClickLister mDeleteClickListener;
    private OnTextWatcher onTextWatcher;
    private OnBindBoardListener onBindBoardListener;

    public PullGoodsInfoAdapter(List<TransportTodoListBean> data) {
        super(data);
        addItemType(Constants.TYPE_PULL_BOARD, R.layout.item_pull_goods_board_info);
        addItemType(Constants.TYPE_PULL_BILL, R.layout.item_pull_goods_bill_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {
        switch (item.getInfoStatus()) {
            case Constants.TYPE_PULL_BACK_NORMAL:
                helper.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case Constants.TYPE_PULL_BACK_SUCCESS:
                helper.itemView.setBackgroundColor(Color.parseColor("#DBFFE4"));
                break;
            case Constants.TYPE_PULL_BACK_FAILED:
                helper.itemView.setBackgroundColor(Color.parseColor("#FFE3E3"));
                break;
        }
        ImageView ivPullType = helper.getView(R.id.iv_pull_type);
        if (item.isRemoteData()) {
            ivPullType.setImageResource(R.mipmap.icon_pei);
        } else {
            ivPullType.setImageResource(R.mipmap.icon_jian);
        }
        helper.setText(R.id.tv_board_number, MapValue.getCarTypeValue(item.getTpScooterType()) + item.getTpScooterCode());//板车类型
        //舱位
        helper.setText(R.id.tv_cangwei_info, StringUtil.toText(item.getTpFreightSpace()));
        //件数 - 重量 - 体积
        helper.setText(R.id.tv_goods_info, String.format(mContext.getString(R.string.format_goods_info)
                , item.getTpCargoNumber()
                , item.getTpCargoWeight()
                , item.getTpCargoVolume()));
        CheckBox cbSelect = helper.getView(R.id.cb_select);
        cbSelect.setChecked(item.isSelected2Commit());
        cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected2Commit(isChecked);
            buttonView.setChecked(isChecked);
        });
        switch (item.getInfoType()) {
            case Constants.TYPE_PULL_BILL:
                TextView tvBindBoard = helper.getView(R.id.tv_bind_board);
                tvBindBoard.setOnClickListener(v -> onBindBoardListener.onBindBoard(helper.getAdapterPosition()));
                if (TextUtils.isEmpty(item.getTpScooterCode())){
                    helper.getView(R.id.tv_board_number).setVisibility(View.INVISIBLE);
                    helper.getView(R.id.tv_bind_board).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.tv_board_number).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_bind_board).setVisibility(View.GONE);
                }
                LinearLayout llEditInfo = helper.getView(R.id.ll_edit_info);
                if (item.isRemoteData()) {
                    llEditInfo.setVisibility(View.GONE);
                } else {
                    llEditInfo.setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.tv_bill_number, StringUtil.toText(item.getBillNumber()));
                EditText etBillNumber = helper.getView(R.id.et_pull_number);
                etBillNumber.setText(item.getTpCargoNumber() == 0 ? "1" : String.valueOf(item.getTpCargoNumber()));
                item.setPullInNumber(item.getTpCargoNumber() == 0 ? 1 : item.getTpCargoNumber());
                EditText etBillWeight = helper.getView(R.id.et_pull_weight);
                etBillWeight.setText(item.getTpCargoWeight() == 0 ? "1" : String.valueOf(item.getTpCargoWeight()));
                item.setPullInWeight(item.getTpCargoWeight() == 0 ? 1 : item.getTpCargoWeight());
                etBillNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (onTextWatcher != null) {
                            onTextWatcher.onNumberTextChanged(helper.getAdapterPosition(), etBillNumber);
                        }
                    }
                });
                etBillWeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (onTextWatcher != null) {
                            onTextWatcher.onWeightTextChanged(helper.getAdapterPosition(), etBillWeight);
                        }
                    }
                });
                ImageView ivDelete = helper.getView(R.id.iv_delete);
                ivDelete.setOnClickListener(v -> {
                    mDeleteClickListener.onDeleteClick(helper.getAdapterPosition());
                });
                TextView tvChangeBoard = helper.getView(R.id.tv_change_board);
                tvChangeBoard.setOnClickListener(v -> {
                    onBindBoardListener.onBindBoard(helper.getAdapterPosition());
                });
                break;
            case Constants.TYPE_PULL_BOARD:
                LinearLayout llDelete = helper.getView(R.id.ll_delete);
                llDelete.setOnClickListener(v -> {
                    mDeleteClickListener.onDeleteClick(helper.getAdapterPosition());
                });
                break;
        }
    }


    public interface OnDeleteClickLister {
        void onDeleteClick(int position);
    }

    public interface OnBindBoardListener {
        void onBindBoard(int position);
    }

    public interface OnTextWatcher {
        void onNumberTextChanged(int index, EditText etNumber);

        void onWeightTextChanged(int index, EditText etWeight);
    }

    public void setOnTextWatcher(OnTextWatcher onTextWatcher) {
        this.onTextWatcher = onTextWatcher;
    }

    public void setOnDeleteClickListener(PullGoodsInfoAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public void setOnBindBoardListener(OnBindBoardListener onBindBoardListener) {
        this.onBindBoardListener = onBindBoardListener;
    }
}
