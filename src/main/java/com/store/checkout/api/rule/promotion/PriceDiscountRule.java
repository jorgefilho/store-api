package com.store.checkout.api.rule.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.store.checkout.api.repository.domain.Promotion;
import com.store.checkout.api.service.shoppingcart.domain.ShoppingCartItem;

@Service
public class PriceDiscountRule implements PricingRule {

	@Override
	public List<ShoppingCartItem> apply(final Promotion promotion, final ShoppingCartItem shoppingCartItem) {
		final List<ShoppingCartItem> items = new ArrayList<>();

		if (promotion != null && shoppingCartItem != null) {
			// Active the promotion
			if ((shoppingCartItem.getQuantity() >= promotion.getNumberOfItems())) {

				shoppingCartItem.setTotalPrice(shoppingCartItem.getTotalPrice().subtract(promotion.getPricingRule()
						.getDiscount().multiply(new BigDecimal(shoppingCartItem.getQuantity()))));
			}
			items.add(shoppingCartItem);
		}

		return items;
	}
}
