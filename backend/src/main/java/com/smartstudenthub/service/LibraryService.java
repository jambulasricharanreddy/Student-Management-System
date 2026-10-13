package com.smartstudenthub.service;

import com.smartstudenthub.dto.BookLoanRequest;
import com.smartstudenthub.dto.BookRequest;
import com.smartstudenthub.entity.Book;
import com.smartstudenthub.entity.LibraryLoan;
import com.smartstudenthub.entity.Student;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.BookRepository;
import com.smartstudenthub.repository.LibraryLoanRepository;
import com.smartstudenthub.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final LibraryLoanRepository loanRepository;
    private final StudentRepository studentRepository;

    private static final int MAX_ACTIVE_LOANS_PER_STUDENT = 4;
    private static final BigDecimal DAILY_OVERDUE_FINE = new BigDecimal("5.00");

    public LibraryService(BookRepository bookRepository, LibraryLoanRepository loanRepository, StudentRepository studentRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.studentRepository = studentRepository;
    }

    public Page<Book> searchBooks(String query, String category, Pageable pageable) {
        return bookRepository.searchBooks(query, category, pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Transactional
    public Book createBook(BookRequest request) {
        if (request.getIsbn() != null && !request.getIsbn().trim().isEmpty() && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ConflictException("ISBN already exists: " + request.getIsbn());
        }

        Book book = new Book(
                request.getIsbn(),
                request.getTitle(),
                request.getAuthor(),
                request.getCategory(),
                request.getPublisher(),
                request.getPublicationYear(),
                request.getTotalCopies() != null ? request.getTotalCopies() : 1,
                request.getShelfLocation()
        );
        return bookRepository.save(book);
    }

    @Transactional
    public synchronized LibraryLoan issueBook(BookLoanRequest request, String issuedBy) {
        Book book = getBookById(request.getBookId());
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        long activeLoans = loanRepository.countByStudentIdAndStatus(student.getId(), "ISSUED");
        if (activeLoans >= MAX_ACTIVE_LOANS_PER_STUDENT) {
            throw new BadRequestException("Student has reached the maximum borrowing limit of " + MAX_ACTIVE_LOANS_PER_STUDENT + " books.");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new ConflictException("No available copies of '" + book.getTitle() + "' in stock.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        LocalDate now = LocalDate.now();
        LocalDate due = request.getDueDate() != null ? request.getDueDate() : now.plusDays(14);

        LibraryLoan loan = new LibraryLoan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setIssuedDate(now);
        loan.setDueDate(due);
        loan.setStatus("ISSUED");
        loan.setIssuedBy(issuedBy);
        loan.setFineAmount(BigDecimal.ZERO);

        return loanRepository.save(loan);
    }

    @Transactional
    public synchronized LibraryLoan returnBook(Long loanId) {
        LibraryLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan record not found"));

        if (!"ISSUED".equalsIgnoreCase(loan.getStatus()) && !"OVERDUE".equalsIgnoreCase(loan.getStatus())) {
            throw new BadRequestException("This book has already been marked as returned");
        }

        LocalDate now = LocalDate.now();
        loan.setReturnDate(now);

        if (now.isAfter(loan.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), now);
            BigDecimal fine = DAILY_OVERDUE_FINE.multiply(BigDecimal.valueOf(daysOverdue));
            loan.setFineAmount(fine);
            loan.setStatus("RETURNED_LATE");
        } else {
            loan.setStatus("RETURNED");
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<LibraryLoan> getStudentLoans(Long studentId) {
        return loanRepository.findByStudentId(studentId);
    }

    public List<LibraryLoan> getAllLoans() {
        return loanRepository.findAll();
    }
}
