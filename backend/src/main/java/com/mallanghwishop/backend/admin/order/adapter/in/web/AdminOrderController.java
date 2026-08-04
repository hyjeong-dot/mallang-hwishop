package com.mallanghwishop.backend.admin.order.adapter.in.web;

import com.mallanghwishop.backend.admin.order.application.port.in.GetAdminOrderListUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.UpdateOrderStatusUseCase;
import com.mallanghwishop.backend.admin.order.application.result.AdminOrderResult;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import com.mallanghwishop.backend.admin.order.application.service.AdminOrderExcelExportService;
import com.mallanghwishop.backend.admin.order.application.service.AdminOrderExcelImportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final GetAdminOrderListUseCase getAdminOrderListUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final OrderSseEmitters orderSseEmitters;
    private final AdminOrderExcelExportService excelExportService;
    private final AdminOrderExcelImportService excelImportService;

    @GetMapping("/excel/download")
    public ResponseEntity<InputStreamResource> downloadOrdersExcel() {
        List<AdminOrderResult> orders = getAdminOrderListUseCase.getAllOrders();
        java.io.ByteArrayInputStream in = excelExportService.exportOrdersToExcel(orders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=orders.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<?> uploadTrackingNumbers(@RequestParam("file") MultipartFile file) {
        excelImportService.importTrackingNumbers(file);
        orderSseEmitters.notify("status-changed");
        return ResponseEntity.ok(Map.of("success", true, "message", "송장 번호가 일괄 등록되었습니다."));
    }

    @GetMapping
    public List<AdminOrderResult> getOrders() {
        return getAdminOrderListUseCase.getAllOrders();
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, 
                             @RequestParam OrderStatus status,
                             @RequestParam(required = false) String cancelReason,
                             @RequestParam(required = false) String cancelReasonType) {
        updateOrderStatusUseCase.updateStatus(id, status, cancelReason, cancelReasonType);
        // 상태 변경 시 모든 SSE 구독자에게 알림
        orderSseEmitters.notify("status-changed");
    }

    /**
     * 관리자 주문 SSE 스트림 구독
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrders() {
        return orderSseEmitters.add();
    }
}
