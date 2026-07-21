package com.logiq.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.profile-image-dir}")
    private String profileImageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(profileImageDir);
        String absoluteUploadPath = uploadPath.toFile().getAbsolutePath();

        // /profile-images/user_1.png විදිහට රූප ලබාගන්න පුළුවන්
        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations("file:" + absoluteUploadPath + "/");
    }
}