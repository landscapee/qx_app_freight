package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 装舱建议列表数据适配器
 */
public class LoadPlaneInstallAdapter extends BaseQuickAdapter <LoadingListBean.DataBean.ContentObjectBean.ScooterBean, BaseViewHolder> {

    private int mWidthairflag;
    private boolean notShowPull;
    private OnDataCheckListener onDataCheckListener;

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
    }

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag, boolean notShowPull) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
        this.notShowPull = notShowPull;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean.ContentObjectBean.ScooterBean item) {
        Spinner spBerth = helper.getView(R.id.sp_berth);
        Spinner spGoodsPos = helper.getView(R.id.sp_goods_position);

        ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_unlock_data);
        helper.getView(R.id.iv_lock_status).setOnClickListener(v -> {
            if (item.isLocked()) {
                ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_unlock_data);
            } else {
                ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_lock_data);
            }
            item.setLocked(!item.isLocked());
//            onLockClickListener.onLockClicked(helper.getAdapterPosition());
        });

        helper.setText(R.id.tv_scooter_number, item.getScooterCode() != null ? item.getScooterCode() : "--")
                .setText(R.id.tv_uld, item.getSerialInd() != null ? item.getSerialInd() : "--")
                .setText(R.id.tv_destination,item.getDestinationStation()!= null ?item.getDestinationStation():"--")
                .setText(R.id.tv_weight, item.getWeight() != 0 ? item.getWeight() + "" : "--");
        if (item.getWaybillList() != null && item.getWaybillList().size() > 0)
            helper.setText(R.id.tv_mailtype, item.getWaybillList().get(0).getWaybillCode().contains("xxx") ? "X" : item.getType() != null ? item.getType() : "--");
        else
            helper.setText(R.id.tv_mailtype, item.getType() != null ? item.getType() : "--");
        Button btnPull = helper.getView(R.id.tv_pull);
        if (notShowPull) {
            btnPull.setVisibility(View.VISIBLE);
            //设置 拉下的状态
            btnPull.setOnClickListener(v -> {
                if (item.getExceptionFlag() == 1) {
                    item.setExceptionFlag(0);
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
                } else {
                    item.setExceptionFlag(1);
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                }
                onDataCheckListener.onDataChecked(item.getScooterCode());
            });
            if (item.getExceptionFlag() == 1) {
                btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
            }
        } else {
            btnPull.setVisibility(View.GONE);
        }

        TextView tv1 = helper.getView(R.id.tv_scooter_number);
        TextView tv3 = helper.getView(R.id.tv_weight);
        TextView tv7 = helper.getView(R.id.tv_mailtype);
//            TextView tv7 = helper.getView(R.id.tv_volume);
        TextView[] tvList = {tv1, tv3, tv7};
        if (item.getSpecialCode() != null && item.getSpecialCode().equals("AVI")) {//活体颜色标注
            helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        } else if (item.getSpecialCode() != null && item.getSpecialCode().equals(Constants.DANGER)) {//枪支
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.orangered));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        } else if (item.getWaybillList()!=null&&item.getWaybillList().size()> 0&&item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        } else {
            helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        }

        List<String> cargos = new ArrayList();
        cargos.add("1H");
        cargos.add("2H");
        cargos.add("3H");
        cargos.add("4H");

        List<String> goods = new ArrayList <>();
        goods.add("11");
        goods.add("22");
        goods.add("33");
        goods.add("44");
        spBerth.setVisibility(View.VISIBLE);
//            if (item.isShowPull()) {
//                spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getBerthList());
//            } else {
        ArrayAdapter <String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal,cargos);
//            }
        spBerth.setAdapter(spinnerAdapter);
        SpinnerAdapter apsAdapter1 = spBerth.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter1.getCount();
        for (int i = 0; i < k; i++) {
            if (item.getCargoName().equals(apsAdapter1.getItem(i).toString())) {
                spBerth.setSelection(i, true);// 默认选中项
                break;
            }
        }
        spBerth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int pos;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (item.isLocked()) {
                    ToastUtil.showToast("数据已锁定，无法修改");
                    spBerth.setSelection(pos, true);
                } else {
                    pos = position;
                    if (item.getOldCargoName().equals(cargos.get(position))){
                        item.setChange(false);
                    }
                    else {
                        item.setCargoName(cargos.get(position));
                        item.setChange(true);
                        onDataCheckListener.onDataChecked(item.getScooterCode());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (TextUtils.isEmpty(item.getLocation())) {//有货位数据
            spGoodsPos.setVisibility(View.GONE);
        } else {
            spGoodsPos.setVisibility(View.VISIBLE);
//                if (item.isShowPull()) {
//                    spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getGoodsPosList());
//                } else {
            ArrayAdapter<String> spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, goods);
            spGoodsPos.setAdapter(spGoodsAdapter);
            SpinnerAdapter apsAdapter2 = spGoodsPos.getAdapter(); //得到SpinnerAdapter对象
            int j = apsAdapter2.getCount();
            for (int i = 0; i < j; i++) {
                if (item.getLocation().equals(apsAdapter2.getItem(i).toString())) {
                    spGoodsPos.setSelection(i, true);// 默认选中项
                    break;
                }
            }
            spGoodsPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int pos;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (item.isLocked()) {
                        ToastUtil.showToast("数据已锁定，无法修改");
                        spGoodsPos.setSelection(pos, true);
                    } else {
                        pos = position;
                        if (item.getOldLocation().equals(goods.get(position))){
                            item.setChange(false);
                        }
                        else {
                            item.setLocation(goods.get(position));
                            item.setChange(true);
                            onDataCheckListener.onDataChecked(item.getScooterCode());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //展示 板车下的 运单
        RecyclerView rvBill=helper.getView(R.id.rv_all_bill);
        List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean> data =item.getWaybillList();
        if (data!=null&&data.size()!=0){
            LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean title = new LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean();
            title.setTitle(true);
            data.add(0,title);
            rvBill.setLayoutManager(new LinearLayoutManager(mContext));
            rvBill.setAdapter(new OnBoardBillsAdapter(data));
        }
        helper.itemView.setOnClickListener(v -> {
            if (item.isShow())
                rvBill.setVisibility(View.GONE);
            else
                rvBill.setVisibility(View.VISIBLE);

            item.setShow(!item.isShow());
        });

        if (item.isShow())
            rvBill.setVisibility(View.VISIBLE);
        else
            rvBill.setVisibility(View.GONE);
    }
    public interface OnDataCheckListener {
        void onDataChecked(String scooterId);
    }

    public void setOnDataCheckListener(OnDataCheckListener onDataCheckListener) {
        this.onDataCheckListener = onDataCheckListener;
    }
}
