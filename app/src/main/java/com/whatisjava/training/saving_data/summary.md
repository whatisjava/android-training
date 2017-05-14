# 保存数据
## 在共享首选项（SharedPreferences）中保存键值
* 如果您有想要保存的相对较小键值集合，您应使用 SharedPreferences API。SharedPreferences 对象指向包含键值对的
文件并提供读写这些文件的简单方法。 每个 SharedPreferences 文件由框架进行管理并且可以专用或共享。

本课向您展示如何使用 SharedPreferences API 存储和检索简单的值。

注：SharedPreferences API 仅用于读写键值对，您不得将其与 Preference API 混淆，后者帮助您为您的应用设置构建
用户界面（尽管它们使用 SharedPreferences 作为其实现以保存应用设置）。 有关使用 Preference API 的信息，请参阅
设置指南。

获取共享首选项的句柄
您可以通过调用以下两种方法之一创建新的共享首选项文件或访问现有的文件：

getSharedPreferences() — 如果您需要按照您用第一个参数指定的名称识别的多个共享首选项文件，请使用此方法。 您
可以从您的应用中的任何 Context 调用此方法。
getPreferences() — 如果您只需使用 Activity 的一个共享首选项，请从 Activity 中使用此方法。 因为此方法会检索
属于该 Activity 的默认共享首选项文件，您无需提供名称。
例如，以下代码在 Fragment 内执行。它访问通过资源字符串 R.string.preference_file_key 识别的共享首选项文件并且
使用专用模式打开它，从而仅允许您的应用访问文件。

Context context = getActivity();
SharedPreferences sharedPref = context.getSharedPreferences(
        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
命名您的共享首选项文件时，您应使用对于您的应用而言唯一可识别的名称，比如 "com.example.myapp.PREFERENCE_FILE_KEY"

或者，如果您只需 Activity 的一个共享首选项文件，您可以使用 getPreferences() 方法：

SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
注意：如果您创建带 MODE_WORLD_READABLE 或 MODE_WORLD_WRITEABLE 的共享首选项文件，那么知道文件标识符的任何
其他应用都可以访问您的数据。

写入共享首选项
要写入共享首选项文件，请通过对您的 SharedPreferences 调用 edit() 来创建一个 SharedPreferences.Editor。

传递您想要使用诸如 putInt() 和 putString() 方法写入的键和值。然后，调用 commit() 以保存所做的更改。例如：

SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPref.edit();
editor.putInt(getString(R.string.saved_high_score), newHighScore);
editor.commit();
从共享首选项读取信息
要从共享首选项文件中检索值，请调用诸如 getInt() 和 getString() 等方法，为您想要的值提供键，并根据需要提供要在
键不存在的情况下返回的默认值。例如：

SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);


## 在文件系统中保存文件
1. 选择内部或外部存储
* File 对象适合按照从开始到结束的顺序不跳过地读取或写入大量数据。 例如，它适合于图片文件或通过网络交换的任何
  内容。
* 所有 Android 设备都有两个文件存储区域：“内部”和“外部”存储。这些名称在 Android 早期产生，当时大多数设备都
  提供内置的非易失性内存（内部存储），以及移动存储介质，比如微型 SD 卡（外部存储）。一些设备将永久性存储空间划
        分为“内部”和“外部”分区，即便没有移动存储介质，也始终有两个存储空间，并且无论外部存储设备是否可移动，API 的行
        为均一致。
            + 内部存储始终可用，只有您的应用可以访问此处保存的文件，当用户卸载您的应用时，系统会从内部存储中移除您的
            应用的所有文件。当您希望确保用户或其他应用均无法访问您的文件时，内部存储是最佳选择。
            + 外部存储可能会被移除，文件可以被全局访问它并非始终可用，因为用户可采用 USB 存储设备的形式装载外部存储，
            并在某些情况下会从设备中将其移除。它是全局可读的，因此此处保存的文件可能不受您控制地被读取。当用户卸载您
            的应用时，只有在您通过 getExternalFilesDir() 将您的应用的文件保存在目录中时，系统才会从此处移除您的应
            用的文件。对于无需访问限制以及您希望与其他应用共享或允许用户使用计算机访问的文件，外部存储是最佳位置。
* 尽管应用默认安装在内部存储中，但您可在您的清单文件中指定 android:installLocation 属性，这样您的应用便可
        安装在在外部存储中。当 APK 非常大且它们的外部存储空间大于内部存储时，用户更青睐这个选择。
    
