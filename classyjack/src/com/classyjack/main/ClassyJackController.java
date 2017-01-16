package com.classyjack.main;

import java.io.File;

/**
 * Created by bfarber on 16/01/2017.
 */
public class ClassyJackController {
    private final ClassyJackPanel jackPanel;

    public ClassyJackController(ClassyJackPanel jackPanel) {
        this.jackPanel = jackPanel;
    }

    public void compilePressed(String srcPath, String classPath) {

        JackGlueLayer jgl = new JackGlueLayer();
        jgl.init();
        StringBuilder sb = jgl.getDumpInfo();

        try {
            // TODO output jar folder
            jgl.buildWithJack(
                    new File(srcPath),
                    new File(classPath).listFiles(),
                    new File(srcPath));
        } catch (Throwable e) {
            sb.append("\n" + e.getMessage());
        }

        jackPanel.resultArea.setText(sb.toString());
    }
}
