package io.github.rhkiswani.javaff.beans;

import io.github.rhkiswani.javaff.beans.ValuesHolder;
import io.github.rhkiswani.javaff.lang.annotations.HashcodeField;

import javax.persistence.Id;

public class PersonByIdAnnotation<T> extends ValuesHolder<T> {

    @Id
    @HashcodeField
    private Integer id;
    @HashcodeField
    private String name;

    private transient String transientVal;
    public static String staticVal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTransientVal() {
        return transientVal;
    }

    public void setTransientVal(String transientVal) {
        this.transientVal = transientVal;
    }
}
