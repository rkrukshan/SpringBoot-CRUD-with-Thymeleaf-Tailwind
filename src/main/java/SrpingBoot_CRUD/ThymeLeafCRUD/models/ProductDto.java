package SrpingBoot_CRUD.ThymeLeafCRUD.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto {
    @NotEmpty(message = "The Name Field is Required")
    private String name;

    @NotEmpty(message = "The brand Field is Required")
    private String brand;

    @NotEmpty(message = "The Category Field is Required")
    private String category;

    @Min(0)
    private double price;

    @Size(min = 10, message = "Description should be contain atleaast 10 Characters")
    @Size(max=100, message = "Description shouldn't be exceed 100 Characters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    private MultipartFile imageFile;

}
