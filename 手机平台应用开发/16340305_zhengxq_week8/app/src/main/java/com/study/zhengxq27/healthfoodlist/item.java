package com.study.zhengxq27.healthfoodlist;

public class item {
    String textview1_content;
    String textview2_content;

    String kind;
    String element;
    String title_color;

    item(String text1, String text2,String foodKind, String element,String color)
    {
        textview1_content = text1;
        textview2_content = text2;
        kind = foodKind;
        this.element = element;
        title_color = color;
    }
    item() { }
}




