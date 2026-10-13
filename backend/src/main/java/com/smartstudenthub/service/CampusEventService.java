package com.smartstudenthub.service;

import com.smartstudenthub.dto.CampusLocationRequest;
import com.smartstudenthub.dto.EventRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CampusEventService {

    private final CampusLocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;

    public CampusEventService(
            CampusLocationRepository locationRepository,
            EventRepository eventRepository,
            EventRegistrationRepository registrationRepository,
            StudentRepository studentRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
    }

    // Campus Locations
    public List<CampusLocation> getActiveLocations() {
        return locationRepository.findByIsActiveTrue();
    }

    public List<CampusLocation> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional
    public CampusLocation createLocation(CampusLocationRequest request) {
        CampusLocation location = new CampusLocation();
        location.setName(request.getName());
        location.setCategory(request.getCategory().toUpperCase());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setDescription(request.getDescription());
        location.setBuildingNumber(request.getBuildingNumber());
        location.setFloorNumber(request.getFloorNumber());
        location.setImageUrl(request.getImageUrl());
        location.setActive(true);
        return locationRepository.save(location);
    }

    @Transactional
    public CampusLocation updateLocation(Long id, CampusLocationRequest request) {
        CampusLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campus location not found"));
        location.setName(request.getName());
        location.setCategory(request.getCategory().toUpperCase());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setDescription(request.getDescription());
        location.setBuildingNumber(request.getBuildingNumber());
        location.setFloorNumber(request.getFloorNumber());
        if (request.getImageUrl() != null) location.setImageUrl(request.getImageUrl());
        return locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    // Events
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByIsActiveTrueOrderByEventDateAsc();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Transactional
    public Event createEvent(EventRequest request, User organizer) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setVenue(request.getVenue());
        event.setEventDate(request.getEventDate());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setCapacity(request.getCapacity());
        event.setRegisteredCount(0);
        event.setOrganizer(organizer);
        event.setActive(true);

        return eventRepository.save(event);
    }

    @Transactional
    public synchronized EventRegistration registerStudentForEvent(Long studentId, Long eventId) {
        Event event = getEventById(eventId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (!event.isActive()) {
            throw new BadRequestException("Event is currently inactive");
        }
        if (LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new BadRequestException("Event registration deadline has passed");
        }
        if (registrationRepository.existsByEventIdAndStudentId(event.getId(), student.getId())) {
            throw new ConflictException("You are already registered for this event");
        }
        if (event.getRegisteredCount() >= event.getCapacity()) {
            throw new ConflictException("Event is fully booked (capacity " + event.getCapacity() + " reached)");
        }

        event.setRegisteredCount(event.getRegisteredCount() + 1);
        eventRepository.save(event);

        EventRegistration reg = new EventRegistration();
        reg.setEvent(event);
        reg.setStudent(student);
        reg.setTicketQrCode("EVT-TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return registrationRepository.save(reg);
    }

    @Transactional
    public EventRegistration checkInTicket(String ticketQrCode) {
        EventRegistration reg = registrationRepository.findByTicketQrCode(ticketQrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid event ticket QR code"));

        if (reg.isAttended()) {
            throw new BadRequestException("Ticket has already been verified and checked in at " + reg.getAttendedAt());
        }

        reg.setAttended(true);
        reg.setAttendedAt(LocalDateTime.now());
        return registrationRepository.save(reg);
    }

    public List<EventRegistration> getStudentEventRegistrations(Long studentId) {
        return registrationRepository.findByStudentId(studentId);
    }

    public List<EventRegistration> getEventRegistrations(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }
}
