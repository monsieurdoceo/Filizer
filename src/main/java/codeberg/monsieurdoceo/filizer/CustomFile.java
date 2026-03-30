package codeberg.monsieurdoceo.filizer;

import java.io.File;

public class CustomFile {

    private String name;
    private FileCreator fileCreator;

    public CustomFile(String path, String name) {
        this.name = name;
        this.fileCreator = new FileCreator(path, name);
    }

    public CustomFile(File parent, String name) {
        this.name = name;
        this.fileCreator = new FileCreator(parent, name);
    }

    public String getName() {
        return this.name;
    }

    public FileCreator Creator() {
        return this.fileCreator;
    }
}
