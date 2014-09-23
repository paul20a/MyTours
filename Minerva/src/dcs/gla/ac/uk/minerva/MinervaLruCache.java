package dcs.gla.ac.uk.minerva;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MinervaLruCache {
	private LruCache<String, Bitmap> bitmapCache;
	//divisor to assign memory to cache
	private final int MEM_DIV = 15;
	
	/**
	 * default constructor
	 */
	public MinervaLruCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int size = maxMemory / MEM_DIV;
		bitmapCache = new LruCache<String, Bitmap>(size) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// size in kb rather than entries
				return bitmap.getByteCount() / 1024;
			}
		};
	}	
	
	/**
	 * 
	 * Store an entry into the cache
	 * 
	 * @param key
	 *            - identifier of bitmap
	 * @param bitmap
	 *            - the Bitmap to be stored
	 */
	public void cacheBitmap(String key, Bitmap bitmap) {
		// ensure entries don't repeat
		 synchronized (bitmapCache) {
		if (getCachedBitmap(key) == null) {
			// add to cache
			bitmapCache.put(key, bitmap);
		}
		 }
	}	
	
	/**
	 * 
	 * retrieve Bitmap from the cache
	 * 
	 * @param key
	 *            - identifier of bitmap
	 * @return - cached Bitmap or null
	 */
	public Bitmap getCachedBitmap(String key) {
		// retrieve entry
		return bitmapCache.get(key);
	}

}
