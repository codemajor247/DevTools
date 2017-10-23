package ir.mrmilad.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.StrictMode;

//JsonClass Developed By Milad Seifoori

@SuppressWarnings("deprecation")
public class JsonClass {

	private static String _RequestURL;
	private static String _Query;
	private static String _Headers;
	private static boolean _HasHeader;

	@SuppressWarnings("static-access")
	public JsonClass(String _RequestURL, String _Query, String _Headers) {
		super();
		this._RequestURL = _RequestURL;
		this._Query = _Query;
		this._Headers = _Headers;
		this._HasHeader = true;
	}

	@SuppressWarnings("static-access")
	public JsonClass(String _RequestURL, String _Query) {
		super();
		this._RequestURL = _RequestURL;
		this._Query = _Query;
		this._HasHeader = false;
	}

	public JsonClass() {
		super();
	}

	@SuppressWarnings("static-access")
	public String SendRequest() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this._RequestURL);

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

			if (!this._Query.equals("")) {
				String[] _Params = this._Query.split("&");
				for (int i = 0; i < _Params.length; i++) {
					String[] _Values = _Params[i].split("=");
					if(_Values.length <2){
						nameValuePairs.add(new BasicNameValuePair(_Values[0]
								.toString(), ""));
					}else {
						nameValuePairs.add(new BasicNameValuePair(_Values[0]
								.toString(), _Values[1].toString()));
					}
				}
			}

			if (this._HasHeader) {
				String[] _Params = this._Headers.split(",");
				for (int i = 0; i < _Params.length; i++) {
					String[] _Values = _Params[i].split("=");
					httppost.addHeader(_Values[0].toString(),
							_Values[1].toString());
				}
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			HttpResponse response = httpclient.execute(httppost);
			InputStream inputStream = response.getEntity().getContent();

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

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

}
