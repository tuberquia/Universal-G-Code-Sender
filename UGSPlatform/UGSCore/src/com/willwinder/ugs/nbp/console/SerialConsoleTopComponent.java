/*
    Copywrite 2015-2016 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.willwinder.ugs.nbp.console;

import com.willwinder.ugs.nbp.lookup.CentralLookup;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.listeners.ControllerListener;
import com.willwinder.universalgcodesender.model.Position;
import com.willwinder.universalgcodesender.types.GcodeCommand;
import com.willwinder.universalgcodesender.utils.Settings;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.willwinder.universalgcodesender.nbp.console//SerialConsole//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "SerialConsoleTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "console", openAtStartup = true)
@ActionID(category = "Window", id = "com.willwinder.universalgcodesender.nbp.console.SerialConsoleTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SerialConsoleAction",
        preferredID = "SerialConsoleTopComponent"
)
@Messages({
    "CTL_SerialConsoleAction=Console",
    "CTL_SerialConsoleTopComponent=Console",
    "HINT_SerialConsoleTopComponent=This is the UGS Console"
})
public final class SerialConsoleTopComponent extends TopComponent implements ControllerListener, MouseListener {

    static String verboseString = "[" + Localization.getString("verbose") + "] ";
    
    BackendAPI backend;
    Settings settings;
    
    JPopupMenu menu = new JPopupMenu();
    JCheckBoxMenuItem verboseMenuItem = new JCheckBoxMenuItem("Show verbose messages.");
    
    public SerialConsoleTopComponent() {
        initComponents();
        setName(Bundle.CTL_SerialConsoleTopComponent());
        setToolTipText(Bundle.HINT_SerialConsoleTopComponent());
        
        menu.add(verboseMenuItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        consoleScrollPane = new javax.swing.JScrollPane();
        consoleTextArea = new javax.swing.JTextArea();
        commandLabel = new javax.swing.JLabel();
        commandTextArea = new com.willwinder.universalgcodesender.uielements.CommandTextArea();

        consoleTextArea.setEditable(false);
        consoleTextArea.setColumns(20);
        consoleTextArea.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        consoleTextArea.setRows(5);
        consoleTextArea.setMaximumSize(new java.awt.Dimension(32767, 32767));
        consoleTextArea.setMinimumSize(new java.awt.Dimension(0, 0));
        consoleScrollPane.setViewportView(consoleTextArea);

        org.openide.awt.Mnemonics.setLocalizedText(commandLabel, org.openide.util.NbBundle.getMessage(SerialConsoleTopComponent.class, "SerialConsoleTopComponent.commandLabel.text")); // NOI18N

        commandTextArea.setText(org.openide.util.NbBundle.getMessage(SerialConsoleTopComponent.class, "SerialConsoleTopComponent.commandTextArea.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(commandLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commandTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commandLabel)
                    .addComponent(commandTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel commandLabel;
    private com.willwinder.universalgcodesender.uielements.CommandTextArea commandTextArea;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JTextArea consoleTextArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        backend = CentralLookup.getDefault().lookup(BackendAPI.class);
        settings = CentralLookup.getDefault().lookup(Settings.class);
        
        commandTextArea.init(backend);
        verboseMenuItem.setSelected(settings.isVerboseOutputEnabled());

        backend.addControllerListener(this);
        this.consoleTextArea.addMouseListener(this);
        
        final Preferences pref = NbPreferences.forModule(ConsolePanel.class);

        // Listen for prefernce changes.
        pref.addPreferenceChangeListener(new PreferenceChangeListener() {
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey().equals("verboseCheckbox")) {
                    verboseMenuItem.setSelected(pref.getBoolean("verboseCheckbox", false));
                }
            }
        });
    }

    @Override
    public void componentClosed() {
        settings.setVerboseOutputEnabled(verboseMenuItem.isSelected());
        NbPreferences.forModule(ConsolePanel.class).putBoolean("verboseCheckbox", verboseMenuItem.isSelected());
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void fileStreamComplete(String string, boolean bln) {
    }

    @Override
    public void commandSkipped(GcodeCommand gc) {
    }

    @Override
    public void commandSent(GcodeCommand gc) {
    }

    @Override
    public void commandComplete(GcodeCommand gc) {
    }

    @Override
    public void commandComment(String string) {
    }

    @Override
    public void messageForConsole(final String msg, final Boolean verbose) {
        if (!verbose || verboseMenuItem.isSelected()) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    consoleTextArea.append((verbose ? verboseString : "") + msg);

                    if (consoleTextArea.isVisible()) {
                        consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
                    }
                }
            });
        }
    }

    @Override
    public void statusStringListener(String string, Position pntd, Position pntd1) {
    }

    @Override
    public void postProcessData(int i) {
    }

    // All this to show a right click popup!
    private void doPop(MouseEvent e){
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }
}
