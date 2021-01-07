package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.entities.Product;

import com.hussam.inventory.inventory.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;


    @Test
    public void getAllProductInSelectedCategory() throws Exception{

        Category category = new Category();

        category.setName("clothes");

        Product product = new Product();

        product.setName("Pepsi");
        product.setAmount(3);
        product.setPrice(3);
        product.setSku("123sds4");
        product.setDateCreated(new Date());
        product.setCategory(category);

        List<Product> allProducts =  new ArrayList<>();
        allProducts.add(product);

        given(productService.getAllByCategory(category.getName())).willReturn(allProducts);

        String url = "/products/";

        mockMvc.perform(get(url).param("category", "clothes")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }

    @Test
    void getProductByIdFromCategory()throws Exception{
        Category category = new Category();

        category.setId(1L);
        category.setName("drink");

        Product product = new Product();

        product.setId(1L);
        product.setName("Pepsi");
        product.setAmount(3);
        product.setPrice(3);
        product.setSku("123sds4");
        product.setDateCreated(new Date());
        product.setCategory(category);

        given(productService.getProductByIdAndCategory(product.getId(), category.getName())).willReturn(java.util.Optional.of(product));
        String url = "/products/";
        mockMvc.perform(get(url + product.getId()).param("category", "drink")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(product.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addNewProduct() throws Exception{
        Category category = new Category();

        category.setId(1L);
        category.setName("drink");

        Product product = new Product();

        product.setId(1L);
        product.setName("Pepsi");
        product.setAmount(3);
        product.setPrice(3);
        product.setSku("123sds4");
        product.setDateCreated(new Date());
        product.setCategory(category);

        when(productService.add(product,category.getName())).thenReturn(product);
        String url = "/products/";
        mockMvc.perform(post(url).param("category", "drink")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(product.getName()));

    }
}
