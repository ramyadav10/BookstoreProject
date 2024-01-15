package com.bridgelabz.order.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.order.dto.Book;
import com.bridgelabz.order.dto.OrderDTO;
import com.bridgelabz.order.dto.User;
import com.bridgelabz.order.exceptions.BookException;
import com.bridgelabz.order.exceptions.OrderException;
import com.bridgelabz.order.exceptions.UserException;
import com.bridgelabz.order.model.Order;
import com.bridgelabz.order.repository.OrderRepository;


@Service
public class OrderService implements IOrderService{
	
	@Autowired
	OrderRepository repo;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	//to place the order in book store
	@Override
	public Order addOrder(OrderDTO dto) {
		User user = restTemplate.getForObject("http://localhost:8080/user/findById/" + dto.getUserId(), User.class);
		if(user.equals(null)) {
			throw new UserException("Invalid user id...please provide valid user id");
		}
		else {
			Book book = restTemplate.getForObject("http://localhost:8081/Book/getbookByIdAPI/" + dto.getBookId(), Book.class);
			if(book.equals(null)) {
				throw new BookException("Invalid book id...please provide valid book id");
			}
			else {
				if((int)dto.getQuantity()>(int)book.getQuantity()) {
					throw new BookException("Currently we dont have that much books available");
				}
					else {	
						Order order = new Order(dto);
					order.setPrice(dto.getQuantity());
					order.setDate(LocalDate.now());
					repo.save(order);
					return order;	
				}
			}
		}
	}
	

	@Override
	public List<Order> getAll() {
		List<Order> orders =  repo.findAll();
		return orders;
	}

	@Override
	public Order getByID(Integer orderId) {
		Optional<Order> order = repo.findById(orderId);
		if(order.isEmpty()) {
			throw new OrderException("Invalid Order Id...please provide valid Order id");
		}
		return order.get();
	}

	@Override
	public Order updateById(Integer orderId,OrderDTO dto) {
		Optional<Order> order = repo.findById(orderId);
		if(order.isEmpty()) {
			throw new OrderException("Invalid Order Id...please provide valid Order id");
		}
		Book book = restTemplate.getForObject("http://localhost:8081/Book/findbyId/" + dto.getBookId(), Book.class);
		if(book.equals(null)) {
			throw new BookException("Invalid book id...please provide valid book id");
		}
		Order newOrder = new Order(dto);
		newOrder.setPrice(dto.getQuantity()*book.getPrice());
		repo.save(newOrder);
		return newOrder;
	}

	@Override
	public Order deleteById(Integer orderId) {
		Optional<Order> order = repo.findById(orderId);
		if(order.isEmpty()) {
			throw new OrderException("Invalid Order Id...please provide valid Order id");
		}
		repo.delete(order.get());
		return order.get();
	}

	
}
