package com.alibaba.demo.dto.data;

import com.alibaba.demo.dto.page.PageDTO;
import lombok.Data;

@Data
public class ShardingOrderESQueryCmd extends PageDTO {

    private String keyword;

}
