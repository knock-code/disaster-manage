package org.gaohui.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaohui.common.annotation.UserLoginToken;
import org.gaohui.common.api.message.MessageHelpApi;
import org.gaohui.common.aspect.RequestCheckAspect;
import org.gaohui.common.pojo.BaseController;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.common.response.ResultInfo;
import org.gaohui.common.utils.DatabaseUtil;
import org.gaohui.common.utils.PageResult;
import org.gaohui.common.vo.PagingVO;
import org.gaohui.message.entity.HelpInfo;
import org.gaohui.message.service.HelpInfoService;
import org.gaohui.message.vo.HelpInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaohui  2022/03/10
 */
@RestController
@RequestMapping("/help-info")
@Api(tags = {"即时通讯"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class HelpInfoController implements MessageHelpApi, BaseController<HelpInfoVO> {
    private final HelpInfoService helpInfoService;

    /**
     * 插入一条求助
     * @param helpInfoVO
     * @return
     */
    @Override
    @UserLoginToken
    public ResultInfo insert(HelpInfoVO helpInfoVO) {
        HelpInfo helpInfo = new HelpInfo();
        BeanUtils.copyProperties(helpInfoVO, helpInfo, "type");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < helpInfoVO.getType().length; i++) {
            if (i < helpInfoVO.getType().length-1) {
                sb.append(helpInfoVO.getType()[i] + ",");
            } else {
                sb.append(helpInfoVO.getType()[i]);
            }
        }
        helpInfo.setType(sb.toString());
        log.debug("拼接之后的类型" + sb.toString());
        helpInfo.setCreateby(RequestCheckAspect.getTokenInfo().getUserid());
        boolean save = helpInfoService.save(helpInfo);
        return save?ResultInfo.success("发布成功!", CodeEnum.SUCCESS)
                :ResultInfo.error("发布失败!",CodeEnum.ERROR);
    }


    /**
     * 依据条件分页查询符合条件的记录
     * @param vo 查询信息
     * @return
     */
    @Override
    @UserLoginToken
    public ResultInfo list(PagingVO vo) {
        if (vo.getCurrent() > 0 && vo.getSize() > 0) {
            // 分页对象
            IPage<HelpInfo> page = new Page<>(vo.getCurrent(),vo.getSize());
            // 条件包装器
            QueryWrapper<HelpInfo> queryWrapper = DatabaseUtil.getQueryWrapper(vo.getCondition(), new HelpInfo());
            IPage<HelpInfo> helpInfoIPage = helpInfoService.page(page, queryWrapper);
            List<HelpInfo> records = helpInfoIPage.getRecords();
            List<HelpInfoVO> helpInfoVOS = new ArrayList<>();
            records.forEach(helpInfo -> {
                HelpInfoVO helpInfoVO = new HelpInfoVO();
                BeanUtils.copyProperties(helpInfo, helpInfoVO,"type");
                String[] split = helpInfo.getType().split(",");
                helpInfoVO.setType(split);
                helpInfoVOS.add(helpInfoVO);
            });
            return ResultInfo.success("响应成功!", CodeEnum.SUCCESS,new PageResult<>(helpInfoIPage.getTotal(), helpInfoVOS));
        }
        return ResultInfo.success("参数异常!", CodeEnum.ERROR);
    }

    /**
     * 将求助改为完成
     * @param id
     * @return
     */
    @GetMapping("finishHelp/{id}")
    @UserLoginToken
    public ResultInfo finishHelp(@PathVariable("id") String id) {
        HelpInfo byId = helpInfoService.getById(id);
        if (byId != null) {
            byId.setIsfinish(true);
            helpInfoService.updateById(byId);
            return ResultInfo.success("完成!", CodeEnum.SUCCESS);
        } else {
            return ResultInfo.success("失败!", CodeEnum.ERROR);
        }
    }

    /**
     * 删除指定id的求助信息
     * @param id 实体id
     * @return
     */
    @Override
    @UserLoginToken
    public ResultInfo delete(String id) {
        HelpInfo byId = helpInfoService.getById(id);
        if (byId != null) {
            boolean flag = helpInfoService.removeById(id);
            return flag?ResultInfo.success("删除成功!", CodeEnum.SUCCESS)
                    :ResultInfo.error("删除失败!", CodeEnum.ERROR);
        } else {
            return ResultInfo.success("失败!", CodeEnum.ERROR);
        }
    }

    /**
     * 依据条件获取所有求助
     * @param param 进行条件查询
     * @return
     */
    @Override
    @UserLoginToken
    public ResultInfo allList(Map<String, Object> param) {
        QueryWrapper<HelpInfo> queryWrapper = DatabaseUtil.getQueryWrapper(param, new HelpInfo());
        List<HelpInfo> helpInfos = helpInfoService.list(queryWrapper);
        List<HelpInfoVO> helpInfoVOS = new ArrayList<>();
        for (HelpInfo helpInfo : helpInfos) {
            HelpInfoVO helpInfoVO = new HelpInfoVO();
            BeanUtils.copyProperties(helpInfo, helpInfoVO,"type");
            String[] split = helpInfo.getType().split(",");
            helpInfoVO.setType(split);
            helpInfoVOS.add(helpInfoVO);
        }
        return ResultInfo.success("查询成功!", CodeEnum.SUCCESS, helpInfoVOS);
    }

    @PostMapping("noCheckGetAllList")
    public ResultInfo noCheckGetAllList(Map<String, Object> param) {
        QueryWrapper<HelpInfo> wrapper = DatabaseUtil.getQueryWrapper(param, new HelpInfo());
        wrapper.select("createtime","content","type","disasterid","title");
        List<HelpInfo> list = helpInfoService.list(wrapper);
        List<HelpInfoVO> helpInfoVOS = new ArrayList<>();
        for (HelpInfo helpInfo : list) {
            HelpInfoVO helpInfoVO = new HelpInfoVO();
            BeanUtils.copyProperties(helpInfo, helpInfoVO,"type");
            String[] split = helpInfo.getType().split(",");
            helpInfoVO.setType(split);
            helpInfoVOS.add(helpInfoVO);
        }
        return ResultInfo.success("查询成功!", CodeEnum.SUCCESS, helpInfoVOS);
    }

    @Override
    @GetMapping("getNoFinishHelpCount")
    public ResultInfo getNoFinishHelpCount() {
        LambdaQueryWrapper<HelpInfo> helpInfoLambdaQueryWrapper = Wrappers.lambdaQuery(HelpInfo.class);
        helpInfoLambdaQueryWrapper.eq(HelpInfo::getIsfinish, false);
        int noFinishHelpCount = helpInfoService.count(helpInfoLambdaQueryWrapper);
        Map<String, Integer> result = new HashMap<>();
        result.put("noFinishHelpCount", noFinishHelpCount);
        return ResultInfo.success("获取成功!", CodeEnum.SUCCESS,result);
    }
}
