package com.tech.mse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

public class JasonExp {
	
	@Override
	public String toString(){
		return "toSTring override";
	}
	/*
	 * public static void main(String[] args) { // Create a new JSONObject
	 * JSONObject jsonObject = new JSONObject(); jsonObject.put("name", "John");
	 * jsonObject.put("age", 30); jsonObject.put("city", "New York");
	 * 
	 * // Convert the JSONObject to a JSON string String jsonString =
	 * jsonObject.toJSONString(); System.out.println(jsonString); }
	 */
	 public static void main(String[] args) {
	        // Create an instance of Gson
	        Gson gson = new Gson();
	        JasonExp q = new JasonExp();
	        // Create an object to be converted
	        Person person = new Person("John", 30, "New York");

	        // Convert the object to a JSON string
	        String jsonString = gson.toJson(person);
	        System.out.println(jsonString);
	        System.out.println(q);
	    }
	 
}

class Person {
    private String name;
    private int age;
    private String city;
    

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
}