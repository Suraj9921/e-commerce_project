package com.shopme.shoppingCart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.shopme.shoppingcart.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

    @Autowired private CartItemRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testSaveItem() {
        Integer customerId = 1;
        Integer productId = 1;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(1);

        CartItem savedItem = repo.save(newItem);

        assertThat(savedItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSave2Items() {
        Integer customerId = 9;
        Integer productId = 1;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem item1 = new CartItem();
        item1.setCustomer(customer);
        item1.setProduct(product);
        item1.setQuantity(1);

        CartItem item2 = new CartItem();
        item2.setCustomer(new Customer(customerId));
        item2.setProduct(new Product(7));
        item2.setQuantity(1);

        Iterable<CartItem> iterable = repo.saveAll(List.of(item1, item2));

        assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 9;
        List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));

        listItems.forEach(System.out::println);

        assertThat(listItems.size()).isEqualTo(2);
    }

    @Test
    public void testFindByCustomerAndProduct() {
        Integer customerId = 9;
        Integer productId = 2;

        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item).isNotNull();

        System.out.println(item);
    }

    @Test
    public void testUpdateQuantity() {
        Integer customerId = 9;
        Integer productId = 7;
        Integer quantity = 4;

        repo.updateQuantity(quantity, customerId, productId);

        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item.getQuantity()).isEqualTo(4);
    }

    @Test
    public void testDeleteByCustomerAndProduct() {
        Integer customerId = 9;
        Integer productId = 1;

        repo.deleteByCustomerAndProduct(customerId, productId);

        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item).isNull();
    }
}