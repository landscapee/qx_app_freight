package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.fragment.ImgPreviewFragment;
import qx.app.freight.qxappfreight.widget.PreviewViewPager;

/**
 * 多图片预览界面
 */
public class ImgPreviewAct extends AppCompatActivity {
    public static final String EXTRA_PREVIEW_LIST = "previewList";
    public static final String EXTRA_POSITION = "position";
    @BindView(R.id.preview_pager)
    PreviewViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bar_layout)
    LinearLayout barLayout;
    private List<String> images = new ArrayList<>();
    private boolean isShowBar = true;

    public static void startPreview(Activity context, List<String> images, int position) {
        Intent intent = new Intent(context, ImgPreviewAct.class);
        intent.putExtra(EXTRA_PREVIEW_LIST, (ArrayList) images);
        intent.putExtra(EXTRA_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_img_preview);
        ButterKnife.bind(this);
        initView();
        registerListener();
    }

    public void initView() {
        images = (List<String>) getIntent().getSerializableExtra(EXTRA_PREVIEW_LIST);
        int position = getIntent().getIntExtra(EXTRA_POSITION, 1);
        toolbar.setTitle((position + 1) + "/" + images.size());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        viewPager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(position);
    }

    public void registerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public class SimpleFragmentAdapter extends FragmentPagerAdapter {
        SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImgPreviewFragment.getInstance(images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void switchBarVisibility() {
        barLayout.setVisibility(isShowBar ? View.GONE : View.VISIBLE);
        toolbar.setVisibility(isShowBar ? View.GONE : View.VISIBLE);
        if (isShowBar) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
        isShowBar = !isShowBar;
    }
}

