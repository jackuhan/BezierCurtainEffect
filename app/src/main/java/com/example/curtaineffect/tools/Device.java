package com.example.curtaineffect.tools;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Desert on 15/11/2.
 */
public class Device {

  private static DeviceInfo info = new DeviceInfo();

  private Device() {
    throw new UnsupportedOperationException("No Instance");
  }

  public static void setUp(Context context) {
    info.uniqueId = getDeviceUniqueId(context);
    info.resolution = getResolution(context);
    info.networkType = getQXNetworkType(context);
    info.qosNetworkType = getLYQosNetworkType(context);
    info.screenWidth = getRealResolution(context).screenWidth;
    info.screenHeight = getRealResolution(context).screenHeight;
  }

  public static TelephonyManager getTelephonyManager(Context context) {
    return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  }

  public static WifiManager getWifiManager(Context context) {
    return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
  }

  public static ConnectivityManager getConnectivityManager(Context context) {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  /**
   * 设备唯一Id
   */
  public static String getDeviceUniqueId(Context context) {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   *
   * @return Device Unique Id
   */
  public static String getDeviceUniqueId() {
    return info.uniqueId;
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   */
  public static String getWiFiMacAddress() {
    return info.macAddress;
  }

  /**
   * 序列号
   */
  public static String getSerialNumber(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      return Build.SERIAL;
    } else {
      return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
  }

  /**
   * 手机型号
   */
  public static String getModel() {
    return Build.MODEL;
  }

  /**
   * 手机品牌
   */
  public static String getBrand() {
    return Build.BRAND;
  }

  /**
   * @return OS版本号
   */
  public static String getOSVer() {
    return Build.VERSION.RELEASE;
  }

  /**
   * 屏幕分辨率
   */
  public static String getResolution(Context context) {
    return getScreenWidth(context) + "x" + getScreenHeight(context);
  }

  public static DeviceResolution getRealResolution(Context context) {
    int realWidth = 0;
    int realHeight = 0;
    Display display = getWindowDisplay(context);
    // For JellyBeans and onward
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      DisplayMetrics metrics = new DisplayMetrics();
      display.getRealMetrics(metrics);
      realWidth = metrics.widthPixels;
      realHeight = metrics.heightPixels;
    } else {
      // Below Jellybeans you can use reflection method
      try {
        Method mGetRawH = Display.class.getMethod("getRawHeight");
        Method mGetRawW = Display.class.getMethod("getRawWidth");
        realWidth = (Integer) mGetRawW.invoke(display);
        realHeight = (Integer) mGetRawH.invoke(display);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }

    return new DeviceResolution(realWidth, realHeight);
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   *
   * @return 屏幕分辨率
   */
  public static String getResolution() {
    return info.resolution;
  }

  private static Display getWindowDisplay(Context context) {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    return windowManager.getDefaultDisplay();
  }

  public static int getScreenHeight(Context context) {

    Display display = getWindowDisplay(context);

    int navigationBarResId =
        context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
    int navigationBarHeight = 0;
    if (navigationBarResId > 0) {
      navigationBarHeight = context.getResources().getDimensionPixelSize(navigationBarResId);
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
      //noinspection deprecation
      return display.getHeight() + navigationBarHeight;
    } else {
      Point screen = new Point();
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
        screen.y += navigationBarHeight;
      } else {
        display.getRealSize(screen);
      }
      return screen.y;
    }
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   *
   * @return 屏幕高度
   */
  public static int getScreenHeight() {
    return info.screenHeight;
  }

  /**
   * @param context
   * @return
   */
  public static int getScreenWidth(Context context) {
    Display display = getWindowDisplay(context);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
      //noinspection deprecation
      return display.getWidth();
    } else {
      Point screen = new Point();
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
        display.getSize(screen);
      } else {
        display.getRealSize(screen);
      }
      return screen.x;
    }
  }

  private static String getDeviceIMEI(Context context) {
    return getTelephonyManager(context).getDeviceId();
  }

  public static String getDeviceIMEI() {
    return info.imei;
  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public static int dip2px(Context context, float dpValue) {
    if(null == context) {
      return (int)dpValue;
    }
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public static int dip2px(Resources res, int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        res.getDisplayMetrics());
  }

  public static int dip2px(Resources res, float dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        res.getDisplayMetrics());
  }

  /**
   * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static float sp2px(Resources res, float spValue) {
    final float scale = res.getDisplayMetrics().scaledDensity;
    return spValue * scale;
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   *
   * @return 屏幕宽度
   */
  public static int getScreenWidth() {
    return info.screenWidth;
  }

  /**
   * Call after {@link #setUp(Context)} ,otherwise will get null value
   *
   * @return 网络类型
   */
  public static String getQXNetworkType() {
    return info.networkType;
  }

  /**
   *
   */
  public static String getLYQosNetworkType() {
    return info.qosNetworkType;
  }

  /**
   * 获取网络类型
   *
   * @return 网络类型
   */
  public static String getQXNetworkType(Context context) {
    NetworkInfo activeNetworkInfo = getConnectivityManager(context).getActiveNetworkInfo();
    if (null != activeNetworkInfo) {
      int activeNetworkType = activeNetworkInfo.getType();
      switch (activeNetworkType) {
        case ConnectivityManager.TYPE_MOBILE:
          return getMobileType(context);
        case ConnectivityManager.TYPE_WIFI:
          return "wifi";
        case ConnectivityManager.TYPE_ETHERNET:
          return "ethernet";
        default:
          return "unknown";
      }
    }
    return "unknown";
  }

  static String getMobileType(Context context) {
    int networkType = getTelephonyManager(context).getNetworkType();
    switch (networkType) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
        return "gprs";
      case TelephonyManager.NETWORK_TYPE_EDGE:
        return "edge";
      case TelephonyManager.NETWORK_TYPE_UMTS:
        return "umts";
      case TelephonyManager.NETWORK_TYPE_HSDPA:
        return "hsdps";
      case TelephonyManager.NETWORK_TYPE_HSUPA:
        return "hsupa";
      case TelephonyManager.NETWORK_TYPE_HSPA:
        return "hspa";
      case TelephonyManager.NETWORK_TYPE_CDMA:
        return "cdma";
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
        return "evdo";
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
        return "evdo";
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
        return "evdo";
      case TelephonyManager.NETWORK_TYPE_1xRTT:
        return "1xrtt";
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return "hspap";
      case TelephonyManager.NETWORK_TYPE_LTE:
        return "lte";
      case TelephonyManager.NETWORK_TYPE_UNKNOWN:
      default:
        return "unknown";
    }
  }

  /**
   * 获取网络类型
   *
   * @return 网络类型
   */
  public static String getLYQosNetworkType(Context context) {
    NetworkInfo activeNetworkInfo = getConnectivityManager(context).getActiveNetworkInfo();
    if (null != activeNetworkInfo) {
      int activeNetworkType = activeNetworkInfo.getType();
      switch (activeNetworkType) {
        case ConnectivityManager.TYPE_MOBILE:
          return getQosMobileType(context);
        case ConnectivityManager.TYPE_WIFI:
          return "1";
        case ConnectivityManager.TYPE_ETHERNET:
          return "13";
        default:
          return "-1";
      }
    }
    return "-1";
  }

  static String getQosMobileType(Context context) {
    int networkType = getTelephonyManager(context).getNetworkType();
    switch (networkType) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
        return "2";
      case TelephonyManager.NETWORK_TYPE_EDGE:
        return "3";
      case TelephonyManager.NETWORK_TYPE_UMTS:
        return "4";
      case TelephonyManager.NETWORK_TYPE_HSDPA:
        return "5";
      case TelephonyManager.NETWORK_TYPE_HSUPA:
        return "6";
      case TelephonyManager.NETWORK_TYPE_HSPA:
        return "7";
      case TelephonyManager.NETWORK_TYPE_CDMA:
        return "8";
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
        return "9";
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
        return "10";
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
        return "10";
      case TelephonyManager.NETWORK_TYPE_1xRTT:
        return "11";
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return "12";
      case TelephonyManager.NETWORK_TYPE_LTE:
        return "14";
      case TelephonyManager.NETWORK_TYPE_UNKNOWN:
      default:
        return "-1";
    }
  }

