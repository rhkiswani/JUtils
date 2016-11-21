package io.github.rhkiswani.javaff.lang;

import io.github.rhkiswani.javaff.decetor.ApiDetectorUtil;
import io.github.rhkiswani.javaff.reflection.ReflectionHelper;
import io.github.rhkiswani.javaff.reflection.ReflectionHelpersFactory;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractObjectHelper<INPUT, OUT> {

    protected final ReflectionHelper reflectionHelper = ReflectionHelpersFactory.instance().getDefault(this.getClass());;

    protected abstract OUT doAction(INPUT input);

    protected List<Field> getFieldsByAnnotation(Object obj,Class annotation){
        List<Field> fields = reflectionHelper.scanFieldsByAnnotation(obj.getClass(), annotation);
        if (ArraysHelper.isEmpty(fields)){
            if (ApiDetectorUtil.isJPAAvailable()){
                fields = reflectionHelper.scanFieldsByAnnotation(obj.getClass(), Id.class);
            }
            if (ArraysHelper.isEmpty(fields)){
                fields = reflectionHelper.getFields(obj.getClass());
            }
        }
        return fields;
    }


}