2. 获取外部存储的权限
    * 要向外部存储写入信息，您必须在您的清单文件中请求 WRITE_EXTERNAL_STORAGE 权限。
        ```xml
        <manifest ...>
            <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            ...
        </manifest>
        ```
        注意：目前，所有应用都可以读取外部存储，而无需特别的权限。 但这在将来版本中会进行更改。如果您的应用需要读
        取外部存储（但不向其写入信息），那么您将需要声明 READ_EXTERNAL_STORAGE 权限。要确保您的应用继续正常工
        作，您应在更改生效前声明此权限。
        ```xml
        <manifest ...>
            <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
            ...
        </manifest>
        ```
        但是，如果您的应用使用 WRITE_EXTERNAL_STORAGE 权限，那么它也隐含读取外部存储的权限。
        您无需任何权限，即可在内部存储中保存文件。 您的应用始终具有在其内部存储目录中进行读写的权限。
3. 将文件保存在内部存储中
    * 在内部存储中保存文件时，您可以通过调用以下两种方法之一获取作为 File 的相应目录：
        getFilesDir()
        返回表示您的应用的内部目录的 File 。
        getCacheDir()
        返回表示您的应用临时缓存文件的内部目录的 File。 务必删除所有不再需要的文件并对在指定时间您使用的内存量实
        现合理大小限制，比如，1MB。 如果在系统即将耗尽存储，它会在不进行警告的情况下删除您的缓存文件。
        要在这些目录之一中新建文件，您可以使用 File() 构造函数，传递指定您的内部存储目录的上述方法之一所提供的
        File。例如：
        
        File file = new File(context.getFilesDir(), filename);
        或者，您可以调用 openFileOutput() 获取写入到内部目录中的文件的 FileOutputStream。例如，下面显示如何
        向文件写入一些文本：
        
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;
        
        try {
          outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
          outputStream.write(string.getBytes());
          outputStream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        或者，如果您需要缓存某些文件，您应改用 createTempFile()。例如，以下方法从 URL 提取文件名并正在您的应用
        的内部缓存目录中以该名称创建文件：
        
        public File getTempFile(Context context, String url) {
            File file;
            try {
                String fileName = Uri.parse(url).getLastPathSegment();
                file = File.createTempFile(fileName, null, context.getCacheDir());
            } catch (IOException e) {
                // Error while creating file
            }
            return file;
        }
        注：您的应用的内部存储设备目录由您的应用在 Android 文件系统特定位置中的软件包名称指定。从技术上讲，如果
        您将文件模式设置为可读，那么，另一应用也可以读取您的内部文件。 但是，此应用也需要知道您的应用的软件包名称
        和文件名。 其他应用无法浏览您的内部目录并且没有读写权限，除非您明确将文件设置为可读或可写。 只要您为内部
        存储上的文件使用 MODE_PRIVATE，其他应用便从不会访问它们。
4. 将文件保存在外部存储中
    * 由于外部存储可能不可用—比如，当用户已将存储装载到电脑或已移除提供外部存储的 SD 卡时—因此，在访问它之前，您应始终确认其容量。 您可以通过调用 getExternalStorageState() 查询外部存储状态。 如果返回的状态为 MEDIA_MOUNTED，那么您可以对您的文件进行读写。 例如，以下方法对于确定存储可用性非常有用：
        ```java
            /* Checks if external storage is available for read and write */
            public boolean isExternalStorageWritable() {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    return true;
                }
                return false;
            }
            
            /* Checks if external storage is available to at least read */
            public boolean isExternalStorageReadable() {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    return true;
                }
                return false;
            }
        ```
    * 尽管外部存储可被用户和其他应用进行修改，但您可在此处保存两类文件：
        1. 公共文件
        应供其他应用和用户自由使用的文件。 当用户卸载您的应用时，用户应仍可以使用这些文件。
        例如，您的应用拍摄的照片或其他已下载的文件。
        
        2. 私有文件
        属于您的应用且在用户卸载您的应用时应予删除的文件。 尽管这些文件在技术上可被用户和其他应用访问（因为它们存储在外部存储中）， 但它们实际上不向您的应用之外的用户提供任何输出值。 当用户卸载您的应用时，系统会删除应用外部私有目录中的所有文件。
        例如，您的应用下载的其他资源或临时介质文件。
        
        如果您要将公共文件保存在外部存储设备上，请使用 getExternalStoragePublicDirectory() 方法获取表示外部存储设备上相应目录的 File。 该方法使用指定您想要保存以便它们可以与其他公共文件在逻辑上组织在一起的文件类型的参数，比如 DIRECTORY_MUSIC 或 DIRECTORY_PICTURES。例如：
        ```java
        public File getAlbumStorageDir(String albumName) {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
            if (!file.mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
            }
            return file;
        }
        ```
        如果您要保存您的应用专用文件，您可以通过调用 getExternalFilesDir() 并向其传递指示您想要的目录类型的名
        称，从而获取相应的目录。通过这种方法创建的各个目录将添加至封装您的应用的所有外部存储文件的父目录，当用户
        卸载您的应用时，系统会删除这些文件。
        
        例如，您可以使用以下方法来创建个人相册的目录：
        ```java
        public File getAlbumStorageDir(Context context, String albumName) {
            // Get the directory for the app's private pictures directory.
            File file = new File(context.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), albumName);
            if (!file.mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
            }
            return file;
        }
        ```
        如果没有适合您文件的预定义子目录名称，您可以改为调用 getExternalFilesDir() 并传递 null。这将返回外部
        存储上您的应用的专用目录的根目录。
        
        切记，getExternalFilesDir() 在用户卸载您的应用时删除的目录内创建目录。如果您正保存的文件应在用户卸载
        您的应用后仍然可用—比如，当您的应用是照相机并且用户要保留照片时—您应改用getExternalStoragePublicDirectory()。
        
        无论您对于共享的文件使用 getExternalStoragePublicDirectory() 还是对您的应用专用文件使用 
        getExternalFilesDir()，您使用诸如 DIRECTORY_PICTURES 的 API 常数提供的目录名称非常重要。这些目录
        名称可确保系统正确处理文件。 例如，保存在 DIRECTORY_RINGTONES 中的文件由系统媒体扫描程序归类为铃声，
        而不是音乐。
5. 查询可用空间
    * 如果您事先知道您将保存的数据量，您可以查出是否有足够的可用空间，而无需调用 getFreeSpace() 或 getTotalSpace()
    引起 IOException。这些方法分别提供目前的可用空间和存储卷中的总空间。 此信息也可用来避免填充存储卷以致超出特定阈值。
    但是，系统并不保证您可以写入与 getFreeSpace() 指示的一样多的字节。如果返回的数字比您要保存的数据大小大出几 MB，
    或如果文件系统所占空间不到 90%，则可安全继续操作。否则，您可能不应写入存储。
    注：保存您的文件之前，您无需检查可用空间量。 您可以尝试立刻写入文件，然后在 IOException 出现时将其捕获。 
    如果您不知道所需的确切空间量，您可能需要这样做。 例如，如果在保存文件之前通过将 PNG 图像转换成 JPEG 更改了文件
    的编码，您事先将不知道文件的大小。
6. 删除文件
    * 您应始终删除不再需要的文件。删除文件最直接的方法是让打开的文件参考自行调用 delete()。
    ```java
    myFile.delete();
    ```
    * 如果文件保存在内部存储中，您还可以请求 Context 通过调用 deleteFile() 来定位和删除文件：
    ```java
    myContext.deleteFile(fileName);
    ```
    * 注：当用户卸载您的应用时，Android 系统会删除以下各项：
        您保存在内部存储中的所有文件
        您使用 getExternalFilesDir() 保存在外部存储中的所有文件。
        但是，您应手动删除使用 getCacheDir() 定期创建的所有缓存文件并且定期删除不再需要的其他文件。
        
## 在 SQL 数据库中保存数据
1. 将数据保存到数据库对于重复或结构化数据（比如契约信息）而言是理想之选。 本课程假定您基本熟悉 SQL 数据库并且可
帮助您开始在 Android 中使用 SQLite 数据库。 您在 Android 中使用数据库所需的 API 在 android.database.sqlite 软件包中提供。

2. 定义架构和契约
SQL 数据库的主要原则之一是架构：数据库如何组织的正式声明。 架构体现于您用于创建数据库的 SQL 语句。您会发现它有助于
创建伴随类，即契约类，其以一种系统性、自记录的方式明确指定您的架构布局。

契约类是用于定义 URI、表格和列名称的常数的容器。 契约类允许您跨同一软件包中的所有其他类使用相同的常数。 您可以在
一个位置更改列名称并使其在您整个代码中传播。

组织契约类的一种良好方法是将对于您的整个数据库而言是全局性的定义放入类的根级别。 然后为枚举其列的每个表格创建内部类。

注：通过实现 BaseColumns 接口，您的内部类可继承名为 _ID 的主键字段，某些 Android 类（比如光标适配器）将需要内部类
拥有该字段。 这并非必需项，但可帮助您的数据库与 Android 框架协调工作。

例如，该代码段定义了单个表格的表格名称和列名称：

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
使用 SQL 辅助工具创建数据库
在您定义了数据库的外观后，您应实现创建和维护数据库和表格的方法。 这里有一些典型的表格创建和删除语句：

private static final String TEXT_TYPE = " TEXT";
private static final String COMMA_SEP = ",";
private static final String SQL_CREATE_ENTRIES =
    "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
    FeedEntry._ID + " INTEGER PRIMARY KEY," +
    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    FeedEntry.COLUMN_NAME_SUBTITLE + TEXT_TYPE + " )";

private static final String SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
就像您在设备的内部存储中保存文件那样，Android 将您的数据库保存在私人磁盘空间，即关联的应用。 您的数据是安全的，
因为在默认情况下，其他应用无法访问此区域。

SQLiteOpenHelper 类中有一组有用的 API。当您使用此类获取对您数据库的引用时，系统将只在需要之时而不是应用启动过程中
执行可能长期运行的操作：创建和更新数据库。 您仅需调用 getWritableDatabase() 或 getReadableDatabase() 即可。

注：由于它们可能长期运行，因此请确保您在后台线程中调用 getWritableDatabase() 或 getReadableDatabase()，比如
使用 AsyncTask 或 IntentService。

要使用 SQLiteOpenHelper，请创建一个替换 onCreate()、onUpgrade() 和onOpen() 回调方法的子类。您可能还希望实现
 onDowngrade()，但这并非必需操作。

例如，下面是一个使用如上所示一些命令的 SQLiteOpenHelper 的实现：

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
要访问您的数据库，请实例化 SQLiteOpenHelper 的子类：

FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());
将信息输入到数据库
通过将一个 ContentValues 对象传递至 insert() 方法将数据插入数据库：

