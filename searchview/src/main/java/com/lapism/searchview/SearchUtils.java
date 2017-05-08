package com.lapism.searchview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.ViewCompat;

import java.util.Locale;


class SearchUtils {

    static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    /*if (isRTL()) {
        // The view has RTL layout
        mSearchArrow.setDirection(SearchArrowDrawable.ARROW_DIRECTION_END);
    } else {
        // The view has LTR layout
        mSearchArrow.setDirection(SearchArrowDrawable.ARROW_DIRECTION_START);
    }*/

    private static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    static boolean isRtlLayout(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.getResources().getConfiguration().getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    // TODO JAK ZJISTIT LTR
}