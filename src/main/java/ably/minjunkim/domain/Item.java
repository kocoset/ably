package ably.minjunkim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_seq")
    private Long itemSeq; // 상품시퀀스
    @Column(name = "item_name")
    private String itemName; // 상품명
    private String thumbnail; // 썸네일 이미지
    private Integer price; // 상품가격

    @ManyToMany(mappedBy = "item")
    private List<MemberWishList> memberWishList = new ArrayList<>(); // 고객 찜 리스트
}
