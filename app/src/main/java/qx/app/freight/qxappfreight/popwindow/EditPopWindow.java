package qx.app.freight.qxappfreight.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SingleFlightAdapter;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;
import qx.app.freight.qxappfreight.utils.DisplayUtil;

/**
 * Created by zzq On 2020/6/12 14:17 & Copyright (C), 青霄科技
 *
 * @文档说明:
 */
public class EditPopWindow extends PopupWindow {
    private Context mContext;
    private View mView;
    private EditText mEditText;
    private List<SearchFlightInfoBean> mData;
    private SingleFlightAdapter mAdapter;
    private OnFlightCheckListener onFlightCheckListener;

    public EditPopWindow(Context context, List<SearchFlightInfoBean> data, EditText editText) {
        mContext = context;
        mEditText = editText;
        mData = data;
        mView = LayoutInflater.from(context).inflate(R.layout.popwindow_flights, null);
        setContentView(mView);
        setWindow();
        initView();
    }

    private void initView() {
        RecyclerView mRvData = mView.findViewById(R.id.rv_flight_result);
        mRvData.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SingleFlightAdapter(mData);
        mRvData.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mEditText.setTag(R.id.search_flight_by_key_result, mData.get(position));
            mEditText.setText(mData.get(position).getFlightNo());
            mEditText.setSelection(mEditText.getText().toString().length());
            dismiss();
            InputMethodManager imm = (InputMethodManager) (mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            onFlightCheckListener.onFlightChecked();
        });
    }

    private void setWindow() {
        this.setWidth(mEditText.getWidth());
        int lineHeight = DisplayUtil.dip2px(mContext, 1);//线的高度
        int height = mData.size() < 5 ? (mEditText.getHeight() + lineHeight) * mData.size() - lineHeight : (mEditText.getHeight() + lineHeight) * 5 + mEditText.getHeight() / 3;
        this.setHeight(height);
        this.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext,R.color.transparent)));
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        // 点击其他地方隐藏键盘 popupWindow
        this.update();
    }

    /**
     * 更新数据
     *
     * @param data
     */
    public void update(List<SearchFlightInfoBean> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        setWindow();
    }

    public interface OnFlightCheckListener {
        void onFlightChecked();
    }

    public void setOnFlightCheckListener(OnFlightCheckListener onFlightCheckListener) {
        this.onFlightCheckListener = onFlightCheckListener;
    }
}
