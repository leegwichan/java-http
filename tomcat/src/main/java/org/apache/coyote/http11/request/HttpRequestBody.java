package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpRequestBody {

    private static final String EMPTY_BODY = "";

    private final String body;

    public HttpRequestBody(String body) {
        this.body = body;
    }

    public HttpRequestBody() {
        this.body = EMPTY_BODY;
    }

    public Optional<String> getParameter(String key) {
        return Optional.ofNullable(parsedFormData(body).get(key));
    }

    private Map<String, String> parsedFormData(String body) {
        Map<String, String> formData = new HashMap<>();
        for (String data : body.split("&")) {
            String[] dataComponent = data.split("=");
            formData.put(dataComponent[0], dataComponent[1]);
        }
        return formData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpRequestBody that = (HttpRequestBody) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }

    @Override
    public String toString() {
        return "HttpRequestBody{" +
                "body='" + body + '\'' +
                '}';
    }
}
