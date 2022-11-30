package org.java_courses;

import dao.OrganizationDAO;
import dao.ProductDAO;
import generated.tables.pojos.Organization;
import generated.tables.pojos.Product;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("NotNullNullableValidation")
public final class AddProductServlet extends HttpServlet {
  private ServletConfig servletConfig;
  private final ProductDAO productDao;
  private final OrganizationDAO organizationDao;
  @Override
  public void init(ServletConfig config) {
    this.servletConfig = config;
  }

  public AddProductServlet(ProductDAO productDao,OrganizationDAO organizationDao ) {
    this.productDao = productDao;
    this.organizationDao = organizationDao;
  }
  @Override
  public ServletConfig getServletConfig() {
    return servletConfig;
  }

  @Override
  public void service(ServletRequest req, ServletResponse res) throws IOException {
    try (ServletOutputStream outputStream = res.getOutputStream()) {
      int amount;
      String prod_name;
      String org_name;
      try{
        amount = Integer.parseInt(req.getParameter("amount"));
        prod_name = req.getParameter("product");
        org_name = req.getParameter("organization");
      }
      catch (Exception e) {
        outputStream.write("Exception thrown\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        throw new RuntimeException("Couldn't parse params");
      }
      if (!organizationDao.dbHas(org_name))
        organizationDao.save(new Organization(org_name));

      var p = new Product(prod_name, amount, org_name);
      productDao.save(p);
      outputStream.write(("All OK\n Product "+ p.toString() + " was added to database.").getBytes(StandardCharsets.UTF_8));
      outputStream.flush();
    }
  }

  @Override
  public String getServletInfo() {
    return "AddProductServlet";
  }
}
