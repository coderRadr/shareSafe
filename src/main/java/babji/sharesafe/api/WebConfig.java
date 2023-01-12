package babji.sharesafe.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
            MediaType.APPLICATION_JSON,
            new MediaType("application", "*+json"),
            MediaType.APPLICATION_OCTET_STREAM
        ));
        converters.add(converter);
        super.configureMessageConverters(converters);
    }
}