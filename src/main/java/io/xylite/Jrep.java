package io.xylite;

import com.google.re2j.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

class Jrep {
    public static void main(String[] args) throws IOException {
        Path path = FileSystems.getDefault().getPath("test");
        Pattern pattern = Pattern.compile("see");
        printInDirectory(pattern, path);
    }

    private static void printInDirectory(Pattern pattern, Path path) throws IOException {
        Files.walk(path).parallel()
                .filter(Files::isRegularFile)
                .forEach((Path p) -> printMatches(pattern, p));
    }

    private static void printMatches(Pattern pattern, Path path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            reader.lines().parallel()
                    .filter(s -> pattern.matcher(s).find())
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void matchInDirectory(Pattern pattern, Path path) throws IOException {
//        Map<Path, List<String>> results = Files.walk(path).parallel()
//                .filter(Files::isRegularFile)
//                .collect(
//                        Collectors.toMap(
//                                Function.identity(),
//                                (Path p) -> (match(pattern, p))
//                        )
//                );
//
//        System.out.println(results);
//    }

//    private static List<String> match(Pattern pattern, Path path) {
//        List<String> matches = Collections.emptyList();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
//            matches = reader.lines().parallel()
//                    .filter(s -> pattern.matcher(s).find())
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return matches;
//    }
}
