package org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Test;
import support.StubSocket;

class Http11ProcessorTest {

    @Test
    void homePageTest() {
        // given
        StubSocket socket = new StubSocket();
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        List<String> expected = List.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: 12",
                "",
                "Hello world!");

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void indexPageTest() throws IOException {
        // given
        String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        String response = readFile("static/index.html");
        List<String> expected = List.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: " + response.getBytes().length,
                "",
                response);

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void cssTest() throws IOException {
        // given
        String httpRequest = String.join("\r\n",
                "GET /test.css HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        String response = readFile("static/test.css");
        List<String> expected = List.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/css;charset=utf-8",
                "Content-Length: " + response.getBytes().length,
                "",
                response);

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void loginPageTest() throws IOException {
        // given
        String httpRequest = String.join("\r\n",
                "GET /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        String response = readFile("static/login.html");
        List<String> expected = List.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: " + response.getBytes().length,
                "",
                response);

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void loginTest() {
        // given
        String httpRequest = String.join("\r\n",
                "POST /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 30",
                "",
                "account=gugu&password=password");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        List<String> expected = List.of(
                "HTTP/1.1 302 Found",
                "Content-Length: 0",
                "Set-Cookie: ",
                "");

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void registerPageTest() throws IOException {
        // given
        String httpRequest = String.join("\r\n",
                "GET /register HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        String response = readFile("static/register.html");
        List<String> expected = List.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: " + response.getBytes().length,
                "",
                response);

        assertThat(socket.output()).contains(expected);
    }

    @Test
    void registerTest() throws IOException{
        // given
        String httpRequest = String.join("\r\n",
                "POST /register HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 80",
                "",
                "account=taka&password=password&email=takan1%40woowahan.com");
        StubSocket socket = new StubSocket(httpRequest);
        Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        List<String> expected = List.of(
                "HTTP/1.1 302 Found",
                "Content-Length: 0",
                "Set-Cookie: ",
                "Location: /index.html",
                "");

        assertThat(socket.output()).contains(expected);
    }

    private String readFile(String resourcePath) throws IOException {
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        return new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
    }
}
