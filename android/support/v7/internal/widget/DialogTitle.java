/* 
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Used by dialogs to change the font size and number of lines to try to fit
 * the text to the available space.
 *
 * @hide
 */
public class DialogTitle extends TextView {

    public DialogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogTitle(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    setSingleLine(false);
                    setMaxLines(2);

                    final TypedArray a = getContext().obtainStyledAttributes(null,
                            R.styleable.TextAppearance,
                            android.R.attr.textAppearanceMedium,
                            android.R.style.TextAppearance_Medium);
                    final int textSize = a.getDimensionPixelSize(
                            R.styleable.TextAppearance_android_textSize, 0);
                    if (textSize != 0) {
                        // textSize is already expressed in pixels
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    a.recycle();

                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }
}