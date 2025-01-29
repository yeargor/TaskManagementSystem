package org.example.controller;

import org.example.model.tickets.Ticket;
import org.example.service.tickets.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public String getTicket(@RequestParam("id") Integer id, Model model){
        Ticket ticket = ticketService.getTicket(id);
        model.addAttribute("tickets", ticketService.convertTicketToList(ticket));
        return "ticketDetails";
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllTickets(Model model){
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "ticketDetails";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('CLIENT')")
    public String createTicketPage(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticketCreation";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CLIENT')")
    public String createTicket(@ModelAttribute Ticket ticket) {
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

    @GetMapping("/update")
    @PreAuthorize("hasRole('CLIENT')")
    public String updateTicketPage(@RequestParam("id") int id, Model model) {
        Ticket ticket = ticketService.getTicket(id);
        model.addAttribute("ticket", ticket);
        return "ticketUpdate";
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('CLIENT')")
    public String updateTicket(@ModelAttribute Ticket ticket) {
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@RequestParam("id") int id) {
        ticketService.deleteTicket(id);
        return "redirect:/";
    }
}
