package org.gaohui.fdfs.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.gaohui.common.annotation.UserLoginToken;
import org.gaohui.common.api.fdfs.FdfsApi;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.common.response.ResultInfo;
import org.gaohui.fdfs.service.FdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaohui
 * @since 2021-10-22
 */
@RestController
@RequestMapping("/fdfs-system")
@Api(tags = {"文件管理系统"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FdfsController implements FdfsApi {

    private final FdfsService fdfsService;


    @Override
    @ApiOperation(value = "上传文件", tags = {"上传文件接口"})
    @UserLoginToken
    public ResultInfo upload(MultipartFile[] files) {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            String upload = fdfsService.upload(file);
            result.add(upload);
        }
        return ResultInfo.success("上传成功!", CodeEnum.SUCCESS, result);
    }

    @Override
    @ApiOperation(value = "下载文件", tags = {"下载文件接口"})
    @UserLoginToken
    public void download(String filePath) throws Exception {

    }


    @ApiOperation(value = "删除文件", tags = {"删除文件接口"})
    @Override
    @UserLoginToken
    public ResultInfo delete(String filePath) {
        return null;
    }

    @ApiOperation(value = "微服务删除文件", tags = {"微服务删除文件接口"})
    @Override
    @UserLoginToken
    public ResultInfo remove(List<String> urlPath) {
        Boolean flag =  fdfsService.delete(urlPath.get(0));
        return flag ? ResultInfo.success("删除成功!", CodeEnum.SUCCESS) : ResultInfo.error("删除失败!", CodeEnum.ERROR);
    }
}

