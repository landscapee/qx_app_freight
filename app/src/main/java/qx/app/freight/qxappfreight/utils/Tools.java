package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.constant.Constants;

/**
 * TODO : 程序特有工具类
 * Created by owen
 * on 2016-09-08.
 */
public class Tools {

    /**
     * 获取当前登录用户得角色名称
     *
     * @param context
     * @return
     */
    public static String getRoleName(Context context) {

        String roleName = "";
        if (Constants.SECURITY_CHECK.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "安检";
        } else if (Constants.COLLECTION.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "收运";
        } else if (Constants.INVENTORY_KEEPER.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "货运";
        } else if (Constants.CARGO_AGENCY.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "货代";
        } else if (Constants.RECEIVE.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "收验";
        }
        return roleName;
    }


    public static void setLoginUserBean(LoginResponseBean userBean) {
        // token
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.token, userBean.getToken());
        // userId
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.userId, userBean.getUserId());//
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.realName, userBean.getUsername());
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.deptcode, userBean.getDepId());

        //当前登录的账号
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_NAME, userBean.getLoginName());
        //当前登录账号的密码
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_PWD, userBean.getPwd());
    }


    private static long lastClickTime;

    /**
     * 是否在一秒类 连续点击 ，规避误操作
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (lastClickTime > 0 && time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 设置顶部权限
     *
     * @param context
     */
    public static void applyCommonPermission(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            //"files"与后台 沟通后 确定的 接收 key
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * clone 类
     * @param obj
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static <T extends Serializable> T IOclone(T obj) throws ClassNotFoundException, IOException{
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        ByteArrayInputStream bins = null;
        ObjectInputStream ojs = null;

        try {
            oos = new ObjectOutputStream(bous);
            oos.writeObject(obj);

            bins = new ByteArrayInputStream(bous.toByteArray());
            ojs = new ObjectInputStream(bins);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                oos.close();
                ojs.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return (T)ojs.readObject();
    }


    /**
     * TODO: 保存 位置信息
     */
    public static void saveGPSPosition(PositionBean bean) {
        SaveUtils.getInstance().setValue(Constants.POSITION, bean);
    }

    /**
     * TODO: 得到 位置信息
     */
    public static PositionBean getGPSPosition() {
        PositionBean bean = (PositionBean) SaveUtils.getInstance().getValue(Constants.POSITION);
        if (bean == null)
            return null;
        return bean;
    }

    /**
     * TODO: 获取当前的位置信息是否有效
     */
    public static boolean isValidForLocasition(long time) {
        Long s = (System.currentTimeMillis() - time) / (1000 * 60);
        return s < Constants.ValidTime;
    }

    private static final String KEY_BSLoaction = "BSLoactionUtil";

    /**
     * TODO: 保存基站位置信息
     */
    public static void saveBSLocation(BSLoactionUtil.BSLocationBean bean) {
        SaveUtils.getInstance().setValue(KEY_BSLoaction, bean);
    }

    /**
     * TODO: 获取基站位置信息
     */
    public static BSLoactionUtil.BSLocationBean getBSLoaction() {
        return (BSLoactionUtil.BSLocationBean) SaveUtils.getInstance().getValue(KEY_BSLoaction);
    }
    public static String getToken() {
        String token = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.token, "");
        return token;
    }
    public static String getRealName() {
        String realName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.realName, "");
        return realName;
    }

    public static String getLoginName() {
        String loginName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.realName, "");
        return loginName;
    }
}
