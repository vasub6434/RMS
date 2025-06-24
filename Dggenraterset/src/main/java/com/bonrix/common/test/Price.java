package com.bonrix.common.test;

public class Price {

	private String item;
    private Integer price;
     
    public Price(String itm, int pr){
        this.item = itm;
        this.price = pr;
    }
    
    public int hashCode(){
       
        int hashcode = 0;
        hashcode = price*20;
       // hashcode += item.hashCode();
        System.out.println(item+" hashcode=="+hashcode );
        return hashcode;
    }
     
    public boolean equals(Object obj){
        System.out.println("In equals");
        if (obj instanceof Price) {
            Price pp = (Price) obj;
            return (pp.item.equals(this.item) && pp.price == this.price);
        } else {
            return false;
        }
    }
     
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
     
    public String toString(){
        return "item: "+item+"  price: "+price;
    }
}
