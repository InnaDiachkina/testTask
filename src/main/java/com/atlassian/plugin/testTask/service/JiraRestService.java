package com.atlassian.plugin.testTask.service;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.bean.PagerFilter;
import java.util.List;
import java.util.function.Function;

public class JiraRestService {
    private static final String USER_NAME = "admin";
    private final SearchService searchService;

    public JiraRestService() {
        this.searchService = ComponentAccessor
                .getComponent(SearchService.class);
    }

    public String searchJira(String jql, List<String> fieldNames) throws Exception {
        UserManager userManager = ComponentAccessor.getUserManager();
        ApplicationUser user = userManager.getUserByName(USER_NAME);
        SearchService.ParseResult parseResult = searchService.parseQuery(user, jql);
        if (parseResult.isValid()) {
            SearchResults searchResults = searchService.search(user,
                    parseResult.getQuery(), PagerFilter.getUnlimitedFilter());
            JSONArray issuesArray = new JSONArray();
            for (Issue issue : searchResults.getIssues()) {
                JSONObject issueJson = new JSONObject();
                FieldExtractorService fieldExtractor = new FieldExtractorService();
                for (String fieldName : fieldNames) {
                    Function<Issue, Object> extractor = fieldExtractor.getExtractor(fieldName);
                    if (extractor != null) {
                        Object extractedValue = extractor.apply(issue);
                        issueJson.put(fieldName, extractedValue);
                    }
                }
                issuesArray.put(issueJson);
            }
            return issuesArray.toString();
        } else {
            throw new Exception("Invalid JQL: " + jql);
        }
    }
}