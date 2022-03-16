package org.gaohui.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gaohui.message.entity.MessageInfo;
import org.gaohui.message.vo.ReturnList;

import java.util.List;
import java.util.Set;

/**
 * @author gaohui  2022/02/22
 */
public interface MessageInfoService extends IService<MessageInfo> {
    /**
     * 获取组织下对应的用户和用户对应的信息
     * @param orgid
     * @return
     */
    Set<ReturnList> selectOneOrgUserInfoMessageInfoList(String orgid);
}
