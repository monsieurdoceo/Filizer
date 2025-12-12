package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileBuilder;
import github.monsieurdoceo.filizer.FileCreator;

public class CustomFile {

    private String name;

    private FileBuilder fileBuilder;
    private FileCreator fileCreator;

    public CustomFile(String path, String name) {
        this.name = name;
        this.fileCreator = new FileCreator(path, name);
        this.fileBuilder = new FileBuilder(this.fileCreator);
    }

    public String getName() {
        return this.name;
    }

    public FileBuilder builder() {
        return this.fileBuilder;
    }

    public FileCreator creator() {
        return this.fileCreator;
    }
}
