/*
 * Copyright 2016 Mohamed Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rhkiswani.javaff.reflection;

import io.github.rhkiswani.javaff.exceptions.SmartException;
import io.github.rhkiswani.javaff.lang.utils.ArraysUtils;
import io.github.rhkiswani.javaff.lang.utils.ObjectUtils;
import io.github.rhkiswani.javaff.reflection.exception.ReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohamed Kiswani
 * @since 0.0.1
 * @see io.github.rhkiswani.javaff.reflection.ReflectionHelper
 */
public class DefaultReflectionHelper<T> implements ReflectionHelper<T>{
    private static final ArrayList<String> IGNORE_NAMES = new ArrayList<>();

    static {
        IGNORE_NAMES.add("$jacocoData");
        IGNORE_NAMES.add("__cobertura_counters");
        IGNORE_NAMES.add("this$");
    }
    @Override
    public List<Field> scanFieldsByAnnotation(Class clazz, Class... annotations) throws ReflectionException {
        if (clazz == null){
            return null;
        }
        if (ArraysUtils.isEmpty(annotations)){
            return null;
        }
        LinkedList<Field> list = new LinkedList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            for (Class aClass : annotations) {
                if (field.isAnnotationPresent(aClass)){
                    list.add(field);
                }
            }
        }
        if (list.size() == 0 && !clazz.getSuperclass().equals(Object.class)){
            return scanFieldsByAnnotation(clazz.getSuperclass(), annotations);
        }
        return list;
    }

    @Override
    public void setFieldValue(T obj, String fieldName, Object value) throws ReflectionException {
        if (obj == null){
            throw new ReflectionException(SmartException.NULL_VAL, "target object");
        }
        Field field = getField(obj.getClass(), fieldName);
        validateType(value, field);
        try {
            field.set(obj, value);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    private void validateType(Object value, Field field) {
        boolean valueNotNull = (value != null);
        if (valueNotNull){
            boolean isPrimitive = field.getType().isPrimitive();
            boolean isInstanceOfType = field.getType().isInstance(value);
            if (isPrimitive){
                boolean isInstanceOfWrapperClass = ObjectUtils.primitiveToWrapper(field.getType()).isInstance(value);
                if (!isInstanceOfWrapperClass){
                    throw new ReflectionException(SmartException.TYPE_ERROR, value.getClass(), field.getType());
                }
            }else if (!isInstanceOfType){
                throw new ReflectionException(SmartException.TYPE_ERROR, value.getClass(), field.getType());
            }
        }
    }

    @Override
    public void setStaticFieldValue(Class clazz, String fieldName, Object value) throws ReflectionException {
        Field field = getField(clazz, fieldName);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        }catch (Exception e){
            throw new ReflectionException(e.getMessage());
        }
    }

    @Override
    public Field getField(Class clazz, String fieldName) throws ReflectionException {
        if (clazz == null){
            throw new ReflectionException(SmartException.NULL_VAL, "target class");
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isIgnored(field)){
                continue;
            }
            if (field.getName().equals(fieldName)){
                field.setAccessible(true);
                return field;
            }
        }
        if (!clazz.getSuperclass().equals(Object.class)){
             return getField(clazz.getSuperclass(), fieldName);
        }
        throw new ReflectionException(SmartException.NOT_FOUND, fieldName);
    }

    @Override
    public Class forName(String className) throws ReflectionException {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    @Override
    public List<Field> getFields(Class clazz) {
        if (clazz == null){
            throw new ReflectionException(SmartException.NULL_VAL, "Class");
        }
        LinkedList<Field> list = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isIgnored(field)){
                continue;
            }
            field.setAccessible(true);
            list.add(field);
        }
        if (!clazz.getSuperclass().equals(Object.class)){
            list.addAll(getFields(clazz.getSuperclass()));
        }
        return list;
    }

    private boolean isIgnored(Field field) {
        for (String ignoreName : IGNORE_NAMES) {
            if (field != null && field.getName().contains(ignoreName)){
                return true;
            }
        }
        return false;
    }

    public <V> V getFieldValue(T obj, String fieldName) throws ReflectionException {
        try {
            if (obj == null){
                throw new ReflectionException(SmartException.NULL_VAL, "Object");
            }
            if (fieldName == null){
                throw new ReflectionException(SmartException.NULL_VAL, "Field Name");
            }
            return (V) getField(obj.getClass(), fieldName).get(obj);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }
}
