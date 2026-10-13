package com.smartstudenthub.controller;

import com.smartstudenthub.dto.BookLoanRequest;
import com.smartstudenthub.dto.BookRequest;
import com.smartstudenthub.entity.Book;
import com.smartstudenthub.entity.LibraryLoan;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Library Management", description = "Book Catalog, Borrowing, Returns, and Overdue Tracking")
public class LibraryController {

    private final LibraryService libraryService;
    private final AuthService authService;

    public LibraryController(LibraryService libraryService, AuthService authService) {
        this.libraryService = libraryService;
        this.authService = authService;
    }

    @GetMapping("/books")
    @Operation(summary = "Search book catalog with pagination and category filtering")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("title").ascending());
        return ResponseEntity.ok(libraryService.searchBooks(query, category, pageRequest));
    }

    @GetMapping("/books/all")
    @Operation(summary = "Get list of all books in catalog")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    @PostMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add book to catalog (Admin only)")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @PostMapping("/loans/issue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Issue book to student with limit and stock check")
    public ResponseEntity<LibraryLoan> issueBook(@Valid @RequestBody BookLoanRequest request) {
        String issuer = authService.getCurrentUser().getFullName();
        return ResponseEntity.ok(libraryService.issueBook(request, issuer));
    }

    @PostMapping("/loans/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Return borrowed book and calculate overdue fines")
    public ResponseEntity<LibraryLoan> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.returnBook(id));
    }

    @GetMapping("/loans/student/{studentId}")
    @Operation(summary = "Get student borrowing history and active loans")
    public ResponseEntity<List<LibraryLoan>> getStudentLoans(@PathVariable Long studentId) {
        return ResponseEntity.ok(libraryService.getStudentLoans(studentId));
    }

    @GetMapping("/loans")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all library loan transactions (Admin only)")
    public ResponseEntity<List<LibraryLoan>> getAllLoans() {
        return ResponseEntity.ok(libraryService.getAllLoans());
    }
}
