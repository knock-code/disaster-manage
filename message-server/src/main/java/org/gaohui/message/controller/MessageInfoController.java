package org.gaohui.message.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.gaohui.common.annotation.UserLoginToken;
import org.gaohui.common.aspect.RequestCheckAspect;
import org.gaohui.common.dto.DisasterOrgUserDTO;
import org.gaohui.common.exception.BusinessException;
import org.gaohui.common.pojo.BaseController;
import org.gaohui.common.pojo.TokenInfo;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.common.response.ResultInfo;
import org.gaohui.common.utils.PageResult;
import org.gaohui.message.entity.MessageInfo;
import org.gaohui.message.feign.DisasterOrgClient;
import org.gaohui.message.feign.DisasterOrgUserClient;
import org.gaohui.message.service.MessageInfoService;
import org.gaohui.message.vo.CharRecordVO;
import org.gaohui.message.vo.MessageInfoVO;
import org.gaohui.message.vo.ReturnList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author gaohui  2022/02/22
 */
@RestController
@RequestMapping("/message-info")
@Api(tags = {"即时通讯"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageInfoController implements BaseController<MessageInfoVO> {

    private final MessageInfoService messageInfoService;

    private final DisasterOrgClient disasterOrgClient;

    private final DisasterOrgUserClient disasterOrgUserClient;

    /**
     * 根据条件获取符合条件的全部个数
     * @param param 进行条件查询
     * @return
     */
    @Override
    public ResultInfo allList(Map<String, Object> param) {
        return null;
    }

    /**
     * 获取用户拥有组织下的分别消息总数量
     * @param userid
     * @return
     */
    @UserLoginToken
    @GetMapping("/getOrgCount/{userid}")
    public ResultInfo getOrgCount(@PathVariable("userid") String userid) {
        /*不能够直接在消息记录表中依据用户的组织表进行分组，
        因为可能有的用户还没有进行聊天可能会查询不到数据，
        应该先查询用户对应加入的组织然后在查询聊天记录保证可以将数据查询出来*/
        // ①获取自己加入的所有组织
        if (StringUtils.isNotBlank(userid)) {
            Map<String, Object> param = new HashMap<>();
            param.put("eq_userid", userid);
            ResultInfo resultInfo = disasterOrgUserClient.allList(param);
            String json = JSONArray.toJSONString(resultInfo.getData());
            List<DisasterOrgUserDTO> disasterOrgUserDTOS = JSONArray.parseArray(json, DisasterOrgUserDTO.class);
            List<Map<String, Object>> maps = new ArrayList<>();
            // ②查询每个组织中对应的未读消息总数
            for (DisasterOrgUserDTO disasterOrgUserDTO : disasterOrgUserDTOS) {
                // 根据组织id查询组织信息
                ResultInfo orgInfo = disasterOrgClient.getInfo(disasterOrgUserDTO.getOrgid());
                Map<String ,Object> map = new HashMap<>();
                map.putAll((Map<? extends String, ?>) orgInfo.getData());
                // 查询组织下未读的消息数量
                QueryWrapper<MessageInfo> query = Wrappers.query(new MessageInfo());
                query.select("orgid","count(*) as total");
                query.eq("receiveby", userid);
                query.eq("isread", false);
                query.groupBy("orgid");
                List<Map<String, Object>> counts = messageInfoService.listMaps(query);
                if (!counts.isEmpty()) {
                    // 取出组织的信息
                    for (Map<String, Object> count : counts) {
                        if (count.get("orgid").equals(disasterOrgUserDTO.getOrgid())) {
                            map.putAll(count);
                        }
                    }
                }
                maps.add(map);
            }
            return ResultInfo.success("查询成功!", CodeEnum.SUCCESS, maps);
        } else {
            return ResultInfo.error("用户不存在!", CodeEnum.ERROR);
        }
    }

    /**
     * 获取组织下对应的用户和用户对应的信息
     * @param orgid
     * @return
     */
    @UserLoginToken
    @GetMapping("/getOneOrgUserInfoMessage/{orgid}")
    public ResultInfo getOneOrgUserInfoMessage(@PathVariable("orgid") String orgid) {
        if (StringUtils.isNotBlank(orgid)) {
            Set<ReturnList> result = messageInfoService.selectOneOrgUserInfoMessageInfoList(orgid);
            return ResultInfo.success("查询成功!", CodeEnum.SUCCESS, result);
        } else {
            return ResultInfo.error("id不能为空!", CodeEnum.ERROR);
        }
    }

    /**
     * 获取我和聊天对象的聊天记录,并清除未读的消息标志
     * @param vo
     * @return
     */
    @PostMapping("/getCharRecord")
    @UserLoginToken
    public ResultInfo getCharRecord(@RequestBody @Validated CharRecordVO vo) {
        // 查询组织下未读的消息数量 让后将其修改为已读  待测试
        LambdaUpdateWrapper<MessageInfo> messageInfoLambdaUpdateWrapper = Wrappers.lambdaUpdate(MessageInfo.class);
        messageInfoLambdaUpdateWrapper.eq(MessageInfo::getReceiveby, RequestCheckAspect.getTokenInfo().getUserid());
        messageInfoLambdaUpdateWrapper.eq(MessageInfo::getRead, false);
        messageInfoLambdaUpdateWrapper.eq(MessageInfo::getOrgid, vo.getOrgid());
        messageInfoLambdaUpdateWrapper.eq(MessageInfo::getSendby,vo.getMember());
        messageInfoLambdaUpdateWrapper.set(MessageInfo::getRead, true);
        Boolean update = messageInfoService.update(messageInfoLambdaUpdateWrapper);
        IPage<MessageInfo> page = new Page<>(vo.getCurrent(),vo.getSize());
        TokenInfo tokenInfo = RequestCheckAspect.getTokenInfo();
        QueryWrapper<MessageInfo> query = Wrappers.query(new MessageInfo());
        query.select("id","sendby","receiveby", "type", "content", "isread", "createtime");
        query.eq("orgid", vo.getOrgid());
        query.and((QueryWrapper<MessageInfo> messageInfoQueryWrapper) -> {
            messageInfoQueryWrapper.or((QueryWrapper<MessageInfo> wrapper) -> {
                wrapper.eq("sendby", tokenInfo.getUserid());
                wrapper.eq("receiveby", vo.getMember());
            });
            messageInfoQueryWrapper.or((QueryWrapper<MessageInfo> wrapper) -> {
                wrapper.eq("receiveby", tokenInfo.getUserid());
                wrapper.eq("sendby", vo.getMember());
            });
        });
        query.orderByAsc("createtime");
        IPage<MessageInfo> content = messageInfoService.page(page, query);
        List<MessageInfo> records = content.getRecords();
        List<MessageInfoVO> messageInfoVOS = new ArrayList<>();
        for (MessageInfo record : records) {
            messageInfoVOS.add(new MessageInfoVO(record));
        }
        return ResultInfo.success("查询成功!", CodeEnum.SUCCESS, new PageResult<>(content.getTotal(), messageInfoVOS));
    }
}
