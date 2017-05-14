# 与其他应用交互
## 向另一个应用发送意图（Intent）
1. 构建隐式Intent
* 隐含 Intent 不声明要启动的组件的类名称，而是声明要执行的操作。 该操作指定您要执行的操作，比如查看、编辑、发送
或 获取 某项。 Intent 通常还包含与操作关联的数据，比如您要查看的地址或您要发送的电子邮件消息。根据要创建的 Intent，
数据可能是 Uri、多种其他数据类型之一，或 Intent 可能根本就不需要数据。
  
* 如果您的数据是 Uri，您可以使用一个简单的 Intent() 构造函数来定义操作和数据。    
  + 发起电话呼叫
  ```java
  Uri number = Uri.parse("tel:5551234");
  Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
  ```
  + 查看地图
  ```java
  // Map point based on address
  Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
  // Or map point based on latitude/longitude
  // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
  Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
  ```
  + 查看网页
  ```java
  Uri webpage = Uri.parse("http://www.android.com");
  Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
  ```
* 其他类型的隐含 Intent 需要提供不同数据类型（比如，字符串）的“额外”数据。 您可以使用各种 putExtra() 方法添加
一条或多条 extra 数据。
  
* 默认情况下，系统基于所包含的 Uri 数据确定 Intent 需要的相应 MIME 类型。如果您未在 Intent 中包含 Uri，您通常
应使用 setType() 指定与 Intent 关联的数据的类型。 设置 MIME 类型可进一步指定哪些类型的 Activity 应接收 Intent。 
    + 发送带附件的电子邮件
    ```java
      Intent emailIntent = new Intent(Intent.ACTION_SEND);
      // The intent does not have a URI, so declare the "text/plain" MIME type
      emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
      emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
      emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
      emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
      // You can also attach multiple items by passing an ArrayList of Uris
   ```
   + 创建日历事件
       ```java
         Intent calendarIntent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);
         Calendar beginTime = Calendar.getInstance().set(2012, 0, 19, 7, 30);
         Calendar endTime = Calendar.getInstance().set(2012, 0, 19, 10, 30);
         calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
         calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
         calendarIntent.putExtra(Events.TITLE, "Ninja class");
         calendarIntent.putExtra(Events.EVENT_LOCATION, "Secret dojo");
      ```
    注：只有 API 级别 14 或更高级别支持此日历事件 Intent
