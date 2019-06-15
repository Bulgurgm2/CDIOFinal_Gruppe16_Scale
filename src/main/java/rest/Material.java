package rest;


import database.dal.*;
import database.dto.*;
import rest.jsonObjects.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/material")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Material {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMaterialJson(JSONmaterial jmaterial) {
        IMaterialDTO material = null;
        try {
            material = jsonToMaterial(jmaterial);
        } catch (IDALException.DALException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        IMaterialDAO materialDAO = MaterialDAO.getInstance();

        try {
            materialDAO.createMaterial(material);
        } catch (IDALException.DALException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok("Råvarebatch oprettet").build();
    }

    @GET
    public ArrayList<JSONmaterial> getMaterialList() throws IDALException.DALException {
        IMaterialDAO materialDAO = MaterialDAO.getInstance();
        List<IMaterialDTO> materials = materialDAO.getMaterialList();
        return materialsToJSON(materials);
    }


    private static IMaterialDTO jsonToMaterial(JSONmaterial jmaterial) throws IDALException.DALException {
        IMaterialDTO material = new MaterialDTO();

        material.setMaterialBatchId(Integer.parseInt(jmaterial.getId()));
        java.util.Date utilDate = new java.util.Date();
        material.setDate(new java.sql.Date(utilDate.getTime()));
        material.setIngredientId(Integer.parseInt(jmaterial.getIngredientid()));
        try {
            material.setAmount(Double.parseDouble(jmaterial.getAmount()));
        } catch (NumberFormatException e){
            throw new IDALException.DALException("Mængde skal være et tal.");
        }
        material.setOrder(false);
        material.setUserId(User.getCurrentUser());
        material.setSupplier(jmaterial.getSupplier());

        return material;
    }

    private static ArrayList<JSONmaterial> materialsToJSON(List<IMaterialDTO> materials){
        JSONmaterial jmaterial;
        ArrayList<JSONmaterial> jmaterials = new ArrayList<>();
        IIngredientDAO ingredientDAO = IngredientDAO.getInstance();
        String date;
        for (IMaterialDTO material: materials){
            date = new SimpleDateFormat("dd-MM-yyyy").format(material.getDate());
            jmaterial = new JSONmaterial();
            jmaterial.setId("" + material.getMaterialBatchId());
            jmaterial.setIngredientid("" + material.getIngredientId());
            jmaterial.setAmount("" + material.getAmount());
            jmaterial.setDate(date);
            jmaterial.setSupplier(material.getSupplier());
            try {
                jmaterial.setIngredientname(ingredientDAO.getIngredient(material.getIngredientId()).getIngredientName());
            } catch (IDALException.DALException e) {
                e.printStackTrace();
            }
            jmaterials.add(jmaterial);
        }
        return jmaterials;
    }

}
