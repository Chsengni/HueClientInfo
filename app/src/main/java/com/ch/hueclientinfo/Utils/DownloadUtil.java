package com.ch.hueclientinfo.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    private String json;
    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
         okHttpClient = new OkHttpClient();

    }

    /**
     * @param url          下载连接
     */
    public void download(final String url,final OnDownloadListener listener) {

        final Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxAge(60 * 5, TimeUnit.SECONDS)
                        .build())

                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Encoding", "identity")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                json = response.body().string();
                listener.onDownloadSuccess(json);
            }
        });
    }
    public interface OnDownloadListener{
        void onDownloadSuccess(String json);
        void onDownloadFailed(Exception e);
    }
}
