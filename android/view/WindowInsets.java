/*
 * Copyright (C) 2014 The Android Open Source Project
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


package android.view;

import android.graphics.Rect;

/**
 * Describes a set of insets for window content.
 *
 * <p>WindowInsets are immutable and may be expanded to include more inset types in the future.
 * To adjust insets, use one of the supplied clone methods to obtain a new WindowInsets instance
 * with the adjusted properties.</p>
 *
 * @see View.OnApplyWindowInsetsListener
 * @see View#onApplyWindowInsets(WindowInsets)
 */
public final class WindowInsets {
    private Rect mSystemWindowInsets;
    private Rect mWindowDecorInsets;
    private Rect mTempRect;
    private boolean mIsRound;

    private boolean mSystemWindowInsetsConsumed = false;
    private boolean mWindowDecorInsetsConsumed = false;

    private static final Rect EMPTY_RECT = new Rect(0, 0, 0, 0);

    /**
     * Since new insets may be added in the future that existing apps couldn't
     * know about, this fully empty constant shouldn't be made available to apps
     * since it would allow them to inadvertently consume unknown insets by returning it.
     * @hide
     */
    public static final WindowInsets CONSUMED;

    static {
        CONSUMED = new WindowInsets(EMPTY_RECT, EMPTY_RECT);
        CONSUMED.mSystemWindowInsetsConsumed = true;
        CONSUMED.mWindowDecorInsetsConsumed = true;
    }

    /** @hide */
    public WindowInsets(Rect systemWindowInsets, Rect windowDecorInsets) {
        this(systemWindowInsets, windowDecorInsets, false);
    }

    /** @hide */
    public WindowInsets(Rect systemWindowInsets, boolean isRound) {
        this(systemWindowInsets, null, isRound);
    }

    /** @hide */
    public WindowInsets(Rect systemWindowInsets, Rect windowDecorInsets, boolean isRound) {
        mSystemWindowInsetsConsumed = systemWindowInsets == null;
        mSystemWindowInsets = mSystemWindowInsetsConsumed ? EMPTY_RECT : systemWindowInsets;

        mWindowDecorInsetsConsumed = windowDecorInsets == null;
        mWindowDecorInsets = mWindowDecorInsetsConsumed ? EMPTY_RECT : windowDecorInsets;

        mIsRound = isRound;
    }

    /**
     * Construct a new WindowInsets, copying all values from a source WindowInsets.
     *
     * @param src Source to copy insets from
     */
    public WindowInsets(WindowInsets src) {
        mSystemWindowInsets = src.mSystemWindowInsets;
        mWindowDecorInsets = src.mWindowDecorInsets;
        mSystemWindowInsetsConsumed = src.mSystemWindowInsetsConsumed;
        mWindowDecorInsetsConsumed = src.mWindowDecorInsetsConsumed;
        mIsRound = src.mIsRound;
    }

    /** @hide */
    public WindowInsets(Rect systemWindowInsets) {
        this(systemWindowInsets, null);
    }

    /**
     * Used to provide a safe copy of the system window insets to pass through
     * to the existing fitSystemWindows method and other similar internals.
     * @hide
     */
    public Rect getSystemWindowInsets() {
        if (mTempRect == null) {
            mTempRect = new Rect();
        }
        if (mSystemWindowInsets != null) {
            mTempRect.set(mSystemWindowInsets);
        } else {
            // If there were no system window insets, this is just empty.
            mTempRect.setEmpty();
        }
        return mTempRect;
    }

    /**
     * Returns the left system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The left system window inset
     */
    public int getSystemWindowInsetLeft() {
        return mSystemWindowInsets.left;
    }

    /**
     * Returns the top system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The top system window inset
     */
    public int getSystemWindowInsetTop() {
        return mSystemWindowInsets.top;
    }

    /**
     * Returns the right system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The right system window inset
     */
    public int getSystemWindowInsetRight() {
        return mSystemWindowInsets.right;
    }

    /**
     * Returns the bottom system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The bottom system window inset
     */
    public int getSystemWindowInsetBottom() {
        return mSystemWindowInsets.bottom;
    }

    /**
     * Returns the left window decor inset in pixels.
     *
     * <p>The window decor inset represents the area of the window content area that is
     * partially or fully obscured by decorations within the window provided by the framework.
     * This can include action bars, title bars, toolbars, etc.</p>
     *
     * @return The left window decor inset
     * @hide pending API
     */
    public int getWindowDecorInsetLeft() {
        return mWindowDecorInsets.left;
    }

    /**
     * Returns the top window decor inset in pixels.
     *
     * <p>The window decor inset represents the area of the window content area that is
     * partially or fully obscured by decorations within the window provided by the framework.
     * This can include action bars, title bars, toolbars, etc.</p>
     *
     * @return The top window decor inset
     * @hide pending API
     */
    public int getWindowDecorInsetTop() {
        return mWindowDecorInsets.top;
    }

    /**
     * Returns the right window decor inset in pixels.
     *
     * <p>The window decor inset represents the area of the window content area that is
     * partially or fully obscured by decorations within the window provided by the framework.
     * This can include action bars, title bars, toolbars, etc.</p>
     *
     * @return The right window decor inset
     * @hide pending API
     */
    public int getWindowDecorInsetRight() {
        return mWindowDecorInsets.right;
    }

    /**
     * Returns the bottom window decor inset in pixels.
     *
     * <p>The window decor inset represents the area of the window content area that is
     * partially or fully obscured by decorations within the window provided by the framework.
     * This can include action bars, title bars, toolbars, etc.</p>
     *
     * @return The bottom window decor inset
     * @hide pending API
     */
    public int getWindowDecorInsetBottom() {
        return mWindowDecorInsets.bottom;
    }

