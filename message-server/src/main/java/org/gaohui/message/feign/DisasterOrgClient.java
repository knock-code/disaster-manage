package org.gaohui.message.feign;

import feign.hystrix.FallbackFactory;
import org.gaohui.common.api.disaster.DisasterOrgApi;
import org.gaohui.common.exception.ServiceException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author gaohui  2022/02/22
 */
@FeignClient(value = "disaster",
        fallbackFactory = DisasterOrgClient.Fallback.class)
public interface DisasterOrgClient extends DisasterOrgApi {

    @Component
    class Fallback implements FallbackFactory<DisasterOrgClient> {
        @Override
        public DisasterOrgClient create(Throwable throwable) {
            throw new ServiceException("disaster服务 " + throwable.getMessage());
        }
    }
}
