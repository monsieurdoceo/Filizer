package github.monsieurdoceo.filizer;

import java.io.File;

public class CustomFile {

    private String name;

    public CustomFile(String path, String name) {
        this.name = name;
    }

    public CustomFile(File parent, String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
