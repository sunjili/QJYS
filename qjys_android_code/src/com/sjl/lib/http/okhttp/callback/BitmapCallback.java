package com.sjl.lib.http.okhttp.callback;

import okhttp3.Response;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by jilisun on 15/12/14.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
