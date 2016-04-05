package com.sjl.lib.sample.okhttp;

import com.google.gson.Gson;
import okhttp3.Response;

import com.sjl.lib.http.okhttp.callback.Callback;

import java.io.IOException;

/**
 * Created by jilisun on 15/12/14.
 */
public abstract class UserCallback extends Callback<User>
{
    @Override
    public User parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        User user = new Gson().fromJson(string, User.class);
        return user;
    }


}
