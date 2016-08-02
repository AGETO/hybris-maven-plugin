package com.divae.ageto.hybris.codegenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Klaus Hauschild
 */
enum Utils {

    ;

    public static Class<?> loadClass(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException exception) {
            throw new RuntimeException(String.format("Unable to load class [%s]", className), exception);
        }
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameters) {
        try {
            return clazz.getMethod(methodName, parameters);
        } catch (final NoSuchMethodException exception) {
            throw new RuntimeException(String.format("Unable to get method [%s] from class [%s]", methodName, clazz.getName()),
                    exception);
        }
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameters) {
        try {
            return clazz.getConstructor(parameters);
        } catch (final NoSuchMethodException exception) {
            throw new RuntimeException(String.format("Unable to get constructor of class [%s]", clazz), exception);
        }
    }

    public static Object newInstance(final Constructor<?> constructor, final Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (final Exception exception) {
            throw new RuntimeException(String.format("Unable to invoke constructor [%s]", constructor), exception);
        }
    }

    public static Object invoke(final Method method, final Object object, final Object... arguments) {
        try {
            return method.invoke(object, arguments);
        } catch (final Exception exception) {
            throw new RuntimeException(String.format("Unable to invoke method [%s]", method), exception);
        }
    }

}
