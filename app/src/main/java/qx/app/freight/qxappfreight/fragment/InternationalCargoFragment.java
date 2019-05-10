package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.BaggageListActivity;
import qx.app.freight.qxappfreight.activity.InternationalCargoListActivity;
import qx.app.freight.qxappfreight.adapter.FlightListAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.contract.GetAllInternationalAndMixedFlightContract;
import qx.app.freight.qxappfreight.presenter.GetAllInternationalAndMixedFlightPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.SearchToolbar;
/**
 * 国际货物上报上报
 *
 * create by swd
 */
public class InternationalCargoFragment extends BaseFragment implements GetAllInternationalAndMixedFlightContract.getAllInternationalAndMixedFlightView , EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    RecyclerView mMfrvData;

    FlightListAdapter mAdapter;
    List<FlightLuggageBean> mList;  //筛选过后的数据
    List<FlightLuggageBean> mListTemp; //原始数据

    private String searchString = ""; //搜索框里面的搜索关键字

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_international_cargo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPresenter = new GetAllInternationalAndMixedFlightPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mListTemp = new ArrayList<>();
        mAdapter = new FlightListAdapter(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getContext(), InternationalCargoListActivity.class).putExtra("flightBean",mList.get(position)));
        });
        mMfrvData.setAdapter(mAdapter);
        //行李上报-搜索逻辑
        SearchToolbar searchToolbar = ((TaskFragment)getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入航班号", new SearchToolbar.OnTextSearchedListener() {
            @Override
            public void onSearched(String text) {
                searchString = text;
                seachWithNum();
            }
        });
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setMinutes("120");
        ((GetAllInternationalAndMixedFlightPresenter) mPresenter).addScooter(entity);
    }

    /**
     * 通过条件筛选数据
     */
    private void seachWithNum() {
        mList.clear();
        if(TextUtils.isEmpty(searchString)){
            mList.addAll(mListTemp);
        }else{
            for(FlightLuggageBean itemData: mListTemp){
                if(itemData.getFlightNo().toLowerCase().contains(searchString.toLowerCase())){
                    mList.add(itemData);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void getAllInternationalAndMixedFlightResult(List<FlightLuggageBean> flightLuggageBeans) {
        mListTemp.clear();
        mListTemp.addAll(flightLuggageBeans);
        seachWithNum();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("22222", "toastView: " + error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }
}