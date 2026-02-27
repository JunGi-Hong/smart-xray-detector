package dna.safe_guard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.directory}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String path = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + path);
    }
}