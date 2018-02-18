package com.ntunin.cybervision.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Николай on 24.07.2016.
 */
public class Delegate {
    Method method;
    Object receiver;
    public Delegate( Object receiver, String methodName, Class<?>... types) throws DelegateException {
        try {
            Class receiverClass = receiver.getClass();
            method = receiverClass.getDeclaredMethod(methodName, types);
        } catch (NoSuchMethodException e) {
            throw new DelegateException("No such method");
        }
        this.receiver = receiver;
    }
    public Object invoke(Object... args) throws DelegateException {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            throw new DelegateException("Illegal access");
        } catch (InvocationTargetException e) {
            throw new DelegateException("Invalid target");
        } catch (IllegalArgumentException e) {
            invoke();
        }
        return null;
    }
}
