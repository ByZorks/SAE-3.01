package sae3_01;

import java.io.File;
import java.util.List;

public abstract class FileComposite {

    protected File file;

    public FileComposite(File file) {
        this.file = file;
    }

    public String toString() {
        return this.file.getName();
    }

    public abstract void actualiser();

    public abstract List<FileComposite> getContenu();

    public abstract boolean isDirectory();

}