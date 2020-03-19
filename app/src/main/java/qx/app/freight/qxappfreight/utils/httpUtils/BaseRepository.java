package qx.app.freight.qxappfreight.utils.httpUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.RespBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.exception.DefaultException;
import qx.app.freight.qxappfreight.utils.ToastUtil;

public abstract class BaseRepository {
    protected static <T> Observable<T> transform(Observable<BaseEntity<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<BaseEntity<T>, Observable<T>>) baseEntity -> {
                    if (null != baseEntity && "200".equals(baseEntity.getStatus())) {
                        if (baseEntity.getData() != null) {
                            return Observable.just(baseEntity.getData());
                        } else {
                            throw new DefaultException(DefaultException.DATA_NULL_EXCEPTION);
                        }
                    } else if (null != baseEntity && "318".equals(baseEntity.getStatus())) {
                        ToastUtil.showToast(baseEntity.getMessage());
                        throw new DefaultException("318");
                    } else if (baseEntity == null) {
                        throw new DefaultException("服务器数据异常");
                    } else {
                        throw new DefaultException(baseEntity.getMessage());
                    }
                });
    }

    /*******
     * 沒有Data，直接返回Massage
     * @param observable
     * @param <T>
     * @return
     */
    protected static <T> Observable<String> nothingtransform(Observable<BaseEntity<Object>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity && "200".equals(baseEntity.getStatus())) {
                        return baseEntity.getMessage();
                    } else if ("500".equals(baseEntity.getStatus())) {
                        throw new DefaultException("318");
                    }
                    else if (null != baseEntity && !"200".equals(baseEntity.getStatus())) {
                        ToastUtil.showToast(baseEntity.getMessage());
                        throw new DefaultException("318");
                    } else {
                        throw new DefaultException("服务器数据异常");
                    }
                });
    }

    /*******
     * Data是个字符串，直接返回data
     * @param observable
     * @param <T>
     * @return
     */
    protected static <T> Observable<String> nothingDatatransform(Observable<BaseEntity<Object>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity && "200".equals(baseEntity.getStatus())) {
                        return baseEntity.getData().toString();
                    } else if (null != baseEntity && "318".equals(baseEntity.getStatus())) {
                        ToastUtil.showToast(baseEntity.getMessage());
                        throw new DefaultException("318");
                    } else {
                        throw new DefaultException("服务器数据异常");
                    }
                });
    }
    /*******
     *登录一期智能调度使用
     * @param observable
     * @param <T>
     * @return
     */
    protected static <T> Observable<RespLoginBean> nothingDatatransformForOneDisP(Observable<RespLoginBean> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity) {
                        return baseEntity;
                    } else {
                        throw new DefaultException("服务器数据异常");
                    }
                });
    }
    /*******
     *登录一期智能调度使用
     * @param observable
     * @param <T>
     * @return
     */
    protected static <T> Observable<RespBean> nothingDatatransformForOneDisPOut(Observable<RespBean> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity) {
                        return baseEntity;
                    } else {
                        throw new DefaultException("服务器数据异常");
                    }
                });
    }
    protected static <T> Observable<String> notingflightTransform(Observable<BaseEntity<Object>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity && "1000".equals(baseEntity.getResponseCode())) {
                        return "成功";
                    }else if("10000".equals(baseEntity.getResponseCode())){
                        return "异常登录";
                    } else {
                        throw new DefaultException(baseEntity.getMessage());
                    }
                });
    }
    /*******
     * Data是个int，直接返回data
     * @param observable
     * @param <T>
     * @return
     */
    protected static <T> Observable<Integer> intDatatransform(Observable<BaseEntity<Integer>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baseEntity -> {
                    if (null != baseEntity && "200".equals(baseEntity.getStatus())) {
                        return baseEntity.getData();
                    } else if (null != baseEntity && "318".equals(baseEntity.getStatus())) {
                        ToastUtil.showToast(baseEntity.getMessage());
                        throw new DefaultException("318");
                    } else {
                        throw new DefaultException("服务器数据异常");
                    }
                });
    }


    protected static <T> Observable<T> flightTransform(Observable<BaseEntity<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<BaseEntity<T>, Observable<T>>) baseEntity -> {
                    if (null != baseEntity && "1000".equals(baseEntity.getResponseCode())) {
                        return Observable.just(baseEntity.getData());
                    }else if("10000".equals(baseEntity.getResponseCode())){
                        throw new DefaultException("异常登录");
                    } else {
                        throw new DefaultException(baseEntity.getMessage());
                    }
                });
    }


}
