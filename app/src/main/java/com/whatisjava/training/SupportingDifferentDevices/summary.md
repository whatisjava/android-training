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
    