package com.example.auctionappdemo.auction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuctionController {

    private AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/auctions")
    public String auctions(Model model, AuctionFilters auctionFilters, Pageable pageable) {

        Page<Auction> page = auctionService.findAllForFiltersAndSort(auctionFilters, pageable);

        model.addAttribute("carsPage", page);
        model.addAttribute("filters", auctionFilters);
        model.addAttribute("pageable", pageable);
        return "auctions";
    }
}
