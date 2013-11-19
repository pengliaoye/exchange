package com.dm.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;


public class InitDbData {


	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {		
        String[] foods = ("Achoccha|Amaranth|Angelica|Anise|Apple|Arrowroot|Arrugula|" +
                "Artichoke|Asparagus|Atemoya|Avocado|Balsam Apple|Balsam Pe" +
                "ar|Bambara groundnut|Bamboo|Banana and Plantains|Barbados " +
                "Cherry|Beans|Beet|Blackberry|Blueberry|Bok Choy|Boniato (T" +
                "ropical Sweetpotato, also Batatas or Carnote)|Broccoli|Bro" +
                "ccoli|Brussels sprouts|Bunch Grape|Burdock|Cabbage|Calabaz" +
                "a (Tropical Pumpkin)|Cantaloupes and Muskmelons|Capers|Car" +
                "ambola (Star Fruit)|Cardoon|Carrot|Cassava (Spanish common" +
                " name Yuca)|Cauliflower|Celeriac|Celery|Celtuce|Chard|Chay" +
                "a|Chayote|Chicory|Chinese Cabbage (Bok Choy)|Chinese Jujub" +
                "e|Chinese Radish|Chives|Chrysanthemum|Chufa|Cilantro|Citro" +
                "n|Coconut Palm|Collards|Comfrey|Corn salad|Corn|Cuban Swee" +
                "t Potato|Cucumber|Cushcush|Daikon|Dandelion|Dasheen|Dill|E" +
                "ggplant|Endive|Eugenia|Fennel|Fig|Galia Muskmelon|Garbanzo" +
                "|Garlic|Gherkin, West Indian|Ginger|Ginseng|Gourds|Grape|G" +
                "uar|Guava|Hanover Salad|Horseradish|Horseradish tree|Huckl" +
                "eberry|Ice Plant|Jaboticaba|Jackfruit|Jicama|Jojoba|Kale|K" +
                "angkong|Kohlrabi|Leek|Lentils|Lettuce|Longan|Loquat|Lovage" +
                "|Luffa Gourd|Lychee|Macadamia|Malanga (also called Tannia " +
                "or Tanier)|Mamey Sapote|Mango|Martynia|Melon|Momordica|Mus" +
                "cadine Grape|Mushroom|Muskmelons and Cantaloupes|Mustard|M" +
                "ustard collard|Mustard, potherb|Naranjillo|Nasturtium|Nect" +
                "arine|Okra|Onion|Orach|Oriental Persimmon|Papaya|Paprika|P" +
                "arsley|Parsley root|Parsnip|Passion Fruit|Peach|Peas|Peanu" +
                "t Production|Pear|Pecan|Pepper|Persimmon|Pimiento|Pineappl" +
                "e|Pineapple Guava|Pitaya|Plum|Pokeweed|Pomegranate|Potato|" +
                "Pumpkin|Purslane|Radicchio|Radish|Rakkyo|Rampion|Raspberry" +
                "|Rhubarb|Romaine Lettuce|Roselle|Rutabaga|Saffron|Salsify|" +
                "Sapodilla|Sarsaparilla|Sassafrass|Scorzonera|Sea kale|Seag" +
                "rape|Shallot|Skirret|Smallage|Sorrel|Southern pea|Soybeans" +
                "|Spinach|Spondias|Squash|Strawberries|Sugar Apple|Swamp Ca" +
                "bbage|Sweet Basil|Sweet Corn|Sweet potato|Swiss Chard|Toma" +
                "tillo|Tomato|Truffles|Turnip|Upland cress|Water celery|Wat" +
                "erchestnut|Watercress|Watermelon|Yams").split("\\|");
        
        int len = foods.length;
        Object[][] params = new Object[len][];
        for (int i = 0; i < len; i++) {
        	params[i] = new Object[]{
        		i + 1,
                foods[i],
                Math.round((Math.random()) * 500) / 500.0,
                Math.ceil(len * Math.random())
        	};
        	System.out.println(Arrays.toString(params[i]));
        }
		
        Connection conn = null;
        try{
	    	conn = ConnUtil.getConn();	    	
			QueryRunner runner = new QueryRunner();
			String sql = "insert into foods(id,name,price,qty) values(?,?,?,?)";
			runner.batch(conn, sql, params);
        } finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
