package com.vaccinesoap.main.endpoint;

import com.vaccinesoap.main.model.VaccineEntity;
import com.vaccinesoap.main.search.SearchHibernate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vaccine")
public class SearchHibernateController {

    @Autowired
    private SearchHibernate vaccineSearchHibernate;

    @GetMapping("/")
    public String index() {
        return "Hello World!!";
    }

    @GetMapping(value = "/search/{query}")
    public List<VaccineEntity> search(@PathVariable final String query) {
        List<VaccineEntity> searchResults = null;
        try {
            searchResults = vaccineSearchHibernate.search(query);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return searchResults;
    }
}
