package ke.co.greencredit.nunua.General;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceHandler {
    // variable
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    // constructor
    public ServiceHandler() {

    }

    // making request request
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    // handling request
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        Log.d(" service call>>>", url);
        try {
            // http client

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;
            // checking http request method
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");

                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);

            }
            assert httpResponse != null;
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;

    }
}
