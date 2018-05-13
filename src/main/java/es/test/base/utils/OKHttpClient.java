package es.test.base.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuxuegang on 2017/5/5.
 */
public final class OKHttpClient {

    private static final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OKHttpClient() {
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final Request.Builder requestBuilder = new Request.Builder();
        private RequestBody requestBody;
        private RequestMethods method;
        private Call call;
        private String url;

        private Builder() {

        }

        public Builder buildFormBody(final Map<String, String> params) {
            final FormBody.Builder builder = new FormBody.Builder();
            if (params == null || params.isEmpty()) {
                this.requestBody = builder.build();
                return this;
            }
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            this.requestBody = builder.build();
            return this;
        }

        public Builder buildFormBody(final List<FormParam> params) {
            final FormBody.Builder builder = new FormBody.Builder();
            if (params == null || params.isEmpty()) {
                this.requestBody = builder.build();
                return this;
            }
            for (final FormParam entry : params) {
                builder.add(entry.getName(), entry.getText());
            }
            this.requestBody = builder.build();
            return this;
        }

        public Builder buildJsonBody(final String json) {
            this.requestBody = RequestBody.create(JSON, json);
            return this;
        }

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public Builder url(final String url, final Map<String, String> params) {
            if (params == null || params.isEmpty()) {
                return this.url(url);
            }
            final StringBuilder urlBuilder = new StringBuilder();
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            if (url.contains("?")) {
                urlBuilder.insert(0, url);
            } else {
                urlBuilder.deleteCharAt(0).insert(0, String.format("%s?", url));
            }
            this.url = urlBuilder.toString();
            return this;
        }

        public Builder url(final String url, final String params) {
            if (StringUtils.isBlank(params)) {
                return this.url(url);
            }
            final StringBuilder urlBuilder = new StringBuilder("&").append(params);
            if (url.contains("?")) {
                urlBuilder.insert(0, url);
            } else {
                urlBuilder.deleteCharAt(0).insert(0, String.format("%s?", url));
            }
            this.url = urlBuilder.toString();
            return this;
        }

        public Builder addHeader(final String name, final String value) {
            this.requestBuilder.addHeader(name, value);
            return this;
        }

        public Builder post() {
            this.method = RequestMethods.POST;
            this.call = client.newCall(this.requestBuilder.url(this.url).post(this.requestBody).build());
            return this;
        }

        public Builder get() {
            this.method = RequestMethods.GET;
            this.requestBuilder.url(this.url);
            this.call = client.newCall(this.requestBuilder.url(this.url).build());
            return this;
        }

        public String execute() throws IOException {
            final Response response = this.call.execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code :" + response);
            }
        }

        public void enqueue(final Callback callback) {
            this.call.enqueue(callback);
        }

        public void enqueue() {
            this.call.enqueue(new Callback() {
                public void onFailure(final Call call, final IOException e) {

                }

                public void onResponse(final Call call, final Response response) throws IOException {

                }
            });
        }
    }

    private enum RequestMethods {
        POST,
        GET
    }

    public static class FormParam {
        private String name;
        private String text;

        public FormParam() {
        }

        public FormParam(final String name, final String text) {
            this.name = name;
            this.text = text;
        }

        public String getName() {
            return this.name;
        }

        public String getText() {
            return this.text;
        }
    }
}
