package com.example.API02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextID, editTextName, editTextEmail;
    Button addButton, reviseButton, delButton;
    ListView listView;
    final String host_ip = "http://192.168.0.100:8000";

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        addButton = (Button) findViewById(R.id.addButton);
        reviseButton = (Button) findViewById(R.id.reviseButton);
        delButton = (Button) findViewById(R.id.delButton);
        listView = (ListView) findViewById(R.id.listView);

        //show data in listview
        show_data();

        //add button
        addButton.setOnClickListener(new addButtonOnClickListener());

        //revise Button
        reviseButton.setOnClickListener(new reviseButtonOnClickListener());

        //delete Button
        delButton.setOnClickListener(new delButtonOnClickListener());

        //item button
        listView.setOnItemClickListener(new listViewOnItemClickListener());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("show_msg","refresh");
                show_data();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                Log.d("show_msg", String.valueOf(i));
                if (i==0){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    //偵測網路連線
    private void detect_network_connection() {
        if (NetworkUtils.isNetworkAvailable(this)){
            Log.d("show_msg","Network ok");
        }else{
            Log.d("show_msg","Network fail");
            Toast.makeText(MainActivity.this, "網路未開啟", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(null);
                }
            });
        }
    }

    //show data in listview
    private void show_data() {
        //偵測網路連線
        detect_network_connection();
//        Log.d("show_msg","show_data");
        OkHttpClient client = new OkHttpClient();//client物件，目的是處理網路請求
        //指定請求的url
        Request request = new Request.Builder()
//                .url("http://192.168.58.40:8080/getAllItems/")
                .url(host_ip + "/getAllItems/")
                .build();
        Call call = client.newCall(request);
        //發送異同步請求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("show_msg", "onFailure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Log.d("show_msg","onResponse");
                String response_body = response.body().string();
//                Log.d("show_msg",response_body);
                showInListView(response_body);
            }
        });
    }

    private void showInListView(String response_body) {
        Log.d("show_msg", "showInListView");
        Gson gson = new Gson();//json資料轉成java物件

        //用於指定轉換的目標類型
        TypeToken<ArrayList<TextString>> typeToken = new TypeToken<ArrayList<TextString>>() {
        };

        //將response_body轉換為ArrayList<TextString>型別的物件
        ArrayList<TextString> list = gson.fromJson(response_body, typeToken.getType());

        //使用匿名
//        ArrayList<TextString> list2 = gson.fromJson(response_body,
//                new TypeToken<ArrayList<TextString>>(){}.getType());

//        Log.d("show_msg", String.valueOf(list.size()));
//        Log.d("show_msg", list.get(0).getId());
//        Log.d("show_msg", list.get(0).getcName());
//        Log.d("show_msg", list.get(0).getcEmail());
        //走訪
//        for (TextString data : list) {
//            Log.d("show_msg", data.getId()+" "+data.getcName()+" "+data.getcEmail());
//        }

        //資料倒入listview
        List<Map<String, Object>> items = new ArrayList<>();
        for (TextString data : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("id",data.getId());
            item.put("name",data.getcName());
            item.put("email",data.getcEmail());
            items.add(item);
        }
        System.out.println(items);
        SimpleAdapter sa = new SimpleAdapter(this,items,R.layout.textstring,
                new String[]{"id","name","email"},
                new int[]{R.id.item_textView_id,R.id.item_textView_name,R.id.item_textView_cEmail});

//        listView.setAdapter(sa);
        //執行緒
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(sa);
            }
        });


    }

    //add button event
    private class addButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //偵測網路連線
            detect_network_connection();
//            Log.d("show_msg", "addButtonOnClick");
//            Log.d("show_msg", editTextID.getText().toString());
//            Log.d("show_msg", editTextName.getText().toString());
//            Log.d("show_msg", editTextEmail.getText().toString());
            String id = editTextID.getText().toString();
            String name = editTextName.getText().toString();
            String email= editTextEmail.getText().toString();

            OkHttpClient client = new OkHttpClient();//client物件，目的是處理網路請求
            ///////////////////////////////////////////////////////////////////////////////////////
            //post
