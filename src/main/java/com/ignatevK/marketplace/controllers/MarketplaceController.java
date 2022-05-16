package com.ignatevK.marketplace.controllers;

import com.ignatevK.marketplace.ProductPost;
import com.ignatevK.marketplace.models.Product;
import com.ignatevK.marketplace.repo.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MarketplaceController {
    @Autowired
    private ProductRepository productRepository;
    private ArrayList<ProductPost> market = new ArrayList<>();

    @GetMapping("/product")
    public String productMain(Model model) {

        Iterable<Product> products = productRepository.findAll(); // check all products
        model.addAttribute("products", products);
        return "product-main";
    }

    @GetMapping("/product/add")
    public String productAdd(Model model) {
        return "product-add";
    }

    @PostMapping("product/add")
    public String productPostAdd(@RequestParam String productType, @RequestParam String productModel, @RequestParam String
            color, @RequestParam String brand, @RequestParam int yearOfCreation, @RequestParam double
            weight, Model model) {

        Product product = new Product(yearOfCreation, productType, productModel, color, brand, weight);
        productRepository.save(product); // add product in DB
        return "redirect:/product";

    }

    @GetMapping("/product/search")
    public String productSearch(Model model) {
        return "product-search";
    }

    // our function for searching anything products in shop
    @PostMapping("product/search")
    public String productPostSearch(@RequestParam String requestParam, Model model) throws IOException {

        market.clear();
        String search = requestParam;
        String url = "https://topdisc.ru/catalog/search/?q=" + search;
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla")
                .cookie("cookiename", "val234")
                .referrer("https://www.holodilnik.ru/")
                .header("headersecurity", "xyz123")
                .get(); //connect with site
        Elements items = doc.getElementsByClass("item column text-center hover-elements");
        Byte rending = 0;

        for (Element item : items) {
            ProductPost productPost = new ProductPost();
            Elements inf = item.getElementsByClass("name");
            String link = "https://topdisc.ru" + inf.attr("href");
            String name = inf.text();
            name = name.substring(name.length() / 2 + 1);
            String price = item.getElementsByClass("price").text();
            price = price.substring(0,price.indexOf("ру")+4);
            String imagine = item.getElementsByTag("img").attr("src");
            rending++;
            productPost.setDetailsLink(link);
            productPost.setTitle(name);
            productPost.setImagineLink(imagine);
            productPost.setPrice(price);

            market.add(productPost);
            if (rending == 12) {
                break;
            }

        }

        return "redirect:/product/result";
    }

    @GetMapping("/product/result")
    public String productResult(Model model) {

        ArrayList<ProductPost> copy = market;
        Iterable<ProductPost> productPosts = copy;
        model.addAttribute("productPosts", productPosts);

        return "product-result";
    }


    // in this function we are working with a certain product
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable(value = "id") long id, Model model) {

        if(!productRepository.existsById(id)) {
            return "redirect:/blog";
        }

        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> res = new ArrayList<>();
        product.ifPresent(res::add);
        model.addAttribute("product", res);
        return "product-details";
    }

    @GetMapping("/product/{id}/edit")
    public String productEdit(@PathVariable(value = "id") long id, Model model) {
        if(!productRepository.existsById(id)) {
            return "redirect:/product";
        }

        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> res = new ArrayList<>();
        product.ifPresent(res::add);
        model.addAttribute("product", res);
        return "product-edit";
    }

    @PostMapping("product/{id}/edit")
    public String productPostUpdate(@PathVariable(value = "id") long id, @RequestParam String productType,
                                    @RequestParam String productModel, @RequestParam String color,
                                    @RequestParam String brand, @RequestParam int yearOfCreation, @RequestParam double
                                                weight, Model model){

        Product product = productRepository.findById(id).orElseThrow();
        product.setBrand(brand);
        product.setProductType(productType);
        product.setProductModel(productModel);
        product.setColor(color);
        product.setWeight(weight);
        product.setYearOfCreation(yearOfCreation);
        productRepository.save(product);

        return "redirect:/product";
    }

    @PostMapping("product/{id}/remove")
    public String productPostRemove(@PathVariable(value = "id") long id, Model model){

        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);

        return "redirect:/product";

    }

}
