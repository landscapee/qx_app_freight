package qx.app.freight.qxappfreight.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryUbnormalGoods;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;

/**
 * Created by guohao on 2019/5/16 14:41 @COPYRIGHT 青霄科技
 *
 * @title：异常详情dialog
 * @description：
 */
@SuppressLint("ValidFragment")
public class ExceptionDetailDialog extends DialogFragment {

    Context context;

    /**
     * 关闭按钮
     */
    ImageView closeIv;

    /**
     * 列表view
     */
    RecyclerView recyclerView;

    //数据源
    InventoryDetailEntity inventoryDetailEntity;



    @SuppressLint("ValidFragment")
    public ExceptionDetailDialog(Builder builder){
        this.inventoryDetailEntity = builder.inventoryDetailEntity;
        this.context = builder.context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exception_detail, container, false);
        closeIv = view.findViewById(R.id.iv_cancel);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_exception_detail);
        //控件初始化
        closeIv = dialog.findViewById(R.id.iv_cancel);
        recyclerView = dialog.findViewById(R.id.recycler_view);
        //dialog外观
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        return dialog;
    }

    public static class Builder{
        Context context;
        InventoryDetailEntity inventoryDetailEntity;

        public Builder context(Context context){
            this.context = context;
            return this;
        }

        public Builder inventoryDetailEntity(InventoryDetailEntity inventoryDetailEntity){
            this.inventoryDetailEntity = inventoryDetailEntity;
            return this;
        }

        public ExceptionDetailDialog build(){
            ExceptionDetailDialog exceptionDetailDialog = new ExceptionDetailDialog(this);
            return exceptionDetailDialog;
        }
    }

    /**
     * 适配器
     */
    public static class MyAdapter extends BaseQuickAdapter<InventoryUbnormalGoods, BaseViewHolder> {

        Context context;
        public MyAdapter(List<InventoryUbnormalGoods> list, Context context){
            super(R.layout.item_exception_detail, list);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, InventoryUbnormalGoods item) {
            helper.setText(R.id.tv_exception_content, helper.getAdapterPosition() + 1 + "丶" + ExceptionUtils.typeToString(item.getUbnormalType()) + ":" + "xx件");
            LinearLayout llParent = helper.getView(R.id.ll_exception);
            //渲染照片
            for(String url: item.getUploadFilePath()){
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                imgParams.rightMargin = 10;
                imageView.setLayoutParams(imgParams);
                Glide.with(context).load(url).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:图片点击事件
                    }
                });
                llParent.addView(imageView);
            }
        }
    }
}
