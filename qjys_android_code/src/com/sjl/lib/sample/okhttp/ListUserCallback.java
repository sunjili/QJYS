package com.sjl.lib.sample.okhttp;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

import com.google.gson.Gson;
import com.sjl.lib.http.okhttp.callback.Callback;

/**
 * Created by jilisun on 15/12/14.
 */
public abstract class ListUserCallback extends Callback<List<User>>
{
    @Override
    public List<User> parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        List<User> user = new Gson().fromJson(string, List.class);
        return user;
    }


}
