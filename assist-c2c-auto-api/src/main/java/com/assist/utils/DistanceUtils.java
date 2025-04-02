package com.assist.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import java.math.BigDecimal;

public class DistanceUtils {

    /**
     * 根据经纬度，计算两点间的距离
     * @param longitudeFrom 第一个点的经度
     * @param latitudeFrom 第一个点的纬度
     * @param longitudeTo 第二个点的经度
     * @param latitudeTo 第二个点的纬度
     * @return 返回距离 单位米
     */
    public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);
        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }

    /**
     * 根据经纬度，计算两点间的距离
     * @param longitudeFrom 第一个点的经度
     * @param latitudeFrom 第一个点的纬度
     * @param longitudeTo 第二个点的经度
     * @param latitudeTo 第二个点的纬度
     * @param accurate 保留小数点几位
     * @return 返回距离 单位千米
     */
    public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo,int accurate) {
        double distance = getDistance(longitudeFrom, latitudeFrom, longitudeTo, latitudeTo);
        if (accurate < 0) {
            throw new RuntimeException("精确度必须是正整数或零");
        }
        return new BigDecimal(distance).divide(new BigDecimal(1000),accurate, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double getDistance(String longitudeFrom, String latitudeFrom, String longitudeTo, String latitudeTo,int accurate) {
        double lng1 = Double.parseDouble(longitudeFrom);
        double lat1 = Double.parseDouble(latitudeFrom);
        double lng2 = Double.parseDouble(longitudeTo);
        double lat2 = Double.parseDouble(latitudeTo);
        return getDistance(lng1, lat1, lng2, lat2, accurate);
    }

    public static void main(String[] args) {
        double result = getDistance(118.039733, 24.583635, 118.1083, 24.608632,2);
        System.out.println("经纬度距离计算结果：" + result+ "千米");
    }
}
