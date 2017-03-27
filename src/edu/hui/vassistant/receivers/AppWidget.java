package edu.hui.vassistant.receivers;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;
import edu.hui.vassistant.R;
import edu.hui.vassistant.activities.DateMemoActivity;

public class AppWidget extends AppWidgetProvider
{
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		RemoteViews views = buildUpdate(context);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", 
								"JUL", "AUG", "SPE", "OCT", "NOV", "DEC"};
	private String[] days = {"SUN", "MON", "TUE", "WED",
	                              "THU", "FRI", "SAT"};
	
	private RemoteViews buildUpdate(Context context)
	{
		RemoteViews views = null;
		Time time = new Time();
		time.setToNow();
		String month = months[time.month];
		views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		views.setTextViewText(R.id.date, new Integer(time.monthDay).toString());
		views.setTextViewText(R.id.month, month);
		views.setTextViewText(R.id.weekday, days[time.weekDay]);
		/*点击背景跳转到备忘管理的Activity*/
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, DateMemoActivity.class), 0);  
		views.setOnClickPendingIntent(R.id.widgetbg, contentIntent);

		return views;
	}
}
