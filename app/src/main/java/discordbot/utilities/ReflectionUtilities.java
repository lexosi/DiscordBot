package discordbot.utilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReflectionUtilities {

    public static Collection<Class<?>> readFilesFromFolder(String packageName) {
        final String PATH = packageName.replace(".", "/");
        final List<Class<?>> CLASSES = new ArrayList<>();

        final URL RESOURCE = Thread.currentThread().getContextClassLoader().getResource(PATH);
        if (RESOURCE == null) {
            return CLASSES;
        }

        final File PACKAGE = new File(RESOURCE.getFile());
        if (!PACKAGE.isDirectory()) {
            return CLASSES;
        }

        final List<File> FILES = new ArrayList<>();
        ReflectionUtilities.readFiles(PACKAGE, FILES);
        FILES.removeIf(file -> !file.getName().endsWith(".java")
                && !file.getName().endsWith(".class"));

        for (File file : FILES) {

            final String NAME = file.getName().substring(0, file.getName().lastIndexOf('.'));
            // get file path without file name
            String filePath = file.getAbsolutePath()
                    .replace(file.getName(), "")
                    .replace("/", ".")
                    .replace("\\", ".");

            // Get text after packageName
            filePath = filePath.substring(filePath.indexOf(packageName) + packageName.length() + 1);

            final String PACKAGE_NAME = filePath.isBlank()
                    ? packageName + "." + NAME
                    : packageName + "." + filePath + NAME;

            try {
                /* Class.forName */
                // Class.forName("es.nex.threads.App")
                if (PACKAGE_NAME.contains("$")) {
                    continue;
                }
                final Class<?> CLAZZ = Class.forName(PACKAGE_NAME);
                CLASSES.add(CLAZZ);
            } catch (Exception ignored) {
            }
        }

        return CLASSES;
    }

    private static void readFiles(File directory, List<File> fileList) {
        if (directory.isFile()) {
            fileList.add(directory);
            return;
        }

        for (File file : directory.listFiles()) {
            ReflectionUtilities.readFiles(file, fileList);
        }
    }
}
