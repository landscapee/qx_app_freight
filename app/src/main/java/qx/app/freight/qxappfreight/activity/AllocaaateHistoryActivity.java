package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.AllocaaateHistoryAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.SearchFilghtEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.AllocaaateHitoryBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetHistoryContract;
import qx.app.freight.qxappfreight.popwindow.EditPopWindow;
import qx.app.freight.qxappfreight.presenter.GetHistoryPresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 复重历史搜索页面
 */
public class AllocaaateHistoryActivity extends BaseActivity implements GetHistoryContract.getHistoryView, EmptyLayout.OnRetryLisenter, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.mfrv_allocate_list)
    MultiFunctionRecylerView mMfrvAllocateList;
    @BindView(R.id.tv_date_start)
    TextView tvDateStart;
    @BindView(R.id.ll_select_date)
    LinearLayout llSelectTime;
    @BindView(R.id.et_flight_no)
    EditText etFlightNo;
    private AllocaaateHistoryAdapter mAdapter;
    private List<GetInfosByFlightIdBean> list;
    private int pageCurrent = 1;
    private long searchTime;
    private SearchFlightInfoBean mSearchFlightInfoBean;
    private EditPopWindow editPopWindow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String key = (String) msg.obj;
            if (TextUtils.isEmpty(key) || key.length() > 6) {
                return;
            }
            long time = (searchTime == 0) ? System.currentTimeMillis() : searchTime;
            SearchFilghtEntity entity = new SearchFilghtEntity();
            entity.setFlightNo(key);
            Log.e("tagTest", "搜索key========" + key);
            entity.setFlightDate(TimeUtils.getTime2_1(time));
            ((GetHistoryPresenter) mPresenter).searchFlightsByKey(entity);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocaaate_history;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
        getData(searchTime);
    }

    private void initView() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "复重历史");
        searchTime = new Date(System.currentTimeMillis()).getTime();
        tvDateStart.setText(TimeUtils.getTime2_1(searchTime));
        list = new ArrayList<>();
        mAdapter = new AllocaaateHistoryAdapter(list);
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(this));
        mMfrvAllocateList.setOnRetryLisenter(this);
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, AllocaaateHisDetailsActivity.class);
            intent.putExtra("dataBean", list.get(position));
            startActivity(intent);
        });
        llSelectTime.setOnClickListener(v -> {
            showDatePickerDialog(this, 2, 0, Calendar.getInstance());
        });
        //模糊搜索航班号,点击搜索按钮开始模糊搜索
        etFlightNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchFlightInfoBean = (SearchFlightInfoBean) etFlightNo.getTag(R.id.search_flight_by_key_result);
                if (mSearchFlightInfoBean != null) {
                    etFlightNo.setTag(R.id.search_flight_by_key_result, null);
                    return;
                }
                handler.removeCallbacksAndMessages(null);
                mPresenter.interruptHttp();
                Message message = handler.obtainMessage();
                message.obj = etFlightNo.getText().toString().trim().toUpperCase();
                handler.sendMessageDelayed(message, 400);
            }
        });
        etFlightNo.setOnClickListener(v -> {
            etFlightNo.setFocusable(true);
            etFlightNo.setFocusableInTouchMode(true);
            etFlightNo.requestFocus();
            InputMethodManager imm = (InputMethodManager) AllocaaateHistoryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    @Override
    public void searchFlightsByKeyResult(List<SearchFlightInfoBean> result) {
        if (editPopWindow == null) {
            editPopWindow = new EditPopWindow(this, result, etFlightNo);
            editPopWindow.showAsDropDown(etFlightNo);
        } else {
            if (editPopWindow.isShowing()) {
                editPopWindow.update(result);
            } else {
                editPopWindow.update(result);
                editPopWindow.showAsDropDown(etFlightNo);
            }
        }
        editPopWindow.setOnFlightCheckListener(() -> getData(searchTime));
    }

    private void getData(long time) {
        mPresenter = new GetHistoryPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        AllocaaateHitoryBean bean = new AllocaaateHitoryBean();
        bean.setReWeighedUserId(UserInfoSingle.getInstance().getUserId());
        bean.setSearchTime(time);
        bean.setFlightNo(etFlightNo.getText().toString().trim().toUpperCase());//将航班号加入搜索条件
        entity.setFilter(bean);
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        ((GetHistoryPresenter) mPresenter).getHistory(entity);
    }

    @Override
    public void getHistoryResult(GetHistoryBean getHistoryBean) {
        if (pageCurrent == 1) {
            list.clear();
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
        list.addAll(getHistoryBean.getRecords());
        mMfrvAllocateList.notifyForAdapter(mAdapter);
    }

    @Override
    public void toastView(String error) {
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            getData(searchTime);
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        getData(searchTime);
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
        getData(searchTime);
    }

    /**
     * 日期选择
     *
     * @param activity
     * @param themeResId
     * @param calendar
     */
    public void showDatePickerDialog(Activity activity, int themeResId, int flag, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                String strDate = "";
                String month = "";
                String day = "";
                if ((monthOfYear + 1) < 10) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = "" + dayOfMonth;
                }
                strDate = year + "-" + month + "-" + day;
                if (flag == 0) {//开始时间
                    tvDateStart.setText(strDate);
                    searchTime = TimeUtils.timeToStamp(strDate);
                    getData(searchTime);
//                    showDatePickerDialog(AllocaaateHistoryActivity.this, 2, 1, Calendar.getInstance());
                } else {//结束时间
//                    if (StringUtil.isEmpty(tvDateStart.getText().toString())) {
//                        tvDateStart.setText(startDate);
//                        tvDateEnd.setText(strDate);
//                        getTaskList();
//                    } else {
//                        if (TimeUtils.timeToStamp(strDate) >= TimeUtils.timeToStamp(tvDateStart.getText().toString())) { //开始时间需要小于等于结束时间
//                            tvDateStart.setText(startDate);
//                            tvDateEnd.setText(strDate);
//                            getTaskList();
//                        } else {
//                            ToastUtil.showToast("结束时间需要在开始时间之后！");
//                        }
//                    }
                }
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
