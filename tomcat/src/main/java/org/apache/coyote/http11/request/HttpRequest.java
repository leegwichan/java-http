package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import javax.annotation.Nullable;
import org.apache.catalina.cookie.Cookie;
import org.apache.catalina.session.Session;

public class HttpRequest {

    private final HttpRequestStartLine startLine;
    private final HttpRequestHeaders headers;
    private final String body;

    public HttpRequest(HttpRequestStartLine startLine, HttpRequestHeaders headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpRequestBuilder builder() {
        return new HttpRequestBuilder();
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public String getPath() {
        return startLine.getPath();
    }

    public Optional<String> getQueryParameter(String key) {
        return startLine.getQueryParameter(key);
    }

    public Map<String, String> getQueryParameters() {
        return startLine.getQueryParameters();
    }

    public boolean matchHeader(String key, String expectedValue) {
        return headers.getHeader(key)
                .map(value -> value.equals(expectedValue))
                .orElse(false);
    }

    @Nullable
    public String getContentType() {
        return headers.getContentType();
    }

    public Cookie getCookie() {
        return headers.getCookie();
    }

    public Optional<Session> getSession() {
        return getCookie().getSession();
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        HttpRequest request = (HttpRequest) object;
        return Objects.equals(startLine, request.startLine)
                && Objects.equals(headers, request.headers)
                && Objects.equals(body, request.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startLine, headers, body);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HttpRequest.class.getSimpleName() + "[", "]")
                .add("startLine=" + startLine)
                .add("headers=" + headers)
                .add("body='" + body + "'")
                .toString();
    }

    public static class HttpRequestBuilder {

        private HttpMethod method;
        private HttpUrl path;
        private String httpVersion;
        private Map<String, String> headers;
        private String body;

        public HttpRequestBuilder() {
            this.headers = new HashMap<>();
        }

        public HttpRequestBuilder method(String method) {
            this.method = HttpMethod.from(method);
            return this;
        }

        public HttpRequestBuilder path(String path) {
            this.path = new HttpUrl(path);
            return this;
        }

        public HttpRequestBuilder httpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public HttpRequestBuilder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public HttpRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            HttpRequestStartLine startLine = new HttpRequestStartLine(method, path, httpVersion);
            HttpRequestHeaders headers = new HttpRequestHeaders(this.headers);
            return new HttpRequest(startLine, headers, body);
        }
    }
}
