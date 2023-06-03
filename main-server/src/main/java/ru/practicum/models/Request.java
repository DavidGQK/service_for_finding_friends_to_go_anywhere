package ru.practicum.models;

import lombok.*;
import ru.practicum.states.RequestState;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests")
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private RequestState status;
}