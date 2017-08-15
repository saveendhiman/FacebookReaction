package com.facebookreactions.reaction;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.facebookreactions.util.DisplayUtil;


/**
 * Created by saveen_dhiman on 15-August-17.
 */

public class Emotion {

  static final int SMALL_SIZE = DisplayUtil.dpToPx(24);
  static final int MEDIUM_SIZE = DisplayUtil.dpToPx(32);
  static final int LARGE_SIZE = DisplayUtil.dpToPx(62);

  int size = 0;

  int startAnimatedSize;
  int endAnimatedSize;

  float x;
  float y;

  float startAnimatedX;

  float startAnimatedY;
  float endAnimatedY;
  private Drawable imageDrawable;
  private Rect imageBound;
  private Context context;


  Emotion(Context context, Drawable imageResource) {
    this.context = context;
    this.imageDrawable = imageResource;
    imageBound = new Rect();
  }

  void draw(final Canvas canvas) {
    imageBound.set((int)x,(int) y, (int)x + size, (int)y + size);
    imageDrawable.setBounds(imageBound);
    imageDrawable.draw(canvas);
  }

  void setCurrentSize(int currentSize) {

    this.size = currentSize;
  }

}
