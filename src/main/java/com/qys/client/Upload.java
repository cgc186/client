package com.qys.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Upload {
    public static String uploadFile(String url, String filePath) {
        try {
            File file = new File(filePath);
            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);

            // 文件边界
            String Boundary = UUID.randomUUID().toString();

            // 1.开启Http连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setDoOutput(true); // 允许输出

            // 2.Http请求行/头
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);

            // 3.Http请求体
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeUTF("--" + Boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n"
                    + "Content-Type: application/octet-stream; charset=utf-8" + "\r\n\r\n");
            InputStream in = new FileInputStream(file);
            byte[] b = new byte[1024];
            int l = 0;
            while ((l = in.read(b)) != -1) out.write(b, 0, l); // 写入文件
            out.writeUTF("\r\n--" + Boundary + "--\r\n");
            out.flush();
            out.close();
            in.close();

            // 4.Http响应
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            if ((line = bf.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // 上传文件测试
        String str = uploadFile("http://localhost:8080/file/upload", "./p.png");
        System.out.println(str);
    }

}
