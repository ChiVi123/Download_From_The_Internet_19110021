package com.android.s19110021;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class PageSourceLoader extends AsyncTaskLoader<String> {
    private String query;
    private String transferProtocol;
    private Context conText;

    public PageSourceLoader(@NonNull Context context, String query, String transferProtocol) {
        super(context);
        this.conText = context;
        this.query = query;
        this.transferProtocol = transferProtocol;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getSource(this.conText, this.query, this.transferProtocol);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        onForceLoad();
    }
}
