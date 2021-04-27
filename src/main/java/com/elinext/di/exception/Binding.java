package com.elinext.di.exception;

import java.util.Objects;

public class Binding<T> {

    private Object instance;
    private Class<T> intfType;
    private Class<? extends T> implType;
    private boolean isSingleton;

    public Binding(Object instance,
                   Class<T> intfType,
                   Class<? extends T> implType,
                   boolean isSingleton) {
        this.instance = instance;
        this.intfType = intfType;
        this.implType = implType;
        this.isSingleton = isSingleton;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class<T> getIntfType() {
        return intfType;
    }

    public void setIntfType(Class<T> intfType) {
        this.intfType = intfType;
    }

    public Class<? extends T> getImplType() {
        return implType;
    }

    public void setImplType(Class<? extends T> implType) {
        this.implType = implType;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding<?> binding = (Binding<?>) o;
        return isSingleton == binding.isSingleton &&
                Objects.equals(instance, binding.instance) &&
                Objects.equals(intfType, binding.intfType) &&
                Objects.equals(implType, binding.implType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, intfType, implType, isSingleton);
    }

    @Override
    public String toString() {
        return "com.elinext.di.exception.Binding{" +
                "instance=" + instance +
                ", intfType=" + intfType +
                ", classType=" + implType +
                ", isSingleton=" + isSingleton +
                '}';
    }
}
