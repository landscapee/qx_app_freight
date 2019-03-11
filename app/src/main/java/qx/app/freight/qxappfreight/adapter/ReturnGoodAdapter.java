package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class ReturnGoodAdapter extends BaseQuickAdapter<LoginResponseBean.RoleRSBean, BaseViewHolder> {


    public ReturnGoodAdapter(List<LoginResponseBean.RoleRSBean> data) {
        super(R.layout.item_horizontal, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LoginResponseBean.RoleRSBean data) {
        helper.setText(R.id.tv_rerutn_good_info, String.format(mContext.getString(R.string.format_return_good_info), "大豆", "3件","400kg","5.2m"));
        helper.setText(R.id.tv_return_good_nu,String.format(mContext.getString(R.string.format_company_info),"冻库","库位12"));
    }


}
