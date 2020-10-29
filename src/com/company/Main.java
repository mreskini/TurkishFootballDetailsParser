package com.company;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
//Coded By Mohammad Eskini On 15 October 2020 at 05:26 PM
class Main {
    //using this function to get the details of the input.

    public static JSONObject getMatchDetails(Integer matchID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"coverageId\": \"d908a79f-b6e8-40e2-a98c-110b30a75676\",\r\n    \"sportId\": 1,\r\n    \"matchId\": " + matchID + ",\r\n    \"options\": {\r\n        \"lang\": \"tr-TR\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://brdg-f73c99e4-d6f8-4742-8124-f6265485c26e.azureedge.net/livescore/matchdetails")
                .method("POST", body)
                .addHeader("Origin", "https://www.iddaa.com")
                .addHeader("Referer", "https://www.iddaa.com")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject output = new JSONObject(response.body().string());
        return output;
    }
    //using this to get the whole list of today's games.
    public static JSONObject getMatchesList() throws JSONException, IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"coverageId\":\"d908a79f-b6e8-40e2-a98c-110b30a75676\",\"options\":{\"lang\":\"tr-TR\",\"day\":\"10/14/2020\",\"betCode\":true,\"sportId\":1,\"origin\":\"iddaa.com\",\"timeZone\":3.5}}");
        Request request = new Request.Builder()
                .url("https://brdg-f73c99e4-d6f8-4742-8124-f6265485c26e.azureedge.net/livescore/matchlist")
                .method("POST", body)
                .addHeader("Origin", "https://www.iddaa.com")
                .addHeader("Referer", "https://www.iddaa.com")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    public static void main(String[] args) throws IOException, JSONException {
        JSONObject matchListResponse = getMatchesList();//getting the whole list
        JSONArray list = new JSONArray(matchListResponse.get("initialData").toString());//optimising the response
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = new JSONObject(//storing each match in a JSONObject called item
                    new JSONArray(
                            new JSONObject(
                                    list
                                            .get(i)
                                            .toString())
                                    .get("matches")
                                    .toString())
                            .get(0)
                            .toString());
            item.put("details" , getMatchDetails(Integer.parseInt(item.get("id").toString())));
            System.out.println(item.toString(4));
        }
    }
}
