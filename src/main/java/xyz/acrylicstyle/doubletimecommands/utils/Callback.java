package xyz.acrylicstyle.doubletimecommands.utils;

public abstract class Callback<T> {
    public abstract void done(T t, Throwable e);
}
