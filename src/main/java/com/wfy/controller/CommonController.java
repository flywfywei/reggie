package com.wfy.controller;

import com.wfy.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String filePre;

    /**
     * 下载图片至浏览器
     * @param name
     * @return
     */
    @GetMapping("/download")
    public void getImg(String name, HttpServletResponse response){
        BufferedInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //字节输入流
            inputStream = new BufferedInputStream(Files.newInputStream(new File(filePre + name).toPath()));
            //字节输出流
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1){
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            log.error("{}：{}", e.getClass(), e.getMessage());
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("{}：{}", e.getClass(), e.getMessage());
            }
        }
    }

    /**
     * 上传图片至服务器
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> saveImg(MultipartFile file){
        //原生的文件名
        String originalFilename = file.getOriginalFilename();

        //截取文件名后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //UUID处理
        String fileName = UUID.randomUUID() + substring;

        //转存
        if (!StringUtils.hasLength(filePre)) {
            filePre = "G:\\cache\\img\\";
        }
        File dir = new File(filePre);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File dest = new File(filePre + fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
}
