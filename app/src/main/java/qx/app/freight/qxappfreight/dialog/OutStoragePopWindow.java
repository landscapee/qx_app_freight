package qx.app.freight.qxappfreight.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.OutStorageAdapter;
import qx.app.freight.qxappfreight.bean.response.WaybillArea;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.utils.DisplayUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 项目名称：qx_pad_international
 * 创建人：张耀 * 创建时间：2020/4/19 11:34
 * 类描述：展示出库 货物库区库位
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class OutStoragePopWindow extends PopupWindow {

    private List <WaybillArea> mDataList;
    private WaybillsBean waybillsBean;
    private OnItemSelectedLisener mOnItemSelectedLisener;
    private RecyclerView mRvRoleList;
    private TextView tvTime;

    private Button btnSure;
    private ImageView ivClose;

    private OutStorageAdapter mOutStorageAdapter;

    private Context context;

    private long time = 0;//出库时间

    public OutStoragePopWindow(Context context, List <WaybillArea> mDataList, WaybillsBean waybillsBean, OnItemSelectedLisener onItemSelectdLisener) {
        mOnItemSelectedLisener = onItemSelectdLisener;
        this.context = context;
        this.mDataList = mDataList;
        this.waybillsBean = waybillsBean;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_out_storage, null);

        mRvRoleList = view.findViewById(R.id.rc_out_storage);
        btnSure = view.findViewById(R.id.btn_sure);
        ivClose = view.findViewById(R.id.iv_close);
        tvTime = view.findViewById(R.id.tv_time);

        mRvRoleList.setLayoutManager(new LinearLayoutManager(context));
        mOutStorageAdapter = new OutStorageAdapter(mDataList);
        mRvRoleList.setAdapter(mOutStorageAdapter);
        btnSure.setOnClickListener(v -> {
            mOnItemSelectedLisener.resultData(mDataList, time);
            dismiss();
        });
        ivClose.setOnClickListener(v -> {
            dismiss();
        });
        tvTime.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDatePicker();
            } else {
                showDatePickerLow();
            }
        });

        this.setWidth(RecyclerView.LayoutParams.MATCH_PARENT);
        this.setHeight(DisplayUtil.getMobileHeight(context) / 5 * 3);
        this.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.transparent)));
        //软键盘不会挡着popupwindow
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setAnimationStyle(R.style.anim_bottom_bottom);   // 设置窗口显示的动画效果
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        // 点击其他地方隐藏键盘 popupWindow
        this.update();

        setContentView(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                time = TimeUtils.timeToStamp(year + "-" + (month + 1) + "-" + dayOfMonth + "");
            }
        });
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(waybillsBean.getChargeTime());
        datePickerDialog.show();
    }

    @SuppressLint("ResourceType")
    private void showDatePickerLow() {
        Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        // 绑定监听器(How the parent is notified that the date is set.)
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, 2, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                time = TimeUtils.timeToStamp(year + "-" + (month + 1) + "-" + dayOfMonth + "");
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(waybillsBean.getChargeTime());
        datePickerDialog.show();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (time == 0) {
            time = new Date().getTime();
        }
        tvTime.setText(TimeUtils.getTime2_1(time));
        setBackgroundAlpha(0.5f, (Activity) context);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1f, (Activity) context);
        super.dismiss();
    }

    public interface OnItemSelectedLisener {
        void resultData(List <WaybillArea> waybillAreaBeans, long time);
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
