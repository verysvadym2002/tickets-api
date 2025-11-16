package com.example.tickets.repository;

import com.example.tickets.document.TicketDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSearchRepository extends ElasticsearchRepository<TicketDocument, String> {

    // fulltext řešíme v service přes CriteriaQuery → tuto metodu SMAŽEME
    // List<TicketDocument> findByTitleContainingOrDescriptionContaining(String t1, String t2);

    // najdi podle projektu
    List<TicketDocument> findByProjectId(Long projectId);
}
