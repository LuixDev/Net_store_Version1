package com.rysolf.netstore;




import java.io.Serializable;

public class Users implements Serializable   {


    private String id;

    private int   count,result;
    private  String name,image,ciudad,categoria,email;
    private  String telefono;
    private String direccion;
    private int usertype;

    private int numRatings;
    private double avgRating;
    private Double latitud;
    private Double longitud;
    private double rating;
    private String text;


public Users(){

}


    public int getUsertype(){

        return usertype;
    }
    public void setUsertype(int usertype){
        this.usertype=usertype;
    }




    public Users(String name, String image, String id,
                 String categoria, String ciudad,int count,int result,String  email, String telefono,String direccion,int usertype, Double latitud,Double longitud,int totalVoters, double totalRating,int numRatings, double avgRating ) {

        this.name = name;
        this.image = image;
        this. email= email;
        this.id = id;



        this.categoria = categoria;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.count=count;
        this.latitud=latitud;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
        this.direccion = direccion;
        this. longitud= longitud;

        this.usertype = usertype;

    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }





    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public  String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public int getCount() {
        return count;
    }

    public int setCount(int count) {
        this.count = count;
        return count;
    }

    public String toString() {
        return new String("longitud:" + longitud + ", latitud:"+ latitud);
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }






}
