package com.facebookreactions.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by saveen_dhiman on 15-August-17.
 */

public class DisplayUtil {

  public static int dpToPx(int dp) {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    return  dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}
