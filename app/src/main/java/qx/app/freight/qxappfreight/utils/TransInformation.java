package qx.app.freight.qxappfreight.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * 原本输入的小写字母
 */
public class TransInformation extends ReplacementTransformationMethod {
    /**
     * 原本输入的小写字母
     */
    @Override
    protected char[] getOriginal() {
        return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    }

    /**
     * 替代为大写字母
     */
    @Override
    protected char[] getReplacement() {
        return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    }
}