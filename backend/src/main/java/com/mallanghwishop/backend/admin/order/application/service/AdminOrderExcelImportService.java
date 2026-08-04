package com.mallanghwishop.backend.admin.order.application.service;

import com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrderExcelImportService {

    private final OrderRepository orderRepository;
    private final AdminOrderService adminOrderService;

    @Transactional
    public void importTrackingNumbers(MultipartFile file) {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 첫 번째 행은 헤더이므로 두 번째 행(index 1)부터 읽음
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 엑셀에서 다운로드 받았던 구조와 동일해야 함
                // 인덱스 0: 주문번호(ID)
                // 인덱스 11: 택배사
                // 인덱스 12: 송장번호
                
                Cell idCell = row.getCell(0);
                if (idCell == null) continue;

                Long orderId = -1L;
                if (idCell.getCellType() == CellType.NUMERIC) {
                    orderId = (long) idCell.getNumericCellValue();
                } else if (idCell.getCellType() == CellType.STRING) {
                    try {
                        orderId = Long.parseLong(idCell.getStringCellValue().trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }

                if (orderId <= 0) continue;

                Cell carrierCell = row.getCell(11);
                Cell trackingNumCell = row.getCell(12);

                String trackingCarrier = carrierCell != null ? getCellValueAsString(carrierCell) : "";
                String trackingNumber = trackingNumCell != null ? getCellValueAsString(trackingNumCell) : "";

                // 송장 번호가 비어있지 않다면 상태를 SHIPPED(배송 중) 또는 배송 완료 등 상황에 맞게 업데이트
                if (!trackingNumber.isEmpty()) {
                    Optional<OrderJpaEntity> optionalOrder = orderRepository.findById(orderId);
                    if (optionalOrder.isPresent()) {
                        OrderJpaEntity order = optionalOrder.get();
                        
                        // 이미 취소된 주문 등은 스킵
                        if (order.getStatus() == OrderStatus.CANCELLED) {
                            continue;
                        }

                        // Order 상태 업데이트 
                        // 어드민 서비스의 updateStatus를 이용하거나 직접 처리
                        // 송장번호 저장
                        order.setTrackingCarrier(trackingCarrier);
                        order.setTrackingNumber(trackingNumber);
                        
                        // 상태를 배송 중으로 변경
                        if (order.getStatus() != OrderStatus.COMPLETED) {
                            // adminOrderService를 통해 상태 변경 (포인트 적립 등 연동을 위해)
                            adminOrderService.updateStatus(order.getId(), OrderStatus.COMPLETED, null, null);
                        } else {
                            orderRepository.save(order);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse excel file", e);
            throw new RuntimeException("엑셀 파일 파싱 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 숫자의 경우 소수점이 나올 수 있으므로 정수 처리
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
