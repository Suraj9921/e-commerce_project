package com.shopme.admin.review;

import com.shopme.admin.customer.CustomerService;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

import java.util.List;

@Controller
public class ReviewController {
    private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";

    @Autowired private ReviewService service;

    @GetMapping("/reviews")
    public String listFirstPage(Model model) {
        return defaultRedirectURL;
    }

//    @GetMapping("/reviews/page/{pageNum}")
//    public String listByPage(
//            @PagingAndSortingParam(listName = "listReviews", moduleURL = "/reviews") PagingAndSortingHelper helper,
//            @PathVariable(name = "pageNum") int pageNum) {
//
//        service.listByPage(pageNum, helper);
//
//        return "reviews/reviews";
//    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        Page<Review> page = service.listByPage(pageNum, sortField, sortDir,keyword);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;

        long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listReviews",listReviews);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("moduleURL","/reviews");

//        model.addAttribute("listCategories", listCategories);

        return "reviews/reviews";
    }

    @GetMapping("/reviews/detail/{id}")
    public String viewReview(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Review review = service.get(id);
            model.addAttribute("review", review);

            return "reviews/review_detail_modal";
        } catch (ReviewNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Review review = service.get(id);

            model.addAttribute("review", review);
            model.addAttribute("pageTitle", String.format("Edit Review (ID: %d)", id));

            return "reviews/review_form";
        } catch (ReviewNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @PostMapping("/reviews/save")
    public String saveReview(Review reviewInForm, RedirectAttributes ra) {
        service.save(reviewInForm);
        ra.addFlashAttribute("message", "The review ID " + reviewInForm.getId() + " has been updated successfully.");
        return defaultRedirectURL;
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "The review ID " + id + " has been deleted.");
        } catch (ReviewNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }
}