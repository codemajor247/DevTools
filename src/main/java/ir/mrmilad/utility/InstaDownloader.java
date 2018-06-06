package ir.mrmilad.utility;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InstaDownloader {

    public static final int Video = 0;
    public static final int Photo = 1;
    Context mContext;

    onCheck listener;

    public void InstaDownloader(Context context){
        this.mContext = context;
    }

    public interface onCheck {
        void onComplete(int type, String url);
    }

    public void setOnCheck(onCheck onCheck) {
        this.listener = listener;
    }

    public void checkUrl(String url) {
        String type = "";
        String html = WebClient.DownloadUrlString(url, "", true);
        Document doc = Jsoup.parse(html);
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
    }

}
