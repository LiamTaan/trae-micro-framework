package com.trae.micro.auth.feign;

import com.trae.micro.core.model.R;
import com.trae.micro.core.model.Menu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "system-service", path = "/api/v1/menus")
public interface SystemFeignClient {

    /**
     * 根据用户ID获取菜单列表
     */
    @GetMapping("/user")
    R<List<Menu>> getMenuListByUserId(@RequestParam("userId") Long userId);
}
