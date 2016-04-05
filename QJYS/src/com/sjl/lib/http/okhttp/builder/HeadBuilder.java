package com.sjl.lib.http.okhttp.builder;

import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.sjl.lib.http.okhttp.request.OtherRequest;
import com.sjl.lib.http.okhttp.request.RequestCall;

/**
 * Created by jilisun on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
