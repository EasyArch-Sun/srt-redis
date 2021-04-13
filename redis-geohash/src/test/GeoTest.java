package test;

import java.util.DistanceHelper;
import java.util.GeoHashHelper;

public class GeoTest {

    public static void main(String[] args) {

        long start=System.currentTimeMillis();

        System.out.println(new GeoHashHelper().around(45.1233,118.123));
        System.out.println(new GeoHashHelper().around(46.1343,117.123));
        System.out.println(DistanceHelper.disstace(45.1233,118.123,46.1343,117.123));

        System.out.println("waste time" + (System.currentTimeMillis()-start));

//        System.out.println(new GeoHashHelper().encode(45.1233,118.123));


    }
}