// Gets the data repository in write mode
SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
ContentValues values = new ContentValues();
values.put(FeedEntry.COLUMN_NAME_TITLE, title);
values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

// Insert the new row, returning the primary key value of the new row
long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
insert() 的第一个参数即为表格名称。

第二个参数将指示框架在 ContentValues 为空（即，您没有 put 任何值）时执行哪些操作。如果指定列名称，则框架将插入一行
并将该列的值设置为 null。如果指定 null（就像此代码示例中一样），则框架不会在没有值时插入行。

从数据库读取信息
要从数据库中读取信息，请使用 query() 方法，将其传递至选择条件和所需列。该方法结合 insert() 和 update() 的元素，
除非列列表定义了您希望获取的数据，而不是希望插入的数据。 查询的结果将在 Cursor 对象中返回给您。

SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
String[] projection = {
    FeedEntry._ID,
    FeedEntry.COLUMN_NAME_TITLE,
    FeedEntry.COLUMN_NAME_SUBTITLE
    };

// Filter results WHERE "title" = 'My Title'
String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
String sortOrder =
    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

Cursor c = db.query(
    FeedEntry.TABLE_NAME,                     // The table to query
    projection,                               // The columns to return
    selection,                                // The columns for the WHERE clause
    selectionArgs,                            // The values for the WHERE clause
    null,                                     // don't group the rows
    null,                                     // don't filter by row groups
    sortOrder                                 // The sort order
    );
