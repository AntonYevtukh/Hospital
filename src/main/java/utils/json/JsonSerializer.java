package utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonSerializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final JsonParser PARSER = new JsonParser();

    private JsonSerializer() {

    }

    public static String serialize(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T deserialize(Class<T> classToken, String jsonString) {
        T object = GSON.fromJson(jsonString, classToken);
        return object;
    }

    public static String getFieldAsJsonString(String jsonString, String fieldName) {
        return PARSER.parse(jsonString).getAsJsonObject().get(fieldName).toString();
    }

    public static int getFieldAsInt(String jsonString, String fieldName) {
        return PARSER.parse(jsonString).getAsJsonObject().get(fieldName).getAsInt();
    }

    public static long getFieldAsLong(String jsonString, String fieldName) {
        return PARSER.parse(jsonString).getAsJsonObject().get(fieldName).getAsLong();
    }
}
