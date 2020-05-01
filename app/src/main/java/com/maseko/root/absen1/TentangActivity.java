package com.maseko.root.absen1;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TentangActivity extends AppCompatActivity {

    @BindView(R.id.versi)
    TextView versi;

    String version = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        ButterKnife.bind(this);


        try {
            PackageInfo pInfo = TentangActivity.this.getPackageManager().getPackageInfo(TentangActivity.this.getPackageName(), 0);
            version = pInfo.versionName;
            Log.d("tes", "onLocationChangedLONG: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versi.setText("V-"+version);
    }
}
