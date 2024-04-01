package com.example.retrospective.repo;

import com.example.retrospective.model.Retrospective;

import java.util.List;

public interface RetrospectiveRepository {
    List<Retrospective> getAllRetrospectives(int page, int pageSize);
    Retrospective getRetrospectiveByName(String name);
    void addRetrospective(Retrospective retrospective);
    void updateRetrospective(Retrospective retrospective);
}
