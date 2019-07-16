package com.jpa1.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Scott Tian
 * @date 20180912
 * @desc copy bean's properties
 */

public class BeanUtil {

    private BeanUtil() {
        throw new IllegalStateException("BeanUtil class");
    }
    //获取不为空的属性值名称
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        //通过集合获取到这个对象的属性
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        //集合转数组
        return emptyNames.toArray(result);
    }

    /**
     * assigns src's properties to target's properties
     * ignore null
     * BeanUtils.copyProperties(src, target) can't ignore null
     *
     * @param src
     * @param target
     */

    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
}
}
