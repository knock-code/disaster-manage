package org.gaohui.message.handler;

import org.gaohui.common.response.CodeEnum;
import org.gaohui.message.message.SendResponse;
import org.gaohui.message.message.SendToAllRequest;
import org.gaohui.message.utils.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author GH
 * 向全部人发送消息
 */
@Component
public class SendToAllHandler implements MessageHandler<SendToAllRequest> {

    @Override
    public void execute(WebSocketSession session, SendToAllRequest message) {
        try {
            // 广播发送消息
            WebSocketUtil.broadcast(SendToAllRequest.TYPE, message);
            // 这里，发送成功
            SendResponse sendResponse = new SendResponse();
            sendResponse.setCode(CodeEnum.SUCCESS.getCode());
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
        } catch (Exception e) {
            // 发生异常则返回失败的信息
            SendResponse sendResponse = new SendResponse();
            sendResponse.setCode(CodeEnum.ERROR.getCode());
            sendResponse.setMessage("全局发送失败!" + e.getMessage());
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
        }
    }

    @Override
    public String getType() {
        return SendToAllRequest.TYPE;
    }

}
