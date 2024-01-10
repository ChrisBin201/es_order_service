package com.chris.orderservice;

import com.chris.orderservice.repo.OrderRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.chris"})
@EntityScan(basePackages = {"com.chris.data.entity.order", "com.chris.data.view.order"})
@OpenAPIDefinition(info = @Info(title = "REST API FOR ORDER SERVICE", version = "0.1.0", description = "ORDER SERVICE API DOCUMENTATION"))
@Slf4j
//@EnableRedisDocumentRepositories(basePackageClasses = {RedisAccessTokenRepo.class})
public class OrderServiceApplication {
	@Autowired
    OrderRepo orderRepo;
//	final
//	ProductItemRepo productItemRepo;
////	@PersistenceContext
////	protected EntityManager entityManager;
//
//	public ProductServiceApplication(ProductRepo productRepo, ProductItemRepo productItemRepo, CategoryRepo categoryRepo) {
//		this.productRepo = productRepo;
//		this.productItemRepo = productItemRepo;
//		this.categoryRepo = categoryRepo;
//	}
////	final
////	UserRepo userRepo;
////	final
////	SellerRepo sellerRepo;
////	final
////	CustomerRepo customerRepo;
//	final
//	CategoryRepo categoryRepo;
//	final
//	PromotionRepo promotionRepo;
//	final
//	OrderRepo orderRepo;
//	final
//	RatingRepo ratingRepo;
//	final
//	OrderItemRepo orderItemRepo;
//	final
//	ShipmentRepo shipmentRepo;
//	final RoleRepo roleRepo;

//	public EcomSocialApplication(ProductRepo productRepo, ProductItemRepo productItemRepo, UserRepo userRepo, SellerRepo sellerRepo, CustomerRepo customerRepo, CategoryRepo categoryRepo, PromotionRepo promotionRepo, OrderRepo orderRepo, RatingRepo ratingRepo, OrderItemRepo orderItemRepo, ShipmentRepo shipmentRepo, RoleRepo roleRepo) {
//		this.productRepo = productRepo;
//		this.productItemRepo = productItemRepo;
//		this.userRepo = userRepo;
//		this.sellerRepo = sellerRepo;
//		this.customerRepo = customerRepo;
//		this.categoryRepo = categoryRepo;
//		this.promotionRepo = promotionRepo;
//		this.orderRepo = orderRepo;
//		this.ratingRepo = ratingRepo;
//		this.orderItemRepo = orderItemRepo;
//		this.shipmentRepo = shipmentRepo;
//		this.roleRepo = roleRepo;
//	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}


//	@Bean
//	CommandLineRunner resetAllData(){
//		return args -> {
//			log.info("resetAllData");
//			invoiceRepo.deleteAllInBatch();
//		};
//	}
}

