package com.qys.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetFileData {

    public static String getFileData(String url, String uuid) {
        //拼接链接
        String httpUrl = url + "?uuid=" + uuid;
        //链接
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            //创建连接
            HttpURLConnection conn = (HttpURLConnection) new URL(httpUrl).openConnection();
            //设置请求方式
            conn.setRequestMethod("GET");
            //设置连接超时时间
            conn.setReadTimeout(15000);
            //开始连接
            conn.connect();
            //获取响应数据
            if (conn.getResponseCode() == 200) {
                //获取返回的数据
                is = conn.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }
            if (br != null) {
                br.close();
            }
            if (null != is) {
                is.close();
            }
            //关闭远程连接
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String s = GetFileData.getFileData("http://localhost:8080/file/getFileData", "815cc9205eec4a3dbeba4f2daf7acfdb");
        System.out.println(s);
    }
}
