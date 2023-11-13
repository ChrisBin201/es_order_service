package com.chris.orderservice;

import com.chris.common.repo.redis.RedisAccessTokenRepo;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.chris"})
@EntityScan(basePackages = {"com.chris.data.entity.order", "com.chris.data.view.order"})
@OpenAPIDefinition(info = @Info(title = "REST API FOR ORDER SERVICE", version = "0.1.0", description = "ORDER SERVICE API DOCUMENTATION"))
@Slf4j
//@EnableRedisDocumentRepositories(basePackageClasses = {RedisAccessTokenRepo.class})
public class OrderServiceApplication {
//	final
//	ProductRepo productRepo;
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
//	CommandLineRunner seedingCategoryAndProduct() {
//		return args -> {
//			int maxC = 20;
//			for (int i1 = 1;i1<= maxC;i1++ ){
//				Category category = new Category();
//				category.setName("category"+i1);
//				category = categoryRepo.save(category);
//				int max = 100;
//				for (int i = 1; i <= max; i++) {
//					Product product = new Product();
//					product.setDescription("des" + i*i1);
//					product.setName("name"+i*i1);
//					product.setPreview("preview"+i*i1);
////					Random random = new Random();
////					int maxI = 19,minI = 1;
////					long randInt = random.nextInt(maxI - minI + 1) + minI;
////					Category category = categoryRepo.getById(randInt);
//					product.setCategory(category);
//					List<Variation> list = new ArrayList<>();
//					for(int j= 1;j<=2;j++) {
//						Variation variation = new Variation();
//						variation.setName("pro"+i*i1+"_"+"var"+j);
//						List<VariationOption> options = new ArrayList<>();
//						for(int k=1;k<=3;k++) {
//							VariationOption o = new VariationOption();
//							o.setValue("pro"+i*i1+"_"+"var"+j+"_"+"vo"+k);
//							options.add(o);
//						}
//						variation.setVariationOptions(options);
//						list.add(variation);
//					}
//					product.setVariationList(list);
////					product.setStatus(Product.ProductStatus.ACTIVE);
//					productRepo.save(product);
//				}
//			}

//			List<Product> list = productRepo.findAll();
////			Product product = productRepo.findById((long)6458).get();
//			Random random = new Random();
//			int maxI = 19,minI = 1;
//			for(Product product: list) {
//				List<Variation> vars = product.getVariationList();
//				Variation var1 = vars.get(0);
//				Variation var2 = vars.get(1);
//				var1.getVariationOptions().forEach(vo1 -> {
//					var2.getVariationOptions().forEach(vo2 -> {
//						long randInt = random.nextInt(maxI - minI + 1) + minI;
//						ProductItem productItem = new ProductItem();
//						productItem.setProduct(product);
//						productItem.setPreview(product.getPreview());
//						productItem.setPrice(randInt * 1000);
//						productItem.setQuantityInStock(randInt * 100);
//						productItem.setVariationOptions(new ArrayList<>(List.of(
//								vo1, vo2
//						)));
//						productItemRepo.save(productItem);
//					});
//				});
//			}
////			log.info(entityManager.contains(product)+" - product");
////			log.info(entityManager.contains(product.getVariationList().get(0))+" - variation");
////			log.info(entityManager.contains(product.getVariationList().get(0).getVariationOptions().get(0))+" - variation option");
//		};
//	}
//	@Bean
//	CommandLineRunner seedingCustomer() {
//		return args -> {
//			int max = 100;
//			for (int i = 1; i <= max; i++) {
//				Customer customer = new Customer();
//				customer.setUsername("customer"+i);
//				customer.setPassword("password"+i);
//				customer.setFullname("CusFullname"+i);
//				customer.setStatus(User.UserStatus.ACTIVE);
//				customerRepo.save(customer);
//			}
//		};
//	}
//	@Bean
//	CommandLineRunner seedingSeller() {
//		return args -> {
//			List<Product> list = productRepo.findAll();
//			int max = 100;
//			for (int i = 1; i <= max; i++) {
//				Seller seller = new Seller();
//				seller.setUsername("seller"+i);
//				seller.setPassword("password"+i);
//				seller.setFullname("SellerFullname"+i);
//				seller.setStatus(User.UserStatus.ACTIVE);
//				seller.setShopName("shop seller"+ i);
//				seller.setDescription("Description shop seller" + i);
//				sellerRepo.save(seller);
//			}
//			for(Product p: list){
//				Random random = new Random();
//				long id =   random.ints(202, 301)
//						.findFirst()
//						.getAsInt();
//				Seller s = sellerRepo.findById(id).get();
//				p.setSeller(s);
//				productRepo.save(p);
//			}
//		};
//	}
//	@Bean
//	CommandLineRunner seedingAdmin() {
//		return args -> {
//				User user = new User();
//				user.setUsername("admin");
//				user.setPassword("admin");
//				user.setFullname("admin");
//				user.setStatus(User.UserStatus.ACTIVE);
//				userRepo.save(user);
//			};
//	}

//	@Bean
//	CommandLineRunner seedingPromotion() {
//		return args -> {
//			int max = 2;
//			for (long i = 1; i <= max; i++) {
//				Promotion p = promotionRepo.findById(i).get();
//				p.setStartDate(LocalDateTime.now().plusDays(i));
//				p.setEndDate(LocalDateTime.now().plusDays(i + i));
//				promotionRepo.save(p);
//			}
//		};
//	}
//	@Bean
//	CommandLineRunner seedingShipment() {
//		return args -> {
//			int max = 2;
//			for (long i = 1; i <= max; i++) {
//				Shipment s = new Shipment();
//				s.setName("shipment"+i);
//				s.setPrice(10000*i);
//				s.setDes("description shipment"+i);
//				shipmentRepo.save(s);
//			}
//		};
//	}
//	@Bean
//	CommandLineRunner seedingOrder() {
//		return args -> {
////			int max = 500;
//			Random random = new Random();
////			for (long i = 1; i <= max; i++) {
//				long finalI = random.ints(102, 201)
//						.findFirst()
//						.getAsInt();;
//				Customer customer = customerRepo.findById(finalI).get();
//				Order order = new Order();
//				order.setDeliveryDate(LocalDateTime.now());
//				order.setPaymentDate(LocalDateTime.now());
//
//				double totalPrice=0;
//				List<OrderItem> orderItemList = new ArrayList<>();
////				for (long j = 1;j<=3;j++){
//					long id =   random.ints(4, 1003)
//						.findFirst()
//						.getAsInt();
//					OrderItem orderItem = new OrderItem();
//					ProductItem productItem = productItemRepo.findById(id).get();
//
//					orderItem.setProductItem(productItem);
//					orderItem.setPrice(productItem.getPrice());
//					orderItem.setQuantity(2);
//					orderItem.setRating(new Rating().builder()
////							.message(customer.getFullname()+" rating")
//							.customer(customer)
//							.rating(
//							random.ints(1,5).findFirst().getAsInt()
//					).build());
//					orderItemList.add(orderItem);
//					totalPrice+=productItem.getPrice();
////				}
//				order.setTotalPrice(totalPrice);
//				order.setStatus(Order.OrderStatus.COMPLETED);
//				order.setOrderItemList(orderItemList);
//				order.setShipment(shipmentRepo.findById((long)random.ints(1, 2)
//						.findFirst()
//						.getAsInt()).get());
//				order.setCustomer(customer);
//				orderRepo.save(order);
////			}
//		};
//	}

//	@Bean CommandLineRunner seedingCategory(){
//		return args -> {
//			int max =20;
//			Random random = new Random();
//			for(long i=75;i<=94;i++){
////			long id = random.ints(15, 34)
////						.findFirst()
////						.getAsInt();
//				Category category =  categoryRepo.findById(i).get();
//				Category parent = new Category();
//				parent.setName(category.getName()+"_p");
//				parent =  categoryRepo.save(parent);
//				category.setParentId(parent.getId());
//				categoryRepo.save(category);
//			}
//		};
//	}

//	@Bean CommandLineRunner seedingRole(){
//		return args -> {
//			Role admin = new Role();
//			admin.setName(Role.RoleName.ADMIN);
//			roleRepo.save(admin);
//			Role seller = new Role();
//			seller.setName(Role.RoleName.SELLER);
//			roleRepo.save(seller);
//			Role cus = new Role();
//			cus.setName(Role.RoleName.CUSTOMER);
//			roleRepo.save(cus);
//		};
//	}

//	@Bean CommandLineRunner addRoleToUser(){
//		return args -> {
//			List<Seller> sellers =  sellerRepo.findAll();
//			List<Customer> customers =  customerRepo.findAll();
//			User admin  = userRepo.findByUsername("admin").get();
//			List<Role> roles = roleRepo.findAll();
//			for(Seller s: sellers){
//				for(Role r: roles)
//					if(r.getName() == Role.RoleName.SELLER) {
//						s.addRole(r);
//						break;
//					}
//				sellerRepo.save(s);
//			}
//			for(Customer c: customers){
//				for(Role r: roles)
//					if(r.getName() == Role.RoleName.CUSTOMER) {
//						c.addRole(r);
//						break;
//					}
//				customerRepo.save(c);
//			}
//			for(Role r: roles)
//				if(r.getName() == Role.RoleName.ADMIN) {
//					admin.addRole(r);
//					break;
//				}
//			userRepo.save(admin);
//		};
//	}

//	@Bean CommandLineRunner resetAllData(){
//		return args -> {
//			productRepo.deleteById((long) 6457);
//			productRepo.deleteAll();
//			productItemRepo.deleteAll();
//			userRepo.deleteAll();
//			categoryRepo.deleteAll();
//			orderRepo.deleteById((long)1548);
//		};
//	}
}

