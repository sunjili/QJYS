package com.sjl.lib.multi_image_selector.view;

import android.content.Context;
import android.util.AttributeSet;

import com.sjl.lib.roundedimageview.RoundedImageView;

/** An image view which always remains square with respect to its width. */
class SquaredImageView extends RoundedImageView {
  public SquaredImageView(Context context) {
    super(context);
  }

  public SquaredImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }
}
