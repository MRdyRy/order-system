package com.rudy.ryanto.order.system.order.service.messaging.publisher.kafka;

import com.rudy.ryanto.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.rudy.ryanto.order.system.kafka.producer.service.KafkaProducer;
import com.rudy.ryanto.order.system.order.service.domain.config.OrderServiceConfigData;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderPaidEvent;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.rudy.ryanto.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;
    private final String REQUEST_AVRO_MODEL_NAME = "RestaurantApprovalRequestAvroModel";

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         OrderServiceConfigData orderServiceConfigData,
                                         OrderKafkaMessageHelper orderKafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.orderKafkaMessageHelper = orderKafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper.
                orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

        try {
            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), orderId, restaurantApprovalRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),restaurantApprovalRequestAvroModel,orderId,
                            REQUEST_AVRO_MODEL_NAME));
            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id : {}",restaurantApprovalRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id:{} error:{}", orderId, e.getMessage());
        }
    }
}
