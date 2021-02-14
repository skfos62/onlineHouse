package com.example.house2;

import com.example.house2.classfile.shopping_list_class;

public class shopping_arcore_cart {
    shopping_list_class shoplist;
    String realname;
    public shopping_list_class cartCkeck(String str){

        if(str.equals("_2")){
            realname = "chair_01.sfb";
            shoplist = new shopping_list_class("chair2",30000,"chair2","chair","y","http://13.125.62.22/list_chair_02.jpg","chair_01.sfb","part01");
            shoplist.setFurArck("y");
            shoplist.setFurcategory("chair");
            shoplist.setFurcopy("chair2");
            shoplist.setFurImage("http://13.125.62.22/list_chair_02.jpg");
            shoplist.setFurname("chair2");
            shoplist.setFurprice(30000);
            shoplist.setFurtsfa("chair_01.sfb");

        } else if(str.equals("Wood__Floor")){
            realname = "chair_04.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("chair");
            shoplist.setFurcopy("chair5");
            shoplist.setFurImage("http://13.125.62.22/list_chair_05.jpg");
            shoplist.setFurname("chair5");
            shoplist.setFurprice(15000);
            shoplist.setFurtsfa(realname);
            return shoplist;

        } else if(str.equals("Color_B15")){
            realname = "etc_01.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("chair");
            shoplist.setFurcopy("chair5");
            shoplist.setFurImage("http://13.125.62.22/list_chair_05.jpg");
            shoplist.setFurname("chair5");
            shoplist.setFurprice(15000);
            shoplist.setFurtsfa(realname);

        } else if(str.equals("_OrangeRed_")){
            shoplist = new shopping_list_class("etc3",17000,"etc3","etc","y","http://13.125.62.22/list_etc_03.jpg","etc_03.sfb","part01");
            realname = "etc_03.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("etc");
            shoplist.setFurcopy("etc3");
            shoplist.setFurImage("http://13.125.62.22/list_etc_03.jpg");
            shoplist.setFurname("etc3");
            shoplist.setFurprice(17000);
            shoplist.setFurtsfa(realname);
            return shoplist;

        } else if(str.equals("AM134_14_plate1")){
            realname = "etc_04.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("etc");
            shoplist.setFurcopy("etc1");
            shoplist.setFurImage("http://13.125.62.22/list_etc_01.jpg");
            shoplist.setFurname("etc1");
            shoplist.setFurprice(5000);
            shoplist.setFurtsfa(realname);
            return shoplist;

        } else if(str.equals("panton")){
            realname = "lamp_ikea.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("etc");
            shoplist.setFurcopy("etc2");
            shoplist.setFurImage("http://13.125.62.22/list_etc_02.jpg");
            shoplist.setFurname("etc2");
            shoplist.setFurprice(20000);
            shoplist.setFurtsfa(realname);
            return shoplist;

        } else if(str.equals("795548")){
            realname = "table_large_rectangular_01.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("table");
            shoplist.setFurcopy("table2");
            shoplist.setFurImage("http://13.125.62.22/list_table_02.jpg");
            shoplist.setFurname("table2");
            shoplist.setFurprice(20000);
            shoplist.setFurtsfa(realname);
            return shoplist;

        }else if(str.equals("MD157_MK_PSS_Material_01")){
            realname = "tables_01.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("table");
            shoplist.setFurcopy("table1");
            shoplist.setFurImage("http://13.125.62.22/list_table_01.jpg");
            shoplist.setFurname("table1");
            shoplist.setFurprice(10000);
            shoplist.setFurtsfa(realname);
            return shoplist;
        }else if(str.equals("_1")){
            realname = "waerhouse.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("furniture");
            shoplist.setFurcopy("furniture2");
            shoplist.setFurImage("http://13.125.62.22/list_furniture_02.jpg");
            shoplist.setFurname("furniture2");
            shoplist.setFurprice(40000);
            shoplist.setFurtsfa(realname);
            return shoplist;
        } else {
            realname = "bar_chair_2.sfb";
            shoplist.setFurArck("y");
            shoplist.setFurcategory("chair");
            shoplist.setFurcopy("chair4");
            shoplist.setFurImage("http://13.125.62.22/list_chair_04.jpg");
            shoplist.setFurname("chair4");
            shoplist.setFurprice(5000);
            shoplist.setFurtsfa(realname);
            return shoplist;
        }

        return shoplist;
    }



}
