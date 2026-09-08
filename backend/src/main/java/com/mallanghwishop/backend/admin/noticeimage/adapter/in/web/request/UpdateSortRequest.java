package com.mallanghwishop.backend.admin.noticeimage.adapter.in.web.request;

import lombok.Data;
import java.util.List;

@Data
public class UpdateSortRequest {
    private List<Long> orderedIds;
}
