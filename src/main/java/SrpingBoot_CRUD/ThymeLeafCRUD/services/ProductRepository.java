package SrpingBoot_CRUD.ThymeLeafCRUD.services;

import SrpingBoot_CRUD.ThymeLeafCRUD.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
