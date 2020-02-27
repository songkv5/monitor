package com.sys.monitor.component;

public enum FileType {
    EXCEL("excel", ".xls"),
    PDF("pdf", ".pdf"),
    ;
    private String name;
    private String suffix;

    FileType(String name) {
        this.name = name;
    }

    FileType(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }
}
