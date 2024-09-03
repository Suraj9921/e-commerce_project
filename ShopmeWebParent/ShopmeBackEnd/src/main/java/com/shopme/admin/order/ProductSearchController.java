package com.shopme.admin.order;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.product.ProductService;

import java.util.List;

@Controller
public class ProductSearchController {

    @Autowired private ProductService service;
    @Autowired private CategoryService categoryService;

    @GetMapping("/orders/search_product")
    public String showSearchProductPage() {
        return "orders/search_product";
    }

    @PostMapping("/orders/search_product")
    public String searchProducts(String keyword) {
        return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
    }

//    @GetMapping("/orders/search_product/page/{pageNum}")
//    public String searchProductsByPage(@PagingAndSortingParam(listName = "listProducts",
//            moduleURL = "/orders/search_product") PagingAndSortingHelper helper,
//                                       @PathVariable(name = "pageNum") int pageNum) {
//        service.searchProducts(pageNum, helper);
//        return "orders/search_product";
//    }

    @GetMapping("/orders/search_product/page/{pageNum}")
    public String searchProductsByPage(Model model,
                                       @PathVariable(name = "pageNum") int pageNum,
                                       @Param("sortField") String sortField,
                                       @Param("sortDir") String sortDir,
                                       @Param("keyword") String keyword,@Param("categoryId") Integer categoryId) {
        Page<Product> page = service.listByPage(pageNum, sortField, sortDir,keyword,categoryId);
        List<Product> listProducts = page.getContent();
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("moduleURL","/orders/search_product");
        model.addAttribute("listCategories", listCategories);
        return "orders/search_product";
    }
}