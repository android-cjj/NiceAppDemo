package com.shine.niceapp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.niceapp.R;


public class ProgressHUD extends Dialog {
    private int drawableId;
    private ImageView imageView;

    public ProgressHUD(Context paramContext) {
        super(paramContext);
    }

    public ProgressHUD(Context paramContext, int paramInt) {
        super(paramContext, paramInt);
    }

    public static void safeDismiss(Dialog paramDialog) {
        if (paramDialog != null) ;
        try {
            paramDialog.dismiss();
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static ProgressHUD show(Context context, CharSequence textCharSequence, int drawableRes, boolean isCancelable, DialogInterface.OnCancelListener onCancelListener) {
        ProgressHUD progressHUD = new ProgressHUD(context, R.style.ProgressHUD);
        progressHUD.drawableId = drawableRes;
        progressHUD.setTitle("");
        progressHUD.setContentView(R.layout.progress_hud);
        if ((textCharSequence == null) || (textCharSequence.length() == 0))
            progressHUD.findViewById(R.id.text_message).setVisibility(View.GONE);
        else {
            progressHUD.imageView = ((ImageView) progressHUD.findViewById(R.id.image_spinner));
            progressHUD.setCancelable(isCancelable);
            progressHUD.setOnCancelListener(onCancelListener);
            progressHUD.getWindow().getAttributes().gravity = 17;
            WindowManager.LayoutParams localLayoutParams = progressHUD.getWindow().getAttributes();
            localLayoutParams.dimAmount = 0.2F;
            progressHUD.getWindow().setAttributes(localLayoutParams);
            ((TextView) progressHUD.findViewById(R.id.text_message)).setText(textCharSequence);
            progressHUD.show();
        }
        return progressHUD;
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        Drawable localDrawable = getContext().getResources().getDrawable(this.drawableId);
        this.imageView.setBackgroundDrawable(localDrawable);
        if ((localDrawable instanceof AnimationDrawable))
            ((AnimationDrawable) this.imageView.getBackground()).start();
    }

    public void setMessage(CharSequence paramCharSequence) {
        if ((paramCharSequence != null) && (paramCharSequence.length() > 0)) {
            TextView msgTv = (TextView) findViewById(R.id.text_message);
            msgTv.setVisibility(View.VISIBLE);
            msgTv.setText(paramCharSequence);
            msgTv.invalidate();
        }
    }

    public void setSpinnerImage(int drawableRes) {
        if (drawableRes <= 0) {
            return;
        }
        Drawable localDrawable;
        this.drawableId = drawableRes;
        if ((this.imageView.getBackground() instanceof AnimationDrawable))
            ((AnimationDrawable) this.imageView.getBackground()).stop();
        this.imageView.setBackgroundResource(drawableRes);
        this.imageView.invalidate();
        localDrawable = getContext().getResources().getDrawable(drawableRes);
        if (!(localDrawable instanceof AnimationDrawable))
            ((AnimationDrawable) localDrawable).start();
    }
}