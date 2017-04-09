/*
 * Copyright (c) LinkedIn Corporation. All rights reserved. Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */

package com.linkedin.flashback.netty.mapper;

import com.linkedin.flashback.serializable.RecordedHttpResponse;
import com.linkedin.flashback.serializable.RecordedStringHttpBody;
import io.netty.handler.codec.http.FullHttpResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author shfeng
 */
public class NettyHttpResponseMapperTest {
  @Test
  public void testFromWithoutBody()
      throws Exception {
    Map<String, String> headers = new HashMap<>();
    headers.put("key1", "value1");
    headers.put("key2", "value2,value3,value4");
    int status = 200;
    RecordedHttpResponse recordedHttpResponse = new RecordedHttpResponse(status, headers, null);
    FullHttpResponse fullHttpResponse = NettyHttpResponseMapper.from(recordedHttpResponse);
    Assert.assertEquals(fullHttpResponse.getStatus().code(), status);
    Assert.assertEquals(fullHttpResponse.headers().get("key1"), "value1");
    List<String> headrValues = fullHttpResponse.headers().getAll("key2");
    Assert.assertEquals(headrValues.size(), 3);
    Assert.assertTrue(headrValues.contains("value2"));
    Assert.assertTrue(headrValues.contains("value3"));
    Assert.assertTrue(headrValues.contains("value4"));
  }

  @Test
  public void testFromWithBody()
      throws Exception {
    Map<String, String> headers = new HashMap<>();
    headers.put("key1", "value1");
    headers.put("key2", "value2,value3,value4");
    int status = 200;
    String str = "Hello world";
    RecordedStringHttpBody recordedStringHttpBody = new RecordedStringHttpBody(str);
    RecordedHttpResponse recordedHttpResponse = new RecordedHttpResponse(status, headers, recordedStringHttpBody);
    FullHttpResponse fullHttpResponse = NettyHttpResponseMapper.from(recordedHttpResponse);
    Assert.assertEquals(fullHttpResponse.getStatus().code(), status);
    Assert.assertEquals(fullHttpResponse.headers().get("key1"), "value1");
    List<String> headrValues = fullHttpResponse.headers().getAll("key2");
    Assert.assertEquals(headrValues.size(), 3);
    Assert.assertTrue(headrValues.contains("value2"));
    Assert.assertTrue(headrValues.contains("value3"));
    Assert.assertTrue(headrValues.contains("value4"));
    Assert.assertEquals(fullHttpResponse.content().array(), str.getBytes());
  }

