-dontwarn butterknife.internal.**

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
