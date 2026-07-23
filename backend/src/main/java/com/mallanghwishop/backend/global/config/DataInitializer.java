package com.mallanghwishop.backend.global.config;

import com.mallanghwishop.backend.admin.cafe.adapter.out.persistence.CafeSettingsJpaRepository;
import com.mallanghwishop.backend.admin.cafe.domain.model.CafeSettings;
import com.mallanghwishop.backend.admin.category.adapter.out.persistence.AdminCategoryJpaRepository;
import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductImageJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductImageJpaRepository;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductJpaRepository;
import com.mallanghwishop.backend.member.adapter.out.persistence.MemberJpaRepository;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.admin.category.domain.model.AdminCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

        private final AdminCategoryJpaRepository categoryRepository;
        private final ProductJpaRepository productRepository;
        private final ProductImageJpaRepository productImageRepository;
        private final MemberJpaRepository memberRepository;
        private final CafeSettingsJpaRepository cafeSettingsRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                // 0. 카페 설정 초기화
                if (cafeSettingsRepository.count() == 0) {
                        cafeSettingsRepository.save(CafeSettings.builder()
                                        .cafeName("말랑이샵")
                                        .description("말랑이가 운영하는 귀여운 소품샵에 오신 걸 환영합니다!")
                                        .openTime(LocalTime.of(9, 0))
                                        .closeTime(LocalTime.of(22, 0))
                                        .manualClosed(false)
                                        .build());
                }

                // 1. 관리자 계정 생성 // 데이터가 하나도 없을 때만 실행됩니다.
                if (memberRepository.count() == 0) {
                        // 관리자 계정
                        memberRepository.save(Member.builder()
                                        .username("admin")
                                        .nickname("관리자")
                                        .password(passwordEncoder.encode("1234"))
                                        .email("admin@ncafe.com")
                                        .phoneNumber("010-0000-0000")
                                        .role("ROLE_ADMIN")
                                        .build());

                        // 테스트 유저 (heo)
                        memberRepository.save(Member.builder()
                                        .username("heo")
                                        .nickname("둥이")
                                        .password(passwordEncoder.encode("1234"))
                                        .email("heo@ncafe.com")
                                        .phoneNumber("010-1234-5678")
                                        .role("ROLE_USER")
                                        .build());
                }

                // 1. 카테고리 데이터 생성 (ID는 자동 발급됨)
                AdminCategory handmadeBowl = findOrCreateCategory("수제볼", "🔮", 1);
                AdminCategory etc = findOrCreateCategory("기타", "🎁", 2);

                // 2. 상품 및 이미지 데이터 생성
                // ─── 수제볼 ───
                saveMenu("보글거북이", "Bogle Turtle", "내맘대로 만드는 슬랑이! 커스텀 서비스로 원하는 촉감, 색상, 밀도 다 맞춰드려볼게요 💖", 12000, handmadeBowl.getId(), 1,
                                "/upload/images/bogle-turtle.png");
                
                saveMenu("귤", "Tangerine", "크런치 4.5점! 근무력 기준 글리 3~5ml(최대) 처방가능. 나눔드린 분들의 책상템, 빵꾸템으로 절찬 사용 중 🧡", 10000, handmadeBowl.getId(), 2,
                                "/upload/images/tangerine.png");
                                
                saveMenu("도키네도넛", "Doki's Donut", "크런치 3.5점! 퐁신마멜베이스와 세미크런치 조합! 대야르촉감 탄생 🍀", 11000, handmadeBowl.getId(), 3,
                                "/upload/images/dokine-donut.png");
                                
                saveMenu("설탕뿌토마토", "Sugared Tomato", "크런치 3.5점! 진공상태로 보내드립니다. 처음만 우둑하고 금방 보글거려요 ❤️ 비즈 그대로 느껴지면서 두부손분들까지 만족시키는 워녕이 만능템 🫶🏻", 11000, handmadeBowl.getId(), 4,
                                "/upload/images/sugar-tomato.png");

        }
        private AdminCategory findOrCreateCategory(String name, String icon, int sortOrder) {
                return categoryRepository.findByName(name)
                                .orElseGet(() -> categoryRepository.save(AdminCategory.builder()
                                                .name(name)
                                                .icon(icon)
                                                .sortOrder(sortOrder)
                                                .isActive(true)
                                                .build()));
        }

        private void saveMenu(String korName, String engName, String desc, int price, Long categoryId, int sortOrder,
                        String... imageUrls) {
                // 이미 존재하는 이름의 상품인 경우 건너뛰어 중복 방지 (사용자가 직접 수정한 데이터 보호)
                if (productRepository.existsByKorName(korName)) {
                        return;
                }

                ProductJpaEntity product = ProductJpaEntity.builder()
                                .korName(korName)
                                .engName(engName)
                                .description(desc)
                                .price(price)
                                .categoryId(categoryId)
                                .isAvailable(true)
                                .isSoldOut(false)
                                .sortOrder(sortOrder)
                                .build();
                product = productRepository.save(product);

                int imgOrder = 0;
                for (String url : imageUrls) {
                        ProductImageJpaEntity image = ProductImageJpaEntity.builder()
                                        .productId(product.getId())
                                        .srcUrl(url)
                                        .sortOrder(imgOrder++)
                                        .build();
                        productImageRepository.save(image);
                }
        }
}
