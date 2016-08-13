package com.divae.ageto.hybris.utils;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Klaus Hauschild
 */
public enum MavenCentralSearch {

    ;

    public static Dependency determineDependencyForLibrary(final String libraryName) {
        final Dependency partialDependency = getPartialDependency(libraryName);
        final RestTemplate restTemplate = new RestTemplate();
        final Result result = restTemplate.getForObject(
                "http://search.maven.org/solrsearch/select?q=a:\"{0}\" AND v:\"{1}\" AND p:\"jar\"&rows=1&wt=json", Result.class,
                partialDependency.getArtifactId(), partialDependency.getVersion());
        final List<Result.Response.Doc> docs = result.getResponse().getDocs();
        if (docs.isEmpty()) {
            throw new IllegalArgumentException(String.format("Unable to determine dependency of library: %s", libraryName));
        }
        final Result.Response.Doc doc = docs.get(0);

        final Dependency dependency = new Dependency();
        dependency.setGroupId(doc.getG());
        dependency.setArtifactId(doc.getA());
        dependency.setVersion(doc.getV());
        return dependency;
    }

    private static Dependency getPartialDependency(final String libraryName) {
        final String rawLibraryName = getRawLibraryName(libraryName);
        final int pivot = rawLibraryName.lastIndexOf("-");
        final String artifactId = rawLibraryName.substring(0, pivot);
        final String version = rawLibraryName.substring(pivot + 1, rawLibraryName.length());
        final Dependency dependency = new Dependency();
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

    private static String getRawLibraryName(final String libraryName) {
        if (libraryName.endsWith(".jar")) {
            return libraryName.substring(0, libraryName.length() - 4);
        }
        return libraryName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Result {

        private Response response;

        public Response getResponse() {
            return response;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Response {

            private List<Doc> docs;

            public List<Doc> getDocs() {
                return docs;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            static class Doc {

                private String g;
                private String a;
                private String v;

                public String getG() {
                    return g;
                }

                public String getA() {
                    return a;
                }

                public String getV() {
                    return v;
                }

            }

        }

    }

}
