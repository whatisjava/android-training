# 声明权限
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.snazzyapp">

    <uses-permission android:name="android.permission.SEND_SMS"/>
    

    <application ...>
        ...
    </application>

</manifest>
```
# 在运行时请求权限
## 系统权限分为两类：正常权限和危险权限：
* 正常权限不会直接给用户隐私权带来风险。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限。
* 危险权限会授予应用访问用户机密数据的权限。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限。如果您
列出了危险权限，则用户必须明确批准您的应用使用这些权限。

