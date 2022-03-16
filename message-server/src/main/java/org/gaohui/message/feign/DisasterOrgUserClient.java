package org.gaohui.message.feign;

import feign.hystrix.FallbackFactory;
import org.gaohui.common.api.disaster.DisasterOrgUserApi;
import org.gaohui.common.exception.ServiceException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author gaohui  2022/02/23
 */
@FeignClient(value = "disaster",
        fallbackFactory = DisasterOrgUserClient.Fallback.class)
public interface DisasterOrgUserClient extends DisasterOrgUserApi {
    @Component
    class Fallback implements FallbackFactory<DisasterOrgUserClient> {
        @Override
        public DisasterOrgUserClient create(Throwable throwable) {
            throw new ServiceException("disaster服务 " + throwable.getMessage());
        }
    }
}
