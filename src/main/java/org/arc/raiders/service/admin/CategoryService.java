package org.arc.raiders.service.admin;

import org.arc.raiders.domain.admin.Category;
import org.arc.raiders.repository.admin.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("adminCategoryService")
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 전체 카테고리 조회
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ID로 카테고리 조회
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // 메인 카테고리만 조회
    @Transactional(readOnly = true)
    public List<Category> getMainCategories() {
        return categoryRepository.findMainCategories();
    }

    // 특정 카테고리의 서브카테고리 조회
    @Transactional(readOnly = true)
    public List<Category> getSubCategories(String categoryName) {
        return categoryRepository.findSubCategoriesByCategoryName(categoryName);
    }

    // 카테고리 생성
    public Category createCategory(String categoryName, String subCategoryName, String codeType) {
        // 중복 체크
        Category existing = categoryRepository.findByCategoryNameAndSubCategoryName(categoryName, subCategoryName);
        if (existing != null) {
            throw new RuntimeException("이미 존재하는 카테고리입니다.");
        }

        Category category = new Category(categoryName, subCategoryName, codeType);
        return categoryRepository.save(category);
    }

    // 카테고리 수정
    public Category updateCategory(Long id, String categoryName, String subCategoryName, String codeType) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + id));

        category.setCategoryName(categoryName);
        category.setSubCategoryName(subCategoryName);
        category.setCodeType(codeType);

        return categoryRepository.save(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("카테고리를 찾을 수 없습니다: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // 특정 카테고리명의 모든 항목 삭제 (메인 + 서브 모두)
    public void deleteCategoryByName(String categoryName) {
        List<Category> categories = categoryRepository.findByCategoryName(categoryName);
        categoryRepository.deleteAll(categories);
    }
}


