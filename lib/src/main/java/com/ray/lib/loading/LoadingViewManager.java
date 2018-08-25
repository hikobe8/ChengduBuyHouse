package com.ray.lib.loading;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-05-07 19:16
 *  description : generate LoadingViewController
 */
public class LoadingViewManager {

     public static LoadingViewController register(Context context) {
         if (context instanceof Activity) {
             return new LoadingViewController(context);
         } else {
             throw new RuntimeException("can't support other Context type except Activity");
         }
     }

    public static LoadingViewController register(View targetView) {
        return new LoadingViewController(targetView);
    }

}
