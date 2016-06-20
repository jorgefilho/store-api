package com.store.checkout.api.shoppingcart.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.store.checkout.api.StoreApiApplication;
import com.store.checkout.api.repository.domain.Product;
import com.store.checkout.api.service.shoppingcart.ShoppingCartService;
import com.store.checkout.api.service.shoppingcart.domain.ShoppingCart;

/**
 * SKUs Scanned: atv, atv, atv, vga Total expected: $249.00 SKUs Scanned: atv,
 * ipd, ipd, atv, ipd, ipd, ipd Total expected: $2718.95 SKUs Scanned: mbp, vga,
 * ipd Total expected: $1949.98
 * 
 * @author jorge
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StoreApiApplication.class)
@WebAppConfiguration
public class ShoppingCartServiceTests {

	private static final BigDecimal ATV_PRICE = BigDecimal.valueOf(109.50);

	private static final String ATV_NAME = "Apple TV";

	private static final String ATV_SKU = "atv";

	private static final BigDecimal MBP_PRICE = BigDecimal.valueOf(1399.99);

	private static final String MBP_NAME = "MacBook Pro";

	private static final String MBP_SKU = "mbp";

	@Autowired
	private ShoppingCartService shopppingCartService;

	@Test
	public void addItem_whenItemIsValid_shouldReturnShoppingCartWithOneItem() {

		final ShoppingCart shoppingCart = shopppingCartService.addItem(getAppleTV(), 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(1, shoppingCart.getItems().size());
		assertEquals(ATV_PRICE, shoppingCart.getTotal());
	}

	@Test
	public void addItem_whenItemHasPromotionBuyOneGetOne_shouldReturnShoppingCartWithOneItem() {

		final ShoppingCart shoppingCart = shopppingCartService.addItem(getMacBookPro(), 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(2, shoppingCart.getItems().size());
		assertEquals(MBP_PRICE, shoppingCart.getTotal());
	}

	/*
	 * SKUs Scanned: mbp, vga, ipd 
	 * Total expected: $1949.98
	 */
	@Test
	public void addItem_whenItemHasPromotionBuyOneGetOneAndAnotherProduct_shouldReturnShoppingCartTotalIs1949_98() {
		final BigDecimal totalExpected = BigDecimal.valueOf(1949.98);

		ShoppingCart shoppingCart = shopppingCartService.addItem(getMacBookPro(), 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(2, shoppingCart.getItems().size());
		assertEquals(MBP_PRICE, shoppingCart.getTotal());

		final BigDecimal productPriceTwo = BigDecimal.valueOf(549.99);
		final Product productTwo = new Product("ipd", "Super iPad", productPriceTwo);

		shoppingCart = shopppingCartService.addItem(productTwo, 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(3, shoppingCart.getItems().size());
		assertEquals(totalExpected, shoppingCart.getTotal());
	}
  
	/* 
	 * Sku Scanned: atv, atv, atv, vga 
	 * Total expected: $249.00
	 */
	@Test
	public void addItem_whenItemHasPromotionOneItemFreeAnotherProduct_shouldReturnShoppingCartTotalIs249_00() {
		final BigDecimal totalExpected = BigDecimal.valueOf(249.00);

		ShoppingCart shoppingCart = shopppingCartService.addItem(getAppleTV(), 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(1, shoppingCart.getItems().size());
		assertEquals(ATV_PRICE, shoppingCart.getTotal());
		
		final BigDecimal totalPriceTwoAtv = ATV_PRICE.multiply(new BigDecimal(2));
		shoppingCart = shopppingCartService.addItem(getAppleTV(), 1);

		
		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(2, shoppingCart.getItems().size());
		assertEquals(totalPriceTwoAtv, shoppingCart.getTotal());
		
		
		shoppingCart = shopppingCartService.addItem(getAppleTV(), 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(3, shoppingCart.getItems().size());
		assertEquals(totalPriceTwoAtv, shoppingCart.getTotal());
		

		final BigDecimal productPriceTwo = BigDecimal.valueOf(30.00);
		final Product productTwo = new Product("vga", "VGA Adapter", productPriceTwo);

		shoppingCart = shopppingCartService.addItem(productTwo, 1);

		assertNotNull(shoppingCart);
		assertFalse(shoppingCart.getItems().isEmpty());
		assertEquals(3, shoppingCart.getItems().size());
		assertEquals(totalExpected, shoppingCart.getTotal());
	}

	private Product getAppleTV() {
		return new Product(ATV_SKU, ATV_NAME, ATV_PRICE);
	}

	private Product getMacBookPro() {
		return new Product(MBP_SKU, MBP_NAME, MBP_PRICE);
	}

}
