package com.study.zhengxq27.healthfoodlist;

public class MessageBus
{
    public String circle_name;
    public String food_name;
    public String food_kind;
    public String element;
    public String title_color;
    MessageBus(String circle, String  food_name, String food_kind, String element, String title_color)
    {
        this.circle_name = circle;
        this.food_name = food_name;
        this.food_kind = food_kind;
        this.element = element;
        this.title_color = title_color;
    }
}
