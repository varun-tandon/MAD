package com.hhsfbla.launch;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Varun on 2/9/2017.
 */

public class TakeScreenshot {
    public static void takeScreenshot(Window w) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg";

            // create bitmap screen capture
            View v1 = w.getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}
