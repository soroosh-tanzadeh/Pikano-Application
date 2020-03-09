package com.ternobo.pikano.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.services.DownloaderService;
import com.ternobo.pikano.tools.Alert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PDFViewer extends BaseAcitivty {

    protected DownloaderService mBoundService;
    private boolean mIsBound = false;
    private Thread checkingThread;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownloaderService.DownloaderBinder binder = (DownloaderService.DownloaderBinder) service;
            mBoundService = binder.getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mIsBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            User currentUser = DataFileAccess.getCurrentUser(this);
            Date chargeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentUser.getAccount_charged_at());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(chargeTime);
            calendar.add(Calendar.YEAR, 1);
            Date nextYear = calendar.getTime();

            if (new Date().before(nextYear)) {
                String filename = getIntent().getStringExtra("filename");
                PDFView pdfView = findViewById(R.id.pdfView);
                File directory = DataFileAccess.getPDFDirectory(PDFViewer.this);
                File outputFile = new File(directory.getPath() + "/" + filename);
                TextView downloading = findViewById(R.id.downloadingText);
                ProgressBar download_progressBar = findViewById(R.id.progressBar);
                pdfView.setVisibility(View.GONE);
                Runnable runnable = () -> {
                    downloading.setVisibility(View.GONE);
                    download_progressBar.setVisibility(View.GONE);
                    pdfView.setVisibility(View.VISIBLE);
                    pdfView.recycle();
                    pdfView.fromFile(outputFile)
                            .enableSwipe(true)
                            .enableAntialiasing(true)
                            .enableAnnotationRendering(true)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .load();
                    download_progressBar.setVisibility(View.GONE);
                    downloading.setVisibility(View.GONE);
                };
                if (directory.exists() && outputFile.exists()) {
                    PDFViewer.this.runOnUiThread(runnable);
                } else {
                    DownloaderService.startDownloadAction(this, filename);
                    doBindService();
                    checkingThread = new Thread(() -> {
                        boolean downloaded = false;
                        while (!downloaded) {
                            if (mIsBound) {
                                downloaded = mBoundService.downloadedList.contains(filename);
                                if (downloaded) {
                                    PDFViewer.this.runOnUiThread(runnable);
                                    break;
                                }
                            }
                        }
                    });
                    checkingThread.start();
                }
            } else {
                Alert.make("عدم دسترسی", "برای دسترسی به این بخش باید اشتراک ویژه خریداری کنید!", this, () -> {
                    finish();
                }).show();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation
        // that we know will be running in our own process (and thus
        // won't be supporting component replacement by other
        // applications).
        Intent intent = new Intent(PDFViewer.this, DownloaderService.class);
        bindService(intent,
                mConnection,
                Context.BIND_AUTO_CREATE);
    }


    protected void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onStop() {
        doUnbindService();
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
