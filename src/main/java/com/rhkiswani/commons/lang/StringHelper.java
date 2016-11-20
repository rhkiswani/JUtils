package com.rhkiswani.commons.lang;

import com.rhkiswani.commons.format.FormatUtil;
import com.rhkiswani.commons.reflection.DefaultReflectionHelper;
import com.rhkiswani.commons.reflection.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class StringHelper {

    public static String toString(Object obj) {
        ReflectionHelper helper = new DefaultReflectionHelper();
        List<Field> fields = helper.getFields(obj.getClass());
        StringBuilder builder = new StringBuilder();
        builder.append(obj.getClass().getSimpleName());
        builder.append("[");
        Object[] values = new Object[fields.size()];
        for (int i = 0; i < fields.size() ; i++) {
            Field f = fields.get(i);
            builder.append(f.getName());
            builder.append("=");
            builder.append("{"+i+"}");
            values[i]= helper.getFieldValue(obj, f.getName());
            if ( i < fields.size() -1){
                builder.append(", ");
            }
        }
        builder.append("]");
        return FormatUtil.formatString(builder.toString(), values);
    }
}
