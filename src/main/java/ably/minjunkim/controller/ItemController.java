package ably.minjunkim.controller;

import ably.minjunkim.Dto.ItemWishBoxListDto;
import ably.minjunkim.domain.Item;
import ably.minjunkim.domain.ItemWishBox;
import ably.minjunkim.domain.Member;
import ably.minjunkim.service.ItemService;
import ably.minjunkim.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    @GetMapping("/items")
    public String items(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // 상품 목록 및 내 찜 박스 조회
        
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        List<Item> items = itemService.findAll();
        List<ItemWishBox> itemWishBoxs = itemService.findWishBoxAll(loginMember);
        model.addAttribute("items", items);
        model.addAttribute("itemWishBoxs", itemWishBoxs);

        return "item/items";
    }

    @PostMapping("/updateWishBox")
    @ResponseBody
    public String updateWishBox(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, ItemWishBox itemWishBox) {
        // 내 찜 박스 수정
        
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        return itemService.updateWishBox(loginMember, itemWishBox);
    }

    @PostMapping("/insertWishBox")
    @ResponseBody
    public String insertWishBox(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, ItemWishBox itemWishBox) {
        // 내 찜 박스 삽입
        
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        return itemService.insertWishBox(loginMember, itemWishBox);
    }

    @PostMapping("/deleteWishBox")
    @ResponseBody
    public String deleteWishBox(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, ItemWishBox itemWishBox) {
        // 내 찜 박스 삭제
        
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        itemService.deleteWishBox(loginMember, itemWishBox);

        return "item/items";
    }

    @GetMapping("/wishList")
    public String wishList(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // 찜 상품 화면 이동
        
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        List<Item> items = itemService.findAll();
        List<ItemWishBox> itemWishBoxs = itemService.findWishBoxAll(loginMember);
        model.addAttribute("items", items);
        model.addAttribute("itemWishBoxs", itemWishBoxs);

        return "members/wishList";
    }

    @PostMapping("/checkWishBox")
    @ResponseBody
    public List<ItemWishBoxListDto> checkWishBox(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // 찜 상품 중복 체크
        
        List<ItemWishBoxListDto> itemWishBoxListDtos = new ArrayList<>();
        List<ItemWishBox> wishBoxAlls = itemService.findWishBoxAll(loginMember);

        for (ItemWishBox wishBoxAll: wishBoxAlls) {
            ItemWishBoxListDto itemWishBoxListDto = new ItemWishBoxListDto();
            itemWishBoxListDto.setItemBoxSeq(wishBoxAll.getItemBoxSeq());
            itemWishBoxListDto.setBoxName(wishBoxAll.getBoxName());
            itemWishBoxListDtos.add(itemWishBoxListDto);
        }

        return itemWishBoxListDtos;
    }

    @PostMapping("/addWishItem")
    @ResponseBody
    public String insertWishItem(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Long itemSeq, Long itemBoxSeq) {
        // 찜 상품 추가

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

       return itemService.insertWishItem(itemSeq, itemBoxSeq, loginMember.getMemberSeq());
    }
}
