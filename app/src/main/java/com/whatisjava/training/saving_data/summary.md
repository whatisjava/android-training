# 保存数据
1. 在共享首选项（SharedPreferences）中保存键值
* 如果您有想要保存的相对较小键值集合，您应使用 SharedPreferences API。SharedPreferences 对象指向包含键值对的文件并提供读写这些文件的简单方法。 每个 SharedPreferences 文件由框架进行管理并且可以专用或共享。

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



2. 在文件系统中保存文件

3. 在sqlite数据库中保存数据