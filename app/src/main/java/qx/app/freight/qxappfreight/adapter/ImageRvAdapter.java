package qx.app.freight.qxappfreight.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ImgPreviewAct;

/**
 * 选择图片预览适配器
 */
public class ImageRvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> mList = new ArrayList<>();

    public ImageRvAdapter(@Nullable List<String> data) {
        super(R.layout.item_image_rv, data);
        mList.clear();
        mList.addAll(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageView ivDelete = helper.getView(R.id.iv_delete);
        Glide.with(mContext).load(item).into(ivImage);
        helper.itemView.setOnLongClickListener(v -> {
            ivDelete.setVisibility(View.VISIBLE);
            return true;
        });
        helper.itemView.setOnClickListener(v -> {
            if (ivDelete.getVisibility() == View.VISIBLE) {
                ivDelete.setVisibility(View.GONE);
            } else {
                ImgPreviewAct.startPreview((Activity) mContext, mList, helper.getAdapterPosition());
            }
        });
        ivDelete.setOnClickListener(v -> {
            mList.remove(helper.getAdapterPosition());
            ImageRvAdapter.this.setNewData(mList);
            ivDelete.setVisibility(View.GONE);
            notifyDataSetChanged();
        });
    }

    public List<String> getFinalPhotoList() {
        return mList;
    }
}