    /**
     * Returns true if this WindowInsets has nonzero system window insets.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return true if any of the system window inset values are nonzero
     */
    public boolean hasSystemWindowInsets() {
        return mSystemWindowInsets.left != 0 || mSystemWindowInsets.top != 0 ||
                mSystemWindowInsets.right != 0 || mSystemWindowInsets.bottom != 0;
    }

    /**
     * Returns true if this WindowInsets has nonzero window decor insets.
     *
     * <p>The window decor inset represents the area of the window content area that is
     * partially or fully obscured by decorations within the window provided by the framework.
     * This can include action bars, title bars, toolbars, etc.</p>
     *
     * @return true if any of the window decor inset values are nonzero
     * @hide pending API
     */
    public boolean hasWindowDecorInsets() {
        return mWindowDecorInsets.left != 0 || mWindowDecorInsets.top != 0 ||
                mWindowDecorInsets.right != 0 || mWindowDecorInsets.bottom != 0;
    }

    /**
     * Returns true if this WindowInsets has any nonzero insets.
     *
     * @return true if any inset values are nonzero
     */
    public boolean hasInsets() {
        return hasSystemWindowInsets() || hasWindowDecorInsets();
    }

    /**
     * Check if these insets have been fully consumed.
     *
     * <p>Insets are considered "consumed" if the applicable <code>consume*</code> methods
     * have been called such that all insets have been set to zero. This affects propagation of
     * insets through the view hierarchy; insets that have not been fully consumed will continue
     * to propagate down to child views.</p>
     *
     * <p>The result of this method is equivalent to the return value of
     * {@link View#fitSystemWindows(android.graphics.Rect)}.</p>
     *
     * @return true if the insets have been fully consumed.
     * @hide Pending API
     */
    public boolean isConsumed() {
        return mSystemWindowInsetsConsumed && mWindowDecorInsetsConsumed;
    }

    /**
     * Returns true if the associated window has a round shape.
     *
     * <p>A round window's left, top, right and bottom edges reach all the way to the
     * associated edges of the window but the corners may not be visible. Views responding
     * to round insets should take care to not lay out critical elements within the corners
     * where they may not be accessible.</p>
     *
     * @return True if the window is round
     */
    public boolean isRound() {
        return mIsRound;
    }

    /**
     * Returns a copy of this WindowInsets with the system window insets fully consumed.
     *
     * @return A modified copy of this WindowInsets
     */
    public WindowInsets consumeSystemWindowInsets() {
        final WindowInsets result = new WindowInsets(this);
        result.mSystemWindowInsets = EMPTY_RECT;
        result.mSystemWindowInsetsConsumed = true;
        return result;
    }

    /**
     * Returns a copy of this WindowInsets with selected system window insets fully consumed.
     *
     * @param left true to consume the left system window inset
     * @param top true to consume the top system window inset
     * @param right true to consume the right system window inset
     * @param bottom true to consume the bottom system window inset
     * @return A modified copy of this WindowInsets
     * @hide pending API
     */
    public WindowInsets consumeSystemWindowInsets(boolean left, boolean top,
            boolean right, boolean bottom) {
        if (left || top || right || bottom) {
            final WindowInsets result = new WindowInsets(this);
            result.mSystemWindowInsets = new Rect(
                    left ? 0 : mSystemWindowInsets.left,
                    top ? 0 : mSystemWindowInsets.top,
                    right ? 0 : mSystemWindowInsets.right,
                    bottom ? 0 : mSystemWindowInsets.bottom);
            result.mSystemWindowInsetsConsumed = !hasSystemWindowInsets();
            return result;
        }
        return this;
    }

    /**
     * Returns a copy of this WindowInsets with selected system window insets replaced
     * with new values.
     *
     * @param left New left inset in pixels
     * @param top New top inset in pixels
     * @param right New right inset in pixels
     * @param bottom New bottom inset in pixels
     * @return A modified copy of this WindowInsets
     */
    public WindowInsets replaceSystemWindowInsets(int left, int top,
            int right, int bottom) {
        final WindowInsets result = new WindowInsets(this);
        result.mSystemWindowInsets = new Rect(left, top, right, bottom);
        result.mSystemWindowInsetsConsumed = !hasSystemWindowInsets();
        return result;
    }

    /**
     * @hide
     */
    public WindowInsets consumeWindowDecorInsets() {
        final WindowInsets result = new WindowInsets(this);
        result.mWindowDecorInsets.set(0, 0, 0, 0);
        result.mWindowDecorInsetsConsumed = true;
        return result;
    }

    /**
     * @hide
     */
    public WindowInsets consumeWindowDecorInsets(boolean left, boolean top,
            boolean right, boolean bottom) {
        if (left || top || right || bottom) {
            final WindowInsets result = new WindowInsets(this);
            result.mWindowDecorInsets = new Rect(left ? 0 : mWindowDecorInsets.left,
                    top ? 0 : mWindowDecorInsets.top,
                    right ? 0 : mWindowDecorInsets.right,
                    bottom ? 0 : mWindowDecorInsets.bottom);
            result.mWindowDecorInsetsConsumed = !hasWindowDecorInsets();
            return result;
        }
        return this;
    }

    /**
     * @hide
     */
    public WindowInsets replaceWindowDecorInsets(int left, int top, int right, int bottom) {
        final WindowInsets result = new WindowInsets(this);
        result.mWindowDecorInsets = new Rect(left, top, right, bottom);
        result.mWindowDecorInsetsConsumed = !hasWindowDecorInsets();
        return result;
    }

    @Override
    public String toString() {
        return "WindowInsets{systemWindowInsets=" + mSystemWindowInsets + " windowDecorInsets=" +
                mWindowDecorInsets + (isRound() ? "round}" : "}");
    }
}
