package com.ternobo.pikano.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ternobo.pikano.R;
import com.ternobo.pikano.activities.PDFViewer;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.ResponseProgress;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.Request;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloaderService extends Service {

    private final IBinder mBinder = new DownloaderBinder();
    public ArrayList<String> pendingList = new ArrayList<>();
    public ArrayList<String> downloadedList = new ArrayList<>();
    public String currentFile = null;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public DownloaderService() {
        super();
    }

    /**
     * Starts this service to perform action Download with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startDownloadAction(Context context, String filename) {
        Intent intent = new Intent(context, DownloaderService.class);
        intent.putExtra("filename", filename);
        context.startService(intent);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        final String filename = intent.getStringExtra("filename");
        this.pendingList.add(filename);
        this.currentFile = filename;
        int download_id = new Random().nextInt(999999);
        DataAccess dataAccess = new DataAccess(getApplicationContext());
        Request fileRequest = dataAccess.getFile(filename);
        //Push Download Notification
        mNotifyManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.download);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setSound(null);
        mBuilder.setWhen(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("com.ternobo.pikano");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "com.ternobo.pikano",
                    "Pikano",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription("no sound");
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            if (mNotifyManager != null) {
                mNotifyManager.createNotificationChannel(channel);
            }
        }
        mBuilder.setProgress(100, 0, false);
        Notification notification = mBuilder.build();


        startForeground(dataAccess.download_id, notification);
        dataAccess.callDownloadRequest(fileRequest, filename, new ResponseProgress() {

            @Override
            public void onProgressUpdate(long numBytes, long totalBytes, float percent, float speed) {
                Log.e("downloading", "Downloading :" + percent + "%");
                Notification notification = mBuilder.build();
                mBuilder.setProgress(100, (int) (percent * 100), false);
                startForeground(download_id, notification);
            }

            @Override
            public void onUIProgressStart(long totalBytes) {
                Log.e("downloading", "Downloading :" + totalBytes + " Bytes");
            }

            @Override
            public void onUIProgressFinished() {
                mBuilder.setContentText("دانلود تکمیل شد. برای مشاهده کلیک کنید");
                Intent intent = new Intent(DownloaderService.this, PDFViewer.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(DownloaderService.this, 0, intent, 0);
                mBuilder.setContentIntent(pendingIntent);
                mNotifyManager.notify(download_id, mBuilder.build());
                DownloaderService.this.pendingList.remove(filename);
                DownloaderService.this.downloadedList.add(filename);
            }
        });
        return START_NOT_STICKY;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     *
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
        super.onCreate();
    }


    /**
     * Return Current Service with pending download list
     */
    public class DownloaderBinder extends Binder {
        public DownloaderService getService() {
            return DownloaderService.this;
        }
    }

}
