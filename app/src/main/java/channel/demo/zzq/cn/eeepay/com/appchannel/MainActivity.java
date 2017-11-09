package channel.demo.zzq.cn.eeepay.com.appchannel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 /**
  * 描述：渠道打包就是分不同的市场打包，比如Google商店、91、360、小米、华为等
  * 为每个渠道定制不同的资源和代码
  * 作者：zhuangzeqin
  * 时间: 2017/11/9-13:43
  * 邮箱：zzq@eeepay.cn
  */
public class MainActivity extends AppCompatActivity {
    private android.widget.TextView textView;
    private android.widget.Button btnlogin;
    private android.widget.TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView2 = (TextView) findViewById(R.id.textView2);
        this.btnlogin = (Button) findViewById(R.id.btn_login);
        this.textView = (TextView) findViewById(R.id.textView);
        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
//
    }



}
