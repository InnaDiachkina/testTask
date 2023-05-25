package com.atlassian.plugin.testTask.controller;

import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.plugin.testTask.service.JiraRestService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/search")
public class JiraRestController {
    private JiraRestService jiraRestService;

    public JiraRestController() {
        this.jiraRestService = new JiraRestService();
    }

    public static class SearchRequest {
        public String jql;
        public List<String> fields;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchJira(SearchRequest searchRequest) {
        try {
            String searchResults = jiraRestService.searchJira(searchRequest.jql,
                    searchRequest.fields);
            return Response.ok(searchResults).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid JQL: " + searchRequest.jql).build();
        } catch (JqlParseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while processing request: "
                    + e.getMessage()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
