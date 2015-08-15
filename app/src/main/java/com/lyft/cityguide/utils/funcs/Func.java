package com.lyft.cityguide.utils.funcs;

/**
 * @class Func
 * @brief
 */
public interface Func<T, U> {
    public U run(T t);
}
