package com.store.checkout.api.rule.promotion.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.store.checkout.api.repository.domain.Promotion;
import com.store.checkout.api.rule.promotion.PricingRule;
import com.store.checkout.api.service.shoppingcart.domain.ShoppingCartItem;

@Component
public class PricingRuleFactory {

	@Autowired
	private ApplicationContext context;

	public static final Logger LOGGER = LoggerFactory.getLogger(PricingRuleFactory.class);

	public List<ShoppingCartItem> apply(final Promotion promotion, final ShoppingCartItem shoppingCartItem) {
		final List<ShoppingCartItem> items = new ArrayList<>();
		try{
			final PricingRule pricingRule = (PricingRule) context.getBean(promotion.getType());
			items.addAll(pricingRule.apply(promotion, shoppingCartItem));
		}catch (Exception e){
			items.add(shoppingCartItem);
			LOGGER.error("Pricing Rule Factory tried to call a Rule Service Undefined: {}", e.getMessage() );
		}
		return items;
	}
}