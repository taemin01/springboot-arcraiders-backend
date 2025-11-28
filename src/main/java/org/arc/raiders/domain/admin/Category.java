package org.arc.raiders.domain.admin;

import jakarta.persistence.*;

@Entity
@Table(name = "\"CATEGORY\"")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "sub_category_name", length = 50)
    private String subCategoryName;

    @Column(name = "code_type", nullable = false, length = 10)
    private String codeType;

    // 기본 생성자
    public Category() {
    }

    // 생성자
    public Category(String categoryName, String subCategoryName, String codeType) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.codeType = codeType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", codeType='" + codeType + '\'' +
                '}';
    }
}


