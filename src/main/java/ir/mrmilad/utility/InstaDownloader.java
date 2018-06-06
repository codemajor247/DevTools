package ir.mrmilad.utility;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InstaDownloader {

    public static final int Video = 0;
    public static final int Photo = 1;
    int ResponseCode = 0;
    Exception exceptionRes;

    onCheck listener;
    String instaUrl;

    public interface onCheck {
        void onChecking();
        void onComplete(int type, String url);
        void onFailed(Exception ex);
    }

    public void setOnCheck(String postUrl, onCheck status) {
        this.instaUrl = postUrl;
        this.listener = status;
        new onFetch().execute();
    }

    public class onFetch extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                listener.onChecking();
                String html = WebClient.DownloadUrlString(instaUrl, "", true);
                ResponseCode = 200;
                return html;
            } catch (Exception ex) {
                exceptionRes = new Exception();
                exceptionRes = ex;
                ResponseCode = 400;
                return "err." + ex;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (ResponseCode == 200) {
                try {
                    String type = "";
                    Document doc = Jsoup.parse(result);
                    Elements metaTags = doc.getElementsByTag("meta");
                    for (Element metaTag : metaTags) {
                        String content = metaTag.attr("content");
                        String name = metaTag.attr("property");
                        if (name.equals("og:type")) {
                            type = content;
                        }
                    }
                    if (type.contains("photo") || type.contains("image")) {
                        for (Element metaTag : metaTags) {
                            String content = metaTag.attr("content");
                            String name = metaTag.attr("property");
                            if (name.equals("og:image")) {
                                listener.onComplete(Photo, content);
                            }
                        }
                    } else {
                        for (Element metaTag : metaTags) {
                            String content = metaTag.attr("content");
                            String name = metaTag.attr("property");
                            if (name.equals("og:video")) {
                                listener.onComplete(Video, content);
                            }
                        }
                    }
                } catch (Exception e) {
                    listener.onFailed(e);
                }

            } else {
                listener.onFailed(exceptionRes);
            }
        }

    }

}
