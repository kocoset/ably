package ably.minjunkim.repository;

import ably.minjunkim.domain.Item;
import ably.minjunkim.domain.ItemWishBox;
import ably.minjunkim.domain.Member;
import ably.minjunkim.domain.MemberWishList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;
    public List<Item> findAll() { // 상품 리스트 전체조회
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
    public List<ItemWishBox> findWishBoxAll(Member member) { // 고객 내 찜 박스 전체 조회
        return em.createQuery("select i from ItemWishBox i join i.member m where m.memberSeq = :memberSeq", ItemWishBox.class)
                .setParameter("memberSeq", member.getMemberSeq())
                .getResultList();
    }
    public List<ItemWishBox> findByItemWishBox(Member member, ItemWishBox itemWishBox) { // 중복 고객 내 찜 박스 조회
        String jpql = "select i " +
                "        from ItemWishBox i join i.member m " +
                "       where m.memberSeq = :memberSeq " +
                "         and i.boxName   = :boxName";

        if (itemWishBox.getItemBoxSeq() != null) {
            jpql += " and i.itemBoxSeq != :itemBoxSeq";
        }

        TypedQuery<ItemWishBox> query = em.createQuery(jpql, ItemWishBox.class)
                .setParameter("memberSeq", member.getMemberSeq())
                .setParameter("boxName", itemWishBox.getBoxName());

        if (itemWishBox.getItemBoxSeq() != null) {
            query.setParameter("itemBoxSeq", itemWishBox.getItemBoxSeq());
        }

        return query.getResultList();
    }
    public String updateWishBox(ItemWishBox itemWishBox) { // 내 찜 박스 수정
        ItemWishBox itemWishBoxUpdate = em.find(ItemWishBox.class, itemWishBox.getItemBoxSeq());
        itemWishBoxUpdate.setBoxName(itemWishBox.getBoxName());
        em.persist(itemWishBoxUpdate);
        return itemWishBoxUpdate.getItemBoxSeq().toString();
    }
    public String insertWishBox(ItemWishBox itemWishBox) { // 내 찜 박스 삽입
        em.persist(itemWishBox);
        return itemWishBox.getItemBoxSeq().toString();
    }
    public void deleteWishBox(ItemWishBox itemWishBox) { // 내 찜 박스 삭제
        ItemWishBox itemWishBoxDelete = em.find(ItemWishBox.class, itemWishBox.getItemBoxSeq());
        em.remove(itemWishBoxDelete);
    }
    public List<MemberWishList> findByItemList(Long itemSeq, Long itemBoxSeq, Long memberSeq) { // 찜 상품 중복 조회(미완성)
        return em.createQuery("select i " +
                        "               from MemberWishList i " +
                        "               join i.member       m " +
                        "          left join i.item         t " +
                        "          left join i.itemWishBox  b " +
                        "              where m.memberSeq  = :memberSeq " +
                        "                and t.itemSeq    = :itemSeq " +
                        "                and b.itemBoxSeq = :itemBoxSeq", MemberWishList.class)
                .setParameter("memberSeq", memberSeq)
                .setParameter("itemSeq", itemSeq)
                .setParameter("itemBoxSeq", itemBoxSeq)
                .getResultList();
    }
}