* 尽可能具体地定义您的 Intent 非常重要。例如，如果您想要使用 ACTION_VIEW Intent 显示图像，您应指定 MIME 类型 image/*。
这可防止可“查看”数据的其他类型的应用（比如地图应用）被 Intent 触发。
  
2. 验证是否存在接收 Intent 的应用
* 尽管 Android 平台保证某些 Intent 可以分解为内置应用之一（比如，“电话”、“电子邮件”或“日历”应用），您应在调用 
Intent 之前始终包含确认步骤。

* 注意：如果您调用了 Intent，但设备上没有可用于处理 Intent 的应用，您的应用将崩溃。

* 要确认是否存在可响应 Intent 的可用 Activity，请调用 queryIntentActivities() 来获取能够处理您的 Intent 的
Activity 列表。如果返回的 List 不为空，您可以安全地使用该 Intent。例如：
```java
PackageManager packageManager = getPackageManager();
List activities = packageManager.queryIntentActivities(intent,
        PackageManager.MATCH_DEFAULT_ONLY);
boolean isIntentSafe = activities.size() > 0;
```
如果 isIntentSafe 是 true，则至少有一个应用将响应该 Intent。 如果它是 false，则没有任何应用处理该 Intent。

注：在您需要在用户尝试使用它之前停用使用该 Intent 的功能时，您应在 Activity 初次启动时执行此检查。 如果您了解
可处理 Intent 的特定应用，您还可以为用户提供下载该应用的链接
 
 
3. 启动具有 Intent 的 Activity
一旦您已创建您的 Intent 并设置 extra 信息，调用 startActivity() 将其发送给系统。如果系统识别可处理 Intent 的多个 Activity，它会为用户显示对话框供其选择要使用的应用，如图 1 所示。如果只有一个 Activity 处理 Intent，系统会立即将其启动。

startActivity(intent);
此处显示完整的示例：如何创建查看地图的 Intent，验证是否存在处理 Intent 的应用，然后启动它：

// Build the intent
Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

// Verify it resolves
PackageManager packageManager = getPackageManager();
List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
boolean isIntentSafe = activities.size() > 0;

// Start an activity if it's safe
if (isIntentSafe) {
    startActivity(mapIntent);
}

4. 显示应用选择器
要显示选择器，请使用 createChooser() 创建Intent 并将其传递给 startActivity()。例如：

Intent intent = new Intent(Intent.ACTION_SEND);
...

// Always use string resources for UI text.
// This says something like "Share this photo with"
String title = getResources().getString(R.string.chooser_title);
// Create intent to show chooser
Intent chooser = Intent.createChooser(intent, title);

// Verify the intent will resolve to at least one activity
if (intent.resolveActivity(getPackageManager()) != null) {
    startActivity(chooser);
}
这将显示一个对话框，其中包含响应传递给 createChooser() 方法的 Intent 的应用列表，并且将提供的文本用作对话框标题。
 
# 获取活动的结果
1. 启动 Activity
 ```
 static final int PICK_CONTACT_REQUEST = 1;  // The request code
 ...
 private void pickContact() {
     Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
     pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
     startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
 }
 ```
2. 接收结果
 ```
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     // Check which request it is that we're responding to
     if (requestCode == PICK_CONTACT_REQUEST) {
         // Make sure the request was successful
         if (resultCode == RESULT_OK) {
             // Get the URI that points to the selected contact
             Uri contactUri = data.getData();
             // We only need the NUMBER column, because there will be only one row in the result
             String[] projection = {Phone.NUMBER};
 
             // Perform the query on the contact to get the NUMBER column
             // We don't need a selection or sort order (there's only one result for the given URI)
             // CAUTION: The query() method should be called from a separate thread to avoid blocking
             // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
             // Consider using CursorLoader to perform the query.
             Cursor cursor = getContentResolver()
                     .query(contactUri, projection, null, null, null);
             cursor.moveToFirst();
 
             // Retrieve the phone number from the NUMBER column
             int column = cursor.getColumnIndex(Phone.NUMBER);
             String number = cursor.getString(column);
 
             // Do something with the phone number...
         }
     }
 }
 ```
注：在 Android 2.3（API 级别 9）之前，在 Contacts Provider 上执行查询（如上面所示）需要您的应用声明 
READ_CONTACTS 权限（请参阅安全与权限）。但是，自 Android 2.3 版本开始，“联系人”应用授予您的应用在联系人提供
程序向您返回结果时从联系人提供程序临时读取信息的权限。 该临时权限仅适用于所请求的特定联系人，因此您只能查询 
Intent 的 Uri 指定的联系人，除非您声明 READ_CONTACTS 权限。
 
# 允许其他应用启动您的 Activity
* 要允许其他应用启动您的 Activity，您需要在清单文件中为对应的 <activity> 元素添加一个 <intent-filter> 元素。
  
  当您的应用安装在设备上时，系统会识别您的 Intent 过滤器并添加信息至所有已安装应用支持的 Intent 内部目录。
  当应用通过隐含 Intent 调用 startActivity() 或 startActivityForResult() 时，系统会找到可以响应该 Intent 的 Activity。
 
1. 添加 Intent 过滤器
为了正确定义您的 Activity 可处理的 Intent，您添加的每个 Intent 过滤器在操作类型和 Activity 接受的数据方面应尽可能具体。

如果 Activity 具有满足以下 Intent 对象条件的 Intent 过滤器，系统可能向 Activity 发送给定的 Intent：
操作
对要执行的操作命名的字符串。通常是平台定义的值之一，比如 ACTION_SEND 或 ACTION_VIEW。
使用 <action> 元素在您的 Intent 过滤器中指定此值。您在此元素中指定的值必须是操作的完整字符串名称，而不是 API 常量（请参阅以下示例）。

数据
与 Intent 关联的数据描述。
用 <data> 元素在您的 Intent 过滤器中指定此内容。使用此元素中的一个或多个属性，您可以只指定 MIME 类型、URI 前缀、URI 
架构或这些的组合以及其他指示所接受数据类型的项。

注：如果您无需声明关于数据的具体信息 Uri（比如，您的 Activity 处理其他类型的“额外”数据而不是 URI 时），您应只指定 
android:mimeType 属性声明您的 Activity 处理的数据类型，比如 text/plain 或 image/jpeg。

类别
提供另外一种表征处理 Intent 的 Activity 的方法，通常与用户手势或 Activity 启动的位置有关。 系统支持多种不同的类别，
但大多数都很少使用。 但是，所有隐含 Intent 默认使用 CATEGORY_DEFAULT 进行定义。
用 <category> 元素在您的 Intent 过滤器中指定此内容。

在您的 Intent 过滤器中，您可以通过声明嵌套在 <intent-filter> 元素中的具有相应 XML 元素的各项，来声明您的 Activity 接受的条件。

例如，此处有一个 Activity 与在数据类型为文本或图像时处理 ACTION_SEND Intent 的 Intent 过滤器：

<activity android:name="ShareActivity">
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:mimeType="text/plain"/>
        <data android:mimeType="image/*"/>
    </intent-filter>
</activity>
每个入站 Intent 仅指定一项操作和一个数据类型，但可以在每个 <intent-filter> 中声明 <action>、<category> 和 
<data> 元素的多个实例。

如果任何两对操作和数据的行为相斥，您应创建单独的 Intent 过滤器指定与哪种数据类型配对时哪些操作可接受。

比如，假定您的 Activity 同时处理 ACTION_SEND 和 ACTION_SENDTO Intent 的文本和图像。在这种情况下，您必须为
两个操作定义两种不同的 Intent 过滤器，因为 ACTION_SENDTO Intent 必须使用数据 Uri 指定使用 send 或 sendto URI 架构的收件人地址。例如：

<activity android:name="ShareActivity">
    <!-- filter for sending text; accepts SENDTO action with sms URI schemes -->
    <intent-filter>
        <action android:name="android.intent.action.SENDTO"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:scheme="sms" />
        <data android:scheme="smsto" />
    </intent-filter>
    <!-- filter for sending text or images; accepts SEND action and text or image data -->
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:mimeType="image/*"/>
        <data android:mimeType="text/plain"/>
    </intent-filter>
</activity>
注：为了接收隐含 Intent，您必须在 Intent 过滤器中包含 CATEGORY_DEFAULT 类别。方法 startActivity() 和
startActivityForResult() 将按照已声明 CATEGORY_DEFAULT 类别的方式处理所有 Intent。如果您不在 Intent 
过滤器中声明它，则没有隐含 Intent 分解为您的 Activity。

2. 处理您的 Activity 中的 Intent
为了决定在您的 Activity 执行哪种操作，您可读取用于启动 Activity 的 Intent。

当您的 Activity 启动时，调用 getIntent() 检索启动 Activity 的 Intent。您可以在 Activity 生命周期的任何时间
执行此操作，但您通常应在早期回调时（比如，onCreate() 或 onStart()）执行。

例如：

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    // Get the intent that started this activity
    Intent intent = getIntent();
    Uri data = intent.getData();

    // Figure out what to do based on the intent type
    if (intent.getType().indexOf("image/") != -1) {
        // Handle intents with image data ...
    } else if (intent.getType().equals("text/plain")) {
        // Handle intents with text ...
    }
}


3. 返回结果

如果您想要向调用您的 Activity 的 Activity 返回结果，只需调用 setResult() 指定结果代码和结果 Intent。当您的
操作完成且用户应返回原始 Activity 时，调用 finish() 关闭（和销毁）您的 Activity。 例如：

// Create intent to deliver some kind of result data
Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
setResult(Activity.RESULT_OK, result);
finish();
您必须始终为结果指定结果代码。通常，它是 RESULT_OK 或 RESULT_CANCELED。您之后可以根据需要为 Intent 提供额外的数据。

注：默认情况下，结果设置为 RESULT_CANCELED。因此，如果用户在完成操作动作或设置结果之前按了返回按钮，原始 Activity 
会收到“已取消”的结果。

如果您只需返回指示若干结果选项之一的整数，您可以将结果代码设置为大于 0 的任何值。如果您使用结果代码传递整数，
且无需包括 Intent，则可调用 setResult() 且仅传递结果代码。例如：

setResult(RESULT_COLOR_RED);
finish();
在这种情况下，只有几个可能的结果，因此结果代码是一个本地定义的整数（大于 0）。 当您向自己应用中的 Activity 返回结果时，
这将非常有效，因为接收结果的 Activity 可引用公共常数来确定结果代码的值。

注：无需检查您的 Activity 是使用 startActivity() 还是 startActivityForResult() 启动的。如果启动您 Activity
 的 Intent 可能需要结果，只需调用 setResult()。如果原始 Activity 已调用 startActivityForResult()，
 则系统将向其传递您提供给 setResult() 的结果；否则，会忽略结果。

