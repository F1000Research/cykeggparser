package org.cytoscape.keggparser.actions;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.keggparser.KEGGParserPlugin;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;


public class KeggHelpAction extends AbstractCyAction {

    protected static Logger logger = LoggerFactory.getLogger(KeggHelpAction.class);
    //private final ActionListener helpActionListener = CyHelpBroker.getHelpActionListener();

    /**
     * Creates a new HelpContentsAction object.
     */
    public KeggHelpAction() {
        super("User manual");
        setMenuGravity(7);
        setPreferredMenu("Apps.KEGGParser");
    }

    /**
     * Merely calls actionPerformed on the CSH.DisplayHelpFromSource object.
     *
     * @param e The triggering event - passed to CSH.DisplayHelpFromSource.actionPerformed(e)
     */
    public void actionPerformed(ActionEvent e) {
        OpenUserManualTask task = new OpenUserManualTask();
        KEGGParserPlugin.taskManager.execute(new TaskIterator(task));
    }

    class OpenUserManualTask extends AbstractTask {
        File manual;

        @Override
        public void run(TaskMonitor taskMonitor) throws Exception {
            taskMonitor.setTitle("User Manual");

            taskMonitor.setProgress(0.1);

            try {
                manual = new File(KEGGParserPlugin.getKEGGParserDir(), "CyKEGGParser_User_Manual.pdf");
                if (!manual.exists()) {
                    ClassLoader cl = this.getClass().getClassLoader();
                    java.io.InputStream in = cl.getResourceAsStream("CyKEGGParser_User_Manual.pdf");
                    if (in == null) {
                        throw  new Exception("Null InputStream: The User Manual  file could not be loaded from the plugin jar file.");
                    }

                    FileOutputStream out = new FileOutputStream(manual);
                    byte[] bytes = new byte[1024];
                    int read;
                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    in.close();
                    out.close();
                }
                taskMonitor.setProgress(0.8);
                taskMonitor.setStatusMessage("Opening File " + manual.getAbsolutePath());
                if (Desktop.isDesktopSupported())
                    Desktop.getDesktop().open(manual);
                else{
                    throw new Exception("Desktop is not supported!");
                }
            } catch (Exception e) {
                throw new Exception("Problems opening User Manual at " + manual.getAbsolutePath() +
                        "\n" + e.getMessage()+
                        "\n Try opening it manually.");
            } finally {
                taskMonitor.setProgress(1);
                System.gc();
            }
        }

        @Override
        public void cancel() {
            super.cancel();
        }

    }


}

