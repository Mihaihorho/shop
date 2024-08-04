package com.example.shop.integrationTests;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setName("ProductName");
        product.setPrice(19.99);
        product.setStock(50);
        productRepository.save(product);
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testGetProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).contains("ProductName");
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testGetProductById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/products/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).contains("ProductName");
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testGetProductByIdNotExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/products/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find product 4"))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println(responseContent);
    }

    @Test
    @WithMockUser(roles = {"WRITE"})
    public void testCreateProduct() throws Exception {
        String newProductJson = "{ \"name\": \"New Product\", \"price\": 29.99, \"stock\": 100 }";

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProductJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    @WithMockUser(roles = {"WRITE"})
    public void testUpdateProduct() throws Exception {
        String updatedProductJson = "{ \"name\": \"Updated Product\", \"price\": 39.99, \"stock\": 75 }";

        mockMvc.perform(put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(39.99))
                .andExpect(jsonPath("$.stock").value(75));
    }

    @Test
    @WithMockUser(roles = {"WRITE"})
    public void testUpdateProductPrice() throws Exception {
        double newPrice = 12.99;
        mockMvc.perform(put("/products/" + product.getId() + "/price?newPrice=" + newPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(newPrice));
    }

    @Test
    @WithMockUser(roles = {"WRITE"})
    public void testUpdateProductStock() throws Exception {
        Integer newStock = 99;
        mockMvc.perform(put("/products/" + product.getId() + "/stock?newStock=" + newStock)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stock").value(newStock));
    }

    @Test
    @WithMockUser(roles = {"WRITE"})
    public void testUpdateProductName() throws Exception {
        String newName = "New Product name";
        mockMvc.perform(put("/products/" + product.getId() + "/name?newName=" + newName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product name"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/" + product.getId()))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testUnauthorizedCreateProduct() throws Exception {
        String newProductJson = "{ \"name\": \"Unauthorized Product\", \"price\": 19.99, \"stock\": 50 }";

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProductJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testUnauthorizedUpdateProduct() throws Exception {
        String newProductJson = "{ \"name\": \"Unauthorized Product\", \"price\": 19.99, \"stock\": 50 }";

        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProductJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testUnauthorizedUpdateProductName() throws Exception {
        String newName = "New Product name";
        mockMvc.perform(put("/products/" + product.getId() + "/name?newName=" + newName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testUnauthorizedUpdateProductPrice() throws Exception {
        double newPrice = 12.99;
        mockMvc.perform(put("/products/" + product.getId() + "/price?newPrice=" + newPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"READ"})
    public void testUnauthorizedUpdateProductStock() throws Exception {
        Integer newStock = 99;
        mockMvc.perform(put("/products/" + product.getId() + "/stock?newStock=" + newStock)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"READ", "WRITE"})
    public void testUnauthorizedDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}