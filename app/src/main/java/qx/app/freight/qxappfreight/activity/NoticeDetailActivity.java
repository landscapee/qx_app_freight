package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
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
//    @BindView(R.id.notice_webview)
//    WebView noticeWebview;
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
//        initWeb();
    }

//    private void initWeb() {
//        //声明WebSettings子类
//        WebSettings webSettings = noticeWebview.getSettings();
//        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
////        //支持插件
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setSupportMultipleWindows(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//        noticeWebview.loadDataWithBaseURL(null,mData.getContent(), "text/html" , "utf-8", null);
//        noticeWebview.setWebChromeClient(new WebChromeClient());
//        noticeWebview.setWebViewClient(new WebViewClient());
//    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "消息提醒");
    }

    private void initView() {
        tvTitle.setText(mData.getTitle());
        tvName.setText("发布单位："+mData.getCreateOrg());
        tvTime.setText( TimeUtils.date2Tasktime6(mData.getCreateDate()));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tvContent.setText(Html.fromHtml(mData.getContent()));
//        tvContent.setText(mData.getContent());
        tvCreatPerson.setText(mData.getCreateUser());
    }

}
