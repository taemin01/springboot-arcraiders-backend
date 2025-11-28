package org.arc.raiders.repository.admin;

import org.arc.raiders.domain.admin.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("adminCategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // categoryName으로 조회
    List<Category> findByCategoryName(String categoryName);

    // codeType으로 조회
    List<Category> findByCodeType(String codeType);

    // 메인 카테고리만 조회 (sub_category_name이 NULL인 것들)
    @Query("SELECT c FROM Category c WHERE c.subCategoryName IS NULL")
    List<Category> findMainCategories();

    // 특정 카테고리의 서브카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.categoryName = :categoryName AND c.subCategoryName IS NOT NULL")
    List<Category> findSubCategoriesByCategoryName(String categoryName);

    // categoryName과 subCategoryName으로 조회
    Category findByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName);
}


