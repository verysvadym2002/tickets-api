package com.example.tickets.service;

import com.example.tickets.document.TicketDocument;
import com.example.tickets.repository.TicketSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketSearchService {

    private final TicketSearchRepository repository;
    private final ElasticsearchOperations operations;

    /**
     * Fulltext vyhledávání podle title nebo description.
     */
    public List<TicketDocument> fulltextSearch(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        Criteria criteria = new Criteria("title").contains(text)
                .or(new Criteria("description").contains(text));

        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<TicketDocument> hits = operations.search(query, TicketDocument.class);

        return hits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * Vyhledávání podle ID projektu.
     */
    public List<TicketDocument> findByProject(Long projectId) {
        return repository.findByProjectId(projectId);
    }

    /**
     * Komplexní vyhledávání přes CriteriaQuery (fulltext + filtry).
     * Můžeš volat:
     * search("bug", projectId, "OPEN", "HIGH", assigneeId)
     */
    public List<TicketDocument> search(
            String text,
            Long projectId,
            String state,
            String priority,
            Long assigneeId,
            String type
    ) {
        Criteria criteria = new Criteria();

        // fulltext (title OR description)
        if (text != null && !text.isBlank()) {
            Criteria textCriteria = new Criteria("title").contains(text)
                    .or(new Criteria("description").contains(text));
            criteria = criteria.and(textCriteria);
        }

        // projekt
        if (projectId != null) {
            criteria = criteria.and(new Criteria("projectId").is(projectId));
        }

        // stav
        if (state != null && !state.isBlank()) {
            criteria = criteria.and(new Criteria("state").is(state));
        }

        // priorita
        if (priority != null && !priority.isBlank()) {
            criteria = criteria.and(new Criteria("priority").is(priority));
        }

        // řešitel
        if (assigneeId != null) {
            criteria = criteria.and(new Criteria("assigneeId").is(assigneeId));
        }

        // typ ticketu
        if (type != null && !type.isBlank()) {
            criteria = criteria.and(new Criteria("type").is(type));
        }

        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<TicketDocument> hits = operations.search(query, TicketDocument.class);

        return hits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

