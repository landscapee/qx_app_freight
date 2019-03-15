package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InPortTallyListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InPortTallyListModel;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 进港理货页面
 */
public class InPortTallyActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter{
    @BindView(R.id.mfrv_in_port_tally) MultiFunctionRecylerView mMfrvInPort;

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_port_tally;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "上一步", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "进港理货");
        mMfrvInPort.setRefreshListener(this);
        mMfrvInPort.setOnRetryLisenter(this);
        initData();
    }
    private void initData(){
        List<InPortTallyListModel> list=new ArrayList<>();
        String flightName[]={"000-999999999","000-333333333","333-666666666"};
        String startPlace[]={"CD","LOS","SH"};
        String middlePlace[]={"KM","BJ","LYG"};
        String endPlace[]={"DJ","CJ","KWM"};
        String docName[]={"AWBA","BWBA","CWBA"};
        boolean docArrived[]={true,false,true};
        int docNumber[]={3,4,6};
        int docWeight[]={300,444,666};
        int manifestNumber[]={5,1,7};
        int manifestWeight[]={333,444,666};
        int tallyNumber[]={6,8,11};
        int tallyWeight[]={345,56,888};
        boolean customsCont[]={false,true,false};
        boolean transport[]={false,false,true};
        boolean unpackagePlate[]={true,false,true};
        boolean chill[]={false,false,true};
        String storeName[]={"现货区","冷藏区","鲜活区"};
        int storeNumber[]={6,8,11};
        String exceptionSituation[]={"无异常","缺货3件","丢货100件"};
        for (int i=0;i<3;i++){
            InPortTallyListModel model=new InPortTallyListModel();
            model.setFlightName(flightName[i]);
            model.setStartPlace(startPlace[i]);
            model.setMiddlePlace(middlePlace[i]);
            model.setEndPlace(endPlace[i]);
            model.setDocName(docName[i]);
            model.setDocArrived(docArrived[i]);
            model.setDocNumber(docNumber[i]);
            model.setDocWeight(docWeight[i]);
            model.setManifestNumber(manifestNumber[i]);
            model.setManifestWeight(manifestWeight[i]);
            model.setTallyNumber(tallyNumber[i]);
            model.setTallyWeight(tallyWeight[i]);
            model.setCustomsCont(customsCont[i]);
            model.setTransport(transport[i]);
            model.setUnpackagePlate(unpackagePlate[i]);
            model.setChill(chill[i]);
            model.setStoreName(storeName[i]);
            model.setStoreNumber(storeNumber[i]);
            model.setExceptionSituation(exceptionSituation[i]);
            list.add(model);
        }
        InPortTallyListAdapter adapter=new InPortTallyListAdapter(list);
        mMfrvInPort.setLayoutManager(new LinearLayoutManager(this));
        mMfrvInPort.setAdapter(adapter);
    }
    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            initData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        initData();
    }
}
