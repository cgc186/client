package com.qys.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile {

    public static File downloadFile(String url, String uuid, String downloadDir) {
        String urlPath = url + "?uuid=" + uuid;

        File file = null;
        try {
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
            // 设定请求的方法，默认是GET
            conn.setRequestMethod("POST");
            // 设置字符编码
            conn.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            conn.connect();

            // 文件名
            String filePathUrl = conn.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf("=") + 1);

            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());

            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            bin.close();
            out.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // 下载文件测试
        downloadFile("http://localhost:8080/file/download", "815cc9205eec4a3dbeba4f2daf7acfdb", "./save");
    }
}