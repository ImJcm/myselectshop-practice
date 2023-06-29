package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByUser(User user, Pageable pageable);
    /*
        select
            p.id,
            p.title as product_title,
            pf.product_id as product_id,
            pf.folder_id as folder_id
        from product as p left join product_folder as pf
            on p.id = pf.product_id
        where p.user_id = ?
            and
            pf.folder_id = ?
        order by p.id {desc or asc}
            limit 0, 10;

        //limit {시작 위치} {출력 갯수}
    */
    Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}
