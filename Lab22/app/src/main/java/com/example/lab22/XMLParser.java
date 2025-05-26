package com.example.lab22;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class XMLParser {

    public String getXmlFromUrl(String url) {
        String xml = null;
        try {
            // Tạo đối tượng Client và tạo Http Connection
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            // Tiến hành Request lên server và nhận đáp ứng Response
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // Lấy các Thực thể trong đáp ứng Response chuyển qua kiểu String và gắn
            // vào file xml
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }
}