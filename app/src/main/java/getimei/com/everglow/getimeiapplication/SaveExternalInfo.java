package getimei.com.everglow.getimeiapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class SaveExternalInfo extends AppCompatActivity {
    final private int REQUEST_READ_PHONE_STATE  = 111;
    private EditText mEtInput;
    private TextView mTvWrite;
    private TextView mTvRead;
    private TextView mTvShow;
    private File mFile;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_external_info);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mTvWrite = (TextView) findViewById(R.id.tv_write);
        mTvRead = (TextView) findViewById(R.id.tv_read);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        requestPermission();
        mTvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                setData();

            }
        });
        mTvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               type = 1;
                setData();
            }
        });
    }

    private void mCreatFile()  {

            //获取手机本身存储根目录Environment.getExternalStoragePublicDirectory("")
            //sd卡根目录Environment.getExternalStorageDirectory()
        mFile = new File(Environment.getExternalStoragePublicDirectory("") + "/save_config.text");
            if (!mFile.exists()) {
                try {
                    mFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SaveExternalInfo.this,"文件创建失败",Toast.LENGTH_SHORT).show();

                }
            }

    }

    //读文件
    public String readFile() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(mFile));
        String readline = "";
        StringBuffer sb = new StringBuffer();
        while ((readline = br.readLine()) != null) {
            sb.append(readline);
        }
        br.close();
        return sb.toString();

    }

    //写文件
    public void writeSDFile(  String info)throws IOException {
        FileOutputStream fos = new FileOutputStream(mFile);
        fos.write(info.getBytes());
        fos.close();
    }

    public void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            mCreatFile();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mCreatFile();
                }else{
                    finish();
                }
                break;

            default:
                break;
        }
    }

    public void setData() {
        if(type == 1){
            String input = mEtInput.getText().toString().trim();
            if (TextUtils.isEmpty(input))
                return;
            try {
                writeSDFile( input);
                Toast.makeText(SaveExternalInfo.this,"存入成功",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SaveExternalInfo.this,"存入失败",Toast.LENGTH_SHORT).show();
            }

        }else{
            try {
                String text = readFile();
                mTvShow.setText(text);
            } catch (IOException e) {
                e.printStackTrace();
                mTvShow.setText("没有找到文件");
            }
        }
    }
}
