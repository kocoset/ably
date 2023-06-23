package ably.minjunkim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class ItemWishBox {
    @Id
    @GeneratedValue
    @Column(name = "item_box_seq")
    private Long itemBoxSeq; // 내 찜 박스 시퀀스
    @NotEmpty
    @Column(name = "box_name")
    private String boxName; // 내 찜 박스명
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_seq")
    private Member member; // 고객

    @OneToMany(mappedBy = "itemWishBox")
    private List<MemberWishList> memberWishLists = new ArrayList<>(); // 찜 상품 리스트
}
