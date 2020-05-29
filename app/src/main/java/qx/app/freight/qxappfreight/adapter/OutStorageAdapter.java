package qx.app.freight.qxappfreight.adapter;//package qx.app.international.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.WaybillArea;
import qx.app.freight.qxappfreight.utils.StringUtil;


public class OutStorageAdapter extends BaseQuickAdapter <WaybillArea, BaseViewHolder> {

    public OutStorageAdapter(List <WaybillArea> list) {
        super(R.layout.item_out_storage, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaybillArea item) {
        //库区
        helper.setText(R.id.tv_area, item.getAreaName());
        //待提货件数
        helper.setText(R.id.tv_number,"待提件数:"+ item.getNumber());
        //待提货重量
        helper.setText(R.id.tv_weight, "待提重量:"+item.getWeight());

        EditText etOutNumber = helper.getView(R.id.et_out_number);

        //防止 复用
        if (etOutNumber.getTag()!=null){
            if (etOutNumber.getTag() instanceof TextWatcher){
                etOutNumber.removeTextChangedListener((TextWatcher)etOutNumber.getTag());
            }
        }
        TextWatcher textWatcherNum = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (StringUtil.isEmpty(etOutNumber.getText().toString())){
                        item.setOutboundNumber(0);
                    }
                    else {
                        item.setOutboundNumber(Integer.valueOf(etOutNumber.getText().toString()));
                    }
            }
        };
        etOutNumber.addTextChangedListener(textWatcherNum);
        etOutNumber.setTag(textWatcherNum);
        etOutNumber.setText(item.getNumber()+"");
    }
}
