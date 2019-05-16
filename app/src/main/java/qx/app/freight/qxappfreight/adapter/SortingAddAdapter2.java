package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.request.InventoryUbnormalGoods;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;

/**
 * 分拣 新增
 * <p>
 * create by guohao -2019/4/26
 */
public class SortingAddAdapter2 extends BaseQuickAdapter<InventoryUbnormalGoods, BaseViewHolder> {

    FragmentManager manager;
    private OnSlectPicListener onSlectPicListener;
    private OnExceptionChooseListener onExceptionChooseListener;

    Context context;
    List<InventoryUbnormalGoods> myData;

    public SortingAddAdapter2(Context context, @Nullable List<InventoryUbnormalGoods> data) {
        super(R.layout.item_sorting_add, data);
        this.context = context;
        this.myData = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryUbnormalGoods item) {

        //显示序号
        helper.setText(R.id.tv_sorting_postion, helper.getAdapterPosition() + 1 + "");

        //显示异常类型
        if (item.getUbnormalType()== 0) {
            //什么都不干
        }else {
            helper.setText(R.id.tv_exception_type_choose, ExceptionUtils.typeToString(item.getUbnormalType()));

            ((EditText) helper.getView(R.id.edit_exception_num)).setText((item.getUbnormalNumber() +""));

        }
        //选择异常类型
        helper.getView(R.id.tv_exception_type_choose).setOnClickListener(listener -> {
            onExceptionChooseListener.onExceptionChoose(helper.getAdapterPosition());
        });
        //设置异常件数
        ((EditText) helper.getView(R.id.edit_exception_num)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    item.setUbnormalNumber(Integer.valueOf(s.toString().trim()));
                } else {
                    item.setUbnormalNumber(0);
                }
            }
        });
        //删除按钮点击事件
        helper.getView(R.id.imgBtn_delete_item).setOnClickListener(listener -> {
            int mypostion = helper.getAdapterPosition();
            myData.remove(helper.getAdapterPosition());
            notifyDataSetChanged();
        });
        //相机按钮点击事件
        helper.getView(R.id.ll_add_img).setOnClickListener(listener -> {
            if (onSlectPicListener != null) {
                onSlectPicListener.onSelectPic(helper.getAdapterPosition());
            }
        });        //显示异常照片
        ((LinearLayout) helper.getView(R.id.ll_exception_img_parent)).removeAllViews();
        if (item.getUploadPath() != null) {
            for (String imgUrl : item.getUploadPath()) {
                //创建父容器
                RelativeLayout relativeLayout = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(150, 150);
                rParams.rightMargin = 15;
                relativeLayout.setLayoutParams(rParams);
                //创建图片控件
                ImageView imgViwe = new ImageView(mContext);
                RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(143, 143);
                imgParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                imgViwe.setLayoutParams(new RelativeLayout.LayoutParams(130, 130));
                imgViwe.setBackground(context.getDrawable(R.drawable.shape_dynamic_black));
                String realUrl = HttpConstant.IMAGEURL + imgUrl;
                Glide.with(mContext).load(realUrl).into(imgViwe);
                //创建按钮控件
                ImageButton imageButton = new ImageButton(mContext);
                RelativeLayout.LayoutParams ibtn = new RelativeLayout.LayoutParams(37, 37);
                ibtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imageButton.setBackgroundResource(R.mipmap.delete_3);
                imageButton.setLayoutParams(ibtn);
                imageButton.setOnClickListener(listener -> {
                    int index = ((LinearLayout) helper.getView(R.id.ll_exception_img_parent)).indexOfChild(relativeLayout);
                    //先删除数据
                    item.getUploadPath().remove(index);
                    //其次删除界面
                    ((LinearLayout) helper.getView(R.id.ll_exception_img_parent)).removeViewAt(index);
                });
                relativeLayout.addView(imgViwe);
                relativeLayout.addView(imageButton);
                ((LinearLayout) helper.getView(R.id.ll_exception_img_parent)).addView(relativeLayout);
            }
        }else{
            Log.e("dime", "异常图片长度：为空");
        }

    }

    public interface OnSlectPicListener {
        void onSelectPic(int position);
    }

    public interface OnExceptionChooseListener {
        void onExceptionChoose(int posstion);
    }

    public void setOnSlectPicListener(OnSlectPicListener onSlectPicListener) {
        this.onSlectPicListener = onSlectPicListener;
    }

    public void setOnExceptionTypeListener(OnExceptionChooseListener listener) {
        this.onExceptionChooseListener = listener;
    }

}

