package com.mallanghwishop.backend.admin.order.adapter.in.web;

import com.mallanghwishop.backend.admin.order.application.port.in.GetAdminOrderListUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.UpdateOrderStatusUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.CreateShippingGroupUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.RefundShippingGroupUseCase;
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
    private final CreateShippingGroupUseCase createShippingGroupUseCase;
    private final RefundShippingGroupUseCase refundShippingGroupUseCase;

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

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<?> updateTrackingInfo(@PathVariable Long id,
                                                @RequestBody Map<String, String> request) {
        String trackingCarrier = request.get("trackingCarrier");
        String trackingNumber = request.get("trackingNumber");
        updateOrderStatusUseCase.updateTrackingInfo(id, trackingCarrier, trackingNumber);
        orderSseEmitters.notify("status-changed");
        return ResponseEntity.ok(Map.of("success", true, "message", "송장 정보가 수정되었습니다."));
    }

    @PostMapping("/batch/status")
    public ResponseEntity<?> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        List<Integer> orderIdsInt = (List<Integer>) request.get("orderIds");
        List<Long> orderIds = orderIdsInt.stream().map(Integer::longValue).toList();
        OrderStatus status = OrderStatus.valueOf((String) request.get("status"));
        
        updateOrderStatusUseCase.batchUpdateStatus(orderIds, status);
        orderSseEmitters.notify("status-changed");
        return ResponseEntity.ok(Map.of("success", true, "message", "선택한 주문 상태가 일괄 변경되었습니다."));
    }

    /**
     * 관리자 주문 SSE 스트림 구독
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrders() {
        return orderSseEmitters.add();
    }

    // --- 합배송 관리 ---

    @PostMapping("/shipping-groups")
    public ResponseEntity<?> createShippingGroup(@RequestBody Map<String, Object> request) {
        List<Integer> orderIdsInt = (List<Integer>) request.get("orderIds");
        List<Long> orderIds = orderIdsInt.stream().map(Integer::longValue).toList();
        
        CreateShippingGroupUseCase.CreateShippingGroupCommand command = CreateShippingGroupUseCase.CreateShippingGroupCommand.builder()
                .orderIds(orderIds)
                .build();
        createShippingGroupUseCase.createShippingGroup(command);
        return ResponseEntity.ok(Map.of("success", true, "message", "합배송 그룹이 생성되었습니다."));
    }

    @PatchMapping("/shipping-groups/{id}/refund")
    public ResponseEntity<?> refundShippingGroup(@PathVariable Long id) {
        refundShippingGroupUseCase.refundShippingGroup(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "합배송 배송비 수동 환불이 완료되었습니다."));
    }
}
