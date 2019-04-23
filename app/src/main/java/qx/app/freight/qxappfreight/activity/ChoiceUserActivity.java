package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ChoiceUserAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.widget.CustomToolbar;


public class ChoiceUserActivity extends BaseActivity {
    @BindView(R.id.gv_choice)
    GridView GvChoice;


    private TestInfoListBean mList;
    private ChoiceUserAdapter adapter;


    public static void startActivity(Activity context, TestInfoListBean mList) {
        Intent intent = new Intent(context, ChoiceUserActivity.class);
        intent.putExtra("TestInfoListBean", mList);
        intent.putExtras(intent);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choice_user;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "选择角色");
        initView();
    }

    private void initView() {
        mList = (TestInfoListBean) getIntent().getSerializableExtra("TestInfoListBean");
        adapter = new ChoiceUserAdapter(this, mList.getFreightInfo());
        GvChoice.setAdapter(adapter);
        GvChoice.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra("Choice", mList.getFreightInfo().get(position));
            setResult(33, intent);
            finish();
        });
//        BtSure.setOnClickListener(v -> finish());
    }


}
