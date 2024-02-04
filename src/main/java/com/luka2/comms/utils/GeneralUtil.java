package com.luka2.comms.utils;

public class GeneralUtil {

    private GeneralUtil() { throw new IllegalStateException("Utility class"); }

    public static <T> int getCount(Iterable<T> collection){
        int counter = 0;
        for(T ignored : collection) counter++;
        return counter;
    }

}
