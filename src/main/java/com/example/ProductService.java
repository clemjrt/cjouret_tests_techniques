package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private List<Product> products;
    private final String PRODUCTS_FILE = "src/main/resources/products.json";

    @PostConstruct
    public void init() {
        // Load products data from JSON file
        ObjectMapper mapper = new ObjectMapper();
        try {
            products = mapper.readValue(new File(PRODUCTS_FILE), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public Product createProduct(Product product) {
        product.setId(products.size() + 1);  // Simulate an auto-generated ID
        products.add(product);
        saveProductsToFile();
        return product;
    }

    public Product updateProduct(int id, Product updatedProduct) {
        Optional<Product> productOpt = products.stream().filter(p -> p.getId() == id).findFirst();
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            product.setInventoryStatus(updatedProduct.getInventoryStatus());
            saveProductsToFile();
            return product;
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) {
            saveProductsToFile();
        }
        return removed;
    }

    private void saveProductsToFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(PRODUCTS_FILE), products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
