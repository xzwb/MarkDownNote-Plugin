package cc.yyf.note.window;

import cc.yyf.note.pojo.DataCenter;
import cc.yyf.note.procesor.DefaultSourceNoteData;
import cc.yyf.note.procesor.FreeMarkProcessor;
import cc.yyf.note.procesor.Processor;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import freemarker.template.TemplateException;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * 工具视窗
 */
public class NoteListWindow {
    private JPanel contentPanel;

    public JPanel getContentPanel() {
        return contentPanel;
    }

    // 生成按钮
    private JButton makeButton;
    // 关闭按钮
    private JButton closeButton;
    // 清空按钮
    private JButton cancelButton;
    // 表格
    private JTable table;
    // 文档标题
    private JTextField textFieldTopic;

    /**
     * 初始化表格
     */
    public void init() {
        // 设置表格
        table.setModel(DataCenter.TABLE_MODEL);
        table.setEnabled(true);
    }


    public NoteListWindow(Project project, ToolWindow toolWindow) {
        init();
        makeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String topic = textFieldTopic.getText();
                if (topic == null || "".equals(topic)) {
                    MessageDialogBuilder.yesNo("操作结果", "文档标题没有输入");
                    return;
                }
                // 要保存的文件名称
                String fileName = topic + ".md";
                VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, project.getBaseDir());
                if (virtualFile != null) {
                    // 获取文件保存的全路径
                    String path = virtualFile.getPath();
                    String fileFullPath = path + File.separator + fileName;
                    Processor processor = new FreeMarkProcessor();
                    try {
                        processor.process(new DefaultSourceNoteData(fileFullPath, topic, DataCenter.NOTE_DATA_LIST));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TemplateException e) {
                        e.printStackTrace();
                    }
                    // 发送通知
                    NotificationGroup firstPluginId = new NotificationGroup("保存文档", NotificationDisplayType.BALLOON, true);
                    Notification notification = firstPluginId.createNotification("文档保存成功", MessageType.INFO);
                    Notifications.Bus.notify(notification);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DataCenter.reset();
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                toolWindow.hide(null);
            }
        });
    }
}