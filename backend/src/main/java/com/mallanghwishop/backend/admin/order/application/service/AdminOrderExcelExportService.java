package com.mallanghwishop.backend.admin.order.application.service;

import com.mallanghwishop.backend.admin.order.application.result.AdminOrderLineItemResult;
import com.mallanghwishop.backend.admin.order.application.result.AdminOrderResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AdminOrderExcelExportService {

    public ByteArrayInputStream exportOrdersToExcel(List<AdminOrderResult> orders) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("주문 내역");

            // 헤더 스타일
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 헤더 생성
            String[] headers = {
                    "주문번호 (ID)", "주문 고유번호", "주문상태", "수령인", "연락처", 
                    "우편번호", "주소", "상세주소", "배송요청사항", 
                    "주문상품", "총 결제금액", "택배사", "송장번호"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 데이터 채우기
            int rowIdx = 1;
            for (AdminOrderResult order : orders) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getOrderUid());
                row.createCell(2).setCellValue(order.getStatusLabel());
                row.createCell(3).setCellValue(order.getRecipientName());
                row.createCell(4).setCellValue(order.getPhoneNumber());
                row.createCell(5).setCellValue(order.getZipcode());
                row.createCell(6).setCellValue(order.getAddress());
                row.createCell(7).setCellValue(order.getDetailAddress() != null ? order.getDetailAddress() : "");
                row.createCell(8).setCellValue(order.getRequestMemo() != null ? order.getRequestMemo() : "");
                
                // 주문 상품 요약 (예: 상품명 2개 등)
                StringBuilder itemsSummary = new StringBuilder();
                for (AdminOrderLineItemResult item : order.getItems()) {
                    if (itemsSummary.length() > 0) itemsSummary.append(", ");
                    itemsSummary.append(item.getProductName()).append(" (").append(item.getQuantity()).append("개)");
                }
                row.createCell(9).setCellValue(itemsSummary.toString());
                row.createCell(10).setCellValue(order.getTotalPrice());
                
                // 택배사와 송장번호
                row.createCell(11).setCellValue(order.getTrackingCarrier() != null ? order.getTrackingCarrier() : "");
                row.createCell(12).setCellValue(order.getTrackingNumber() != null ? order.getTrackingNumber() : "");
            }

            // 컬럼 너비 자동 맞춤
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Excel 엑셀 파일 생성 중 오류가 발생했습니다.", e);
        }
    }
}
