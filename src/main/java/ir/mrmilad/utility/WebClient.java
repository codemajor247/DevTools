package ir.mrmilad.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.os.StrictMode;

public class WebClient {

	/**
	 * Download URL Content as String . Get Method
	 **/
	@SuppressLint("NewApi")
	public static String DownloadUrlString(String _RequestURL, String _QueryString, boolean IsNormal) {
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			if (IsNormal) {
				String response = "";
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(_RequestURL);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
					return response;

				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}

			} else {
				String[] _Params = _QueryString.split("&");
				String data = "";
				for (int i = 0; i < _Params.length; i++) {
					String[] _Values = _Params[i].split("=");
					String _Key = _Values[0];
					String _Val = _Values[1];
					if (data.equals("")) {
						data = URLEncoder.encode(_Key, "UTF-8") + "=" + URLEncoder.encode(_Val, "UTF-8");
					} else {
						data += "&" + URLEncoder.encode(_Key, "UTF-8") + "=" + URLEncoder.encode(_Val, "UTF-8");
					}
				}

				URL url = new URL(_RequestURL);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				return sb.toString();
			}

		} catch (Exception ex) {
			return "err-exeption";
		}
	}

	/**
	 * Send Request to URL with Post Method
	 **/
	@SuppressLint("NewApi")
	public static String DownloadUrlString(String _RequestURL, String _RequestQuery, String _RequestHeader, boolean _HasHeader) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(_RequestURL);

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

			if (!_RequestQuery.equals("")) {
				String[] _Params = _RequestQuery.split("&");
				for (int i = 0; i < _Params.length; i++) {
					String[] _Values = _Params[i].split("=");
					nameValuePairs.add(new BasicNameValuePair(_Values[0].toString(), _Values[1].toString()));
				}
			}

			if (_HasHeader) {
				String[] _Params = _RequestHeader.split(",");
				for (int i = 0; i < _Params.length; i++) {
					String[] _Values = _Params[i].split("=");
					httppost.addHeader(_Values[0].toString(), _Values[1].toString());
				}
			}

			HttpResponse response = httpclient.execute(httppost);
			InputStream inputStream = response.getEntity().getContent();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}

			return stringBuilder.toString();

		} catch (ClientProtocolException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * Hit URL
	 **/
	public static boolean HitUrl(String Url) {
		try {
			URL url = new URL(Url);
			URLConnection hc = url.openConnection();
			hc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			hc.getContentType();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean UploadChunk(String URL,String FilePath,String FileName){
//		final int cSize = 1024 * 1024; // size of chunk
//	    File file = new File(FilePath);
//	    final long pieces = file.length()/cSize; // used to return file length.
//
//	    HttpPost request = new HttpPost(URL);
//
//	    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
//
//	    for (int i= 0; i< pieces; i++) {
//	        byte[] buffer = new byte[cSize];
//
//	        if(stream.read(buffer) ==-1)
//	          break;
//	        MultipartEntity multipartEntity = new MultipartEntity();
//	        MultipartEntity entity = new MultipartEntity();
//	        entity.addPart("chunk_id", new StringBody(String.valueOf(i))); //Chunk Id used for identification.
//	        request.setEntity(entity);
//	        ByteArrayInputStream arrayStream = new ByteArrayInputStream(buffer);
//
//	        entity.addPart("file_data", new InputStreamBody(arrayStream, FileName));
//
//	        HttpClient client = app.getHttpClient();
//	        client.execute(request);
//	    }
		return true;
	}
}
