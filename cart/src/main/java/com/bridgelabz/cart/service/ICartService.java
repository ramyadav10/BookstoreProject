package com.bridgelabz.cart.service;

import java.util.List;

import javax.validation.Valid;

import com.bridgelabz.cart.dto.CartDTO;
import com.bridgelabz.cart.dto.QuantityDTO;
import com.bridgelabz.cart.model.Cart;
import com.bridgelabz.cart.model.UserModel;

public interface ICartService {
	
	//String callUserAndRetrieve( Integer Userid);
	
	//UserModel callUserAndRetrieveObject(Integer Userid);

	Cart insertToCart(@Valid CartDTO dto);

	List<Cart> getAllCarts();

	Cart getCartByID(Integer cartId);

	Cart updateById(Integer cartId, @Valid CartDTO dto);

	Cart updateQuantity(QuantityDTO dto);
	
	Cart deleteById(Integer cartId);
}
