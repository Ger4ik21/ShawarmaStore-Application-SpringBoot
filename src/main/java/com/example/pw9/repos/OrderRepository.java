package com.example.pw9.repos;


import com.example.pw9.model.ShawarmaOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<ShawarmaOrder, Long>{

}
