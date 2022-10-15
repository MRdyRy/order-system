package com.rudy.ryanto.order.system.order.service.messaging.listener.kafka;

import com.rudy.ryanto.order.system.domain.valueobject.PaymentStatus;
import com.rudy.ryanto.order.system.kafka.consumer.KafkaConsumer;
import com.rudy.ryanto.order.system.kafka.order.avro.model.PaymentResponsetAvroModel;
import com.rudy.ryanto.order.system.order.service.domain.PaymentResponseMessageListenerImpl;
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
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponsetAvroModel> {

    private final PaymentResponseMessageListenerImpl paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListenerImpl paymentResponseMessageListener,
                                        OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponsetAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment response received with keys:{} , partition:{}, and offset:{}",
                messages.size(),keys.toString(),partitions.toString(),offsets.toString());
        messages.forEach(paymentResponsetAvroModel -> {
            if(PaymentStatus.COMPLETE.equals(paymentResponsetAvroModel.getPaymentStatus())){
                log.info("processing successful payment for id : {} ",paymentResponsetAvroModel.getId().toString());
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.
                        paymentResponseAvroModelToPaymentResponse(paymentResponsetAvroModel));
            } else if (PaymentStatus.CANCELED.equals(paymentResponsetAvroModel.getPaymentStatus())
            || PaymentStatus.FAILED.equals(paymentResponsetAvroModel.getPaymentStatus())) {
                log.info("processing unsuccessfull payment for id : {} ",paymentResponsetAvroModel.getId().toString());
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.
                        paymentResponseAvroModelToPaymentResponse(paymentResponsetAvroModel));
            }
        });
    }
}
