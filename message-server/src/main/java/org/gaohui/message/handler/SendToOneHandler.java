package org.gaohui.message.handler;

import com.alibaba.fastjson.JSONObject;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.message.entity.MessageInfo;
import org.gaohui.message.message.SendResponse;
import org.gaohui.message.message.SendToOneRequest;
import org.gaohui.message.service.MessageInfoService;
import org.gaohui.message.utils.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author GH
 * o2o 点对点发送消息 处理器
 */
@Component
public class SendToOneHandler implements MessageHandler<SendToOneRequest> {

    @Autowired
    private MessageInfoService messageInfoService;

    @Override
    public void execute(WebSocketSession session, SendToOneRequest message) {
        try {
            // 获得用户对应的 Session
            WebSocketSession toUserSession = WebSocketUtil.USER_SESSION_MAP.get(message.getReceiveby());
            // 先将消息进行存储
            // 构建消息记录实体对象
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setCreateby(message.getSendby());
            // 先将message对象转化为JSON串然后转为map取出content
            messageInfo.setContent(message.getContent());
            messageInfo.setRead(toUserSession == null ? false : true);
            messageInfo.setSendby(message.getSendby());
            messageInfo.setReceiveby(message.getReceiveby());
            messageInfo.setOrgid(message.getOrgId());
            messageInfo.setType(message.getType());
            messageInfoService.save(messageInfo);
            // o2o发送(将发送过来的信息原封不动送达接收方)
            WebSocketUtil.send(message.getReceiveby(), SendToOneRequest.TYPE, message);
            // 发送成功则返回成功的信息
            SendResponse sendResponse = new SendResponse();
            sendResponse.setCode(CodeEnum.SUCCESS.getCode());
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
        } catch (Exception e) {
            // 发生异常则返回失败的信息
            SendResponse sendResponse = new SendResponse();
            sendResponse.setCode(CodeEnum.ERROR.getCode());
            sendResponse.setMessage("发送失败!" + e.getClass().getCanonicalName() + e.getMessage());
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
        }
    }

    @Override
    public String getType() {
        return SendToOneRequest.TYPE;
    }

}
