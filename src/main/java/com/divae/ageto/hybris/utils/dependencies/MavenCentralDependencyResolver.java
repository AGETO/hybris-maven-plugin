package com.divae.ageto.hybris.utils.dependencies;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Klaus Hauschild
 */
public class MavenCentralDependencyResolver extends AbstractDependencyResolver {

    @Override
    protected Dependency resolve(final String artifactId, final String version) {
        final RestTemplate restTemplate = new RestTemplate();
        final Result result = restTemplate.getForObject(
                "http://search.maven.org/solrsearch/select?q=a:\"{0}\" AND v:\"{1}\" AND p:\"jar\"&rows=1&wt=json", Result.class,
                artifactId, version);
        final List<Result.Response.Doc> docs = result.getResponse().getDocs();
        if (docs.isEmpty()) {
            throw new UnresolvableDependencyException(artifactId, version);
        }
        final Result.Response.Doc doc = docs.get(0);
        final Dependency dependency = new Dependency();
        dependency.setGroupId(doc.getG());
        dependency.setArtifactId(doc.getA());
        dependency.setVersion(doc.getV());
        return dependency;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Result {

        Response response;

        @JsonGetter
        Response getResponse() {
            return response;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Response {

            List<Doc> docs;

            @JsonGetter
            List<Doc> getDocs() {
                return docs;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            static class Doc {

                private String g;
                private String a;
                private String v;

                @JsonGetter
                String getG() {
                    return g;
                }

                @JsonGetter
                String getA() {
                    return a;
                }

                @JsonGetter
                String getV() {
                    return v;
                }

            }

        }

    }

}
