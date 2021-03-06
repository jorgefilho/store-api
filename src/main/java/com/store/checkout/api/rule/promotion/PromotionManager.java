package com.store.checkout.api.rule.promotion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store.checkout.api.repository.domain.Promotion;
import com.store.checkout.api.rule.promotion.factory.PricingRuleFactory;
import com.store.checkout.api.service.PromotionService;
import com.store.checkout.api.service.shoppingcart.domain.ShoppingCartItem;

@Component
public class PromotionManager {

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private PricingRuleFactory pricingRuleFactory;

	public List<ShoppingCartItem> apply(final ShoppingCartItem shoppingCartItem) {
		final List<ShoppingCartItem> items = new ArrayList<>();

		if (shoppingCartItem != null) {
			final Promotion promotion = promotionService.getActiveBySku(shoppingCartItem.getSku());

			if (promotion != null) {
				items.addAll(pricingRuleFactory.apply(promotion, shoppingCartItem));
			} else {
				items.add(shoppingCartItem);
			}
		}
		return items;
	}
}
