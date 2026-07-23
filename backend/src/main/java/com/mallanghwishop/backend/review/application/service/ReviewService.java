package com.mallanghwishop.backend.review.application.service;

import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.order.domain.model.OrderLineItem;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.review.adapter.in.web.dto.ReviewRequest;
import com.mallanghwishop.backend.review.adapter.in.web.dto.ReviewResponse;
import com.mallanghwishop.backend.review.domain.model.Review;
import com.mallanghwishop.backend.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LoadMemberPort loadMemberPort;
    private final OrderRepository orderRepository;
    private final LoadProductPort loadProductPort;


    /**
     * 주문에 포함된 상품 이름들을 조회하여 쉼표로 결합
     */
    private String resolveProductNames(Order order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            return null;
        }
        return order.getItems().stream()
                .map(OrderLineItem::getProductId)
                .distinct()
                .map(productId -> loadProductPort.findAvailableById(productId)
                        .map(Product::getKorName)
                        .orElse("삭제된 상품"))
                .collect(Collectors.joining(", "));
    }

    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewResponse createReview(String username, ReviewRequest request) {
        Member member = loadMemberPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 이미 리뷰를 작성한 주문인지 확인
        if (reviewRepository.existsByOrder_Id(request.getOrderId())) {
            throw new IllegalStateException("이미 리뷰를 작성한 주문입니다.");
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        Review review = Review.builder()
                .memberId(member.getId())
                .order(order)
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        Review saved = reviewRepository.save(review);
        log.info("Review created: orderId={}", request.getOrderId());

        String productNames = resolveProductNames(order);
        return ReviewResponse.from(saved, member.getNickname(), productNames);
    }

    /**
     * 내 리뷰 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getMyReviews(String username) {
        Member member = loadMemberPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return reviewRepository.findByMemberIdOrderByCreatedAtDesc(member.getId()).stream()
                .map(r -> {
                    String productNames = resolveProductNames(r.getOrder());
                    return ReviewResponse.from(r, member.getNickname(), productNames);
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 주문에 대한 리뷰 조회
     */
    @Transactional(readOnly = true)
    public ReviewResponse getReviewByOrderId(Long orderId, String username) {
        Member member = loadMemberPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Review review = reviewRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        String productNames = resolveProductNames(review.getOrder());
        return ReviewResponse.from(review, member.getNickname(), productNames);
    }


    /**
     * 특정 상품의 리뷰 목록 (공개 API)
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        return reviewRepository.findByOrder_Items_ProductIdOrderByCreatedAtDesc(productId).stream()
                .map(r -> {
                    String nickname = loadMemberPort.findById(r.getMemberId())
                            .map(Member::getNickname)
                            .orElse("익명");
                    String productNames = resolveProductNames(r.getOrder());
                    return ReviewResponse.from(r, nickname, productNames);
                })
                .collect(Collectors.toList());
    }
}
