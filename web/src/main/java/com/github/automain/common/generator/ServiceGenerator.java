package com.github.automain.common.generator;

public class ServiceGenerator {

    public String generate(String upperTableName) {
        return getImportHead() + getClassHead(upperTableName) + "\n}";
    }

    private String getImportHead() {
        return "import com.github.automain.common.container.ServiceDaoContainer;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Service implements ServiceDaoContainer {\n";
    }

}
