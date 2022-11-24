package com.example.termproject;

public class IconAssignment {
    public static int getIconID(int iconRefValue) {
        switch(iconRefValue) {
            case 0:
                return R.drawable.events_bike;
            case 1:
                return R.drawable.events_cafe;
            case 2:
                return R.drawable.events_food;
            case 3:
                return R.drawable.events_nature;
            case 4:
                return R.drawable.events_party;
            case 5:
                return R.drawable.events_run;
            case 6:
                return R.drawable.events_sparkle;
            default:
                return -1;
        }
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
