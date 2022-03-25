package com.clara.productapi.modules.rabbit;

import com.clara.productapi.modules.dto.SalesConfirmationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SalesConfirmationSender {
    @Autowired
    private RabbitTemplate template ;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

public void sendSalesConfirmationMessage(SalesConfirmationDto message){

    try {
        log.info("Sending message: {}", new ObjectMapper().writeValueAsString(message));
        template.convertAndSend(productTopicExchange, salesConfirmationKey, message);
        log.info("Message was sent successfully!!!!!!!!");
    } catch (Exception e) {
        log.info("Error while trying to send the message!");
    }
}

}
