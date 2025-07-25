package SrpingBoot_CRUD.ThymeLeafCRUD.controllers;

import SrpingBoot_CRUD.ThymeLeafCRUD.models.Product;
import SrpingBoot_CRUD.ThymeLeafCRUD.models.ProductDto;
import SrpingBoot_CRUD.ThymeLeafCRUD.services.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @GetMapping({"", "/"})
    public String getProducts(Model model) {
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "Upload image"));
        }

        if (result.hasErrors()) {
            return "products/createProduct";
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.out.println("Image upload failed: " + e.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());

            model.addAttribute("product", product);
            model.addAttribute("productDto", productDto);
            return "products/editproduct";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/edit")
    public String updateProduct(Model model,
                                @RequestParam int id,
                                @Valid @ModelAttribute("productDto") ProductDto productDto,
                                BindingResult result) {
        try {
            Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/editproduct";
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());

            if (!productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (IOException e) {
                    System.out.println("Failed to delete old image: " + e.getMessage());
                }

                MultipartFile newImage = productDto.getImageFile();
                String storageFileName = System.currentTimeMillis() + "_" + newImage.getOriginalFilename();

                try (InputStream inputStream = newImage.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println("Failed to upload new image: " + e.getMessage());
                }

                product.setImageFileName(storageFileName);
            }

            repo.save(product);
            return "redirect:/products";

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
try{
    Product product = repo.findById(id).get();
    Path imagePath = Paths.get("public/images/" + product.getImageFileName());

    try{
        Files.delete(imagePath);
    }
    catch (Exception e){
        System.out.println("Exception : " + e.getMessage());
    }
    repo.delete(product);
}
catch (Exception e){
    System.out.println("Exception: " + e.getMessage());
}
return "redirect:/products";
    }


}
