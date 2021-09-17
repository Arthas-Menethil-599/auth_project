package org.itstep.auth_project.controllers;

import org.itstep.auth_project.config.StaticConfig;
import org.itstep.auth_project.entities.BookingEntity;
import org.itstep.auth_project.services.BookingService;
import org.itstep.auth_project.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("booking")
public class BookingController {

    private final BookingService bookingService;
    private final SearchService searchService;

    public BookingController(BookingService service, SearchService searchService) {
        this.bookingService = service;
        this.searchService = searchService;
    }

    @GetMapping(value = "/index")
    public String getBookingIndexPage(Model model) {
        model.addAttribute("bookingEntity", new BookingEntity());
        var bookings = bookingService.getBookingEntities();
        model.addAttribute("bookings", bookings);
        return "booking/index";
    }

    @PostMapping(value = "add")
    public String addBooking(@ModelAttribute("bookingEntity") BookingEntity bookingEntity){
        bookingService.addBookingEntity(bookingEntity);
        return "redirect:/booking/index";
    }

    @PostMapping(value = "update")
    public String updateBooking(@ModelAttribute("bookingEntity") BookingEntity bookingEntity){
        bookingService.saveBookingEntity(bookingEntity);
        return "redirect:/booking/index";
    }

    @GetMapping(value = "/details/{id}")
    public String getDetailsPage(@PathVariable("id") Long id, Model model) {
        var bookingEntity = bookingService.getBookingEntity(id);
        model.addAttribute("bookingEntity", bookingEntity);
        return "booking/details";
    }

    @PostMapping(value = "/delete")
    public String deleteBooking(@RequestParam("id") Long id) {
        var bookingEntity = bookingService.getBookingEntity(id);
        bookingService.deleteBookingEntity(bookingEntity);
        return "redirect:/booking/index";
    }

    @GetMapping("/search")
    public String doSearch( @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "name", defaultValue = "", required = false) String name,
                            @RequestParam(value = "description", defaultValue = "", required = false) String description,
                            @RequestParam(value = "rooms_from", defaultValue = "0", required = false) Integer roomsFrom,
                            @RequestParam(value = "rooms_to", defaultValue = "0", required = false) Integer roomsTo,
                            @RequestParam(value = "price_from", defaultValue = "0", required = false) Integer priceFrom,
                            @RequestParam(value = "price_to", defaultValue = "0", required = false) Integer priceTo,
                            Model model) {

        var finalPage = page - 1;

        if(finalPage<0) {
            finalPage=0;
        }

        var size = searchService.countBookingEntities(name,description,roomsFrom,roomsTo,priceFrom,priceTo);

        var bookingEntities = searchService.searchBookingEntities(finalPage,StaticConfig.PAGE_SIZE,name,description,roomsFrom,roomsTo,priceFrom,priceTo);

        int pageCount = (size - 1) / StaticConfig.PAGE_SIZE + 1;

        model.addAttribute("bookingEntities", bookingEntities);
        model.addAttribute("pageCount", pageCount);

        return "search";
    }

}
