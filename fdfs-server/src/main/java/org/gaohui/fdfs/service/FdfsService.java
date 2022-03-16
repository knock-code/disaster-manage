package org.gaohui.fdfs.service;

import org.gaohui.fdfs.entity.Fdfs;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaohui
 * @since 2021-10-22
 */
public interface FdfsService extends IService<Fdfs> {

    String upload(MultipartFile file);

    Boolean delete(String filePath);
}
