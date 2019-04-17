package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import qx.app.freight.qxappfreight.R;

/**
 * 顶部通用的状态栏
 */
public class SearchToolbar extends LinearLayout {
    private EditText mEtSearch;
    private ImageView mIvClose;

    public SearchToolbar(Context context) {
        this(context, null);
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setHintAndListener(String hint, OnTextSearchedListener listener) {
        mEtSearch.setHint(hint);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    listener.onSearched(s.toString().trim());
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
                    listener.onSearched("");
                }
            }
        });
    }

    public View getCloseView() {
        return mIvClose;
    }

    public EditText getSearchView() {
        return mEtSearch;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_search_toolbar, this);
        mEtSearch = findViewById(R.id.et_search_text);
        mIvClose = findViewById(R.id.iv_close_search);
    }

    public interface OnTextSearchedListener {
        void onSearched(String text);
    }
}
