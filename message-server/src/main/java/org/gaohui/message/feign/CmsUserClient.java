package org.gaohui.message.feign;

import feign.hystrix.FallbackFactory;
import org.gaohui.common.api.cms.CmsUserApi;
import org.gaohui.common.exception.ServiceException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author gaohui  2022/02/23
 */
@FeignClient(value = "cms",
        fallbackFactory = CmsUserClient.Fallback.class)
public interface CmsUserClient extends CmsUserApi {

    @Component
    class Fallback implements FallbackFactory<CmsUserClient> {
        @Override
        public CmsUserClient create(Throwable throwable) {
            throw new ServiceException("cms服务 " + throwable.getMessage());
        }
    }
}
