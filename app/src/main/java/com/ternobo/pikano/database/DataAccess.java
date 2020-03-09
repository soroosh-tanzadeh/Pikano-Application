package com.ternobo.pikano.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.RESTobjects.requests.Code;
import com.ternobo.pikano.RESTobjects.requests.Files;
import com.ternobo.pikano.RESTobjects.requests.Phone;
import com.ternobo.pikano.RESTobjects.requests.Profile;
import com.ternobo.pikano.RESTobjects.requests.Simple;
import com.ternobo.pikano.RESTobjects.requests.URLs;
import com.ternobo.pikano.activities.BaseAcitivty;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DataAccess {

    private final String token = "58cgi4zgwa5ofu1ie09dnynox1ydnk";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String download_channel_id = "PDF_DOWNLOAD_CHANNEL";
    private Context context;
    public int download_id;
    public DataAccess(Context context) {
        this.context = context;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    protected Request getRequest(String... params) {
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        builder.addHeader("token", this.token);
        RequestBody body = RequestBody.create(JSON, params[1]);
        builder.post(body);
        Request request = builder.build();
        return request;
    }

    public Request sendVerificationCode(String phone) {
        Gson gson = new Gson();
        return this.getRequest(URLs.sendVerification, gson.toJson(new Phone(phone)));
    }

    public Request verifyCode(int user_id, String code) {
        Gson gson = new Gson();
        return this.getRequest(URLs.verifyPhone, gson.toJson(new Code(user_id, code)));
    }

    public Request setProfile(String token, String name, int grade, String profile) {
        Gson gson = new Gson();
        Log.d("request", gson.toJson(new Profile(token, name, grade, profile)));
        return this.getRequest(URLs.setupProfile, gson.toJson(new Profile(token, name, grade, profile)));
    }

    public Request getCurrentUser() {
        Gson gson = new Gson();
        try {
            DataFileAccess dataFileAccess = new DataFileAccess(this.context);
            MainDB db = dataFileAccess.readLocalDB();
            String api_token = db.getCurrentUser().getApi_token();
            return this.getRequest(URLs.getUserInfo, gson.toJson(new Simple(api_token)));
        } catch (Exception ex) {
            Log.e("Read_LocalDB", ex.getMessage(), ex);
            return null;
        }
    }

    public Request getGrades(User user) {
        Gson gson = new Gson();
        return this.getRequest(URLs.getGrades, gson.toJson(new Simple(user.getApi_token())));
    }

    public Request getFile(String filename) {
        Gson gson = new Gson();
        User user = DataFileAccess.getCurrentUser(context);
        this.download_id = new Random().nextInt(10000);
        return this.getRequest(URLs.downloadFile + filename, gson.toJson(new Simple(user.getApi_token())));
    }

    public Request getBooks() {
        User user = DataFileAccess.getCurrentUser(context);
        String api_token = user.getApi_token();
        Gson gson = new Gson();
        return this.getRequest(URLs.getBooks, gson.toJson(new Simple(api_token)));
    }

    public Request getMostRatedFiles() {
        User user = DataFileAccess.getCurrentUser(context);
        String api_token = user.getApi_token();
        Gson gson = new Gson();
        return this.getRequest(URLs.getMostRatedFiles, gson.toJson(new Simple(api_token)));
    }

    public Request getFiles(int category, int book) {
        User user = DataFileAccess.getCurrentUser(context);
        String api_token = user.getApi_token();
        Gson gson = new Gson();
        return this.getRequest(URLs.getFiles, gson.toJson(new Files(category, book, token)));
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager cm = (ConnectivityManager)
                this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return (networkInfo != null && networkInfo.isConnected());
    }


    public void callDownloadRequest(Request request, String filename, final ResponseProgress runnable) {
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    BaseAcitivty.current.runOnUiThread(() -> {
                        Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    BaseAcitivty.current.runOnUiThread(() -> {

                    });
                    Log.e("TAG", "=============onResponse===============");
                    Log.e("TAG", "request headers:" + response.request().headers());
                    Log.e("TAG", "response headers:" + response.headers());

                    //your original response body
                    ResponseBody body = response.body();
                    //wrap the original response body with progress
                    ResponseBody responseBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

                        //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                        @Override
                        public void onUIProgressStart(long totalBytes) {
                            super.onUIProgressStart(totalBytes);
                            Log.e("TAG", "onUIProgressStart:" + totalBytes);
                            runnable.onUIProgressStart(totalBytes);
                        }

                        @Override
                        public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                            Log.e("TAG", "=============start===============");
                            Log.e("TAG", "numBytes:" + numBytes);
                            Log.e("TAG", "totalBytes:" + totalBytes);
                            Log.e("TAG", "percent:" + percent);
                            Log.e("TAG", "speed:" + speed);
                            Log.e("TAG", "============= end ===============");
                            runnable.onProgressUpdate(numBytes, totalBytes, percent, speed);
                        }

                        //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                        @Override
                        public void onUIProgressFinish() {
                            super.onUIProgressFinish();
                            Log.e("TAG", "onUIProgressFinish:");
                            runnable.onUIProgressFinished();
                        }

                    });
                    BufferedSource source = responseBody.source();
                    File outFile = new File(DataFileAccess.getPDFDirectory(context), filename);
                    outFile.createNewFile();
                    BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                    Log.d(
                            "file", outFile.getPath()
                    );
                    source.readAll(sink);
                    sink.flush();
                    source.close();
                }
            });
        } else {
            Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }
    }


    public void callRequest(Request request, final ResponsRun runnable) {
        if (isNetworkAvailable()) {
            final ProgressDialog progressDialog = ProgressDialog.show(context, "درحال دریافت اطلاعات", "لطفا صبر کنید...");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                progressDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    BaseAcitivty.current.runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
                    });
                }


                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == 200) {
                        runnable.run(response);
                    } else if (response.code() == 401) {
                        runnable.onUnauthorized(response);
                    } else {
                        Log.d("Error", response.body().string());
                    }
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }
    }


    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    /**
     * Progress Response Body For Downloading File
     */

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
///////////////////////////////
}
