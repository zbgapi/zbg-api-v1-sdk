package com.dd.api.client.convert;

import com.dd.api.exceptions.ApiException;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.NonNull;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static com.dd.api.constant.ApiConstants.RESPONSE_OK_CODE;
import static com.dd.api.constant.ApiConstants.UTF_8;

/**
 * @author zhangzp
 */
public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        try {
            String response = value.string();
            //先转JsonObject
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            JsonObject codeMsg = jsonObject.getAsJsonObject("resMsg");

            if (codeMsg == null) {
                return null;
            }

            if (!RESPONSE_OK_CODE.equals(codeMsg.get("code").getAsString())) {
                throw new ApiException(codeMsg.get("code").getAsString(), codeMsg.get("message").getAsString());
            }

            Object data = jsonObject.get("datas");
            if (data == null) {
                return null;
            }

            MediaType mediaType = value.contentType();
            Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
            if (charset == null) {
                charset = UTF_8;
            }

            try (ByteArrayInputStream bis = new ByteArrayInputStream(new Gson().toJson(data).getBytes());
                 InputStreamReader reader = new InputStreamReader(bis, charset)) {

                JsonReader jsonReader = gson.newJsonReader(reader);
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        } catch (JsonSyntaxException e) {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }
    }
}
