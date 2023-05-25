package com.atlassian.plugin.testTask.service;

import com.atlassian.jira.issue.Issue;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class FieldExtractorService {
    private Map<String, Function<Issue, Object>> fieldExtractors = new HashMap<>();

    public FieldExtractorService() {
        initialize();
    }

    private void initialize() {
        fieldExtractors.put("id", Issue::getId);
        fieldExtractors.put("key", Issue::getKey);
        fieldExtractors.put("description", Issue::getDescription);
        fieldExtractors.put("summary", Issue::getSummary);
        fieldExtractors.put("issuetype", issue -> Objects.requireNonNull(issue.getIssueType()).getName());
        fieldExtractors.put("project", issue -> Objects.requireNonNull(issue.getProjectObject()).getName());
        fieldExtractors.put("reporter", issue -> issue.getReporter().getDisplayName());
        fieldExtractors.put("assignee", issue -> issue.getAssignee().getDisplayName());
        fieldExtractors.put("created", Issue::getCreated);
        fieldExtractors.put("updated", Issue::getUpdated);
        fieldExtractors.put("priority", issue -> Objects.requireNonNull(issue.getPriority()).getName());
        fieldExtractors.put("resolution", issue -> issue.getResolution().getName());
        fieldExtractors.put("status", issue -> issue.getStatus().getName());
        // ...
    }

    public Function<Issue, Object> getExtractor(String fieldName) {
        return fieldExtractors.get(fieldName);
    }
}
