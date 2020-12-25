package com.example.bookmycutt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BookingConfirmedActivity extends AppCompatActivity {
    private TextView sname,msg,detils;
    private Button nextBooking;
    private NotificationManagerCompat notificationManagerCompat;
    public final static String  CHANNEL_ID="personal_notification";
    public final int   NOTIFICATION_ID=001;
    public final static long[]  pattern={100,300,300,300};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);
        sname=findViewById(R.id.textView3);
        final Employees emp=(Employees) getApplicationContext();

        sname.setText(emp.getShopname());
        msg=findViewById(R.id.textView8);
        msg.setText("Thank you for booking your Appointment with "+emp.getShopname());
        detils=findViewById(R.id.textView9);
        nextBooking=findViewById(R.id.next);
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Channel_1", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Hello");
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        notificationManagerCompat=NotificationManagerCompat.from(this);

        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_event_available)
                .setContentTitle("Booking Confirmed")
                .setContentText("At "+emp.getShopname()+" On "+emp.date+" At "+emp.timeofBooking)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(pattern)
                .build();
        notificationManagerCompat.notify(emp.i+1,notification);
        nextBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingConfirmedActivity.this,DashboardActivity.class));
            }
        });
        detils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }





}
