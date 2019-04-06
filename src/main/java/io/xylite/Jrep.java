package io.xylite;

import com.google.re2j.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Jrep {
    public static void main(String[] args) throws IOException {
        Path path = FileSystems.getDefault().getPath("test");
        Pattern pattern = Pattern.compile("see");
        matchInDirectory(pattern, path);
    }

    private static void matchInDirectory(Pattern pattern, Path path) throws IOException {
        Map<Path, List<String>> results = Files.walk(path).parallel()
                .filter(Files::isRegularFile)
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                (Path p) -> (match(pattern, p))
                        )
                );

        System.out.println(results);
    }

    private static List<String> match(Pattern pattern, Path path) {
        List<String> matches = Collections.emptyList();

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            matches = reader.lines().parallel()
                    .filter(s -> pattern.matcher(s).find())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
