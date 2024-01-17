package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @GetMapping
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView("/product/index");
        if (productRepository != null) {
            modelAndView.addObject("list", productRepository.findAll());
        }
        return modelAndView;
    }
    @GetMapping("/create")
    public ModelAndView create(Model model) {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        List<String> productTypes = Arrays.asList("Type1", "Type2", "Type3");
        model.addAttribute("productTypes", productTypes);

        return modelAndView;
    }


    @PostMapping("/save")
    public ModelAndView save(Product product) {
        productRepository.save(product); // Lưu thông tin sản phẩm vào cơ sở dữ liệu
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        Product product = productRepository.findById(id).orElse(null);
        modelAndView.addObject("item", product);
        return modelAndView;
    }



    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
    @GetMapping("/search")
    public ModelAndView searchProducts(@RequestParam(name = "name", required = false) String productName) {
        ModelAndView modelAndView = new ModelAndView("/product/index");

        if (productName != null && !productName.isEmpty()) {
            List<Product> searchResults = productRepository.findByNameContainingIgnoreCase(productName);
            modelAndView.addObject("list", searchResults);
            modelAndView.addObject("searchTerm", productName);
        } else {
            modelAndView.addObject("list", productRepository.findAll());
        }
        return modelAndView;
    }
    @GetMapping("/searchPrice")
    public ModelAndView searchProductsBySalary(
            @RequestParam(name = "minSalary", required = false) Double minSalary,
            @RequestParam(name = "maxSalary", required = false) Double maxSalary) {

        ModelAndView modelAndView = new ModelAndView("/product/index");

        if (minSalary != null && maxSalary != null) {
            List<Product> searchResults = productRepository.findAllByPriceBetween(minSalary, maxSalary);
            modelAndView.addObject("list", searchResults);
            modelAndView.addObject("searchTerm", "Salary between " + minSalary + " and " + maxSalary);
        } else {
            modelAndView.addObject("list", productRepository.findAll());
        }

        return modelAndView;
    }
    @GetMapping("/sort")
    public ModelAndView sortProductsBySalary() {
        ModelAndView modelAndView = new ModelAndView("/product/index");
        List<Product> sortedProducts = productRepository.findAllByOrderByPriceAsc();
        modelAndView.addObject("list", sortedProducts);
        modelAndView.addObject("searchTerm", "Sorted by Salary (Ascending)");
        return modelAndView;
    }


}
