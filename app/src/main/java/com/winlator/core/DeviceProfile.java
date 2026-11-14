package com.winlator.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

/**
 * Detects the device class this fork is tuned for (Snapdragon 8 Gen 2 / Adreno 740,
 * 8 GB of RAM shared between CPU and GPU) so low-memory defaults can be applied
 * automatically. Values are starting points and are meant to be refined with testing.
 */
public abstract class DeviceProfile {
    /** Devices reporting less than this much RAM get the low-memory defaults. */
    private static final long LOW_MEMORY_THRESHOLD_BYTES = 10L * 1024 * 1024 * 1024;

    /** Video memory (MB) DXVK reports to games on low-memory devices when the user hasn't set one. */
    public static final String LOW_MEMORY_DXVK_DEVICE_MEMORY_MB = "2048";

    private static Boolean lowMemoryDevice = null;

    public static synchronized boolean isLowMemoryDevice(Context context) {
        if (lowMemoryDevice == null) {
            ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            lowMemoryDevice = memoryInfo.totalMem > 0 && memoryInfo.totalMem < LOW_MEMORY_THRESHOLD_BYTES;
        }
        return lowMemoryDevice;
    }

    /** True on Snapdragon 8 Gen 2 (SM8550, Adreno 740). */
    public static boolean isSnapdragon8Gen2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            String soc = Build.SOC_MODEL;
            return soc != null && soc.toUpperCase().startsWith("SM8550");
        }
        return false;
    }
}
