package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Shoes;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){

        model.addAttribute("form", new ShoesForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(ShoesForm form){

        Shoes Shoe = new Shoes();
        Shoe.setName(form.getName());
        Shoe.setPrice(form.getPrice());
        Shoe.setStockQuantity(form.getStockQuantity());
        Shoe.setBrand(form.getBrand());
        Shoe.setCollaboration(form.getCollaboration());

        itemService.saveItem(Shoe);
        return "redirect:/items";

    }

    @GetMapping("/items")
    private String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
    /*
    상품 수정폼
     */

    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Shoes item = (Shoes)itemService.findOne(itemId);
        ShoesForm form = new ShoesForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setBrand(item.getBrand());
        form.setCollaboration(item.getCollaboration());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    /*
    상품 수정
     */
/*    @PostMapping(value = "items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form){

        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }*/
    /*
    상품 수정 개선
     */
    @PostMapping(value = "items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") ShoesForm form){
        itemService.updateItem(form.getId(), form.getName(), form.getPrice());
        return "redirect:/items";
    }
}
