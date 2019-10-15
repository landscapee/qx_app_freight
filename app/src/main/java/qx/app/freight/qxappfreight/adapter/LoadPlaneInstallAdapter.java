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
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 装舱建议列表数据适配器
 */
public class LoadPlaneInstallAdapter extends BaseQuickAdapter <LoadingListBean.DataBean.ContentObjectBean.ScooterBean, BaseViewHolder> {

    private int mWidthairflag;
    private boolean notShowPull;
    private OnDataCheckListener onDataCheckListener;

    private List<String> cargos = new ArrayList <>();
    private List<String> goods = new ArrayList <>();
    SpinnerAdapter apsAdapter1;
    SpinnerAdapter apsAdapter2;

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
    }

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag, boolean notShowPull,List<String> cargos ,List<String> goods) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
        this.notShowPull = notShowPull;
        this.cargos.clear();
        this.cargos.add("");
        this.cargos.addAll(cargos);
        this.goods.clear();
        this.goods.add("");
        this.goods.addAll(goods);
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
        if (!item.isLocked()) {
            ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_unlock_data);
        } else {
            ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_lock_data);
        }
        helper.setText(R.id.tv_scooter_number, item.getScooterCode() != null ? item.getScooterCode() : "--")
                .setText(R.id.tv_uld, item.getSerialInd() != null ? item.getSerialInd() : "--")
                .setText(R.id.tv_destination,item.getDestinationStation()!= null ?item.getDestinationStation():"--")
                .setText(R.id.tv_weight, item.getWeight() != 0 ? item.getWeight() + "" : "--")
                .setText(R.id.tv_volume, item.getVolume() != 0 ? item.getVolume() + "" : "--");
        if (item.getWaybillList() != null && item.getWaybillList().size() > 0&&item.getWaybillList().get(0).getWaybillCode()!=null)
            helper.setText(R.id.tv_mailtype, item.getWaybillList().get(0).getWaybillCode().contains("xxx") ? "X" : item.getType() != null ? item.getType() : "--");
        else
            helper.setText(R.id.tv_mailtype, item.getType() != null ? item.getType() : "--");
        Button btnPull = helper.getView(R.id.tv_pull);

        notShowPull = !"BY".equals(item.getType());
        if (notShowPull) {
            btnPull.setVisibility(View.VISIBLE);
            //设置 拉下的状态
            btnPull.setOnClickListener(v -> {
                if (item.getExceptionFlag() == 1) {
                    item.setExceptionFlag(0);
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.white));
                    btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2e8));
                } else {
                    item.setExceptionFlag(1);
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                    btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.gray_8f));
                }
                onDataCheckListener.onDataChecked(item.getScooterCode());
            });
            if (item.getExceptionFlag() == 1) {
                btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.gray_8f));
            } else {
                btnPull.setTextColor(mContext.getResources().getColor(R.color.white));
                btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2e8));
            }
        } else {
            btnPull.setVisibility(View.INVISIBLE);
        }

        TextView tv1 = helper.getView(R.id.tv_scooter_number);
        TextView tv3 = helper.getView(R.id.tv_weight);
        TextView tv4 = helper.getView(R.id.tv_volume);
        TextView tv7 = helper.getView(R.id.tv_mailtype);
//            TextView tv7 = helper.getView(R.id.tv_volume);
        TextView[] tvList = {tv1, tv3,tv4, tv7};
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

//        List<String> cargos = new ArrayList();
//        cargos.add("1");
//        cargos.add("2");
//        cargos.add("3");
//        cargos.add("4");
//
//        List<String> goods = new ArrayList <>();
//        goods.add("13P");
//        goods.add("12P");
//        goods.add("21P");
//        goods.add("22P");
//        goods.add("23P");
//        goods.add("33P");
//        goods.add("32P");
//        goods.add("31P");
//        goods.add("42R");
//        goods.add("43R");
//        goods.add("44R");
//        goods.add("43L");
//        goods.add("44L");
        spBerth.setVisibility(View.VISIBLE);
//            if (item.isShowPull()) {
//                spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getBerthList());
//            } else {
        apsAdapter1 = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal,cargos);
//            }
        spBerth.setAdapter(apsAdapter1);
//        apsAdapter1 = spBerth.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter1.getCount();
        for (int i = 0; i < k; i++) {
            if (Tools.compareFist(item.getCargoName(),apsAdapter1.getItem(i).toString())) {
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
                    if (Tools.compareFist(item.getOldCargoName(),cargos.get(position))){
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
            apsAdapter2 = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, goods);
            spGoodsPos.setAdapter(apsAdapter2);
//            apsAdapter2 = spGoodsPos.getAdapter(); //得到SpinnerAdapter对象
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


    /**
     * 更新 sp的选择
     */
    public void notifySp(List<String> cargos ,List<String> goods){
        this.cargos.clear();
        this.cargos.add("");
        this.cargos.addAll(cargos);
        this.goods.clear();
        this.goods.add("");
        this.goods.addAll(goods);
//        if (apsAdapter1 != null){
//            apsAdapter1.notify();
//        }
//        if (apsAdapter2 != null){
//            apsAdapter2.notify();
//        }
    }
}
