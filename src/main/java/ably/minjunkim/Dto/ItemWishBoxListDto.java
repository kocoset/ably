package ably.minjunkim.Dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemWishBoxListDto {
    private Long itemBoxSeq; // 상품시퀀스
    private String boxName; // 내 찜 박스명
}
