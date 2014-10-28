package org.kaziprst.android.ndfilter;

import android.content.Context;

public class ShutterSpeedCalculator {
    // Make sure to keep this and the strings.xml arrays in sync.
    private static final double[] shutter_speeds = new double[]{1.0 / 8000, 1.0 / 6400, 1.0 / 5000,
            1.0 / 4000, 1.0 / 3200, 1.0 / 2500, 1.0 / 2000, 1.0 / 1600, 1.0 / 1250, 1.0 / 1000,
            1.0 / 800, 1.0 / 640, 1.0 / 500, 1.0 / 400, 1.0 / 320, 1.0 / 250, 1.0 / 200, 1.0 / 160,
            1.0 / 125, 1.0 / 100, 1.0 / 80, 1.0 / 60, 1.0 / 50, 1.0 / 40, 1.0 / 30, 1.0 / 25,
            1.0 / 20, 1.0 / 15, 1.0 / 13, 1.0 / 10, 1.0 / 8, 1.0 / 6, 1.0 / 5, 1.0 / 4, 1.0 / 3,
            1.0 / 2.5, 1.0 / 2, 1.0 / 1.6, 1.0 / 1.3, 1, 1.3, 1.6, 2, 2.5, 3, 4, 5, 6, 8, 10,
            13, 15, 20, 25, 30};

    private static final int[] nd_values = new int[] { 2, 4, 8, 16, 32, 64, 100, 128, 256, 400,
            512, 1024, 2048, 4096, 8192};

    public static String calculateShutterSpeed(Context c, int shutter_index, int nd_index) {
        double shutter_speed = shutter_speeds[shutter_index] * nd_values[nd_index];

        StringBuilder result = new StringBuilder();
        int rounded_speed = (int)(1/shutter_speed);
        if (shutter_speed < 1 && rounded_speed != 1) {
            result.append("1/").append(rounded_speed).append(c.getString(R.string.seconds));
        } else if (rounded_speed == 1) {
            result.append("1").append(c.getString(R.string.seconds));
        } else if (shutter_speed < 60) {
            result.append(roundNumber(shutter_speed)).append(c.getString(R.string.seconds));
        } else if (shutter_speed < 60*60) {
            long minutes = (long)shutter_speed / 60;
            long seconds = (long)shutter_speed - minutes * 60;
            result.append(minutes).append(c.getString(R.string.minutes));
            if (seconds != 0) {
                result.append(" ").append(seconds).append(c.getString(R.string.seconds));
            }
        } else {
            long hours = (long)shutter_speed / (60 * 60);
            long minutes = ((long)shutter_speed - hours * 60 * 60) / 60;
            result.append(hours).append(c.getString(R.string.hours));
            if (minutes != 0) {
                result.append(" ").append(minutes).append(c.getString(R.string.minutes));
            }
        }
        return result.toString();
    }

    private static String roundNumber(double number) {
        if (number == (long) number) {
            return Long.toString((long)number);
        }
        return String.format("%.1f", number);
    }
}
