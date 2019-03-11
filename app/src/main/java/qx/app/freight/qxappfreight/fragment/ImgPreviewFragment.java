package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ImgPreviewAct;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 图片预览view的fragment
 * create by ym
 */
public class ImgPreviewFragment extends Fragment {
    public static final String PATH = "path";

    public static ImgPreviewFragment getInstance(String path) {
        ImgPreviewFragment fragment = new ImgPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_img_preview, container, false);
        final ImageView imageView = contentView.findViewById(R.id.preview_image);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        String path = getArguments().getString(PATH);
        path = (path == null) ? "" : path;
        if (path.startsWith("http")) {
            Glide.with(container.getContext()).load(path).into(new SimpleTarget<GlideDrawable>(480, 800) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                    mAttacher.update();
                }
            });
        } else {
            Glide.with(container.getContext()).load(new File(path)).into(new SimpleTarget<GlideDrawable>(480, 800) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                    mAttacher.update();
                }
            });
        }
        mAttacher.setOnViewTapListener((view, x, y) -> {
            ImgPreviewAct activity = (ImgPreviewAct) getActivity();
            activity.switchBarVisibility();
        });
        return contentView;
    }
}
