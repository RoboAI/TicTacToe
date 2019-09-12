package com.example.fuat.newtictactoe;

/**
 * Created by Fuat on 21/09/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("unused")
public class Utilities {
    @SuppressWarnings("unused")
    public static class DisplayData{
        String name;
        Point size;
        Point realSize;
        DisplayMetrics metrics;
        DisplayMetrics realMetrics;
        float fDiagnalSize;

        public DisplayData(){
            name = "";
            size = new Point();
            metrics = new DisplayMetrics();
            realSize = new Point();
            realMetrics = new DisplayMetrics();
        }
    }
    public static String getAppName(Context context){
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( context.getPackageName(), 0);
        } catch (final NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "[unknown]");
    }

    public static long getRandomNumber(){
        return (new Random()).nextInt();
    }

    public static long getTimeMillies(){
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getTimeText(){
        return DateFormat.getTimeInstance().format(new Date());
    }

    public static String getDateText(){
        return DateFormat.getDateInstance().format(new Date());
    }

    public static int[] getTimeHMSM(){
        Calendar calendar = Calendar.getInstance();

        int[] time = new int[]{
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND)};

        return time;
    }

    public static void requestFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void requestLandscape(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void requestPortrait(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static Rect getViewGlobalRect(View view) {
        Rect rc = new Rect();
        //For coordinates location relative to the screen/display
        view.getGlobalVisibleRect(rc);
        return rc;
    }

    public static Rect getViewLocalRect(View view) {
        Rect rc = new Rect();
        //For coordinates location relative to the parent
        view.getLocalVisibleRect(rc);
        return rc;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static DisplayData getScreenDimentions(WindowManager windowManager, DisplayData displayData) {
        Display display = windowManager.getDefaultDisplay();

        displayData.name = windowManager.getDefaultDisplay().getName(); // minSdkVersion=17+

        // app display area
        display.getSize(displayData.size);
        display.getMetrics(displayData.metrics);

        // actual screen size
        display.getRealSize(displayData.realSize);
        display.getRealMetrics(displayData.realMetrics);

        return displayData;
    }
}