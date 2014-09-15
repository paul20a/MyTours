package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * 
 * Asynchronous bitmapDrawable class
 * 
 * @author Paul Cairney
 *
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapProcessor> bitMapProcessorTaskReference;

    /**
     * 
     * Constructor with paramaters
     * 
     * @param res - Resources from the context
     * @param bitmap - the default bitmap
     * @param bitmapProcessor - the async task processing the bitmap
     */
    public AsyncDrawable(Resources res, Bitmap bitmap,
    		BitmapProcessor bitmapProcessor) {
        super(res, bitmap);
        bitMapProcessorTaskReference =
            new WeakReference<BitmapProcessor>(bitmapProcessor);
    }

    /**
     * @return the task associated to this AsyncDrawable
     */
    public BitmapProcessor getBitMapProcessor() {
        return bitMapProcessorTaskReference.get();
    }
}