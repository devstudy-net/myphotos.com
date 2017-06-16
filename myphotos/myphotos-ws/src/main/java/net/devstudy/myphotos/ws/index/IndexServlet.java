/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.devstudy.myphotos.ws.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.ws.bean.PhotoWebServiceBean;
import net.devstudy.myphotos.ws.bean.ProfileWebServiceBean;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {

    @Inject
    @Property("myphotos.host.soap.api")
    private String soapHost;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<WebServiceModel> webservices = getWebServiceModels(new Class<?>[]{
            ProfileWebServiceBean.class,
            PhotoWebServiceBean.class
        });
        req.setAttribute("webservices", webservices);
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    private List<WebServiceModel> getWebServiceModels(Class<?>[] classes) {
        List<WebServiceModel> list = new ArrayList<>();
        for (Class<?> clazz : classes) {
            list.add(new WebServiceModel(clazz.getAnnotation(WebService.class)));
        }
        return list;
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    public class WebServiceModel {

        private final String name;
        private final String port;
        private final String address;

        public WebServiceModel(WebService webService) {
            this.name = webService.serviceName();
            this.port = webService.portName();
            this.address = String.format("%s/%s", soapHost, webService.serviceName());
        }

        public String getName() {
            return name;
        }

        public String getPort() {
            return port;
        }

        public String getAddress() {
            return address;
        }
    }
}