  @Test
  public void testCookieHeader() throws URISyntaxException, IOException {
    Map<String, String> headers = new HashMap<>();
    headers.put("key1", "value1");
    headers.put("Set-Cookie",
        "QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmEtNDg0YS04YTVlLWFzZGZhXCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmFzZGYtNDg0YS04YTVlLWFzZGZ8bjphc2RmYXNkZmFzZC0zN2NhLTQyY2YtYTkwOS05NWUwZGQxOWUzMzRcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, QUJDPVwiUjowfGk6MTM4NTA3XCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGk6MTM4NTA3fGU6NDJcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, Z3Vlc3RpZGM9MGQyOGJkYTYtNWQ0Mi00ZWU5LWJkMWUtYXNkYXNkYTsgRG9tYWluPWFzZGFmc2RmYXNkZmFzZGZhLmNvbTsgUGF0aD0v");
    int status = 200;
    String str = "Hello world";
    RecordedStringHttpBody recordedStringHttpBody = new RecordedStringHttpBody(str);

    RecordedHttpResponse recordedHttpResponse = new RecordedHttpResponse(status, headers, recordedStringHttpBody);
    FullHttpResponse fullHttpResponse = NettyHttpResponseMapper.from(recordedHttpResponse);
    Assert.assertEquals(fullHttpResponse.getStatus().code(), status);
    Assert.assertEquals(fullHttpResponse.headers().get("key1"), "value1");
    List<String> headrValues = fullHttpResponse.headers().getAll("Set-Cookie");
    Assert.assertEquals(headrValues.size(), 5);
    Assert.assertTrue(headrValues.contains(
        "ABC=\\\"R:0|g:fcaa967e-asdfa-484a-8a5e-asdfa\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertTrue(headrValues.contains(
        "ABC=\\\"R:0|g:fcaa967e-asdfasdf-484a-8a5e-asdf|n:asdfasdfasd-37ca-42cf-a909-95e0dd19e334\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertTrue(headrValues.contains(
        "ABC=\\\"R:0|i:138507\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertTrue(headrValues.contains(
        "ABC=\\\"R:0|i:138507|e:42\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertTrue(
        headrValues.contains("guestidc=0d28bda6-5d42-4ee9-bd1e-asdasda; Domain=asdafsdfasdfasdfa.com; Path=/"));

  }

  @Test
  public void testNonCookieHeader() throws URISyntaxException, IOException {
    Map<String, String> headers = new HashMap<>();
    headers.put("key1", "value1");
    headers.put("Not-Set-Cookie",
        "QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmEtNDg0YS04YTVlLWFzZGZhXCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGc6ZmNhYTk2N2UtYXNkZmFzZGYtNDg0YS04YTVlLWFzZGZ8bjphc2RmYXNkZmFzZC0zN2NhLTQyY2YtYTkwOS05NWUwZGQxOWUzMzRcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, QUJDPVwiUjowfGk6MTM4NTA3XCI7IFZlcnNpb249MTsgTWF4LUFnZT0zMDsgRXhwaXJlcz1UaHUsIDIzLU1hci0yMDE3IDE4OjAxOjIwIEdNVDsgUGF0aD0v, QUJDPVwiUjowfGk6MTM4NTA3fGU6NDJcIjsgVmVyc2lvbj0xOyBNYXgtQWdlPTMwOyBFeHBpcmVzPVRodSwgMjMtTWFyLTIwMTcgMTg6MDE6MjAgR01UOyBQYXRoPS8=, Z3Vlc3RpZGM9MGQyOGJkYTYtNWQ0Mi00ZWU5LWJkMWUtYXNkYXNkYTsgRG9tYWluPWFzZGFmc2RmYXNkZmFzZGZhLmNvbTsgUGF0aD0v");
    int status = 200;
    String str = "Hello world";
    RecordedStringHttpBody recordedStringHttpBody = new RecordedStringHttpBody(str);

    RecordedHttpResponse recordedHttpResponse = new RecordedHttpResponse(status, headers, recordedStringHttpBody);
    FullHttpResponse fullHttpResponse = NettyHttpResponseMapper.from(recordedHttpResponse);
    Assert.assertEquals(fullHttpResponse.getStatus().code(), status);
    Assert.assertEquals(fullHttpResponse.headers().get("key1"), "value1");
    List<String> headrValues = fullHttpResponse.headers().getAll("Not-Set-Cookie");
    Assert.assertEquals(headrValues.size(), 5);
    Assert.assertFalse(headrValues.contains(
        "ABC=\\\"R:0|g:fcaa967e-asdfa-484a-8a5e-asdfa\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertFalse(headrValues.contains(
        "ABC=\\\"R:0|g:fcaa967e-asdfasdf-484a-8a5e-asdf|n:asdfasdfasd-37ca-42cf-a909-95e0dd19e334\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertFalse(headrValues.contains(
        "ABC=\\\"R:0|i:138507\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertFalse(headrValues.contains(
        "ABC=\\\"R:0|i:138507|e:42\\\"; Version=1; Max-Age=30; Expires=Thu, 23-Mar-2017 18:01:20 GMT; Path=/"));
    Assert.assertFalse(
        headrValues.contains("guestidc=0d28bda6-5d42-4ee9-bd1e-asdasda; Domain=asdafsdfasdfasdfa.com; Path=/"));

  }
}
