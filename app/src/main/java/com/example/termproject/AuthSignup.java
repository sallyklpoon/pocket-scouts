package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class AuthSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup);
        TextInputEditText birthday_value = findViewById(R.id.birthday_value);
        birthday_value.setInputType(InputType.TYPE_NULL);
        birthday_value.setKeyListener(null);
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        materialDateBuilder.setTitleText("Select Birthday!");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(selection)).toString();
            birthday_value.setText(dateString);
        });
        birthday_value.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "Material"));
        birthday_value.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                birthday_value.callOnClick();
            }
        });
        String[] genders = getResources().getStringArray(R.array.gender_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, genders);
        AutoCompleteTextView textView = findViewById(R.id.gender_dropdown);
        textView.setAdapter(adapter);
    }
}