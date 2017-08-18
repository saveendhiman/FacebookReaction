package com.facebookreactions.reaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.facebookreactions.R;
import com.facebookreactions.util.Constants;
import com.facebookreactions.util.DisplayUtil;


/**
 * Created by saveen_dhiman on 15-August-17.
 */

public class Emotion {

  static final int SMALL_SIZE = DisplayUtil.dpToPx(24);
  static final int MEDIUM_SIZE = DisplayUtil.dpToPx(32);
  static final int LARGE_SIZE = DisplayUtil.dpToPx(62);

  private static final int SPACING_TO_LABEL = DisplayUtil.dpToPx(16);
  private static final int MAX_WIDTH_TITLE = DisplayUtil.dpToPx(56);

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
  private Paint textPaint;
  private RectF textBound;

  private Context context;
  private float labelRatio;
  private Bitmap imageTitle;

  Emotion(Context context, String title,Drawable imageResource) {
    this.context = context;
    this.imageDrawable = imageResource;

    textPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    imageBound = new Rect();
    textBound = new RectF();
    snapShotLabelView(title);

  }

  private void snapShotLabelView(String title) {
    LayoutInflater inflater = LayoutInflater.from(context);
    TextView labelView = (TextView) inflater.inflate(R.layout.view_label, null, false);
    labelView.setText(title);

    int width = (int) context.getResources().getDimension(R.dimen.label_width);
    int height = (int) context.getResources().getDimension(R.dimen.label_height);

    labelRatio = width / height  ;
    imageTitle = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

    Canvas canvas = new Canvas(imageTitle);
    labelView.layout(0, 0, width, height);
    labelView.draw(canvas);
  }

  private void setAlphaTitle(int alpha) {
    textPaint.setAlpha(alpha);
  }

  void draw(final Canvas canvas) {
    imageBound.set((int)x,(int) y, (int)x + size, (int)y + size);
    imageDrawable.setBounds(imageBound);
    imageDrawable.draw(canvas);

    drawLabel(canvas);
  }

  private void drawLabel(Canvas canvas) {
    int width = size - MEDIUM_SIZE ;
    int height = (int) (width / labelRatio);

    if (width <= 0) return;

    setAlphaTitle(Constants.MAX_ALPHA * width / MAX_WIDTH_TITLE);

    float x = this.x + (size - width) / 2;
    float y = this.y - SPACING_TO_LABEL - height;

    textBound.set(x, y, x + width, y + height);
    canvas.drawBitmap(imageTitle, null, textBound, textPaint);
  }

  void setCurrentSize(int currentSize) {

    this.size = currentSize;
  }


}
