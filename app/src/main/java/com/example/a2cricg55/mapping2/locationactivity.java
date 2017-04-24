package com.example.a2cricg55.mapping2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

public class locationactivity extends Activity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latlong);

        Button location = (Button) findViewById(R.id.btnLoc);
        location.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        EditText latt = (EditText)findViewById(R.id.lat);
        double latitude = Double.parseDouble(latt.getText().toString());
        EditText longgg = (EditText)findViewById(R.id.longg);
        double longitude = Double.parseDouble(longgg.getText().toString());

        bundle.putDouble("com.example.thelocationlat",latitude);
        bundle.putDouble("com.example.thelocationlong", longitude);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
