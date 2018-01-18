package com.example.huangjl.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taisys.oti.Card;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Card mCard = new Card();
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mCard)
                    mCard = new Card();
                String res[] = mCard.GetCardInfo();
                if (res != null && res[0].equals(Card.RES_OK)) {
                    button.setText("CardInfor: " + res[1]);
                    Log.d("MainActivity","GetCardInfo:" + res[1]);
                } else {
                    button.setText("GetCardInfo failed！"+res[0]);
                    Log.d("MainActivity","GetCardInfo failed！"+res[0]);
                }
            }
        });
        // 申请通用权限
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = requestPermissions();
            if (permissions != null) {
                requestPermissions(permissions, 1003);
                return;
            }
        }
        mCard.setPrintLog(true);
        mCard.OpenSEService(MainActivity.this, "A000000018506373697A672D63617264", new Card.SCSupported() {
            @Override
            public void isSupported(boolean b) {
                if (b){

                    Log.d("MainActivity","该手机支持OpenMobileAPI！");
                    Toast.makeText(MainActivity.this,"该手机支持OpenMobileAPI！",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("MainActivity","该手机不支持OpenMobileAPI！");
                    Toast.makeText(MainActivity.this,"该手机不支持OpenMobileAPI！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mCard.CloseSEService();
    }

    @Override
    public void onBackPressed() {
        mCard.CloseSEService();
        finish();
//        System.exit(0);
    }
    /**
     * 请求需要的权限
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String[] requestPermissions() {
        String[] permissions = null;
        List<String> permissionsList = new ArrayList<>();
        addPermission(permissionsList, Manifest.permission.READ_SMS);
        addPermission(permissionsList, Manifest.permission.SEND_SMS);
        addPermission(permissionsList, Manifest.permission.RECEIVE_SMS);
        addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS);
        addPermission(permissionsList, Manifest.permission.READ_CONTACTS);
        addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE);
        if (permissionsList.size() > 0) {
            permissions = new String[permissionsList.size()];
            for (int i = 0; i < permissionsList.size(); i++) {
                permissions[i] = permissionsList.get(i);
            }
        }
        return permissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1003) {
            for (int i = 0; i < grantResults.length; i++) {
                int grant = grantResults[i];
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "权限未被允许，请手动开启", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
