# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/maxwell/Design/adt-bundle-mac-x86_64-20130729/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-libraryjars libs/alipaysdk.jar
-dontwarn com.alipay.**
-keep class com.alipay.** {*; }

#-libraryjars libs/android-async-http-1.4.6.jar
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** {*; }

#-libraryjars libs/android-logging-log4j-1.0.3.jar
-dontwarn de.mindpipe.android.logging.log4j.**
-keep class de.mindpipe.android.logging.log4j.** {*; }

#-libraryjars libs/baidumapapi_v3_2_0.jar
#-libraryjars libs/locSDK_3.3.jar
-dontwarn com.baidu.android.**
-dontwarn com.baidu.lbsapi.**
-dontwarn com.baidu.location.**
-dontwarn com.baidu.mapapi.**
-dontwarn com.baidu.platform.**
-dontwarn com.baidu.vi.**
-dontwarn vi.com.gdi.**

-keep class com.baidu.android.** {*; }
-keep class com.baidu.lbsapi.** {*; }
-keep class com.baidu.location.** {*; }
-keep class com.baidu.mapapi.** {*; }
-keep class com.baidu.platform.** {*; }
-keep class com.baidu.vi.** {*; }
-keep class vi.com.gdi.** {*; }


-dontwarn org.apache.log4j.**
-keep class org.**{*;}
-keep class javax.swing.**{*;}
-keep class java.awt.**{*;}
#-libraryjars libs/log4j-1.2.8.jar
#-keep class org.apache.log4j.** {*; }
-dontwarn com.activeandroid.**
-keep class com.activeandroid.ActiveAndroid{*;}

-keep class com.activeandroid.**{*;}
-keepattributes *Annotation*,Signature

-dontwarn retrofit.**
-keep interface rx.**{*;}
-keep class rx.**{*;}
-keep class com.google.gson.** {*;}
-keep class com.google.inject.** {*;}
-keep class org.apache.http.** {*;}
-keep class org.apache.james.mime4j.** {*;}
-keep class javax.inject.** {*;}
-keep class retrofit.**{*;}



-dontwarn rx.*
-keep class com.example.testobfuscation.** { *; }
-keep class sun.misc.Unsafe { *; }

-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.**
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# 保留所有Support问题
-keep class android.support.**{ *;}

-keep class android-support-** { *; }
-keep interface android.support.** { *; }
-keep interface android-support-** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

#-libraryjars libs/qiniu-android-sdk-7.0.3.jar
-dontwarn com.qiniu.android.**
-keep class com.qiniu.android.** {*; }


#-libraryjars libs/universal-image-loader-1.9.3-with-sources.jar
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** {*; }


#-libraryjars libs/volley.jar
-dontwarn com.android.volley.**
-keep class com.android.volley.** {*; }


-keep class ** extends xj.property.beans.XJ { *; }
# 20160405 添加对netbean.BaseBean的子类混淆
-keep class ** extends xj.property.netbase.BaseBean { *; }
-keep class xj.property.beans.**{*; }
-dontwarn xj.property.cache.**
-keep class xj.property.cache.**{*; }


-keep class xj.property.widget.**{*; }

-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}

-dontwarn  com.easemob.**
#2.0.9后的不需要加下面这个keep
#-keep class org.xbill.DNS.** {*;}
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
#不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
-keep class xj.property.utils.SmileUtils {  *;}

#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**


-keep class org.ice4j.** {*;}

-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}

-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.** {*;}

-keep class ch.imvs.** {*;}

-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-dontwarn  com.xiaomi.**

-keep class xj.property.receiver.XJMessageReceiver {*;}

-keep class com.tencent.mm.sdk.** { *;}


-dontwarn cn.jpush.**
-dontwarn com.google.gson.jpush.**

-keep class com.google.gson.jpush.** { *; }

-keep class cn.jpush.** { *; }

-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class com.tencent.** { *;}
-keep class com.sina.** { *;}


-keep public class [xj.property].R$*{
    public static final int *;
}


-dontwarn demo.**
-keep class demo.** { *;}
-keep class com.hp.hpl.sparta.** { *;}


-dontwarn retrofit.**

-keep class retrofit.** { *; }

-keep class xj.property.statistic.** { *; }

-keepattributes Signature
-keepattributes Exceptions

-dontwarn okio.**

#不混淆org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}