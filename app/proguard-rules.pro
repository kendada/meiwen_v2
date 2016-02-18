# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/abc/Library/Android/sdk/tools/proguard/proguard-android.txt
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

-ignorewarnings
-libraryjars libs/BmobSDK_V3.4.4_0930.jar
-libraryjars libs/BmobPush_V0.6beta_20150408.jar

-keepattributes Signature
-keep class cn.bmob.v3.** {*;}
-keep class cn.bmob.push.** {*;}

-keep class cc.meiwen.model.**{*;}

-keep class cc.meiwen.view.**{*;}

-keep class com.umeng.analytics.**{*;}

-keep public class cc.emm.** {*;}
-keep public interface cc.emm.** {*;}
-dontwarn cc.emm.**