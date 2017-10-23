package ir.mrmilad.inapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class Payment {
    private String Api;
    private String DeviceID;
    private String Email;
    private String Mobile;
    private Context context;
    private Activity activity;

    public Payment(String api, String email, String mobile,Activity activity) {
        this.Api = api;
        this.Email = email;
        this.Mobile = mobile;
        this.activity = activity;
        getDeviceID(activity.getApplicationContext());
    }

    private void getDeviceID(Context context){
        try {
            this.DeviceID = Settings.Secure.getString(((Context) context).getContentResolver(), "android_id");
        } catch (Exception ex) {

        }
    }

    public void startPayment(){
        String paymentURL = "http://appmanager.mrmilad.ir/Payments.aspx?";
        paymentURL += "api=" + this.Api + "&uid="+this.DeviceID+"&email=" + this.Email + "&mobile=" + this.Mobile;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(paymentURL));
        this.activity.startActivity(i);
    }

}
