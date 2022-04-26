package com.android.s19110021;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        createCustomSpinner();
    }

    private void init() {
        spinner = findViewById(R.id.protocol);
    }

    private void createCustomSpinner(){
        final List<String> states = Arrays.asList("http://", "https://");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_custom_spinner, states);
        arrayAdapter.setDropDownViewResource(R.layout.my_custom_spinner);

        spinner.setAdapter(arrayAdapter);
    }

}