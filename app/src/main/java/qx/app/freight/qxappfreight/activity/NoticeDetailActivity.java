package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 公告详情页面
 * Created by swd
 */
public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_creat_person)
    TextView tvCreatPerson;
    private NoticeViewBean mData;
    private CustomToolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        mData = (NoticeViewBean) getIntent().getSerializableExtra("NoticeViewBean");
        initTitle();
        initView();
    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "消息提醒");
    }

    private void initView() {
        tvTitle.setText(mData.getTitle());
        tvName.setText("发布单位："+mData.getCreateOrg());
        tvTime.setText( TimeUtils.date2Tasktime6(mData.getCreateDate()));
        tvContent.setText(mData.getContent());
        tvCreatPerson.setText(mData.getCreateUser());
    }

}