  /**
   * 判断当前网络是否可用
   */
  public static boolean isNetworkConnected(Context context) {
    if (context != null) {
      ConnectivityManager mConnectivityManager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  static class DeviceInfo {

    /**
     * 龙源 Qos 网络类型,取值区别于{@link #networkType}
     */
    public String qosNetworkType = null;
    /**
     * 设备唯一Id
     */
    private String uniqueId = null;
    /**
     *
     */
    private String imei = "";
    /**
     * WiFi Mac 地址
     */
    private String macAddress = null;
    /**
     * 屏幕分辨率,eg:720x1080
     */
    private String resolution = null;
    /**
     * 奇秀网络类型字典,区别于龙源的值
     */
    private String networkType = null;
    /**
     * 屏幕高度
     */
    private int screenHeight = 0;
    /**
     * 屏幕宽度
     */
    private int screenWidth = 0;
    public DeviceInfo() {
    }

    @Override public String toString() {
      return "DeviceInfo{" +
          "qosNetworkType='" + qosNetworkType + '\'' +
          ", uniqueId='" + uniqueId + '\'' +
          ", imei='" + imei + '\'' +
          ", macAddress='" + macAddress + '\'' +
          ", resolution='" + resolution + '\'' +
          ", networkType='" + networkType + '\'' +
          ", screenHeight=" + screenHeight +
          ", screenWidth=" + screenWidth +
          '}';
    }
  }

  private static class DeviceResolution {
    private int screenWidth;
    private int screenHeight;

    public DeviceResolution(int width, int height) {
      if (width >= height) {
        this.screenWidth = height;
        this.screenHeight = width;
        return;
      }
      this.screenHeight = height;
      this.screenWidth = width;
    }


  }
}