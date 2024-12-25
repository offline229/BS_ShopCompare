package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * 查询按创建时间降序排列的商品列表（分页）
     * @param pageable 分页请求对象
     * @return 商品分页结果
     */
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 如果你需要支持按其他字段进行排序或过滤，可以扩展类似的方法
    // 例如按商品类别分类分页查询：
    Page<Product> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    // 例如按平台和类别过滤分页查询：
    Page<Product> findByPlatformAndCategoryOrderByCreatedAtDesc(String platform, String category, Pageable pageable);
}
