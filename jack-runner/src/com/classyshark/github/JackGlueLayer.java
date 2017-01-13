package com.classyshark.github;

import com.android.jack.api.ConfigNotSupportedException;
import com.android.jack.api.JackConfig;
import com.android.jack.api.JackProvider;
import com.android.jack.api.impl.JackProviderImpl;
import com.android.jack.api.v01.Api01CompilationTask;
import com.android.jack.api.v01.Api01Config;
import com.android.jack.api.v01.CompilationException;
import com.android.jack.api.v01.ConfigurationException;
import com.android.jack.api.v01.UnrecoverableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Goal of this class, super simple glue layer with jack
 */

public class JackGlueLayer {
    private JackProvider provider;

    public static void main(String[] args) {
        JackGlueLayer jgl = new JackGlueLayer();
        jgl.init();
        jgl.dumpInfo();

        try {
            jgl.buildWithJack(
                    new File("/Users/bfarber/Development/jack-runner/src"),
                    new File[]{
                            new File("/Users/bfarber/Development/jack-runner/libs/android.jar")
                    },
                    new File("/Users/bfarber/Development/jack-runner/src"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void init() {
        // do what ever init is needed
        provider = new JackProviderImpl();
    }

    // the return type could be Jack compilation result
    public Object buildWithJack(File srcRootFolder, File[] classpathFolders, File outputFolder)
            throws ConfigurationException, ConfigNotSupportedException, IllegalStateException,
            CompilationException, UnrecoverableException {
        return buildWithJack(srcRootFolder, classpathFolders, outputFolder, null);
    }

    // the return type could be Jack compilation result
    // build with parameters
    public Object buildWithJack(File srcRootFolder, File[] classpathFolders, File outputFolder,
                                String[] jackParams) throws ConfigurationException, ConfigNotSupportedException,
            IllegalStateException, CompilationException, UnrecoverableException {
        Api01Config config = provider.createConfig(Api01Config.class);
        config.setProperty("jack.classpath.default-libraries", "false");
        config.setClasspath(Arrays.asList(classpathFolders));
        config.setSourceEntries(getSourceFiles(new ArrayList<File>(), srcRootFolder.toPath()));
        config.setOutputDexDir(outputFolder);
        Api01CompilationTask compilationTask = config.getTask();
        compilationTask.run();
        return compilationTask;
    }

    private List<File> getSourceFiles(List<File> result, Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                File f = path.toFile();
                if (f.isDirectory()) {
                    getSourceFiles(result, path);
                } else if (path.toAbsolutePath().toString().endsWith(".java")) {
                    result.add(f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void dumpInfo() {
        System.out.println("Compiler version: " + provider.getCompilerVersion());
        System.out.println("Compiler release name: " + provider.getCompilerReleaseName());
        System.out.println("Compiler release code: " + provider.getCompilerReleaseCode());
        System.out.println("Compiler sub-release kind: " + provider.getCompilerSubReleaseKind());
        System.out.println("Compiler sub-release code: " + provider.getCompilerSubReleaseCode());
        String str;
        str = provider.getCompilerBuildId();
        System.out.println("Compiler build id: " + ((str != null) ? str : "Unknown"));
        str = provider.getCompilerSourceCodeBase();
        System.out.println("Compiler souce code base: " + ((str != null) ? str : "Unknown"));
        System.out.print("Supported configurations: ");
        for (Class<? extends JackConfig> config : provider.getSupportedConfigs()) {
            System.out.print(config.getSimpleName() + " ");
            assert provider.isConfigSupported(config);
        }
        System.out.println();
    }
}
