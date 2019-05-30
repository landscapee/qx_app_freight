package qx.app.freight.qxappfreight.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.MainListBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

public class StoreInfoRvAdapter extends BaseQuickAdapter<MainListBean.DataBean.SingeDataBean, BaseViewHolder> {
    private String mGoodsType;

    public StoreInfoRvAdapter(List<MainListBean.DataBean.SingeDataBean> data, String mGoodsType) {
        super(R.layout.item_store_info, data);
        this.mGoodsType = mGoodsType;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainListBean.DataBean.SingeDataBean item) {
        helper.setText(R.id.tv_goods_order, "001").setText(R.id.tv_goods_number, String.format(mContext.getString(R.string.format_store_number_goods_info), item.getSingleNumber()+"", StringUtil.formatString2(item.getSingleWeight()), StringUtil.formatString2(item.getSingleVolume())));
        helper.setText(R.id.tv_goods_info, String.format(mContext.getString(R.string.format_store_goods_info), item.getGoodsName(), item.getPackageType(), mGoodsType));
        if (!TextUtils.isEmpty(item.getStoreInfo())) {
            helper.getView(R.id.ll_store_info).setVisibility(View.VISIBLE);
            helper.getView(R.id.btn_chose_store).setVisibility(View.GONE);
            String[] info = item.getStoreInfo().split("-");
            helper.setText(R.id.tv_store_type, info[0]).setText(R.id.tv_store_number, info[1] + "号仓库");
        } else {
            helper.getView(R.id.ll_store_info).setVisibility(View.GONE);
            helper.getView(R.id.btn_chose_store).setVisibility(View.VISIBLE);
            helper.getView(R.id.btn_chose_store).setOnClickListener(v -> ToastUtil.showToast(mContext, "去选仓位"));
        }
    }
}
