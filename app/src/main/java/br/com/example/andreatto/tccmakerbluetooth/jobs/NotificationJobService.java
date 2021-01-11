package br.com.example.andreatto.tccmakerbluetooth.jobs;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import br.com.example.andreatto.tccmakerbluetooth.R;

@SuppressLint("NewApi")
public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters jobP) {
        AsyncTask backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                //Executar qualquer trabalho que for necessário
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                notificar("Olha Que Novidade", "Já sei utilizar JobServices!");
                jobFinished(jobP, false);
            }
        };
        backgroundTask.execute();
        return true; //Ainda há algo a ser executado?
        //Retornamos true porque o trabalho ainda está sendo executado dentro
        //do AsyncTask.
        //Se fosse uma operação mais simples retornavamos false.
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void notificar(String title, String text)
    {
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(getApplicationContext());
        notification.setStyle(new NotificationCompat.BigTextStyle());
        notification.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        notification.setContentText(text);
        notification.setContentTitle(title);
        notification.setPriority(NotificationCompat.PRIORITY_MIN);
        notification.setAutoCancel(true);
        Intent update = new Intent(Intent.ACTION_VIEW);
        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(getApplicationContext());
        stackBuilder1.addNextIntent(update);
        PendingIntent updateIntent = stackBuilder1.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(updateIntent);
        NotificationManagerCompat.from(getApplicationContext())
                .notify(100, notification.build());
    }
}
