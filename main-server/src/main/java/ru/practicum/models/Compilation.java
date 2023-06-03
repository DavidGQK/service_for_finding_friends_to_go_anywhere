package ru.practicum.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations")
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;

    @OneToMany
    @JoinColumn(name = "comp_id", insertable = false, updatable = false)
    private List<EventsCompilation> compilationEvents;
}