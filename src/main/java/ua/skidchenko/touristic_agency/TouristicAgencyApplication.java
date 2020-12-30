package ua.skidchenko.touristic_agency;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import ua.skidchenko.touristic_agency.interceptors.PutUserInfoIntoModelInterceptor;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "ua.skidchenko")
@EnableJpaRepositories("ua.skidchenko.touristic_agency.repository")
public class TouristicAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouristicAgencyApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver () {
        CookieLocaleResolver r = new CookieLocaleResolver();
        r.setDefaultLocale(Locale.forLanguageTag("uk-UA"));
        r.setCookieName("lang");
        r.setCookieDomain("localhost");
        r.setCookieMaxAge(24*60*60);
        return r;
    }

    @Bean
    public WebMvcConfigurer configurer () {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors (@NotNull InterceptorRegistry registry) {
                LocaleChangeInterceptor l = new LocaleChangeInterceptor();
                l.setParamName("lang");
                registry.addInterceptor(l);

                registry.addInterceptor(new PutUserInfoIntoModelInterceptor());
            }
        };
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Qualifier("cacheOfUsersSorts")
    public Map<String, TourSortingHolder> cacheOfUsersSorts() {
        return new ConcurrentHashMap<>(10);
    }

}
