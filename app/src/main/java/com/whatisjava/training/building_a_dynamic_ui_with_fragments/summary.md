## 使用Fragment构建动态的UI
1. 创建一个Fragment类
    ```java
    public class ArticleFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.article_view, container, false);
        }
    }
    ```
2. 通过xml布局文件添加Fragment到Activity
    ```xml
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="horizontal"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent">
    
        <fragment android:name="com.example.android.fragments.HeadlinesFragment"
                  android:id="@+id/headlines_fragment"
                  android:layout_weight="1"
                  android:layout_width="0dp"
                  android:layout_height="match_parent" />
    
        <fragment android:name="com.example.android.fragments.ArticleFragment"
                  android:id="@+id/article_fragment"
                  android:layout_weight="2"
                  android:layout_width="0dp"
                  android:layout_height="match_parent" />
    
    </LinearLayout>
    ```
3. 创建Activity并加载布局文件
    ```java
    import android.os.Bundle;
    import android.support.v4.app.FragmentActivity;
    
    public class MainActivity extends FragmentActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.news_articles);
        }
    }
    ```
    * 如果你使用了V7appcompat library，你的activity应该继承AppCompatActivity（AppCompatActivity为
    FragmentActivity的子类），当你通过activity的xml布局文件添加fragment的话，你无法在运行时remove掉
    Fragment，如果你想在与用户交互期间更改 Fragment，你必须在Activity运行时为Activity添加fragment。

4. 运行时为Activity添加Fragment
    * FragmentManager 类提供的方法让您可以在运行时为 Activity 添加、移除和替换片段，从而营造出动态的用户体验。
    * 如需执行添加或移除片段等事务，您必须使用 FragmentManager 创建 FragmentTransaction，后者将提供添加、移
    除、替换片段以及执行其他片段事务所需的 API。
    * 如果您的 Activity 允许移除和替换片段，应在 Activity 的 onCreate() 方法执行期间为 Activity 添加初始片
    段。
    * 在处理片段（尤其是在运行时添加片段的情况下）时，请谨记以下重要准则：您的 Activity 布局必须包含一个可以插入
    片段的容器 View，例如FrameLayout