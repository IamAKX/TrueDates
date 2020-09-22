package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neosao.truedates.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class Otp extends AppCompatActivity {

    MaterialEditText edtPhoneNumber;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initializeComponents();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtPhoneNumber.getText().toString().replace(" ","").length()!=10)
                    edtPhoneNumber.setError("Enter valid mobile number");
                else
                    startActivity(new Intent(getBaseContext(), PhoneValidation.class).putExtra("phone",edtPhoneNumber.getText().toString()));
            }
        });
    }

    private void initializeComponents() {
        edtPhoneNumber = findViewById(R.id.phone_number_edt);
        continueBtn = findViewById(R.id.continueBtn);
    }
}