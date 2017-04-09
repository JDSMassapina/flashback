/*
 * Copyright (c) LinkedIn Corporation. All rights reserved. Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */

package com.linkedin.flashback.netty.builder;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URISyntaxException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author shfeng
 */
public class RecordedHttpMessageBuilderTest {
  @Test
  public void testAddHeaders()
      throws Exception {
    HttpRequest nettyRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "www.google.com");
    nettyRequest.headers().add("key1", "value1");
    nettyRequest.headers().add("key1", "value2");
    nettyRequest.headers().add("key2", "value1");
    RecordedHttpRequestBuilder recordedHttpRequestBuilder = new RecordedHttpRequestBuilder(nettyRequest);
    Map<String, String> headers = recordedHttpRequestBuilder.getHeaders();
    Assert.assertEquals(headers.size(), 2);
  }

  @Test
  public void testGetHeaders()
      throws Exception {
    HttpRequest nettyRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "www.google.com");
    nettyRequest.headers().add("key1", "value1");
    nettyRequest.headers().add("key1", "value2");
    nettyRequest.headers().add("key2", "value1");
    RecordedHttpRequestBuilder recordedHttpRequestBuilder = new RecordedHttpRequestBuilder(nettyRequest);
    Map<String, String> headers = recordedHttpRequestBuilder.getHeaders();
    Assert.assertEquals(headers.size(), 2);
    Assert.assertEquals(headers.get("key1"), "value1, value2");
    Assert.assertEquals(headers.get("key2"), "value1");
  }

  @Test
  public void testSetCookieHeader() throws URISyntaxException {
    HttpRequest nettyRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "www.abc.com");
    nettyRequest.headers()
        .add("Set-Cookie",
            "ABC=\\\"R:0|g:fcaa967e-asdfa-484a-8a5e-asdfa\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Set-Cookie",
            "ABC=\\\"R:0|g:fcaa967e-asdfasdf-484a-8a5e-asdf|n:asdfasdfasd-37ca-42cf-a909-95e0dd19e334\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Set-Cookie",
            "ABC=\\\"R:0|i:138507\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Set-Cookie",
            "ABC=\\\"R:0|i:138507|e:42\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Set-Cookie", "guestidc=0d28bda6-5d42-4ee9-bd1e-asdasda; Domain=asdafsdfasdfasdfa.com; Path=/");

    RecordedHttpRequestBuilder recordedHttpRequestBuilder = new RecordedHttpRequestBuilder(nettyRequest);
    Map<String, String> headers = recordedHttpRequestBuilder.getHeaders();

    Assert.assertEquals(headers.size(), 1);
    Assert.assertEquals(headers.get("Set-Cookie"),
        "QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmEtNDg0YS04YTVlLWFzZGZhXCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmFzZGYtNDg0YS04YTVlLWFzZGZ8bjphc2RmYXNkZmFzZC0zN2NhLTQyY2YtYTkwOS05NWUwZGQxOWUzMzRcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, QUJDPVwiUjowfGk6MTM4NTA3XCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGk6MTM4NTA3fGU6NDJcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, Z3Vlc3RpZGM9MGQyOGJkYTYtNWQ0Mi00ZWU5LWJkMWUtYXNkYXNkYTsgRG9tYWluPWFzZGFmc2RmYXNkZmFzZGZhLmNvbTsgUGF0aD0v");
  }

  @Test
  public void testNonSetCookieHeader() throws URISyntaxException {
    HttpRequest nettyRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "www.abc.com");
    nettyRequest.headers()
        .add("Not-Set-Cookie",
            "ABC=\\\"R:0|g:fcaa967e-asdfa-484a-8a5e-asdfa\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Not-Set-Cookie",
            "ABC=\\\"R:0|g:fcaa967e-asdfasdf-484a-8a5e-asdf|n:asdfasdfasd-37ca-42cf-a909-95e0dd19e334\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Not-Set-Cookie",
            "ABC=\\\"R:0|i:138507\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Not-Set-Cookie",
            "ABC=\\\"R:0|i:138507|e:42\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/");
    nettyRequest.headers()
        .add("Not-Set-Cookie", "guestidc=0d28bda6-5d42-4ee9-bd1e-asdasda; Domain=asdafsdfasdfasdfa.com; Path=/");

    RecordedHttpRequestBuilder recordedHttpRequestBuilder = new RecordedHttpRequestBuilder(nettyRequest);
    Map<String, String> headers = recordedHttpRequestBuilder.getHeaders();

    Assert.assertEquals(headers.size(), 1);
    Assert.assertNotEquals(headers.get("Not-Set-Cookie"),
        "QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmEtNDg0YS04YTVlLWFzZGZhXCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmFzZGYtNDg0YS04YTVlLWFzZGZ8bjphc2RmYXNkZmFzZC0zN2NhLTQyY2YtYTkwOS05NWUwZGQxOWUzMzRcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, QUJDPVwiUjowfGk6MTM4NTA3XCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGk6MTM4NTA3fGU6NDJcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, Z3Vlc3RpZGM9MGQyOGJkYTYtNWQ0Mi00ZWU5LWJkMWUtYXNkYXNkYTsgRG9tYWluPWFzZGFmc2RmYXNkZmFzZGZhLmNvbTsgUGF0aD0v");
  }
}
