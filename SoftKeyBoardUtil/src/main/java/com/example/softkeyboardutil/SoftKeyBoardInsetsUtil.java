package com.example.softkeyboardutil;

import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

/**
 * com.example.sourcestudy20221111.CustomView
 * by 付和健
 */
public final class SoftKeyBoardInsetsUtil {

    static private Pair<View,Integer> pair = null;

    static private boolean isHiding = false;

    static private int softKeyBoardHeight = 0;

    static private void setViewMoveWithKeyBoard(View view)
    {

        WindowInsetsAnimationCompat.Callback callback = new WindowInsetsAnimationCompat.Callback(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
            @NonNull
            @Override
            public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {

                    Insets in = insets.getInsets(WindowInsetsCompat.Type.ime());
                    in = Insets.of(0,0,0,in.bottom-insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
                    insets = new WindowInsetsCompat.Builder().setInsets(WindowInsetsCompat.Type.navigationBars(),in).build();

                    int bottom = ((ViewGroup)view.getParent()).getBottom() - view.getBottom();
                    if(bottom<in.bottom)
                    {
                        ViewCompat.offsetTopAndBottom(view,bottom - in.bottom);
                    }

                    if(!isHiding)
                    {
                        softKeyBoardHeight = in.bottom;
                    }

                    if(pair==null)
                    {
                        pair = new Pair<>(view,bottom);
                    }
                    if(bottom>in.bottom && isHiding)
                    {
                        int offset = softKeyBoardHeight - in.bottom;
                        softKeyBoardHeight = in.bottom;
                        if(bottom - offset >= pair.second)
                        {
                            ViewCompat.offsetTopAndBottom(view,offset);
                        }
                        else
                        {
                            if(bottom>pair.second)
                            {
                                ViewCompat.offsetTopAndBottom(view,pair.second - bottom);
                            }
                        }
                    }
                    return insets;
            }

            @Override
            public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
                isHiding = false;
            }

            @NonNull
            @Override
            public WindowInsetsAnimationCompat.BoundsCompat onStart(@NonNull WindowInsetsAnimationCompat animation, @NonNull WindowInsetsAnimationCompat.BoundsCompat bounds) {
                return super.onStart(animation, bounds);
            }
        };

        ViewCompat.setWindowInsetsAnimationCallback(view.getRootView(),callback);
    }

    /**
      *
      * @description 设置打开/关闭软键盘并使view跟随软件盘的出现一起移动
      * @params view:——view
      * @return void
      */
    static void  openAndHideSoftKeyBoardAndMakeViewMoveWithKeyBoard(View view)
    {
        setViewMoveWithKeyBoard(view);
        openSoftKeyBoard(view);
    }

    /**
      *
      * @description 打开软键盘
      * @params view:——子view
      * @return void
      */
    static void openSoftKeyBoard(View view)
    {
        ViewCompat.getWindowInsetsController(view).show(WindowInsetsCompat.Type.ime());
    }

    /**
      *
      * @description 隐藏软键盘
      * @params view:— 子view
      * @return void
      */
    static void hideSoftKeyBoard(View view)
    {
        ViewCompat.getWindowInsetsController(view).hide(WindowInsetsCompat.Type.ime());
        ViewCompat.setWindowInsetsAnimationCallback(view.getRootView(),null);
        pair = null;
        isHiding = true;
    }

    static boolean softKeyBoardIsShow(View view){
        return ViewCompat.getRootWindowInsets(view).isVisible(WindowInsetsCompat.Type.ime());
    }


    /**
      *
      * @description 设置activity以全屏显示
      * @params activity:—— 需要全屏希显示的activity
      * @return void
      */
    static public  void setFullScreen(Activity activity)
    {
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(),false);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

}
