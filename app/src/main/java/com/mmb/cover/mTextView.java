package com.mmb.cover;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class mTextView extends AppCompatTextView {
    public mTextView(Context context) {
        super(context);
        Typeface irsans = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(irsans);

    }

    public mTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface irsans = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(irsans);

    }

    public mTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
