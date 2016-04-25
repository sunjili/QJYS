package com.sjl.lib.multi_image_selector.bean;

import java.util.List;

import android.text.TextUtils;

/**
 * 文件夹
 * Created by jilisun on 2015/4/7.
 */
public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return TextUtils.equals(other.path, path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
