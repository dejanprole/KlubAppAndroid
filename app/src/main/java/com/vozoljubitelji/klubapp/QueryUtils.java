package com.vozoljubitelji.klubapp;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macosx on 9/26/17.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName(); //for logging

    //private QueryUtils() {} //default constructor
    public QueryUtils() {} //default constructor



    public static List<KlubPost> fetchNewData(String postUrl) {

        Log.i(LOG_TAG, "Fetch post data");

        URL url = createUrl(postUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<KlubPost> klubPosts = extractResultsFromJson(jsonResponse);

        //Log.i("LISTKLUBPOST", klubPosts.toString());

        return klubPosts;

    }

    //private static URL createUrl (String stringUrl) {
    public static URL createUrl (String stringUrl) {

            URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        return url;
    }

    //private static String makeHttpRequest(URL url) throws IOException {

    public static String makeHttpRequest(URL url) throws IOException {

            String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("ERRORRESPONSEJSON", "Error response " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("PROBLEMWITHJSONRESULTS", "Problem with json results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {

        //private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<KlubPost> extractResultsFromJson(String postJson) {

        if (TextUtils.isEmpty(postJson)){
            return null;
        }

        List<KlubPost> klubPosts = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(postJson);
            //JSONObject results = baseJsonResponse.getJSONObject("");
            JSONArray postArray = obj.getJSONArray("all_posts");

            for (int i=0; i<postArray.length(); i++) {
                JSONObject post = postArray.getJSONObject(i);
                String referenceId = post.getString("reference_id");
                String category = post.getString("category");
                String author = post.getString("user");
                String date = post.getString("date");
                String title = post.getString("title");
                String text = post.getString("post_text");
                String imageUrl = post.getString("gallery_resized_first_image");
                //String balance = post.getString("balance");
                //String year = post.getString("year");
                //String income = post.getString("income");
                String url = post.getString("post_text"); //I do not need this in this moment

                List<String> imagesUrl = new ArrayList<>();


                if (post.has("images")) {
                JSONArray images = post.getJSONArray("images");
                for (int j=0; j<images.length(); j++){

                    JSONObject image = images.getJSONObject(j);
                    String image_Url = image.getString("image_url");
                    imagesUrl.add(image_Url);

                    //Log.i("QU_IMAGES", image_Url.toString());

                    //images_Url = image.getString("image_url");

                }
                    //Log.i("QU_IMAGES", imagesUrl.toString());



                } else {
                    //Log.i("QUERYUTILS", "POST WITH NO IMAGES");
                }


                KlubPost klubPost = new KlubPost(category, date, title, text, author, referenceId, imageUrl, url, imagesUrl);
                klubPosts.add(klubPost);


                //Log.i("QUERY UTILS GETMIMAGES", klubPost.getmImagesUrl().toString());


            }



        } catch (JSONException e) {
            Log.e("Query Utils", "Problem sa obradom rezultata", e);
        }
        return klubPosts;
    }


}
