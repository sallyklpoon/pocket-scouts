package com.example.termproject;

public class IconAssignment {
    public static int iconID(int iconRefValue) {
        switch(iconRefValue) {
            case 1:
                return R.drawable.events_bike;
            case 2:
                return R.drawable.events_cafe;
            case 3:
                return R.drawable.events_food;
            case 4:
                return R.drawable.events_nature;
            case 5:
                return R.drawable.events_party;
            case 6:
                return R.drawable.events_run;
            case 7:
                return R.drawable.events_sparkle;
            default:
                return -1;
        }
    }
}
