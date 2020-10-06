package com.vaccinesoap.main.search;

import com.vaccinesoap.main.model.VaccineEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class SearchHibernate {


    @PersistenceContext
    private EntityManager entityManager;


    /**
     * A basic search for the entity Vaccine. The search is done by exact match per
     * keywords on fields
     *
     * @param text The query text.
     */
    public List<VaccineEntity> search(String text) {

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                            getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(VaccineEntity.class).get();

        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "email", "model")
                        .matching(text)
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, VaccineEntity.class);

        @SuppressWarnings("unchecked")
        List<VaccineEntity> results = jpaQuery.getResultList();

        return results;
    } // method search


}

