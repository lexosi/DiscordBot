package discordbot.utilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

        final List<File> FILES = Arrays.asList(PACKAGE.listFiles()).stream()
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".class")
                        || file.getName().endsWith(".java"))
                .toList();
        for (File file : FILES) {
            final String NAME = file.getName().substring(0, file.getName().lastIndexOf('.'));
            final String PACKAGE_NAME = packageName + "." + NAME;

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
}
