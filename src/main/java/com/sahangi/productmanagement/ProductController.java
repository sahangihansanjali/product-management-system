package com.sahangi.productmanagement;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute(
                    "products",
                    productService.searchProducts(search)
            );
        } else {
            model.addAttribute(
                    "products",
                    productService.getAllProducts()
            );
        }

        model.addAttribute("search", search);

        return "products";
    }

    @GetMapping("/new")
    public String showAddProductForm(Model model) {

        model.addAttribute("product", new Product());

        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result) {

        if (result.hasErrors()) {
            return "product-form";
        }

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "product",
                productService.getProductById(id)
        );

        return "product-form";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "product",
                productService.getProductById(id)
        );

        return "product-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return "redirect:/products";
    }
}