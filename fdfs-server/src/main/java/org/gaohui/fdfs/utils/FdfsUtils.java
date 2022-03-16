package org.gaohui.fdfs.utils;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.gaohui.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FdfsUtils {

    private final FastFileStorageClient storageClient;
    private final FdfsWebServer fdfsWebServer;

    /**
     * 上传文件
     *
     * @param file_buff
     * @param file_ext_name
     * @return
     */
    public String uploadFile(byte[] file_buff, String file_ext_name) {
        StorePath storePath = storageClient.uploadFile(new ByteArrayInputStream(file_buff),
                file_buff.length, file_ext_name, null);
        return getResAccessUrl(storePath);
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    public String uploadFile(MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                String fileName = multipartFile.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    byte[] fileBytes = multipartFile.getBytes();
                    return this.uploadFile(fileBytes, fileType);
                }
            }
        } catch (IOException e) {
            throw new BusinessException("读取文件发生错误");
        }
        return null;
    }

    /**
     * 获得文件地址
     *
     * @param storePath
     * @return
     */
    private String getResAccessUrl(StorePath storePath) {
        return "http://106.14.83.220:8888" + "/" + storePath.getFullPath();
    }

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    public byte[] downloadFile(String url) {
        if (StringUtils.isNotBlank(url)) {
            StorePath storePath = StorePath.parseFromUrl(url);
            return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param url
     */
    public void deleteFile(String url) {
        storageClient.deleteFile(url);
    }
}