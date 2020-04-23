package qx.app.freight.qxappfreight.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.OutStorageAdapter;
import qx.app.freight.qxappfreight.bean.response.WaybillArea;
import qx.app.freight.qxappfreight.utils.DisplayUtil;

/**
 * 项目名称：qx_pad_international
 * 创建人：张耀 * 创建时间：2020/4/19 11:34
 * 类描述：展示出库 货物库区库位
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class OutStoragePopWindow extends PopupWindow {

    private List <WaybillArea> mDataList ;
    private OnItemSelectedLisener mOnItemSelectedLisener;
    private RecyclerView mRvRoleList;

    private Button btnSure;
    private ImageView ivClose;

    private OutStorageAdapter mOutStorageAdapter;

    private Context context;

    public OutStoragePopWindow(Context context, List<WaybillArea> mDataList, OnItemSelectedLisener onItemSelectdLisener) {
        mOnItemSelectedLisener = onItemSelectdLisener;
        this.context = context;
        this.mDataList = mDataList;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_out_storage, null);

        mRvRoleList = view.findViewById(R.id.rc_out_storage);
        btnSure = view.findViewById(R.id.btn_sure);
        ivClose = view.findViewById(R.id.iv_close);

        mRvRoleList.setLayoutManager(new LinearLayoutManager(context));
        mOutStorageAdapter = new OutStorageAdapter(mDataList);
        mRvRoleList.setAdapter(mOutStorageAdapter);

        btnSure.setOnClickListener(v -> {
            mOnItemSelectedLisener.resultData(mDataList);
            dismiss();
        });
        ivClose.setOnClickListener(v -> {
            dismiss();
        });


        this.setWidth(RecyclerView.LayoutParams.MATCH_PARENT);
        this.setHeight(DisplayUtil.getMobileHeight(context)/5*3);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        //软键盘不会挡着popupwindow
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setAnimationStyle(R.style.anim_bottom_bottom);   // 设置窗口显示的动画效果
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        // 点击其他地方隐藏键盘 popupWindow
        this.update();

        setContentView(view);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.5f,(Activity) context);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1f,(Activity) context);
        super.dismiss();
    }

    public interface OnItemSelectedLisener {
        void resultData(List <WaybillArea> waybillAreaBeans);
    }

    /**
     * 设置窗口
     */
    public void setBackgroundAlpha(float bgAlpha, Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        window.setAttributes(lp);
    }
}
