package com.eccomerceSpringBoot.productservice.Service;

import java.util.Arrays;
import java.util.List;

import java.util.UUID;

import com.eccomerceSpringBoot.productservice.dto.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eccomerceSpringBoot.productservice.Model.Order;
import com.eccomerceSpringBoot.productservice.Model.OrderLineItems;
import com.eccomerceSpringBoot.productservice.Repository.OrderRepository;
import com.eccomerceSpringBoot.productservice.dto.OrderLineItemsDto;
import com.eccomerceSpringBoot.productservice.dto.OrderRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems =
					orderRequest.getOrderLineItemsDto()
					.stream()
					.map(orderLineItem -> mapToOrderLineItmes(orderLineItem)).toList();
		
		order.setOrderLineItems(orderLineItems);

		List<String> skuCodes = order.getOrderLineItems().stream()
								.map(OrderLineItems::getSkuCode).toList();

		InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
						.uri("http://inventory-service/api/inventory",
								uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
								.retrieve()
										.bodyToMono(InventoryResponse[].class)
												.block(); //for sync communication

		boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);

		if(allProductsInStock){
			orderRepository.save(order);
		}else {
			throw new IllegalArgumentException("One of your product is not in Stock.. Please check first");
		}

	}

	private OrderLineItems mapToOrderLineItmes(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		
		orderLineItems.setId(orderLineItemsDto.getId());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		
		return orderLineItems;
		
//		return OrderLineItems.builder()
//				.id(orderLineItemsDto.getId())
//				.skuCode(orderLineItemsDto.getSkuCode())
//				.price(orderLineItemsDto.getPrice())
//				.quantity(orderLineItemsDto.getQuantity())
//				.build();
	}
}
