package org.gaohui.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gaohui.message.entity.HelpInfo;
import org.gaohui.message.mapper.HelpInfoMapper;
import org.gaohui.message.service.HelpInfoService;
import org.springframework.stereotype.Service;

/**
 * @author gaohui  2022/03/10
 */
@Service
public class HelpInfoServiceImpl extends ServiceImpl<HelpInfoMapper, HelpInfo> implements HelpInfoService {
}
