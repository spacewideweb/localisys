package atpl.cc.localisys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.Localisys_HomeActivity;

/**
 * Created by android on 9/6/17.
 */

public class Constants {
    public static String work = "close";
    public static String disput ="";
   public static ShareDialog shareDialog;
    static TextView cancel,done;
    static Dialog dialog;


    public static String getDateAgo(String date1,String date2) {
        /*2018-04-01T21:55:00Z*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = sdf.parse(date1);
            Date now = sdf.parse(date2);
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            if (days < 7)
                return days + " Days";
            else
                return days / 7 + " Weeks";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NONE";
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public static String getDateDiffer(String date1,String date2) {
        /*2018-04-01T21:55:00Z*/
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
        try {
            Date date = sdf.parse(date1);
            Date now = sdf.parse(date2);
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            if (days < 7)
                return days+"";
            else
                return days / 7+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }



    public static void instagram(Context context){

        Intent i;
        try{
            i=new Intent(Intent.ACTION_MAIN);
            PackageManager managerclock = context.getPackageManager();
            i = managerclock.getLaunchIntentForPackage("com.instagram.android");
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            if (i!=null){
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"MY TEXT");
                context.startActivity(shareIntent);
            }
            else {


            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            context.startActivity(i);

        }

    }

    public static void twitter(Context context){

        TweetComposer.Builder builder = null;
        try {
            builder = new TweetComposer.Builder(context)
                    .text("Pb demo")
                    .url(new URL("http://atpl.cc/"));
            builder.show();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static void facebook(Activity context){

        shareDialog = new ShareDialog(context);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Pb Demo")
                    .setContentDescription(
                            "This is demo sample  showcases simple Facebook sharing integration")
                    .setContentUrl(Uri.parse("http://atpl.cc/"))
                    .build();

            shareDialog.show(linkContent);
        }

    }

    public static void  showReportProblemDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_problem,null);
        TextView tv_rcancel = (TextView)view.findViewById(R.id.tv_rcancel);
        TextView tv_report = (TextView)view.findViewById(R.id.tv_report);
        final EditText describe_problem = (EditText) view.findViewById(R.id.describe_problem);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_rcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problem = describe_problem.getText().toString();
                if(problem.equalsIgnoreCase("")){
                    Constants.Information_Dialog(context, "Please fill your problem");
                }
                else
                {
                    Toast.makeText(context, "Your report has been submitted", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
    }

    public static void resetDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_reset, null);

        TextView tv_recancel = (TextView) view.findViewById(R.id.tv_recancel);
        TextView tv_reset = (TextView) view.findViewById(R.id.tv_reset);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_recancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public static void  showPasswordDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_changepassword,null);
        TextView tv_pcancel = (TextView)view.findViewById(R.id.tv_pcancel);
        TextView tv_changepwd = (TextView)view.findViewById(R.id.tv_changepwd);
        final EditText edt_enter_password = (EditText) view.findViewById(R.id.edt_enter_password);
        final EditText edt_re_enter_password = (EditText) view.findViewById(R.id.edt_re_enter_password);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_pcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String enterPassword = edt_enter_password.getText().toString();
                String re_enterPassword = edt_re_enter_password.getText().toString();

                if(enterPassword.equalsIgnoreCase("") || re_enterPassword.equalsIgnoreCase("")){
                    Constants.Information_Dialog(context, "All fields are required");
                }
                else if(!enterPassword.equals(re_enterPassword)){
                    Constants.Information_Dialog(context, "Passwords should be matched");
                }
                else{
                    Toast.makeText(context, "Your password changed successfully", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public static void Information_Dialog(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_infodialog,null);
        TextView tv_Infomsg = (TextView)view.findViewById(R.id.tv_Infomsg);
        TextView btn_ok = (TextView)view.findViewById(R.id.btn_ok);
        tv_Infomsg.setText(msg);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showingDialog(final Context context){
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feedback_popup);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        done=(TextView)dialog.findViewById(R.id.end_contract);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disput="dis";
               dialog.getContext().startActivity(new Intent(context, Localisys_HomeActivity.class));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Bitmap decodeSampledBitmapFromFile(Context context,int drawable,
                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options
                options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(),drawable,options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), drawable,options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
// BEGIN_INCLUDE (calculate_sample_size)
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }


            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
// END_INCLUDE (calculate_sample_size)
    }


}
