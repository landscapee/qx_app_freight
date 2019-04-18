package qx.app.freight.qxappfreight.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 拉货上报信息列表适配器
 */
public class PullGoodsInfoAdapter extends BaseMultiItemQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private PullGoodsInfoAdapter.OnDeleteClickLister mDeleteClickListener;
    private PullGoodsInfoAdapter.OnTextWatcher onTextWatcher;

    public PullGoodsInfoAdapter(List<TransportTodoListBean> data) {
        super(data);
        addItemType(Constants.TYPE_PULL_BOARD, R.layout.item_pull_goods_board_info);
        addItemType(Constants.TYPE_PULL_BILL, R.layout.item_pull_goods_bill_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {
        if (item.getInfoType() == Constants.TYPE_PULL_BILL) {
            helper.setText(R.id.tv_bill_number, item.getBillNumber());
        }
        helper.setText(R.id.tv_board_number, MapValue.getCarTypeValue(item.getTpScooterType()) + item.getTpScooterCode());//板车类型
        View viewDelete = helper.getView(R.id.ll_delete);
        //件数 - 重量 - 体积
        helper.setText(R.id.tv_goods_info, String.format(mContext.getString(R.string.format_goods_info)
                , item.getTpCargoNumber()
                , item.getTpCargoWeight()
                , item.getTpCargoVolume()));
        //航班号
        helper.setText(R.id.tv_flight_number, item.getTpFlightNumber());
        //机位号
        helper.setText(R.id.tv_place_number, item.getTpFlightLocate());
        // 时间
        helper.setText(R.id.tv_plan_time, TimeUtils.getHMDay(item.getTpFlightTime()));
        //舱位
        helper.setText(R.id.tv_cangwei_info, item.getTpFreightSpace());
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
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
    }

    public void setOnDeleteClickListener(PullGoodsInfoAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

    public interface OnTextWatcher {
        void onNumberTextChanged(int index, EditText etNumber);

        void onWeightTextChanged(int index, EditText etWeight);
    }

    public void setOnTextWatcher(OnTextWatcher onTextWatcher) {
        this.onTextWatcher = onTextWatcher;
    }
}
