package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ReturnBean;
import qx.app.freight.qxappfreight.utils.MapValue;

/**
 * TODO : xxx
 * Created by pr
 */
public class ReturnGoodAdapter extends BaseQuickAdapter<ReturnBean, BaseViewHolder> {


    public ReturnGoodAdapter(@Nullable List<ReturnBean> data) {
        super(R.layout.item_horizontal, data);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, ReturnBean item) {
        helper.setText(R.id.tv_rerutn_good_info, String.format(mContext.getString(R.string.format_number_info123),
                //名字
                item.getCargoCn(),
                //件数
                item.getNumber(),
                //体积
                0,
                //重量
                item.getWeight()));
        //库区|库位
        helper.setText(R.id.tv_return_good_nu, String.format(mContext.getString(R.string.format_company_info),(item.getRepName()==null)?"- -":item.getRepName()
        ,(item.getRepPlaceNum()==null)?"- -":item.getRepPlaceNum()));
        //运单名
        helper.setText(R.id.tv_receive_nb, MapValue.getCarTypeValue(item.getScooterType()+"")+ item.getScooterCode());
    }


}
