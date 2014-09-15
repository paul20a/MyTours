package dcs.gla.ac.uk.minerva;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipelessPager extends ViewPager {

	    /**
	     * @param context - Context
	     */
	    public SwipelessPager(Context context) {
	        super(context);
	    }

	    /**
	     * @param context - Context
	     * @param attrs - AttributeSet
	     */
	    public SwipelessPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    /* (non-Javadoc)
	     * @see android.support.v4.view.ViewPager#onInterceptTouchEvent(android.view.MotionEvent)
	     */
	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent arg0) {
	        // Disable swipe between pages
	        return false;
	    }

	    /* (non-Javadoc)
	     * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	     */
	    @SuppressLint("ClickableViewAccessibility")
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	// Disable swipe between pages
	        return false;
	    }

	}