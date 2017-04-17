package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 网路连接专门类
 * Created by lfs-ios on 2017/4/17.
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    //api_key
    private static final String API_KEY = "207adffbfef009bc81e24027db811372";
    //查询最近上传的100张图片
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    //输入关键字查询
    private static final String SEARCH_METHOD  = "flickr.photos.search";



    //https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=207adffbfef009bc81e24027db811372&format=json&nojsoncallback=1&extras=url_s

    private static final Uri ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            //.appendQueryParameter("method","flickr.photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback" ,"1")
            .appendQueryParameter("extras", "url_s")
            .build();


    /**
     * 处理网络请求，返回字节数组，
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {


        URL url = new URL(urlSpec);

        Log.i(TAG, "URL = " + urlSpec);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            InputStream in = connection.getInputStream();//从网络获取获取输入流

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                throw new IOException(connection.getResponseMessage() +
                ": with " +
                urlSpec);
            }

            int bytesRead = 0;

            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {

                out.write(buffer, 0, bytesRead);
            }
            out.close();

            Log.i(TAG, String.valueOf(out));
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }


    /**
     * 字符数组转换成String
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {

        //String，点用getUrlBytes函数，将out.toByteArray()转换成String返回
        return new String(getUrlBytes(urlSpec));
    }


    /**
     * 下载，返回加载后的结果 列表
     * @return
     */
    public List<GalleryItem> fetchRecentPhotos() {

        String url = buildUrl(FETCH_RECENTS_METHOD, null);

        return downloadGalleryItems(url);
    }

    /**
     * 搜索方法
     * @param query
     * @return
     */
    public List<GalleryItem> searchPhotos(String query) {

        String url = buildUrl(SEARCH_METHOD, query);

        return downloadGalleryItems(url);
    }


    /**
     * @return 返回解析json后，并将数据保存在GalleryItem对象列表中
     */
    private List<GalleryItem> downloadGalleryItems(String url) {

        List<GalleryItem> items = new ArrayList<>();

        try{
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received json: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            //调用函数parseItems，
            parseItems(items, jsonBody);
        }catch (IOException e) {

            Log.e(TAG, "Failed to fetch items", e);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

    /**
     * 创建url的辅助方法
     * @param method
     * @param query
     * @return
     */
    private String buildUrl(String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method);

        if (method.equals(SEARCH_METHOD)) {

            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }


    /**
     * @param items  GalleryItem对象列表
     * @param jsonBody   与原生JSON数据对应的对象树
     * @throws IOException
     * @throws JSONException
     */
    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");

        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();

            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")) {

                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            item.setOwner(photoJsonObject.getString("owner"));
            items.add(item);
        }
    }
}
