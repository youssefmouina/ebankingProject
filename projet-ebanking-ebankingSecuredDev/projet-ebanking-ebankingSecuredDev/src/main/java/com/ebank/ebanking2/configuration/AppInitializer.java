package com.ebank.ebanking2.configuration;


import jakarta.servlet.Filter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class, SecurityConfig.class }; // global beans (DataSource, Services, Security, etc)
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class }; // MVC config (for REST controllers)
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Map all requests to the DispatcherServlet
    }

    @Override
    protected Filter[] getServletFilters() {
        DelegatingFilterProxy securityFilter = new DelegatingFilterProxy("springSecurityFilterChain");
        return new Filter[]{securityFilter};
    }
}