package com.tus.traunreut.webserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "clubs")
public class Club {
    @Id
    private String id;
    private String name;
    private List<String> teamIds;

    // Image URL
}
