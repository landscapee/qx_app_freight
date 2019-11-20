package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

/**
 * 选择图片预览适配器
 */
public class ImageRvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageRvAdapter(@Nullable List<String> data) {
        super(R.layout.item_image_rv, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageView ivDelete = helper.getView(R.id.iv_delete);
        helper.addOnClickListener(R.id.iv_delete);
//        helper.itemView.setOnLongClickListener(v -> {
//            ivDelete.setVisibility(View.VISIBLE);
//            return true;
//        });
        if (item =="111"){
            ivImage.setImageResource(R.mipmap.icon_chose_photo_2);
            ivDelete.setVisibility(View.GONE);
        }else {
            Glide.with(mContext).load(item).into(ivImage);
            ivDelete.setVisibility(View.VISIBLE);
        }

//        helper.itemView.setOnClickListener(v -> {
//            if (ivDelete.getVisibility() == View.VISIBLE) {
//                ivDelete.setVisibility(View.GONE);
//            } else {
//                ImgPreviewAct.startPreview((Activity) mContext, mList, helper.getAdapterPosition());
//            }
//        });
//        ivDelete.setOnClickListener(v -> {
//            mList.remove(helper.getAdapterPosition());
//            ImageRvAdapter.this.setNewData(mList);
//            ivDelete.setVisibility(View.GONE);
//            notifyDataSetChanged();
//        });
    }



}
