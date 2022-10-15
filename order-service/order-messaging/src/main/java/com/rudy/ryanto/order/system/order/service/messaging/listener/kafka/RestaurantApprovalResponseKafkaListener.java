package com.rudy.ryanto.order.system.order.service.messaging.listener.kafka;

import com.rudy.ryanto.order.system.kafka.consumer.KafkaConsumer;
import com.rudy.ryanto.order.system.kafka.order.avro.model.OrderApprovalStatus;
import com.rudy.ryanto.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.rudy.ryanto.order.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.rudy.ryanto.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalMessageListener;
import com.rudy.ryanto.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    private final RestaurantApprovalMessageListener restaurantApprovalMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public RestaurantApprovalResponseKafkaListener(RestaurantApprovalMessageListener restaurantApprovalMessageListener,
                                                   OrderMessagingDataMapper orderMessagingDataMapper) {
        this.restaurantApprovalMessageListener = restaurantApprovalMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment response received with keys:{} , partition:{}, and offset:{}",
                messages.size(),keys.toString(),partitions.toString(),offsets.toString());

        messages.forEach(restaurantApprovalResponseAvroModel -> {
            if(OrderApprovalStatus.APPROVED.equals(restaurantApprovalResponseAvroModel.getOrderApprovalStatus())){
                log.info("processing approved order for order id : {} ",restaurantApprovalResponseAvroModel.getOrderId());
                restaurantApprovalMessageListener.orderApproved(orderMessagingDataMapper.
                        restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
            }else if(OrderApprovalStatus.REJECTED.equals(restaurantApprovalResponseAvroModel.getOrderApprovalStatus())){
                log.info("processing rejected order for order id : {} ",restaurantApprovalResponseAvroModel.getOrderId());
                restaurantApprovalMessageListener.orderRejected(orderMessagingDataMapper.
                        restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
            }
        });
    }
}
