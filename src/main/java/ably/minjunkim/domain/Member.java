package ably.minjunkim.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_seq")
    private Long memberSeq; // 고객시퀀스
    @NotEmpty
    @Column(name = "login_id")
    private String loginId; //고객메일ID
    @NotEmpty
    private String password; // 패스워드
    @OneToMany(mappedBy = "member")
    private List<ItemWishBox> itemWishes = new ArrayList<>(); // 내 찜 박스 리스트

    @OneToMany(mappedBy = "member")
    private List<MemberWishList> memberWishList = new ArrayList<>(); // 찜 상품 리스트
}
