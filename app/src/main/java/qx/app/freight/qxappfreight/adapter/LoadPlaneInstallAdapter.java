package qx.app.freight.qxappfreight.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwenfeng.library.pulltorefresh.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.MainActivity;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 装舱建议列表数据适配器
 */
public class LoadPlaneInstallAdapter extends BaseQuickAdapter <LoadingListBean.DataBean.ContentObjectBean.ScooterBean, BaseViewHolder> {

    private int mWidthairflag;
    private boolean notShowPull;

    private boolean showLock;
    private OnDataCheckListener onDataCheckListener;

    private List <String> cargos = new ArrayList <>();
    private List <String> goods = new ArrayList <>();

    private PopupWindow windowPopList;
    private View listView;
    private RecyclerView rcList;
    private BerthAdapter berthAdapter;

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
    }

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag, boolean notShowPull, List <String> cargos, List <String> goods) {
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

    public void setShowLock(boolean showLock) {
        this.showLock = showLock;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean.ContentObjectBean.ScooterBean item) {

        LinearLayout llBerthDrop = helper.getView(R.id.ll_berth_drop); //舱位选择
        LinearLayout llGoodsDrop = helper.getView(R.id.ll_goods_drop);//货位选择

        ImageView lock = helper.getView(R.id.iv_lock_status);
        if (showLock) {
            lock.setVisibility(View.VISIBLE);
        } else
            lock.setVisibility(View.INVISIBLE);

        helper.getView(R.id.iv_lock_status).setOnClickListener(v -> {
            if(item.isSplit()){
                ToastUtil.showToast("拆下的板车不能锁定");
                return;
            }
            onDataCheckListener.onLockClicked(helper.getAdapterPosition());
        });
        if (item.getLock() == 0 || item.getLock() == 3) {
            if (item.getLock() == 3) {
                //数据发生变化 自动解锁
            }
            lock.setImageResource(R.mipmap.icon_unlock_data);
        } else {
            lock.setImageResource(R.mipmap.icon_lock_data);
        }
        String weightAndVolume = "";
        if (item.getWeight() > 0) {
            weightAndVolume = weightAndVolume + item.getWeight() + "/";
        } else {
            weightAndVolume = weightAndVolume + "--/";
        }
        if (!StringUtil.isEmpty(item.getSpecialCode())) {
            weightAndVolume = weightAndVolume + Tools.getVolumeForSpCode(item.getSpecialCode());
        } else {
            weightAndVolume = weightAndVolume + "--";
        }
        helper.setText(R.id.tv_scooter_number, !item.isSplit() && item.getSerialInd() != null ? item.getSerialInd() : item.getScooterCode() != null ? item.getScooterCode() : "--")
                .setText(R.id.tv_destination, item.getDestinationStation() != null ? item.getDestinationStation() : "--")
                .setText(R.id.tv_berth, item.getCargoName())
                .setText(R.id.tv_goods, item.getLocation())
                .setText(R.id.tv_weight, weightAndVolume);

        if (item.getWaybillList() != null && item.getWaybillList().size() > 0 && item.getWaybillList().get(0).getWaybillCode() != null)
            helper.setText(R.id.tv_mailtype, item.getWaybillList().get(0).getWaybillCode().contains("xxx") ? "X" : item.getType() != null ? item.getType() : "--");
        else
            helper.setText(R.id.tv_mailtype, item.getType() != null ? item.getType() : "--");

        Button btnPull = helper.getView(R.id.tv_pull);
        if ("C".equals(item.getType()) || "M".equals(item.getType()) || "X".equals(item.getType())) //只有 类型为 C，M,X的 板车 才能拉回
            notShowPull = true;
        else
            notShowPull = false;

        if (notShowPull) {
            btnPull.setVisibility(View.VISIBLE);
            //设置 拉下的状态
            btnPull.setOnClickListener(v -> {
                if (item.getExceptionFlag() == 1) {
                    item.setExceptionFlag(0);
                    btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2e8));
                } else {
                    item.setExceptionFlag(1);
                    btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.gray_8f));
                }
                onDataCheckListener.onDataChecked(item.getScooterCode());
            });
            if (item.getExceptionFlag() == 1) {
                btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.gray_8f));
            } else {
                btnPull.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2e8));
            }
        } else {
            btnPull.setVisibility(View.INVISIBLE);
        }
        Button btnTake = helper.getView(R.id.tv_take);
        btnTake.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            onDataCheckListener.onTakeSplit(helper.getAdapterPosition());

        });
        if (item.isSplit()) {
            btnTake.setText("X");
            btnTake.setBackgroundColor(mContext.getResources().getColor(R.color.gray_8f));
        } else {
            btnTake.setText("拆");
            btnTake.setBackgroundColor(mContext.getResources().getColor(R.color.green_45b));
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
        } else if (item.getWaybillList() != null && item.getWaybillList().size() > 0 && item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
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
        //调舱===========================
        llBerthDrop.setOnClickListener(v -> {
            if (item.getLock() == 1){
                ToastUtil.showToast("该条数据已锁定");
                return;
            }
            showPopList(cargos,item,1,llBerthDrop);
        });
        llGoodsDrop.setOnClickListener(v -> {
            if (item.getLock() == 1){
                ToastUtil.showToast("该条数据已锁定");
                return;
            }
            if (!StringUtil.isEmpty(item.getCargoName())){
                List <String> goodsTemp = new ArrayList <>();
                String cargoFirst = item.getCargoName().substring(0,1);
                for (String str :goods){
                    if (!StringUtil.isEmpty(str)){
                        String goodsFirst = str.substring(0,1);
                        if (Tools.compareFist(cargoFirst,goodsFirst)){
                            goodsTemp.add(str);
                        }
                    }

                }
                if (goodsTemp.size()>0)
                    showPopList(goodsTemp,item,2,llGoodsDrop);
                else
                    ToastUtil.showToast("该舱位下没有货位信息");
            }
            else {
                ToastUtil.showToast("请先选择舱位");
            }


        });

        if (TextUtils.isEmpty(item.getLocation())) {//没有有货位数据
            if (mWidthairflag == 0) {
                helper.setVisible(R.id.ll_goods_drop, true);
            } else {
                helper.setGone(R.id.ll_goods_drop, false);
            }
        } else {
            helper.setVisible(R.id.ll_goods_drop, true);
        }
        //展示 板车下的 运单
        RecyclerView rvBill = helper.getView(R.id.rv_all_bill);
        List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean> data = item.getWaybillList();
        if (data != null && data.size() != 0) {
            LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean title = new LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean();
            title.setTitle(true);
            data.add(0, title);
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

        void onTakeSplit(int position);

        void onLockClicked(int position);

    }

    public void setOnDataCheckListener(OnDataCheckListener onDataCheckListener) {
        this.onDataCheckListener = onDataCheckListener;
    }


    /**
     * 更新 sp的选择
     */
    public void notifySp(List <String> cargos, List <String> goods) {
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

    private void showPopList(List<String> list,LoadingListBean.DataBean.ContentObjectBean.ScooterBean item,int flag,LinearLayout llClick){
        if ( windowPopList == null) {
            listView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.popup_berth_list, null);
            windowPopList = new PopupWindow(listView,
                    DisplayUtil.dp2Px(mContext, 80),
                    DisplayUtil.dp2Px(mContext, 200));
        }
        //软键盘不会挡着popupwindow
        windowPopList.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        windowPopList.setBackgroundDrawable(getDrawable());//设置背景透明以便点击外部消失
        windowPopList.setOutsideTouchable(true);//点击外部收起
        windowPopList.setFocusable(true);
        rcList = listView.findViewById(R.id.rc_berth);
        rcList.setLayoutManager(new LinearLayoutManager(mContext));
        berthAdapter = new BerthAdapter(list);
        rcList.setAdapter(berthAdapter);
        berthAdapter.setOnItemClickListener((adapter, view, position1) -> {
            if (flag == 1){
                if (!Tools.compareFist(item.getCargoName(),list.get(position1))){
                    item.setCargoName(list.get(position1));
                    item.setLocation("");
                    //设置舱位是否发生改变
                    if (item.getOldCargoName().equals(item.getCargoName())&&item.getOldLocation().equals(item.getLocation())) {
                        item.setChange(false);
                    }
                    else {
                        item.setChange(true);
                    }
                    onDataCheckListener.onDataChecked(item.getScooterCode());
                }
            }
            else {
                if (!item.getLocation().equals(list.get(position1))){
                    item.setLocation(list.get(position1));
                    //设置货位是否发生改变
                    if (item.getOldCargoName().equals(item.getCargoName())&&item.getOldLocation().equals(item.getLocation())) {
                        item.setChange(false);
                    }
                    else {
                        item.setChange(true);
                    }
                    onDataCheckListener.onDataChecked(item.getScooterCode());
                }
            }
            windowPopList.dismiss();
            notifyDataSetChanged();
        });
        windowPopList.setAnimationStyle(R.style.animTranslate);
        windowPopList.showAsDropDown(llClick,  0, 20);
    }

    /**
     * 生成一个 透明的背景图片
     * @return
     */
    private Drawable getDrawable() {
        ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
        bgdrawable.getPaint().setColor(mContext.getResources().getColor(android.R.color.transparent));
        return bgdrawable;
    }

}
