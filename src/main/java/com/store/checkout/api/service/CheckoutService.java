package com.store.checkout.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.checkout.api.controller.contract.CheckoutItem;
import com.store.checkout.api.controller.domain.MessageResponse;
import com.store.checkout.api.controller.domain.builder.MessageReponseBuilder;
import com.store.checkout.api.repository.ProductRepository;
import com.store.checkout.api.repository.domain.Product;
import com.store.checkout.api.session.domain.ShoppingCart;
import com.store.checkout.api.validation.ProductValidation;

@Service
public class CheckoutService {

	@Autowired
	private ProductValidation productValidation;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ShoppingCartService shippingCartService;

	public MessageResponse addItem(final CheckoutItem checkoutItem) {

		final MessageReponseBuilder messageResponseBilder = new MessageReponseBuilder();

		final Map<String, String> errors = productValidation.validate(checkoutItem);

		if (errors.isEmpty()) {

			final Product product = productRepository.findBySku(checkoutItem.getSku());

			final ShoppingCart shoppingCart = shippingCartService.addItem(product);

			messageResponseBilder.entity(shoppingCart);
			messageResponseBilder.addMessage("checkout", "Item added with success!");

		} else {
			messageResponseBilder.errors(errors);
		}
		return messageResponseBilder.build();
	}
}
