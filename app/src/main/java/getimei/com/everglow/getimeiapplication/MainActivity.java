package getimei.com.everglow.getimeiapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_READ_PHONE_STATE  = 111;
    private TextView mTvShow;
    private TextView mTvLineiNumber;
    private TextView mTvSimoperator;
    private TextView mTvSaveInfo;
    private TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvGet = (TextView) findViewById(R.id.tv_get_iemi);
        mTvShow = (TextView) findViewById(R.id.tv_show_iemi);
        mTvLineiNumber = (TextView) findViewById(R.id.tv_linei_number);
        mTvSimoperator = (TextView) findViewById(R.id.tv_show_simoperator_name);
        mTvSaveInfo = (TextView) findViewById(R.id.tv_save_info);

        tvGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();

            }
        });
        mTvSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SaveExternalInfo.class));
            }
        });
    }

    public void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            setText();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setText();
                }
                break;

            default:
                break;
        }
    }
    private void setText() {
        telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        mTvShow.setText("IEMI:"+telephonyManager.getDeviceId());
        mTvLineiNumber.setText("电话号码："+telephonyManager.getLine1Number());
        if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY)
            mTvSimoperator.setText("运营商："+telephonyManager.getSimOperatorName());
    }
}
