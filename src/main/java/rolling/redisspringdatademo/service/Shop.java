package rolling.redisspringdatademo.service;

import lombok.Data;

@Data
public class Shop {
    private String id;

    private String name;

    private Long typeId;

    private String images;
}