//            RequestBody formBody = new FormBody.Builder()
//                    .add("cName",name)
//                    .add("cSex","M")
//                    .add("cBirthday","2023-10-26")
//                    .add("cEmail",email)
//                    .add("cPhone","092222222")
//                    .add("cAddr","台灣")
//                    .build();
//            Request request = new Request.Builder()
////                .url("http://192.168.58.40:8080/getAllItems/")
//                    .url(host_ip + "/createItem/")
//                    .post(formBody)
//                    .build();
            ///////////////////////////////////////////////////////////////////////////////////////
            //get
            String param = "cName="+name+
                    "&cSex="+"M"+
                    "&cBirthday="+"2023-10-26"+
                    "&cEmail="+email+
                    "&cPhone="+"09222222"+
                    "&cAddr="+"台灣";
            Log.d("show_msg", param);
            Request request = new Request.Builder()
                    .url(host_ip + "/createItem/?"+param)
                    .build();
            ///////////////////////////////////////////////////////////////////////////////////////
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("show_msg", "onFailure");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "連線失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String response_body = response.body().string();
                    Log.d("show_msg", response_body);
                    if (response_body.equals("true")){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "新增失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    show_data();

                }
            });


        }
    }

    private class reviseButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //偵測網路連線
            detect_network_connection();
            Log.d("show_msg", "ButtonOnClickListener");
            String id = editTextID.getText().toString();
            String name = editTextName.getText().toString();
            String email= editTextEmail.getText().toString();
            OkHttpClient client = new OkHttpClient();//client物件，目的是處理網路請求

            //post
            RequestBody formBody = new FormBody.Builder()
                    .add("cName",name)
                    .add("cSex","M")
                    .add("cBirthday","2023-10-26")
                    .add("cEmail",email)
                    .add("cPhone","092222222")
                    .add("cAddr","台灣")
                    .build();
            Request request = new Request.Builder()
//                .url("http://192.168.58.40:8080/getAllItems/")
                    .url(host_ip + "/updateItem/"+id+"/")
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("show_msg", "onFailure");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "連線失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String response_body = response.body().string();
                    Log.d("show_msg", response_body);
                    if (response_body.equals("true")){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "修改失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    show_data();
                }
            });

        }
    }

    //item event
    private class listViewOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Log.d("show_msg", "listViewOnItemClickListener");
            TextView item_textView_id = (TextView) view.findViewById(R.id.item_textView_id);
            TextView item_textView_name = (TextView) view.findViewById(R.id.item_textView_name);
            TextView item_textView_cEmail = (TextView) view.findViewById(R.id.item_textView_cEmail);
//            Log.d("show_msg", item_textView_id.getText().toString()
//                    +" "+item_textView_name.getText().toString()
//                    +" "+item_textView_cEmail.getText().toString()
//            );
            editTextID.setText(item_textView_id.getText().toString());;
            editTextName.setText(item_textView_name.getText().toString());
            editTextEmail.setText(item_textView_cEmail.getText().toString());

        }
    }

    //delete event
    private class delButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //偵測網路連線
            detect_network_connection();
            Log.d("show_msg", "delButtonOnClickListener");
            String id = editTextID.getText().toString();
            OkHttpClient client = new OkHttpClient();//client物件，目的是處理網路請求
            Request request = new Request.Builder()
//                .url("http://192.168.58.40:8080/getAllItems/")
                    .url(host_ip + "/deleteItem/"+id+"/")
                    .build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("show_msg", "onFailure");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "連線失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String response_body = response.body().string();
                    Log.d("show_msg", response_body);
                    if (response_body.equals("true")){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "刪除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "刪除失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    show_data();
                }
            });


        }
    }
}









