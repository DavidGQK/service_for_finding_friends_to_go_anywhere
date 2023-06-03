package ru.practicum.repositories.events;

import org.springframework.data.domain.Pageable;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.states.EventSortBy;
import ru.practicum.states.EventState;
import ru.practicum.states.RequestState;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExtendedEventRepositoryImpl implements ExtendedEventRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Event> extendedSearchByFilters(CombineEventFilters filter, EventSortBy sorting, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate parameters = filter(root, criteriaBuilder, query, filter);
        query.select(root).where(parameters);

        if ((sorting == null) || (sorting == EventSortBy.EVENT_DATE)) {
            query.orderBy(criteriaBuilder.asc(root.get("eventDate")));
        }
        return em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private Predicate filterByText(Root<Event> root, CriteriaBuilder criteriaBuilder, String text) {
        String value = "%" + text.toLowerCase() + "%";
        Predicate annotation = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), value);
        Predicate description = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), value);
        return criteriaBuilder.or(annotation, description);
    }

    private Predicate filterByStart(Root<Event> root, CriteriaBuilder criteriaBuilder, LocalDateTime start) {
        return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start);
    }

    private Predicate filterByEnd(Root<Event> root, CriteriaBuilder criteriaBuilder, LocalDateTime end) {
        return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end);
    }

    private Predicate filterByPeriod(Root<Event> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now());
    }

    private Predicate filterIfPaid(Root<Event> root, CriteriaBuilder criteriaBuilder, Boolean isPaid) {
        return criteriaBuilder.equal(root.get("paid"), isPaid);
    }

    private Predicate filterIfAvailable(Root<Event> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<Event> cq) {
        Subquery request = cq.subquery(Long.class);
        Root<Request> rootRequest = request.from(Request.class);
        Predicate predicateOne = criteriaBuilder.equal(rootRequest.get("eventId"), root.get("id"));
        Predicate predicateTwo = criteriaBuilder.equal(rootRequest.get("status"), RequestState.CONFIRMED);
        request.select(criteriaBuilder.count(rootRequest.get("id"))).where(criteriaBuilder.and(predicateOne,
                predicateTwo));
        Predicate predicateThree = criteriaBuilder.lessThan(request, root.get("participantLimit"));
        Predicate predicateFour = criteriaBuilder.equal(root.get("participantLimit"), 0);
        return criteriaBuilder.or(predicateThree, predicateFour);
    }

    private Predicate filterByUsers(Root<Event> root, CriteriaBuilder criteriaBuilder, Long[] users) {
        Predicate[] predicatesArr = new Predicate[users.length];
        for (int i = 0; i < users.length; i++) {
            predicatesArr[i] = criteriaBuilder.equal(root.get("owner").get("id"), users[i]);
        }
        return criteriaBuilder.or(predicatesArr);
    }

    private Predicate filterByCategories(Root<Event> root, CriteriaBuilder criteriaBuilder, Long[] categories) {
        Predicate[] predicatesArr = new Predicate[categories.length];
        for (int i = 0; i < categories.length; i++) {
            predicatesArr[i] = criteriaBuilder.equal(root.get("category").get("id"), categories[i]);
        }
        return criteriaBuilder.or(predicatesArr);
    }

    private Predicate filterByStates(Root<Event> root, CriteriaBuilder criteriaBuilder, EventState[] states) {
        Predicate[] predicatesArr = new Predicate[states.length];
        for (int i = 0; i < states.length; i++) {
            predicatesArr[i] = criteriaBuilder.equal(root.get("state"), states[i]);
        }
        return criteriaBuilder.or(predicatesArr);
    }

    private Predicate filter(Root<Event> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<Event> query,
                             CombineEventFilters filter) {
        List<Predicate> predicateList = new ArrayList<>();
        if (filter.getText() != null) {
            predicateList.add(filterByText(root, criteriaBuilder, filter.getText()));
        }
        if (filter.getRangeStart() != null) {
            predicateList.add(filterByStart(root, criteriaBuilder, filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            predicateList.add(filterByEnd(root, criteriaBuilder, filter.getRangeEnd()));
        }
        if ((filter.getRangeStart() == null) && (filter.getRangeEnd() == null)) {
            predicateList.add(filterByPeriod(root, criteriaBuilder));
        }
        if ((filter.getUsers() != null) && (filter.getUsers().length > 0)) {
            predicateList.add(filterByUsers(root, criteriaBuilder, filter.getUsers()));
        }
        if ((filter.getCategories() != null) && (filter.getCategories().length > 0)) {
            predicateList.add(filterByCategories(root, criteriaBuilder, filter.getCategories()));
        }
        if ((filter.getStates() != null) && (filter.getStates().length > 0)) {
            predicateList.add(filterByStates(root, criteriaBuilder, filter.getStates()));
        }
        if (filter.getPaid() != null) {
            predicateList.add(filterIfPaid(root, criteriaBuilder, filter.getPaid()));
        }
        if ((filter.getOnlyAvailable() != null) && (filter.getOnlyAvailable())) {
            predicateList.add(filterIfAvailable(root, criteriaBuilder, query));
        }
        return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
    }
}