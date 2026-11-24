package com.example.task01;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Task01Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //здесь вы можете вручную протестировать ваше решение, вызывая реализуемый метод и смотря результат
        // например вот так:

        /*
        System.out.println(extractSoundName(new File("task01/src/main/resources/3727.mp3")));
        */
    }

    public static String extractSoundName(File file) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(
                "ffprobe", "-v", "error", "-show_entries", "format_tags=title",
                "-of", "default=noprint_wrappers=1:nokey=1", file.getAbsolutePath())
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start();

        try (InputStream output = process.getInputStream()) {
            String title = new String(output.readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("ffprobe exited with code " + exitCode + " for " + file);
            }
            return title.replaceFirst("\\r?\\n\\z", "");
        } finally {
            process.destroy();
        }
    }
}
