## 构建您的第一个App
1. 下载AndroidStudio 
2. 创建项目
3. 创建模拟器
4. 构建简单的用户界面
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:tools="http://schemas.android.com/tools"
       android:orientation="horizontal"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
        <EditText android:id="@+id/edit_message"
            android:layout_weight="1"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="@string/edit_message" />
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/button_send"
            android:onClick="sendMessage"/>
    </LinearLayout>
    ‘’‘
5. 运行程序
6. 构建一个Intent启动另一个Activity
    ```
    public class MainActivity extends AppCompatActivity {
        public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
    
        /** Called when the user clicks the Send button */
        public void sendMessage(View view) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            EditText editText = (EditText) findViewById(R.id.edit_message);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    ```
7. 创建第二个Activity接收并显示Intent中传递的消息
    ```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_display_message);
    
       Intent intent = getIntent();
       String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
       TextView textView = new TextView(this);
       textView.setTextSize(40);
       textView.setText(message);
    
       ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
       layout.addView(textView);
    }
    ```