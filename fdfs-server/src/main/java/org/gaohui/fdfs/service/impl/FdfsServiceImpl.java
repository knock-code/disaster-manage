package org.gaohui.fdfs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.gaohui.common.aspect.RequestCheckAspect;
import org.gaohui.common.pojo.TokenInfo;
import org.gaohui.fdfs.entity.Fdfs;
import org.gaohui.fdfs.mapper.FdfsMapper;
import org.gaohui.fdfs.service.FdfsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gaohui.fdfs.utils.FdfsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaohui
 * @since 2021-10-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FdfsServiceImpl extends ServiceImpl<FdfsMapper, Fdfs> implements FdfsService {

    private final FdfsMapper fdfsMapper;
    private final FdfsUtils fdfsUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile file) {
        TokenInfo tokenInfo = RequestCheckAspect.getTokenInfo();
        String saveUrl = fdfsUtils.uploadFile(file);
        if (StringUtils.isNotBlank(saveUrl)) {
            Fdfs fdfs = new Fdfs();
            fdfs.setCreateby(tokenInfo.getName());
            fdfs.setPath(saveUrl);
            fdfs.setName(file.getOriginalFilename());
            fdfs.setSuffix(getFileType(file.getOriginalFilename()));
            fdfs.setType(file.getContentType());
            fdfs.setSize(BigDecimal.valueOf(file.getSize()));
            this.setFileWidthAndHeight(file, fdfs);
            fdfsMapper.insert(fdfs);
            return saveUrl;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("空的文件路径!");
        }
        System.out.println(filePath);
        System.out.println("高辉");
        LambdaQueryWrapper<Fdfs> fdfsLambdaQueryWrapper = Wrappers.lambdaQuery(Fdfs.class);
        fdfsLambdaQueryWrapper.eq(Fdfs::getPath, filePath);
        Fdfs fdfs = fdfsMapper.selectOne(fdfsLambdaQueryWrapper);
        if (fdfs == null) {
            throw new IllegalArgumentException("无效的文件路径:" + filePath);
        }
        fdfsUtils.deleteFile(filePath);
        int flag = fdfsMapper.delete(fdfsLambdaQueryWrapper);
        return flag > 0?true:false;
    }

    /**
     * 得到文件后缀
     *
     * @param fileName 文件名称
     * @return fileType
     */
    private String getFileType(String fileName) {
        return org.apache.commons.lang3.StringUtils.isBlank(fileName) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    private void setFileWidthAndHeight(MultipartFile multipartFile, Fdfs file) {
        String width = "";
        String height = "";
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
            if (image != null) {
                width = Integer.toString(image.getWidth());
                height = Integer.toString(image.getHeight());
            }
        } catch (IOException e) {
            log.error("FileServiceImpl setFileWidthAndHeight ioexception");
        }
        file.setWidth(width);
        file.setHeight(height);
    }
}
