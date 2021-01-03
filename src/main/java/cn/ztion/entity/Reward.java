package cn.ztion.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Reward {
    private String name;
    private String icon;
    private Integer cnt;
}
