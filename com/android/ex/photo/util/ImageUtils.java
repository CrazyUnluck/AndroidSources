/*
 * Copyright (C) 2011 Google Inc.
 * Licensed to The Android Open Source Project.
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

package com.android.ex.photo.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.android.ex.photo.PhotoViewActivity;
import com.android.ex.photo.loaders.PhotoBitmapLoader.BitmapResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;


/**
 * Image utilities
 */
public class ImageUtils {
    // Logging
    private static final String TAG = "ImageUtils";

    /** Minimum class memory class to use full-res photos */
    private final static long MIN_NORMAL_CLASS = 32;
    /** Minimum class memory class to use small photos */
    private final static long MIN_SMALL_CLASS = 24;

    private static final String BASE64_URI_PREFIX = "base64,";
    private static final Pattern BASE64_IMAGE_URI_PATTERN = Pattern.compile("^(?:.*;)?base64,.*");

    public static enum ImageSize {
        EXTRA_SMALL,
        SMALL,
        NORMAL,
    }

    public static final ImageSize sUseImageSize;
    static {
        // On HC and beyond, assume devices are more capable
        if (Build.VERSION.SDK_INT >= 11) {
            sUseImageSize = ImageSize.NORMAL;
        } else {
            if (PhotoViewActivity.sMemoryClass >= MIN_NORMAL_CLASS) {
                // We have plenty of memory; use full sized photos
                sUseImageSize = ImageSize.NORMAL;
            } else if (PhotoViewActivity.sMemoryClass >= MIN_SMALL_CLASS) {
                // We have slight less memory; use smaller sized photos
                sUseImageSize = ImageSize.SMALL;
            } else {
                // We have little memory; use very small sized photos
                sUseImageSize = ImageSize.EXTRA_SMALL;
            }
        }
    }

    /**
     * @return true if the MimeType type is image
     */
    public static boolean isImageMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }

    /**
     * Create a bitmap from a local URI
     *
     * @param resolver The ContentResolver
     * @param uri The local URI
     * @param maxSize The maximum size (either width or height)
     *
     * @return The new bitmap or null
     */
    public static BitmapResult createLocalBitmap(ContentResolver resolver, Uri uri, int maxSize) {
        // TODO: make this method not download the image for both getImageBounds and decodeStream
        BitmapResult result = new BitmapResult();
        InputStream inputStream = null;
        try {
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            final Point bounds = getImageBounds(resolver, uri);
            inputStream = openInputStream(resolver, uri);
            if (bounds == null || inputStream == null) {
                result.status = BitmapResult.STATUS_EXCEPTION;
                return result;
            }
            opts.inSampleSize = Math.max(bounds.x / maxSize, bounds.y / maxSize);

            final Bitmap decodedBitmap = decodeStream(inputStream, null, opts);

            // Correct thumbnail orientation as necessary
            // TODO: Fix rotation if it's actually a problem
            //return rotateBitmap(resolver, uri, decodedBitmap);
            result.bitmap = decodedBitmap;
            result.status = BitmapResult.STATUS_SUCCESS;
            return result;

        } catch (FileNotFoundException exception) {
            // Do nothing - the photo will appear to be missing
        } catch (IOException exception) {
            result.status = BitmapResult.STATUS_EXCEPTION;
        } catch (IllegalArgumentException exception) {
            // Do nothing - the photo will appear to be missing
        } catch (SecurityException exception) {
            result.status = BitmapResult.STATUS_EXCEPTION;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignore) {
            }
        }
        return result;
    }

    /**
     * Wrapper around {@link BitmapFactory#decodeStream(InputStream, Rect,
     * BitmapFactory.Options)} that returns {@code null} on {@link
     * OutOfMemoryError}.
     *
     * @param is The input stream that holds the raw data to be decoded into a
     *           bitmap.
     * @param outPadding If not null, return the padding rect for the bitmap if
     *                   it exists, otherwise set padding to [-1,-1,-1,-1]. If
     *                   no bitmap is returned (null) then padding is
     *                   unchanged.
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return The decoded bitmap, or null if the image data could not be
     *         decoded, or, if opts is non-null, if opts requested only the
     *         size be returned (in opts.outWidth and opts.outHeight)
     */
    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        ByteArrayOutputStream out = null;
        InputStream byteStream = null;
        try {
            out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int n = is.read(buffer);
            while (n >= 0) {
                out.write(buffer, 0, n);
                n = is.read(buffer);
            }

            final byte[] bitmapBytes = out.toByteArray();

            // Determine the orientation for this image
            final int orientation = Exif.getOrientation(bitmapBytes);

            // Create an InputStream from this byte array
            byteStream = new ByteArrayInputStream(bitmapBytes);

            final Bitmap originalBitmap = BitmapFactory.decodeStream(byteStream, outPadding, opts);

            if (byteStream != null && originalBitmap == null && !opts.inJustDecodeBounds) {
                Log.w(TAG, "ImageUtils#decodeStream(InputStream, Rect, Options): "
                        + "Image bytes cannot be decoded into a Bitmap");
                throw new UnsupportedOperationException(
                        "Image bytes cannot be decoded into a Bitmap.");
            }
            if (originalBitmap != null && orientation != 0) {
                final Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                        originalBitmap.getHeight(), matrix, true);
            }
            return originalBitmap;
        } catch (OutOfMemoryError oome) {
            Log.e(TAG, "ImageUtils#decodeStream(InputStream, Rect, Options) threw an OOME", oome);
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "ImageUtils#decodeStream(InputStream, Rect, Options) threw an IOE", ioe);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
            if (byteStream != null) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
    }

    /**
     * Gets the image bounds
     *
     * @param resolver The ContentResolver
     * @param uri The uri
     *
     * @return The image bounds
     */
    private static Point getImageBounds(ContentResolver resolver, Uri uri)
            throws IOException {
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        InputStream inputStream = null;
        String scheme = uri.getScheme();
        try {
            opts.inJustDecodeBounds = true;
            inputStream = openInputStream(resolver, uri);
            if (inputStream == null) {
                return null;
            }
            decodeStream(inputStream, null, opts);

            return new Point(opts.outWidth, opts.outHeight);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    private static InputStream openInputStream(ContentResolver resolver, Uri uri) throws
            FileNotFoundException {
        String scheme = uri.getScheme();
        if ("http".equals(scheme) || "https".equals(scheme)) {
            try {
                return new URL(uri.toString()).openStream();
            } catch (MalformedURLException e) {
                // Fall-back to the previous behaviour, just in case
                Log.w(TAG, "Could not convert the uri to url: " + uri.toString());
                return resolver.openInputStream(uri);
            } catch (IOException e) {
                Log.w(TAG, "Could not open input stream for uri: " + uri.toString());
                return null;
            }
        } else if ("data".equals(scheme)) {
            byte[] data = parseDataUri(uri);
            if (data != null) {
                return new ByteArrayInputStream(data);
            }
        }
        return resolver.openInputStream(uri);
    }

    private static byte[] parseDataUri(Uri uri) {
        String ssp = uri.getSchemeSpecificPart();
        try {
            if (ssp.startsWith(BASE64_URI_PREFIX)) {
                String base64 = ssp.substring(BASE64_URI_PREFIX.length());
                return Base64.decode(base64, Base64.URL_SAFE);
            } else if (BASE64_IMAGE_URI_PATTERN.matcher(ssp).matches()){
                String base64 = ssp.substring(
                        ssp.indexOf(BASE64_URI_PREFIX) + BASE64_URI_PREFIX.length());
                return Base64.decode(base64, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "Mailformed data URI: " + ex);
            return null;
        }
    }
}
