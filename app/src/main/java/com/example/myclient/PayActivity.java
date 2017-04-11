package com.example.myclient;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayActivity extends Activity {

    // 显示点餐信息WebView
    private WebView wv;
    // 查询点餐信息按钮和结算按钮
    private Button queryBtn,payBtn;
    // 订单编号
    private EditText orderIdEt;
    // 查询点餐信息监听器
    View.OnClickListener queryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 获得订单编号
            String orderId = orderIdEt.getText().toString();
            // 请求服务器url
            String url = HttpUtil.BASE_URL+"servlet/PayServlet?id="+orderId;
            // 将返回信息在WebView中显示
            try {
                wv.loadUrl(url);
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.i("11111111",e.getMessage());
            }
        }
    };
    // 结算监听器
    View.OnClickListener payListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 获得订单编号
            String orderId = orderIdEt.getText().toString();

            new PayThread(orderId).start();
            // 请求服务器url
          /*  String url = HttpUtil.BASE_URL+"servlet/PayMoneyServlet?id="+orderId;
            // 获得查询结果

            try {
                String result = HttpUtil.queryStringForGet(url);
                // 显示结算结果
               // Toast.makeText(PayActivity.this, result, Toast.LENGTH_LONG).show();
                Log.i("3333333333","已结算");
                // 使结算按钮失效
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.i("22222 paylistener fail",e.getMessage());
            }

            */
            payBtn.setEnabled(false);
        }
    };
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity的界面布局
        setContentView(R.layout.pay);
        // 获得WebView实例
        wv = (WebView)findViewById(R.id.pay_webview);
        // 实例化查询按钮
        queryBtn = (Button)findViewById(R.id.pay_query_Button01);
        // 实例化结算按钮
        payBtn = (Button)findViewById(R.id.pay_Button01);
        // 实例化订单编号编辑框
        orderIdEt = (EditText)findViewById(R.id.pay_order_number_EditText01);

        // 添加查询点餐信息监听器
        queryBtn.setOnClickListener(queryListener);
        // 添加结算信息监听器
        payBtn.setOnClickListener(payListener);


        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==111)
                {
                    if(msg.obj.toString().equals("SUCCESS"))
                        Toast.makeText(PayActivity.this,"已结算",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(PayActivity.this,"结算出错",Toast.LENGTH_LONG).show();
                }

            }
        };
    }

    public class PayThread extends Thread{

        public String orderId;
        String url = HttpUtil.BASE_URL+"servlet/PayMoneyServlet?id="+orderId;
        String result=null;
        public PayThread(String orderId)
        {
            this.orderId=orderId;
        }

        @Override
        public void run() {

            try {
                String result = HttpUtil.queryStringForGet(url);
                // 显示结算结果
               // Toast.makeText(PayActivity.this, result, Toast.LENGTH_LONG).show();
                Log.i("3333333333","已结算");
                // 使结算按钮失效
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.i("22222 paylistener fail",e.getMessage());
            }

            Message msg=handler.obtainMessage();

           msg.what=111;
            if(result.equals("已结算")) {
                msg.obj = "SUCCESS";
           }else  {
                msg.obj = "FAILED";
            }
            handler.sendMessage(msg);


        }
    }

}
