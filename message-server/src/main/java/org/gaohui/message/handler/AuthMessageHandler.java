package org.gaohui.message.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.gaohui.common.aspect.JwtUtils;
import org.gaohui.common.constant.ServerCommonConstant;
import org.gaohui.common.pojo.TokenInfo;
import org.gaohui.common.response.CodeEnum;
import org.gaohui.message.message.AuthRequest;
import org.gaohui.message.message.AuthResponse;
import org.gaohui.message.utils.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author GH
 * 权限认证handler
 */
@Component
public class AuthMessageHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void execute(WebSocketSession session, AuthRequest message){
        // 如果未传递 accessToken
        if (StringUtils.isEmpty(message.getAccessToken())) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse().setCode(CodeEnum.ERROR.getCode()).setMessage("认证 " + ServerCommonConstant.KEYWORD_TOKEN +  " 未传入"));
            return;
        }

        TokenInfo tokenInfo = null;
        try {
            tokenInfo = jwtUtils.checkToken(message.getAccessToken());
            if (tokenInfo != null) {
                // 添加到 WebSocketUtil 中
                // 使用用户id作为session的key
                WebSocketUtil.addSession(session, tokenInfo.getUserid());
                // 认证成功
                WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(CodeEnum.SUCCESS.getCode()).setMessage("身份认证成功!"));
            } else {
                // 认证失败
                WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(CodeEnum.ERROR.getCode()).setMessage("身份认证失败!"));
            }
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                // 验证失败
                WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(CodeEnum.ERROR.getCode()).setMessage("身份认证失败!"));
            } else if (e instanceof ExpiredJwtException) {
                // token 过期
                WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(CodeEnum.ERROR.getCode()).setMessage("token过期请中心登录!"));
            }
        }
        // 通知所有人，某个人加入了。这个是可选逻辑，仅仅是为了演示
        /*WebSocketUtil.broadcast(UserJoinNoticeRequest.TYPE,
                new UserJoinNoticeRequest().setNickname(tokenInfo.getName()));*/
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }

}
