package com.github.automain.common.generator;

public class ServiceGenerator {

    public String generate(String upperTableName) {
        return getImportHead() + getClassHead(upperTableName) + "\n}";
    }

    private String getImportHead() {
        return "package com.github.automain.service;\n\nimport com.github.automain.common.container.ServiceContainer;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Service implements ServiceContainer {\n";
    }

}
