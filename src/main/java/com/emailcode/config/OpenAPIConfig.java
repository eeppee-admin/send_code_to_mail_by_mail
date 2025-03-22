package com.emailcode.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "邮箱验证码API文档",
        version = "1.0",
        description = """
            ## 接口使用说明
            - 测试前准备：
              1. 确保Redis服务已启动（默认端口6379）
              2. 配置正确的QQ邮箱SMTP参数
            - 请求头：Content-Type: application/json
            """
    ),
    servers = @Server(url = "http://localhost:8080", description = "本地环境")
)
public class OpenAPIConfig {
}