## 支持不同语言
1. 创建语言区域目录和字符串文件
    ```
    MyProject/
        res/
           values/
               strings.xml
           values-es/
               strings.xml
           values-fr/
               strings.xml
    ```
2. 使用字符串资源
    ```
    // Get a string resource from your app's Resources
    String hello = getResources().getString(R.string.hello_world);

    // Or supply a string resource to a method that requires a string
    TextView textView = new TextView(this);
    textView.setText(R.string.hello_world);
    
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_world" />
    ```
## 支持不同屏幕
* 安卓设备的屏幕分类通常使用两个属性：尺寸和密度
    + 尺寸维度有四个：small, normal, large, xlarge
    + 密度维度有四个：ldpi, mdpi, hdpi, xhdpi
    
1. 创建不同的布局文件
```
MyProject/
    res/
        layout/              # default (portrait)
            main.xml
        layout-land/         # landscape
            main.xml
        layout-large/        # large (portrait)
            main.xml
        layout-large-land/   # large landscape
            main.xml
```
2. 创建不同的图片资源
 * xxxhdpi : 4.0
 * xxhdpi : 3.0
 * xhdpi : 2.0
 * hdpi : 1.5
 * mdpi : 1.0 (baseline)
 * ldpi : 0.75
 + 如果为xhdpi的设备生成200 × 200的图片，那么你必须为hdpi的设备生成150 × 150的图片，
 为mdpi的设备生成100×100的图片，为ldpi的设备生成75×75的图片，才能显示一样的大小，
 并将这些图片分别放置到这样的文件夹中：
 ```
 MyProject/
     res/
         drawable-xhdpi/
             awesomeimage.png
         drawable-hdpi/
             awesomeimage.png
         drawable-mdpi/
             awesomeimage.png
         drawable-ldpi/
             awesomeimage.png
 ```
 注：ldpi的资源可以由hdpi的资源由系统自动缩小一半来适配
 
## 支持不同系统平台版本
 1. 指定最低和目标sdk级别。
 ```
 <manifest xmlns:android="http://schemas.android.com/apk/res/android" ... >
     <uses-sdk android:minSdkVersion="4" android:targetSdkVersion="15" />
     ...
 </manifest>
 ```
 2. 运行时检查系统版本
 ```
 private void setUpActionBar() {
     // Make sure we're running on Honeycomb or higher to use ActionBar APIs
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
         ActionBar actionBar = getActionBar();
         actionBar.setDisplayHomeAsUpEnabled(true);
     }
 }
 ```
 + When parsing XML resources, Android ignores XML attributes that aren’t supported by the current device. So you can safely use XML attributes that are only supported by newer versions without worrying about older versions breaking when they encounter that code. 
 3. Use Platform Styles and Themes
 ```
 // To make your activity look like a dialog box:
 <activity android:theme="@android:style/Theme.Dialog">
 
 // To make your activity have a transparent background:
 <activity android:theme="@android:style/Theme.Translucent">
 
 // To apply your own custom theme defined in /res/values/styles.xml:
 <activity android:theme="@style/CustomTheme">
 
 // To apply a theme to your entire app (all activities), add the android:theme attribute to the <application> element:
 <application android:theme="@style/CustomTheme">
 ```