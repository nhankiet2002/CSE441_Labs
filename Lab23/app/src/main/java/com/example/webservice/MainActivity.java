package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView lv1; 
    ArrayList<ListArticle> mylist;
    MyArrayAdapter myadapter;
    String URL_RSS = "https://vnexpress.net/rss/tin-moi-nhat.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1 = findViewById(R.id.lv1);
        mylist = new ArrayList<>();
        // Truyền this (context của MainActivity) vào MyArrayAdapter
        myadapter = new MyArrayAdapter(this, R.layout.layout_listview, mylist);
        lv1.setAdapter(myadapter);

        LoadArticlesTask task = new LoadArticlesTask();
        task.execute(URL_RSS);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mylist != null && position < mylist.size()) {
                    ListArticle selectedArticle = mylist.get(position);
                    String articleLink = selectedArticle.getLink();
                    if (articleLink != null && !articleLink.isEmpty()) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleLink));
                            startActivity(browserIntent);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Không thể mở link", Toast.LENGTH_SHORT).show();
                            Log.e("OpenLink", "Lỗi mở link: " + e.getMessage());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Không tìm thấy link bài báo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    class LoadArticlesTask extends AsyncTask<String, Void, ArrayList<ListArticle>> {
        // Không cần Bitmap mIcon_val ở đây nữa, vì nó sẽ được set cho từng ListArticle

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myadapter.clear(); // Xóa dữ liệu cũ trên UI
            Toast.makeText(MainActivity.this, "Đang tải tin tức...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<ListArticle> doInBackground(String... urls) {
            ArrayList<ListArticle> articles = new ArrayList<>();
            String xmlString;
            XMLParser xmlParser = new XMLParser(); // Giả sử lớp XMLParser đã được tạo đúng
            xmlString = xmlParser.getXmlFromUrl(urls[0]);

            if (xmlString == null) {
                Log.e("LoadArticlesTask", "Không thể lấy XML từ URL.");
                return articles; // Trả về danh sách rỗng nếu không lấy được XML
            }

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true); // Quan trọng để xử lý CDATA và namespace (nếu có)
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new StringReader(xmlString));

                int eventType = parser.getEventType();
                ListArticle currentArticle = null;
                String textContent = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tagName != null && tagName.equalsIgnoreCase("item")) {
                                currentArticle = new ListArticle(null, "", "", "");
                            }
                            textContent = ""; // Reset textContent cho mỗi thẻ mới
                            break;
                        case XmlPullParser.TEXT:
                            if (parser.getText() != null) {
                                // Nối các phần text lại nếu thẻ có nhiều đoạn text (ví dụ CDATA bị ngắt)
                                textContent += parser.getText().trim();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (currentArticle != null && tagName != null) {
                                if (tagName.equalsIgnoreCase("title")) {
                                    currentArticle.setTitle(textContent);
                                } else if (tagName.equalsIgnoreCase("link")) {
                                    currentArticle.setLink(textContent);
                                } else if (tagName.equalsIgnoreCase("description")) {
                                    // textContent lúc này là nội dung của <description>
                                    Pattern patternImg = Pattern.compile("<img\\s+src\\s*=\\s*\"([^\"]+)\"");
                                    Matcher matcherImg = patternImg.matcher(textContent);
                                    String imageUrl = null;
                                    if (matcherImg.find()) {
                                        imageUrl = matcherImg.group(1);
                                    }

                                    String summary;
                                    int brIndex = textContent.lastIndexOf("</br>");
                                    if (brIndex != -1 && brIndex + 5 < textContent.length()) {
                                        summary = textContent.substring(brIndex + 5).trim();
                                    } else {
                                        // Nếu không có </br>, thử loại bỏ các thẻ HTML đơn giản
                                        summary = textContent.replaceAll("<[^>]*>", "").trim();
                                    }
                                    currentArticle.setInfo(summary);

                                    // Tải ảnh
                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        try {
                                            URL imgUrl = new URL(imageUrl);
                                            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                                            connection.setConnectTimeout(10000); // 10 giây
                                            connection.setReadTimeout(15000);   // 15 giây
                                            connection.setDoInput(true);
                                            connection.connect();
                                            InputStream input = connection.getInputStream();
                                            Bitmap articleBitmap = BitmapFactory.decodeStream(input);
                                            currentArticle.setImg(articleBitmap);
                                            input.close();
                                            connection.disconnect();
                                        } catch (IOException e) {
                                            Log.e("LoadImage", "Lỗi tải ảnh: " + imageUrl + " - " + e.getMessage());
                                            // Optional: Set a placeholder image if download fails
                                            // currentArticle.setImg(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_image));
                                        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra khi tải ảnh
                                            Log.e("LoadImage", "Lỗi không xác định khi tải ảnh: " + imageUrl + " - " + e.getMessage());
                                        }
                                    }
                                } else if (tagName.equalsIgnoreCase("item")) {
                                    // Chỉ thêm vào danh sách nếu có tiêu đề (để tránh item rỗng)
                                    if (currentArticle.getTitle() != null && !currentArticle.getTitle().isEmpty()) {
                                        articles.add(currentArticle);
                                    }
                                    currentArticle = null; // Reset cho item tiếp theo
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                Log.e("LoadArticlesTask", "XmlPullParserException: " + e.getMessage(), e);
            } catch (IOException e) {
                Log.e("LoadArticlesTask", "IOException: " + e.getMessage(), e);
            } catch (Exception e) { // Bắt các lỗi chung khác
                Log.e("LoadArticlesTask", "Lỗi không xác định trong doInBackground: " + e.getMessage(), e);
            }
            return articles;
        }

        @Override
        protected void onPostExecute(ArrayList<ListArticle> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                // Không cần mylist.clear() và mylist.addAll() nữa vì MyArrayAdapter
                // sẽ tự động xử lý khi dữ liệu nguồn của nó thay đổi nếu bạn truyền
                // mylist (là một ArrayList) vào constructor của nó và sau đó cập nhật mylist này.
                // Tuy nhiên, để chắc chắn UI cập nhật đúng, việc clear và addAll là an toàn.
                mylist.clear();
                mylist.addAll(result);
                myadapter.notifyDataSetChanged(); // Quan trọng để ListView vẽ lại
                Toast.makeText(MainActivity.this, "Tải tin tức thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Không thể tải tin tức hoặc không có tin tức mới.", Toast.LENGTH_LONG).show();
                Log.d("LoadArticlesTask", "Kết quả rỗng hoặc null sau khi parse.");
            }
        }
    }
}