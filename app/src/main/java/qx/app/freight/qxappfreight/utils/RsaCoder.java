package qx.app.freight.qxappfreight.utils;


import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author mac
 */
public class RsaCoder {

    /**
     * 非对称密钥算法
     */
    private static final String KEY_ALGORITHM = "RSA";


    public static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    /**
     * 公钥
     **/
    public static final String P_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANWnvOm0wS9hsY9xiAs2G09Ah88zaJHMwEdXiVBa+pCJVSlJdSglewnHFWuT/4GBMTbcfusTQWChjDNn3trzWwcCAwEAAQ==";
    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return {@link String}
     * @description 公钥加密
     * @date 2020/11/17 10:37
     * @author tanyo
     */
    public static String encryptByPublicKey(String data, String key)  {
        byte[] encode = new byte[1024];
        try {
            encode = RsaCoder.encryptByPublicKey(data.getBytes(), Base64.decode(key, Base64.NO_WRAP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(encode, Base64.NO_WRAP);
    }

}
