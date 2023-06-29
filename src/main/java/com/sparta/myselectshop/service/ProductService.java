package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        Product product = productRepository.save(new Product(productRequestDto, user));

        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto productMypriceRequestDto) {
        int myprice = productMypriceRequestDto.getMyprice();

        if(myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해주십쇼.");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품을 찾을 수 없습니다."));

        product.update(productMypriceRequestDto);

        return new ProductResponseDto(product);
    }

    /*
        readOnly = true : 조회를 효율적으로 하기 위해서 = 성능을 높이기위해
        ProductResponseDto에서 OneToMany  ProductFolder List의 값을 가져오기 때문에
        지연로딩되어서 트랜잭션환경이 필요하다.?
    */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if(userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user,pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

        /*List<Product> productList = productRepository.findAllByUser(user);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }*/

        //return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
        //return responseDtoList;

        return productList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다.")
        );

        product.updateByItemDto(itemDto);
    }

    /* Admin 계정으로 접근시 */
    /*public List<ProductResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }

        //return productRepository.findAll().stream().map(ProductResponseDto::new).toList();

        return responseDtoList;
    }*/
}
