package org.identifiers.cloud.hq.ws.registry.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.configuration
 * Timestamp: 2018-10-11 20:58
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@EnableJpaAuditing
@EnableRetry
public class ApplicationConfiguration {
    @Bean
    public JavaMailSender getJavaMailSender(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password,
            @Value("${spring.mail.protocol}") String protocol,
            @Value("${spring.mail.properties.mail.smtp.auth") String auth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable") String tlsEnabled,
            @Value("${spring.mail.properties.mail.smtp.starttls.required") String tlsRequired,
            @Value("${spring.mail.properties.mail.debug") String debug
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", tlsEnabled);
        props.put("mail.smtp.starttls.required", tlsRequired);
        props.put("mail.debug", debug);
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
