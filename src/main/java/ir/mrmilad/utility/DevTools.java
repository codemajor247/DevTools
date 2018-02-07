package ir.mrmilad.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.style.MetricAffectingSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import ir.mrmilad.tools.R;

/**
 * DevTools v1.2 By Milad.Seifoori@Gmail.com . http://MrMilad.ir
 **/

@SuppressWarnings("deprecation")
public class DevTools {

    private final static Random random = new Random();
    private static final int BUFFER_SIZE = 4096;
    private static char[] buf;
    private static final char[] symbols = new char[36];
    private static String strWeekDay = "";
    private static String strMonth = "";
    private static int date;
    private static int month;
    private static int year;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static String _Transfer = "";
    public static String _JsonData = "";
    public static String _InAppKey = "";
    public static String _ZipName = "";
    public static String _ZipPath = "";
    public static Boolean _IsZip = false;
    public static int Position_Top = 1;
    public static int Position_Bottom = 3;
    public static int Position_CenterVertical = 2;

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    public static class TypefaceSpan extends MetricAffectingSpan {
        private Typeface mTypeface;

        public TypefaceSpan(Typeface typeface) {
            mTypeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

    public static void ShowToastTypeFace(String Message, Context context, Typeface typeface) {
        SpannableString efr = new SpannableString(Message);
        efr.setSpan(new TypefaceSpan(typeface), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toast.makeText(context, efr, Toast.LENGTH_SHORT).show();
    }

    public static float getDensity(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }

    public static int convertDiptoPix(Context ctx, int dip) {
        float scale = getDensity(ctx);
        return (int) (dip * scale + 0.5f);
    }

    public static int convertPixtoDip(Context ctx, int pixel) {
        float scale = getDensity(ctx);
        return (int) ((pixel - 0.5f) / scale);
    }

    @SuppressLint("NewApi")
    public static int getDisplayWidth(Activity act) {
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @SuppressLint("NewApi")
    public static int getDisplayHeight(Activity act) {
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public final class SessionIdentifierGenerator {

        @SuppressLint("TrulyRandom")
        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }

    }

    /**
     * Get Device Uniq ID e.g. 8f8s8g1df6g6867dfg0
     **/
    public static String getDeviceID(Context context) {
        try {
            return Secure.getString(((Context) context).getContentResolver(), "android_id");
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Get Device Brand e.g. samsung,lg...
     **/
    public static String getDeviceBrand() {
        try {
            return Build.BRAND;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Get Device Brand e.g. SM-G920F
     **/
    public static String getDeviceModel() {
        try {
            return Build.MODEL;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Get Device Os SDK Version e.g. 19,23...
     **/
    public static String getDeviceSdkVersion() {
        try {
            return Build.VERSION.SDK;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Get Network Operator e.g. Irancell,MCI,Rightel
     **/
    public static String getNetworkOperator(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService("phone");
            if (tel.getNetworkOperator() != null && tel.getNetworkOperator().toString() != "") {
                return tel.getNetworkOperatorName();
            } else {
                return "No sim";
            }
        } catch (Exception ex) {
            return "err 404";
        }
    }

    /**
     * Get Random number between start and end
     **/
    public static int RandomNumber(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt(max - min + 1) + min;
    }

    private static void calcSolarCalendar(Date MiladiDate) {

        int ld;

        int miladiYear = MiladiDate.getYear() + 1900;
        int miladiMonth = MiladiDate.getMonth() + 1;
        int miladiDate = MiladiDate.getDate();
        int WeekDay = MiladiDate.getDay();

        int[] buf1 = new int[12];
        int[] buf2 = new int[12];

        buf1[0] = 0;
        buf1[1] = 31;
        buf1[2] = 59;
        buf1[3] = 90;
        buf1[4] = 120;
        buf1[5] = 151;
        buf1[6] = 181;
        buf1[7] = 212;
        buf1[8] = 243;
        buf1[9] = 273;
        buf1[10] = 304;
        buf1[11] = 334;

        buf2[0] = 0;
        buf2[1] = 31;
        buf2[2] = 60;
        buf2[3] = 91;
        buf2[4] = 121;
        buf2[5] = 152;
        buf2[6] = 182;
        buf2[7] = 213;
        buf2[8] = 244;
        buf2[9] = 274;
        buf2[10] = 305;
        buf2[11] = 335;

        if ((miladiYear % 4) != 0) {
            date = buf1[miladiMonth - 1] + miladiDate;

            if (date > 79) {
                date = date - 79;
                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = date / 31;
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = miladiYear - 621;
                } else {
                    date = date - 186;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 621;
                }
            } else {
                if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                    ld = 11;
                } else {
                    ld = 10;
                }
                date = date + ld;

                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = miladiYear - 622;
            }
        } else {
            date = buf2[miladiMonth - 1] + miladiDate;

            if (miladiYear >= 1996) {
                ld = 79;
            } else {
                ld = 80;
            }
            if (date > ld) {
                date = date - ld;

                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = (date / 31);
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = miladiYear - 621;
                } else {
                    date = date - 186;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 621;
                }
            } else {
                date = date + 10;

                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = miladiYear - 622;
            }

        }

        switch (month) {
            case 1:
                strMonth = "فروردین";
                break;
            case 2:
                strMonth = "اردیبهشت";
                break;
            case 3:
                strMonth = "خرداد";
                break;
            case 4:
                strMonth = "تیر";
                break;
            case 5:
                strMonth = "مرداد";
                break;
            case 6:
                strMonth = "شهریور";
                break;
            case 7:
                strMonth = "مهر";
                break;
            case 8:
                strMonth = "آبان";
                break;
            case 9:
                strMonth = "آذر";
                break;
            case 10:
                strMonth = "دی";
                break;
            case 11:
                strMonth = "بهمن";
                break;
            case 12:
                strMonth = "اسفند";
                break;
        }

        switch (WeekDay) {

            case 0:
                strWeekDay = "شنبه";
                break;
            case 1:
                strWeekDay = "یکشنبه";
                break;
            case 2:
                strWeekDay = "دوشنبه";
                break;
            case 3:
                strWeekDay = "سه شنبه";
                break;
            case 4:
                strWeekDay = "چهارشنبه";
                break;
            case 5:
                strWeekDay = "پنجشنبه";
                break;
            case 6:
                strWeekDay = "جمعه";
                break;
        }

    }

    public static void getScreenResolution(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
    }

    /**
     * Create ZipFile from FileArray
     **/
    public static void Zip(String[] files, String zipFile) {

        String[] _files = files;
        String _zipFile = zipFile;

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < _files.length; i++) {

                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uncompress zipFile
     **/
    public static void UnZip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Get Short Shamsi Date e.g. 1395/01/01
     **/
    public static String getShortDate() {
        Date _date = new Date();
        calcSolarCalendar(_date);
        Locale loc = new Locale("en_US");
        return String.valueOf(year) + "/" + String.format(loc, "%02d", month) + "/" + String.format(loc, "%02d", date);
    }

    /**
     * Get Long Shamsi Date e.g. یکشنبه 14 اس�?ند 1395
     **/
    public static String getLongDate() {
        Date _date = new Date();
        calcSolarCalendar(_date);
        Locale loc = new Locale("en_US");
        return strWeekDay + " " + String.format(loc, "%02d", date) + " " + strMonth + " " + String.valueOf(year);
    }

    /**
     * Generate Uniq String
     **/
    public static String Guid(int lenght) {
        buf = new char[lenght];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    /**
     * Get Prefrence Value as boolean
     **/
    public static boolean getPreferences_Boolean(Context ctx, String Name, String Key, boolean initial) {
        SharedPreferences pref = ctx.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return pref.getBoolean(Key, initial);
    }

    /**
     * Set Prefrence Value as boolean
     **/
    public static void setPreferences_Boolean(Context ctx, String Name, String Key, boolean Value) {
        try {
            @SuppressWarnings("static-access")
            SharedPreferences pref = ctx.getSharedPreferences(Name, ctx.MODE_PRIVATE);
            Editor editor = pref.edit();
            editor.putBoolean(Key, Value);
            editor.commit();

        } catch (Exception ex) {
        }
    }

    /**
     * Get Prefrence Value as String
     **/
    public static String getPreferences_String(Context ctx, String Name, String Key, String initString) {
        SharedPreferences pref = ctx.getSharedPreferences(Name, Context.MODE_PRIVATE);
        initString = pref.getString(Key, initString);
        return initString;
    }

    public static void setPreferences_String(Context ctx, String Name, String Key, String Value) {
        try {
            @SuppressWarnings("static-access")
            SharedPreferences pref = ctx.getSharedPreferences(Name, ctx.MODE_PRIVATE);
            Editor editor = pref.edit();
            editor.putString(Key, Value);
            editor.commit();

        } catch (Exception ex) {
        }
    }

    public static long getPreferences_Long(Context ctx, String Name, String Key, long data) {
        SharedPreferences pref = ctx.getSharedPreferences(Name, Context.MODE_PRIVATE);
        data = pref.getLong(Key, data);
        return data;
    }

    public static void setPreferences_Long(Context ctx, String Name, String Key, long Value) {
        try {
            @SuppressWarnings("static-access")
            SharedPreferences pref = ctx.getSharedPreferences(Name, ctx.MODE_PRIVATE);
            Editor editor = pref.edit();
            editor.putLong(Key, Value);
            editor.commit();

        } catch (Exception ex) {
        }
    }

    /**
     * Get Prefrence Value as Integer
     **/
    public static int getPreferences_Int(Context ctx, String Name, String Key, int _InitValue) {

        SharedPreferences pref = ctx.getSharedPreferences(Name, Context.MODE_PRIVATE);
        _InitValue = pref.getInt(Key, _InitValue);
        return _InitValue;
    }

    /**
     * Set Prefrence Value as Integer
     **/
    public static void setPreferences_Int(Context ctx, String Name, String Key, int Value) {
        try {
            @SuppressWarnings("static-access")
            SharedPreferences pref = ctx.getSharedPreferences(Name, ctx.MODE_PRIVATE);
            Editor editor = pref.edit();
            editor.putInt(Key, Value);
            editor.commit();

        } catch (Exception ex) {
        }
    }

    /**
     * Get Typeface from Font . font address e.g. Assets/fonts/myfont.ttf
     **/
    public static Typeface getFontName(Context ctx, String FontName) {
        Typeface ft = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + FontName + ".ttf");
        return ft;
    }

    /**
     * Show Toast Message
     **/
    public static void ShowToast(String Message, Context context) {
        Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
    }

    /**
     * Show Custom Toast Message . FontName can be Null
     **/

    @SuppressLint("InflateParams")
    public static void ShowCustomToast(Activity activity, int Icon, int LayoutBackground, int Position,
                                       String MessageText, String TextColor, Typeface FontName, boolean RtlSupport) {

        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();
        View customToastroot;
        if (RtlSupport) {
            customToastroot = inflater.inflate(R.layout.custom_toast_layout_rtl, null);
        } else {
            customToastroot = inflater.inflate(R.layout.custom_toast_layout_ltr, null);
        }

        LinearLayout LinearMessageBox = (LinearLayout) customToastroot.findViewById(R.id.LinearMessageBox);
        LinearMessageBox.setBackgroundResource(LayoutBackground);
        ImageView imageIcon = (ImageView) customToastroot.findViewById(R.id.imageIcon);
        imageIcon.setBackgroundResource(Icon);
        TextView txtMessage = (TextView) customToastroot.findViewById(R.id.txtMessage);
        txtMessage.setText(MessageText);
        txtMessage.setTextColor(Color.parseColor(TextColor));
        if (FontName != null) {
            txtMessage.setTypeface(FontName);
        }
        Toast customtoast = new Toast(context);
        customtoast.setView(customToastroot);
        switch (Position) {
            case 1:
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                break;
            case 2:
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                break;
            case 3:
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                break;
        }
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
    }

    /**
     * Check Device Has Internet connection
     **/
    @SuppressWarnings({"static-access"})
    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    /**
     * Check Directory is Exist
     **/
    public static boolean IsDir(String _DirPath) {
        File _Dir = new File(_DirPath);
        return _Dir.isDirectory();
    }

    /**
     * Check File is Exist
     **/
    public static boolean IsFile(String _FilePath) {
        File _File = new File(_FilePath);
        return _File.isFile();
    }

    /**
     * Make Directory
     **/
    public static void MakeDir(String _DirPath) {
        File _Dir = new File(_DirPath);
        _Dir.mkdir();
    }

    /**
     * Delete Directory or File
     **/
    public static boolean DeleteFileOrDir(String _Path) {
        try {
            File _FileOrDir = new File(_Path);
            _FileOrDir.delete();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Share your app apk file
     **/
    public static void ShareApp(Activity act, String _package) {

        final PackageManager pm = act.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.sourceDir.startsWith("/data/") && packageInfo.sourceDir.contains(_package)) {
                Intent localIntent = new Intent();
                localIntent.setAction("android.intent.action.SEND");
                localIntent.setType("application/android.com.app");
                localIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(packageInfo.sourceDir)));
                act.startActivity(localIntent);
                break;
            }
        }
    }

    /**
     * Share Text File
     **/
    public static void ShareText(Activity act, String subject, String body, String _What) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        act.startActivity(Intent.createChooser(sharingIntent, "اشتراک گزاری" + " " + _What));
    }

    /**
     * Open Url with Intent
     **/
    public static void OpenURL(Activity act, String _Url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(_Url));
        act.startActivity(i);
    }

    /**
     * Convert Timestamp to dateTime
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String getDateTimestamp(long timestamp, String format) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format(format, cal).toString();
        return date;
    }

    /**
     * Format currency 10000 price to 10,000
     *
     * @param price
     * @return
     */
    public static String getCurrencyFormat(long price) {
        return String.format("%,d", Long.parseLong(String.valueOf(price)));
    }

    /**
     * Copy database from asset file to local database
     *
     * @param context
     * @param toAddress
     * @param dbName
     */
    public static void copyDatabase(Context context, String toAddress, String dbName) {
        try {
            File dbPath = new File(toAddress);
            if (!dbPath.isDirectory()) {
                dbPath.mkdir();
            }

            byte[] buffer = new byte[1024];
            OutputStream myOutput = null;
            int length;
            InputStream myInput = null;

            myInput = context.getAssets().open(dbName);
            myOutput = new FileOutputStream(toAddress + "/" + dbName);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();

        } catch (Exception ex) {

        }
    }

    /**
     * change progresBar color
     *
     * @param activity
     * @param progressBar
     * @param color
     */
    public static void changeProgressBarColor(Activity activity, int progressBar, int color) {
        ProgressBar progressBar1 = (ProgressBar) activity.findViewById(progressBar);
        progressBar1.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Install apk from file
     * @param _context
     * @param packageName
     * @param file
     */
    public static void installApkFromFile(Context _context, String packageName, File file) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(_context, packageName + ".provider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }
}
