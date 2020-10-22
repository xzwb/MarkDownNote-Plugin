package cc.yyf.note.window;

import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

public class GitHubUploadSettingView extends UploadSettingView {
    /**
     * 获取最外层控件
     * @return
     */
    @Override
    JComponent getViewComponent() {
        return new OtherSettingWindow().getCommentPanel();
    }

    /**
     * 在setting中展示的名字
     * @return
     */
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "上传github仓库";
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }
}
