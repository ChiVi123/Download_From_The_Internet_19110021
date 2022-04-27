package com.android.s19110021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemSelectedListener {
    private final String QUERY = "query";
    private final String PROTOCOL = "protocol";
    private String spinnerString;
    private Spinner spinner;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initSpinner();

        if (getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    private void init() {
        spinner = findViewById(R.id.protocol);
        editText = findViewById(R.id.url);
        textView = findViewById(R.id.page_source);
    }

    private void initSpinner(){
        final List<String> states = Arrays.asList("http://", "https://");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_custom_spinner, states);
        arrayAdapter.setDropDownViewResource(R.layout.my_custom_spinner);

        if (spinner != null){
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(arrayAdapter);
        }
    }

    public void getPageSource(View view) {
        handleInputMethodManager(view);

        String query = editText.getText().toString();

        handleNetworkInfo(query);
    }

    private void handleInputMethodManager(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void handleCreateToast(String query) {
        if (query.length() == 0){
            Toast.makeText(this, "URL is not type", Toast.LENGTH_LONG).show();
        }
        else if(!URLUtil.isValidUrl(query)){
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show();
        }
    }

    private void handleSetBundle(String query, String spinner) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY, query);
        queryBundle.putString(PROTOCOL, spinner);
        getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    private void handleNetworkInfo(String query) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null){
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && (query.length() != 0)){
            handleSetBundle(query, this.spinnerString);
        }
        else{
            handleCreateToast(query);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        String transferProtocol = "";
        if (args != null){
            queryString = args.getString(QUERY);
            transferProtocol = args.getString(PROTOCOL);
        }
        return new PageSourceLoader(this, queryString, transferProtocol);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try{
            textView.setText(data);
        }
        catch (Exception e){
            e.printStackTrace();
            textView.setText("No response");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerString = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String[] values = getResources().getStringArray(R.array.protocol_option);
        spinnerString = values[0];
    }
}