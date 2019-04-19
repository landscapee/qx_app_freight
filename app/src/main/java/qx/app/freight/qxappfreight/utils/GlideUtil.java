package qx.app.freight.qxappfreight.utils;

import android.content.Context;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.MyApplication;


/**
 * Glide帮助类
 */
public class GlideUtil
{

    @Deprecated
    public static <T> DrawableTypeRequest<T> load(Context context, T model)
    {
        return load(model);
    }

    /**
     * 默认调用方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param <T>
     * @return
     */
    public static <T> DrawableTypeRequest<T> load(T model)
    {
        return (DrawableTypeRequest<T>) Glide.with(MyApplication.getContext()).load(model)
                .error(R.mipmap.icon)
                .dontAnimate();
    }

    //---------以下为扩展方法------------

//    @Deprecated
//    public static <T> DrawableTypeRequest<T> loadHeadImage(Context context, T model)
//    {
//        return loadHeadImage(model);
//    }

//    /**
//     * 加载用户头像方法
//     *
//     * @param model String, byte[], File, Integer, Uri
//     * @param <T>
//     * @return
//     */
//    public static <T> DrawableTypeRequest<T> loadHeadImage(T model)
//    {
//        return (DrawableTypeRequest<T>) load(model)
//                .placeholder(R.drawable.ic_default_head)
//                .error(R.drawable.ic_default_head)
//                .dontAnimate();
//    }
//
//    /**
//     * 加载用户头像方法
//     *
//     * @param model String, byte[], File, Integer, Uri
//     * @param <T>
//     * @return
//     */
//    public static <T> DrawableTypeRequest<T> loadHeadImage2(T model)
//    {
//        return (DrawableTypeRequest<T>) load(model)
//                .placeholder(R.drawable.userinfo_head_default)
//                .error(R.drawable.userinfo_head_default)
//                .dontAnimate();
//    }
}
