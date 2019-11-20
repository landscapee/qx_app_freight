package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.NotTransportListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.SecurityCheckResult;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 未收运记录列表页面
 */
public class NotTransportListActivity extends BaseActivity {
    @BindView(R.id.srv_list)
    SlideRecyclerView mSrvList;
    private List <SecurityCheckResult> mNotTransportList = new ArrayList <>();
    private NotTransportListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_not_transport_list;
    }

    /**
     * 设置数据并返回前面的页面
     */
    private void setDataAndBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("not_transport_list", (Serializable) mNotTransportList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void businessLogic(Bundle savedInstanceState) {
//        String test=null;
//        Log.e("test","test============="+test.length());
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> setDataAndBack());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> setDataAndBack());
        toolbar.setMainTitle(Color.WHITE, "未收运记录");
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.icon_add_not_transport_record, v -> {
            mSrvList.closeMenu();
            String goodsName = getIntent().getStringExtra("goods_name");
            Intent intent = new Intent(NotTransportListActivity.this, AddNotTransportRecordActivity.class);
            intent.putExtra("goods_name", goodsName);
            NotTransportListActivity.this.startActivityForResult(intent, 124);
        });
        mNotTransportList.clear();
        mNotTransportList.addAll((ArrayList<SecurityCheckResult>)getIntent().getSerializableExtra("start_data"));
        mSrvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NotTransportListAdapter(mNotTransportList);
        setDeleteListener();
        mSrvList.setAdapter(mAdapter);
        setIsBack(true, () ->
                {
                    setDataAndBack();
                }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 124 && resultCode == RESULT_OK) {
            if (data != null) {
                mNotTransportList.add((SecurityCheckResult)data.getSerializableExtra("single_item"));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setDeleteListener() {
        mAdapter.setOnDeleteClickListener(position -> {
                    mSrvList.closeMenu();
                    mNotTransportList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
        );
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
    }
}
