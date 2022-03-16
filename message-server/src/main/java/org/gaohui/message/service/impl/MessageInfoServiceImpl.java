package org.gaohui.message.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gaohui.common.aspect.RequestCheckAspect;
import org.gaohui.common.dto.DisasterOrgUserDTO;
import org.gaohui.common.dto.UserDTO;
import org.gaohui.common.pojo.TokenInfo;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.common.response.ResultInfo;
import org.gaohui.message.entity.MessageInfo;
import org.gaohui.message.feign.CmsUserClient;
import org.gaohui.message.feign.DisasterOrgUserClient;
import org.gaohui.message.mapper.MessageInfoMapper;
import org.gaohui.message.service.MessageInfoService;
import org.gaohui.message.vo.MessageInfoVO;
import org.gaohui.message.vo.ReturnList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gaohui  2022/02/22
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements MessageInfoService {

    @Autowired
    private CmsUserClient cmsUserClient;
    @Autowired
    private DisasterOrgUserClient disasterOrgUserClient;

    /**
     * 获取组织下对应的用户和用户对应的信息
     * @param orgid
     * @return
     */
    @Override
    public Set<ReturnList> selectOneOrgUserInfoMessageInfoList(String orgid) {
        Map<String, Object> map = new HashMap<>();
        map.put("eq_orgid", orgid);
        // 获取组织下的全部成员
        ResultInfo resultInfo = disasterOrgUserClient.allList(map);
        String json = JSONArray.toJSONString(resultInfo.getData());
        List<DisasterOrgUserDTO> disasterOrgUserDTOS = JSONArray.parseArray(json, DisasterOrgUserDTO.class);
        // 在组织成员集合中先将自己去除掉
        String userid = RequestCheckAspect.getTokenInfo().getUserid();
        disasterOrgUserDTOS.removeIf(disasterOrgUserDTO -> disasterOrgUserDTO.getUserid()[0].equals(userid));
        Set<ReturnList> returnListSet = new HashSet<>();
        for (DisasterOrgUserDTO disasterOrgUserDTO : disasterOrgUserDTOS) {
            ReturnList returnList = new ReturnList();
            // 获取每个成员用户的信息
            ResultInfo userInfo = cmsUserClient.getInfo(disasterOrgUserDTO.getUserid()[0]);
            String userJson = JSONObject.toJSONString(userInfo.getData());
            UserDTO userDTO = JSONObject.parseObject(userJson, UserDTO.class);
            returnList.setUser(userDTO);
            // 获取每个人员的最新消息和消息总数
            /**
             * ①查询最新的不管读没读
             */
            LambdaQueryWrapper<MessageInfo> messageInfoLambdaQueryWrapper = Wrappers.lambdaQuery(MessageInfo.class);
            messageInfoLambdaQueryWrapper.select(MessageInfo::getId,MessageInfo::getType,MessageInfo::getContent,MessageInfo::getCreatetime);
            messageInfoLambdaQueryWrapper.eq(MessageInfo::getOrgid, disasterOrgUserDTO.getOrgid());
            /**
             * ？？？此处条件没有写好导致查询出来有可能不是最新的，是自己发过的最新的消息  @@待测试
             */
            messageInfoLambdaQueryWrapper.and((LambdaQueryWrapper<MessageInfo> queryWrapper) -> {
                queryWrapper.or((LambdaQueryWrapper<MessageInfo> wrapper) -> {
                    wrapper.eq(MessageInfo::getReceiveby, userDTO.getId());
                    wrapper.eq(MessageInfo::getSendby, RequestCheckAspect.getTokenInfo().getUserid());
                });
                queryWrapper.or((LambdaQueryWrapper<MessageInfo> wrapper) -> {
                    wrapper.eq(MessageInfo::getSendby, userDTO.getId());
                    wrapper.eq(MessageInfo::getReceiveby, RequestCheckAspect.getTokenInfo().getUserid());
                });
            });
            messageInfoLambdaQueryWrapper.orderByDesc(MessageInfo::getCreatetime);
            messageInfoLambdaQueryWrapper.last("limit 1");
            MessageInfo one = this.getOne(messageInfoLambdaQueryWrapper);
            returnList.setMessage(new MessageInfoVO(one));
            /**
             * ②获取这个成员用户的未读消息总数
             */
            QueryWrapper<MessageInfo> query = Wrappers.query(new MessageInfo());
            query.eq("orgid", disasterOrgUserDTO.getOrgid());
            /**
             * ？？？？此处条件不全有可能查询出不属于自己的聊天记录而是发送人和其他成员的聊天未读的记录。 @@待校验
             */
            query.eq("sendby",userDTO.getId());
            query.eq("receiveby", RequestCheckAspect.getTokenInfo().getUserid());
            query.eq("isread", false);
            int count = this.count(query);
            returnList.setCount(count);
            returnListSet.add(returnList);
        }
        return returnListSet;
    }
}
