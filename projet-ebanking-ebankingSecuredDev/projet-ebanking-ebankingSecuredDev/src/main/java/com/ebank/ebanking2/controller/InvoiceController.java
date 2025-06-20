package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.InvoiceService;
import com.ebank.ebanking2.model.dto.InvoiceDTO;
import com.ebank.ebanking2.model.dto.InvoicePayDTO;
import com.ebank.ebanking2.model.dto.InvoiceResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.method.P;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/invoices")
public class InvoiceController {


    @Autowired
    InvoiceService invoiceService;

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #clientId == authentication.principal.id)")
    @GetMapping("invoice/{clientId}/{provider}/{reference}")
    public ResponseEntity<InvoiceResDTO> getInvoice(@PathVariable("clientId") @P("clientId") Long clientId, @PathVariable("provider") String provider, @PathVariable("reference") String reference) {
        InvoiceResDTO invoice = invoiceService.getInvoice(clientId, provider, reference);
        System.out.println("invoice : "+invoice);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PreAuthorize("hasRole('CLIENT') and #invoicePayDTO.clientId == authentication.principal.id")
    @PutMapping("invoice/pay")
    public ResponseEntity<InvoiceResDTO> payInvoice(@RequestBody @P("invoicePayDTO") InvoicePayDTO invoicePayDTO){
        return new ResponseEntity<>(invoiceService.payInvoice(invoicePayDTO), HttpStatus.CREATED);
    }


    //bach tb9a tcree les invoices bach t3mr lbase de donnee wiwlli 3ndk b7al chi api
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("invoice")
    public ResponseEntity<InvoiceResDTO> addInvoice(@RequestBody InvoiceDTO invoiceDTO){
        return new ResponseEntity<>(invoiceService.addInvoice(invoiceDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("invoices")
    public ResponseEntity<List<InvoiceResDTO>> addInvoices(@RequestBody List<InvoiceDTO> invoiceDTOs){
        return ResponseEntity.ok(invoiceService.addInvoices(invoiceDTOs));
    }
    @PreAuthorize("hasRole('EMPLOYEE') or ( hasRole('CLIENT') and @compteService.getClientByCompteId(#id).id == authentication.principal.id)")
    @GetMapping("{id}")
    public ResponseEntity<Page<InvoiceResDTO>> getInvoicesByCompteId(@PathVariable("id") @P("id") Long id, @RequestParam("offset") Integer offset, @RequestParam("size") Integer size){
        return ResponseEntity.ok(invoiceService.getInvoicesByCompteId(id, offset, size));
    }
}
