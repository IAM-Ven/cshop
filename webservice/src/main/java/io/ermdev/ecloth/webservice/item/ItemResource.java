package io.ermdev.ecloth.webservice.item;

import io.ermdev.ecloth.data.exception.EntityNotFoundException;
import io.ermdev.ecloth.data.exception.UnsatisfiedEntityException;
import io.ermdev.ecloth.data.service.ItemService;
import io.ermdev.ecloth.model.entity.Item;
import io.ermdev.ecloth.model.resource.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("item")
public class
ItemResource {

    @QueryParam("categoryId")
    private Long categoryId;

    private ItemService itemService;

    @Autowired
    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    @Path("{itemId}")
    @GET
    public Response getById(@PathParam("itemId") long itemId) {
        try {
            Item item = itemService.findById(itemId);
            return Response.status(Response.Status.FOUND).entity(item).build();
        } catch (EntityNotFoundException e) {
            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @GET
    public Response getAll() {
        try {
            List<Item> items = itemService.findAll();
            return Response.status(Response.Status.FOUND).entity(items).build();
        } catch (EntityNotFoundException e) {
            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @POST
    public Response add(Item item) {
        try {
            item = itemService.add(item, categoryId);
            return Response.status(Response.Status.OK).entity(item).build();
        } catch (EntityNotFoundException e) {
            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } catch (UnsatisfiedEntityException e) {
            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
    }
}
