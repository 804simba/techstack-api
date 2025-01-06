package com.techstack.api.payload.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Builder
@Data
public class PaginatedData<T> {
    private long total;
    private long page;
    private List<T> data;

    public PaginatedData(long total, long page, List<T> data) {
        this.total = total;
        this.page = page;
        this.data = data;
    }
}
