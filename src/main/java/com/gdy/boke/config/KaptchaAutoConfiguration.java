package com.gdy.boke.config;

import com.gdy.boke.service.Kaptcha;
import com.gdy.boke.service.impl.GoogleKaptcha;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnClass(DefaultKaptcha.class)
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaAutoConfiguration {

    private final KaptchaProperties properties;

    public KaptchaAutoConfiguration(KaptchaProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultKaptcha defaultKaptcha() {
        Properties prop = new Properties();

        prop.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, String.valueOf(properties.getWidth()));
        prop.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, String.valueOf(properties.getHeight()));

        KaptchaProperties.Content content = properties.getContent();
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, content.getSource());
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, String.valueOf(content.getLength()));
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, String.valueOf(content.getSpace()));

        KaptchaProperties.BackgroundColor backgroundColor = properties.getBackgroundColor();
        prop.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, backgroundColor.getFrom());
        prop.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, backgroundColor.getTo());

        KaptchaProperties.Border border = properties.getBorder();
        prop.setProperty(Constants.KAPTCHA_BORDER, border.getEnabled() ? "yes" : "no");
        prop.setProperty(Constants.KAPTCHA_BORDER_COLOR, border.getColor());
        prop.setProperty(Constants.KAPTCHA_BORDER_THICKNESS, String.valueOf(border.getThickness()));

        KaptchaProperties.Font font = properties.getFont();
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, font.getName());
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, String.valueOf(font.getSize()));
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, font.getColor());

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(new Config(prop));
        return defaultKaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public Kaptcha kaptchaRender(DefaultKaptcha defaultKaptcha) {
        return new GoogleKaptcha(defaultKaptcha);
    }

}