要查看游标中的某一行，请使用 Cursor 移动方法之一，您必须在开始读取值之前始终调用这些方法。 一般情况下，您应通过
调用 moveToFirst() 开始，其将“读取位置”置于结果中的第一个条目中。 对于每一行，您可以通过调用 Cursor 获取方法
之一读取列的值，比如 getString() 或 getLong()。对于每种获取方法，您必须传递所需列的索引位置，您可以通过调用 
getColumnIndex() 或 getColumnIndexOrThrow() 获取。例如：

cursor.moveToFirst();
long itemId = cursor.getLong(
    cursor.getColumnIndexOrThrow(FeedEntry._ID)
);
从数据库删除信息
要从表格中删除行，您需要提供识别行的选择条件。 数据库 API 提供了一种机制，用于创建防止 SQL 注入的选择条件。 
该机制将选择规范划分为选择子句和选择参数。 该子句定义要查看的列，还允许您合并列测试。 参数是根据捆绑到子句的项进行
测试的值。由于结果并未按照与常规 SQL 语句相同的方式进行处理，它不受 SQL 注入的影响。

// Define 'where' part of query.
String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
String[] selectionArgs = { "MyTitle" };
// Issue SQL statement.
db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
更新数据库
当您需要修改数据库值的子集时，请使用 update() 方法。

更新表可将 insert() 的内容值语法与 delete() 的 where 语法相结合。

SQLiteDatabase db = mDbHelper.getReadableDatabase();

// New value for one column
ContentValues values = new ContentValues();
values.put(FeedEntry.COLUMN_NAME_TITLE, title);

// Which row to update, based on the title
String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
String[] selectionArgs = { "MyTitle" };

int count = db.update(
    FeedReaderDbHelper.FeedEntry.TABLE_NAME,
    values,
    selection,
    selectionArgs);