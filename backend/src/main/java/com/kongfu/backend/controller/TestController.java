package com.kongfu.backend.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/clientWeb")
public class  TestController{
    @GET
    @Path("/getData/{page}/{pageSize}")
    public String getData(@PathParam("page") int page, @PathParam("pageSize") int pageSize) {
        System.out.println(page+"++"+pageSize);
        return "xxx";
    }
}