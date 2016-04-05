package com.sjl.lib.http.okhttp.utils;

/**
 * Created by jilisun on 15/12/14.
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
