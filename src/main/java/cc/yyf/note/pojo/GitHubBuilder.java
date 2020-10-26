package cc.yyf.note.pojo;

/**
 * github信息的建造工厂
 */
public class GitHubBuilder {
    private static GitHub gitHub;

    public static GitHub build(String address, String token) {
        if (gitHub == null) {
            return new GitHub(address, token);
        }
        gitHub.setGitHubAddress(address);
        gitHub.setGitHubToken(token);
        return gitHub;
    }
}
