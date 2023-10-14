package discordbot.utilities;

import java.util.Collection;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

@SuppressWarnings("deprecation")
public class ReflectionUtilities {

    public static Collection<Class<?>> readFilesFromFolder(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream().collect(Collectors.toSet());

        /*
         * final String PATH = packageName.replace(".", "/");
         * 
         * final URL RESOURCE =
         * Thread.currentThread().getContextClassLoader().getResource(PATH);
         * if (RESOURCE == null) {
         * return CLASSES;
         * }
         * 
         * final File PACKAGE = new File(RESOURCE.getFile());
         * if (!PACKAGE.isDirectory()) {
         * return CLASSES;
         * }
         * 
         * final List<File> FILES = new ArrayList<>();
         * ReflectionUtilities.readFiles(PACKAGE, FILES);
         * FILES.removeIf(file -> !file.getName().endsWith(".java")
         * && !file.getName().endsWith(".class"));
         * 
         * for (File file : FILES) {
         * 
         * final String NAME = file.getName().substring(0,
         * file.getName().lastIndexOf('.'));
         * // get file path without file name
         * String filePath = file.getAbsolutePath()
         * .replace(file.getName(), "")
         * .replace("/", ".")
         * .replace("\\", ".");
         * 
         * // Get text after packageName
         * filePath = filePath.substring(filePath.indexOf(packageName) +
         * packageName.length() + 1);
         * 
         * final String PACKAGE_NAME = filePath.isBlank()
         * ? packageName + "." + NAME
         * : packageName + "." + filePath + NAME;
         * 
         * try {
         * /* Class.forName
         * // Class.forName("es.nex.threads.App")
         * if (PACKAGE_NAME.contains("$")) {
         * continue;
         * }
         * final Class<?> CLAZZ = Class.forName(PACKAGE_NAME);
         * CLASSES.add(CLAZZ);
         * } catch (Exception ignored) {
         * }
         * }
         */
    }
}
