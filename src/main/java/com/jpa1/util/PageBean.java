package com.jpa1.util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T> {
    List<T> data;
    Integer pageNo;
    Integer pageSize;
    Long totalCount;

    /**
     * org.springframework.data.domain.Page -> PageBean
     *
     * @return
     */
    /**
     * org.springframework.data.domain.Page -> PageBean
     *
     * @param page
     * @return
     */
    //这是我自己写的
    public static <T> PageBean<T> toPageBean2(Page<T> page){
        return PageBean.<T>builder()
                .data(page.getContent())
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements()).build();
    }

    public static <T> PageBean<T> toPageBean(Page<T> page) {
        return PageBean.<T>builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
//                .currentPageSize(page.getNumberOfElements())
//                .totalPage(page.getTotalPages())
                .data(page.getContent())
                .totalCount(page.getTotalElements())
                .build();
    }
}