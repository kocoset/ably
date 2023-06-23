package ably.minjunkim.service;

import ably.minjunkim.domain.Item;
import ably.minjunkim.domain.ItemWishBox;
import ably.minjunkim.domain.Member;
import ably.minjunkim.domain.MemberWishList;
import ably.minjunkim.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    public List<Item> findAll() { // 상품 전체 조회
        return itemRepository.findAll();
    }
    public List<ItemWishBox> findWishBoxAll(Member member) { // 내 찜 상품 박스 전체 조회
        return itemRepository.findWishBoxAll(member);
    }
    @Transactional
    public String updateWishBox(Member member, ItemWishBox itemWishBox) { // 내 찜 상품 박스 수정
        String message = validateDuplicateItemWishBox(member, itemWishBox); //회원 별 중복 박스명 체크
        if(message.equals("ok")) {
            return itemRepository.updateWishBox(itemWishBox);
        }
        return message;
    }

    @Transactional
    public String insertWishBox(Member member, ItemWishBox itemWishBox) { // 내 찜 상품 박스 삽입
        String message = validateDuplicateItemWishBox(member, itemWishBox); //회원 별 중복 박스명 체크
        if(message.equals("ok")) {
            itemWishBox.setMember(member);
            return itemRepository.insertWishBox(itemWishBox);
        }
        return message;
    }

    @Transactional
    public void deleteWishBox(Member member, ItemWishBox itemWishBox) { // 내 찜 상품 박스 삭제
        itemRepository.deleteWishBox(itemWishBox);
    }

    @Transactional
    public String insertWishItem(Long itemSeq, Long itemBoxSeq, Long memberSeq) { // 찜 상품 추가(미완성)
        String message = validateDuplicateItemList(itemSeq, itemBoxSeq, memberSeq); //회원 별 중복 박스명 체크
        if(message.equals("ok")) {
            //return itemRepository.insertWishItem(itemSeq, itemBoxSeq);
        }
        return message;
    }

    private String validateDuplicateItemWishBox(Member member, ItemWishBox itemWishBox) { // 중복 박스 체크 로직
        List<ItemWishBox> findItemWishBox = itemRepository.findByItemWishBox(member, itemWishBox);
        if (!findItemWishBox.isEmpty()) {
            return "000000";
        }
        return "ok";
    }

    private String validateDuplicateItemList(Long itemSeq, Long itemBoxSeq, Long memberSeq) { // 중복 상품 체크 로직
        List<MemberWishList> findItemList = itemRepository.findByItemList(itemSeq, itemBoxSeq, memberSeq);
        if (!findItemList.isEmpty()) {
            return "000000";
        }
        return "ok";
    }
}
