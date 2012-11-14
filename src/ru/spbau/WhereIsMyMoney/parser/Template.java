package ru.spbau.WhereIsMyMoney.parser;

public class Template {
    private final String template;
    private final int type;

    public Template(String template, int type) {
        this.template = template;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTemplate() {
        return template;
    }
}
