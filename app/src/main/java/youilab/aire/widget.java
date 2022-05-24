package youilab.aire;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import youilab.aire.AppTools.GPS;
import youilab.aire.Services.IntegrASIntentService;

public class widget extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;//cantidad de widgets habilitados

        for (int i =0; i<N;i++)
        {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, Splash.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.air_widget);
            views.setOnClickPendingIntent(R.id.widget_air, pendingIntent);



             if(IntegrASIntentService.MyCurrentNet!=null)
            {
                String[] dataDate = IntegrASIntentService.MyCurrentNet.getFecha().split("T");
                views.setTextViewText(R.id.lblNetAir, IntegrASIntentService.MyCurrentNet.getName() );
                views.setTextViewText(R.id.lbValueStatusAir,String.valueOf(IntegrASIntentService.MyCurrentNet.getStatusNameNet()));
                views.setTextViewText(R.id.dateConsult,dataDate[1].substring(0,5) + "hrs");
                views.setTextViewText(R.id.time,dataDate[0] );
                changeColor(views,IntegrASIntentService.MyCurrentNet.getStatusNet(),context);
            }



            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    private void changeColor(RemoteViews views,int statusAir,Context context)
    {
        if(statusAir==1)
            views.setTextColor(R.id.lbValueStatusAir,context.getResources().getColor(R.color.bien));
        else  if(statusAir==2)
            views.setTextColor(R.id.lbValueStatusAir,context.getResources().getColor(R.color.aceptable));
        else  if(statusAir==3)
            views.setTextColor(R.id.lbValueStatusAir,context.getResources().getColor(R.color.mala));
        else if(statusAir==4)
            views.setTextColor(R.id.lbValueStatusAir,context.getResources().getColor(R.color.muy_mala));
        else  if(statusAir==5)
            views.setTextColor(R.id.lbValueStatusAir,context.getResources().getColor(R.color.extremadamente_mala));

    }
}
