package com.dm.exchange.servlet;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dm.entity.Food;
import com.dm.service.FoodService;

@WebServlet(urlPatterns = "/pagination")
public class PaginationServlet extends HttpServlet{

	private static final long serialVersionUID = 1687131081277393211L;

	@Inject
	private FoodService foodService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int[] range = new int[]{0, 10};
		List<Food> foodList = foodService.getAll();
		JsonGenerator gen = Json.createGenerator(resp.getOutputStream());
		gen.writeStartArray();
		for(Food f : foodList){
    		gen.writeStartObject()
            .write("name", f.getName())
            .write("price", f.getPrice())
            .write("qty", f.getQty())
            .writeEnd();
		}
        gen.writeEnd();
        gen.flush();
	}

}
