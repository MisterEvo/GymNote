package com.sommerfeld.gymnote.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getCurrentTimestamp() {
        String currentDateTime = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            currentDateTime = dateFormat.format(new Date());
        } catch (Exception e) {
            return null;
        }

        return currentDateTime;
    }
}
