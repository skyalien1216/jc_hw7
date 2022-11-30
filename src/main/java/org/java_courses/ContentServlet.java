package org.java_courses;

import dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("NotNullNullableValidation")
public final class ContentServlet extends HttpServlet {
  private ServletConfig servletConfig;
  private final ProductDAO productDao;
  public ContentServlet(ProductDAO productDao) {
    this.productDao = productDao;
  }

  @Override
  public void init(ServletConfig config) {
    this.servletConfig = config;
  }

  @Override
  public ServletConfig getServletConfig() {
    return servletConfig;
  }

  @Override
  public void service(ServletRequest req, ServletResponse res) throws IOException {
    try (ServletOutputStream outputStream = res.getOutputStream()) {
      outputStream.write(productDao.all().toString().getBytes(StandardCharsets.UTF_8));
      outputStream.flush();
    }
  }

  @Override
  public String getServletInfo() {
    return "ContentServlet";
  }
}
