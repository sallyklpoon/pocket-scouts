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

    public static int getIconMipMapID(int iconRefValue) {
        switch(iconRefValue) {
            case 0:
                return R.mipmap.events_bike_circle;
            case 1:
                return R.mipmap.events_cafe_circle;
            case 2:
                return R.mipmap.events_food_circle;
            case 3:
                return R.mipmap.events_nature_circle;
            case 4:
                return R.mipmap.events_party_circle;
            case 5:
                return R.mipmap.events_run_circle;
            case 6:
                return R.mipmap.events_sparkle_circle;
            default:
                return -1;
        }
    }

}
