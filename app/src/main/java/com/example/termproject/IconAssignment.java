package com.example.termproject;

public class IconAssignment {
    public static int getIconID(int iconRefValue) {
        if (iconRefValue == 0) {
            return R.drawable.events_bike;
        } else if (iconRefValue == 1) {
            return R.drawable.events_cafe;
        } else if (iconRefValue == 2) {
            return R.drawable.events_food;
        } else if (iconRefValue == 3) {
            return R.drawable.events_nature;
        } else if (iconRefValue == 4) {
            return R.drawable.events_party;
        } else if (iconRefValue == 5) {
            return R.drawable.events_run;
        } else if (iconRefValue == 6) {
            return R.drawable.events_sparkle;
        }
        return -1;
    }

    public static int getIconMipMapID(Long iconRefValue) {
        if (iconRefValue == 0) {
            return R.mipmap.events_bike_circle;
        } else if (iconRefValue == 1) {
            return R.mipmap.events_cafe_circle;
        } else if (iconRefValue == 2) {
            return R.mipmap.events_food_circle;
        } else if (iconRefValue == 3) {
            return R.mipmap.events_nature_circle;
        } else if (iconRefValue == 4) {
            return R.mipmap.events_party_circle;
        } else if (iconRefValue == 5) {
            return R.mipmap.events_run_circle;
        } else if (iconRefValue == 6) {
            return R.mipmap.events_sparkle_circle;
        }
        return -1;
    }

}
