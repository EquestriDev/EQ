package com.equestriworlds.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UUIDFetcher {
    private static UUIDFetcher _instance = new UUIDFetcher();
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private final JSONParser _jsonParser = new JSONParser();

    public UUID getPlayerUUID(String name) {
        UUID uuid = null;
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.add(name);
        try {
            HttpURLConnection connection = UUIDFetcher.createConnection();
            String body = JSONArray.toJSONString(nameList.subList(0, Math.min(100, 1)));
            UUIDFetcher.writeBody(connection, body);
            JSONArray array = (JSONArray)this._jsonParser.parse((Reader)new InputStreamReader(connection.getInputStream()));
            for (Object profile : array) {
                JSONObject jsonProfile = (JSONObject)profile;
                String id = (String)jsonProfile.get((Object)"id");
                uuid = UUIDFetcher.getUUID(id);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return uuid;
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    private static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID fromBytes(byte[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        long mostSignificant = byteBuffer.getLong();
        long leastSignificant = byteBuffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }

    public static UUID getUUIDOf(String name) {
        if (_instance == null) {
            _instance = new UUIDFetcher();
        }
        return _instance.getPlayerUUID(name);
    }
}
