package br.com.barberpro.web;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  private final String location;

  public WebConfiguration(@Value("${app.upload-dir:./data/barber-photos}") String dir) {
    String uri = Path.of(dir).toAbsolutePath().normalize().toUri().toString();
    location = uri.endsWith("/") ? uri : uri + "/";
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}
