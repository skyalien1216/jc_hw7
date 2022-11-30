package org.java_courses;

import dao.OrganizationDAO;
import dao.ProductDAO;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;

@SuppressWarnings("NotNullNullableValidation")
public final class Start {

  public static void main(String[] args) throws Exception {

    //http://localhost:3466/

    final Server server = new DefaultServer().build();

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    context.setContextPath("/products/");
    context.addServlet(new ServletHolder(new ContentServlet(new ProductDAO())), "/*");
    context.addServlet(new ServletHolder(new AddProductServlet(new ProductDAO(), new OrganizationDAO())), "/add/*");

    final ResourceHandler resourceHandler = new ResourceHandler();
    ContextHandler resContext = new ContextHandler();
    resContext.setContextPath("/*");
    final URL resource = Start.class.getResource("/index.html");
    resourceHandler.setBaseResource(Resource.newResource(resource.toExternalForm()));
    resourceHandler.setDirectoriesListed(false);
    resContext.setHandler(resourceHandler);

    final String jdbcConfig = Start.class.getResource("/jdbc_config").toExternalForm();
    final JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfig);
    final ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(jdbcLoginService);
    securityHandler.setHandler(context);
    server.addBean(jdbcLoginService);

    HandlerCollection collection = new HandlerCollection();
    collection.setHandlers(new Handler[]{resContext, securityHandler});
    server.setHandler(collection);

    server.start();
  }
}
