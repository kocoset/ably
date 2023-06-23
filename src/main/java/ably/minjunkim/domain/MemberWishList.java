package ably.minjunkim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class MemberWishList {
    @Id
    @GeneratedValue
    @Column(name = "member_wish_seq")
    private Long memberWishSeq; // 찜 상품 시퀀스

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_seq")
    private Member member; // 고객

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_box_seq")
    private ItemWishBox itemWishBox; // 내 찜 박스

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 상품
}
