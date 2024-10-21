package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;


//data structure:
// .gitlet
//  -staging area
//      -staged for addition
//      -staged for removal
//  -Objects
//      -blobs
//      -commits
//  -HEAD
//  -BRANCHES_DIR
//      -master


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    public static List<String> REMOVED_FILE = new ArrayList<>();
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "Objects");
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    public static final File STAGING_AREA = join(GITLET_DIR, "staging area");
    public static final File STAGED_FOR_ADDITION = join(STAGING_AREA, "staged for addition");
    public static final File STAGED_FOR_REMOVAL = join(STAGING_AREA, "staged for removal");
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    //判断有没有初始化
    public static void checkInitialized() {
        File gitDir = new File(".gitlet");
        if (!gitDir.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
            //throw new IllegalArgumentException("")
        }
    }

    public static void init() {
        if (GITLET_DIR.exists()) { //如果存在的话，要报错
            System.out.println("A Gitlet version-control " +
                    "system already exists in the current directory.");
            return; //
        } else {
            //创建".gitlet"文件
            GITLET_DIR.mkdir();

            //创建Object/commits和Object/blobs
            OBJECTS_DIR.mkdir();
            COMMITS_DIR.mkdir();
            BLOBS_DIR.mkdir();

            //保存initialCommit
            ///////////////System.out.println("will save initialCommit");
            Commit initialCommit = new Commit("initial commit", null);
            //String commitID = Utils.sha1(initialCommit.getMessage() +
            // initialCommit.getTimestamp()+initialCommit.getBlobs() + initialCommit.getParent());
            String commitID = Utils.sha1(initialCommit.getTimestamp());
            File commitFile = join(COMMITS_DIR, commitID);
            Utils.writeObject(commitFile, initialCommit);
            //把commit的信息写入以commit信息序列化成的字符为名的文件中保存

            //创建STAGING_AREA
            STAGING_AREA.mkdir();
            STAGED_FOR_ADDITION.mkdir();
            STAGED_FOR_REMOVAL.mkdir();

            //create a branch初始branch是master
            BRANCHES_DIR.mkdir();
            File master = join(BRANCHES_DIR, "master");
            Utils.writeContents(master, commitID); //master记录initialCommit的id

            //create a head that points at the master branch
            Utils.writeContents(HEAD, "master");
        }
    }

    public static void add(String filename) {
        File fileToadd = new File(filename);
        if (!fileToadd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            //根据文件内容生成fileHash
            String fileContents = Utils.readContentsAsString(fileToadd);
            String fileHash = Utils.sha1(fileContents);
            //根据以文件内容生成的hash值判断blobs里有没有相同版本的文件
            // ?????存疑：如果我用filename作为文件名，（不）相同文件会覆写
            File blobFile = new File(BLOBS_DIR, fileHash);
            //添加到staging area的文件名就是传入的filename

            File fileInaddition = new File(STAGED_FOR_ADDITION, filename);
            File fileInremoval = new File(STAGED_FOR_REMOVAL, filename);
            if (!blobFile.exists()) {
                //如果blobs中没有相同版本的file，把它加到暂存区
                //Utils.writeContents(blobFile, fileContents);
                Utils.writeContents(fileInaddition, fileContents);
            } else if (fileInremoval.exists()) {
                //如果add的文件在STAGED_FOR_REMOVAL,则把这里面的文件删除
                fileInremoval.delete();
            }
        }
    }

    public static void commit(String message) {
        List<String> fileInAddition = Utils.plainFilenamesIn(STAGED_FOR_ADDITION);
        List<String> fileInRemoval = Utils.plainFilenamesIn(STAGED_FOR_REMOVAL);
        //如果暂存区没有file，报错
        if (fileInAddition.isEmpty() && fileInRemoval.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        //获取父提交的id和commit
        String parentCommitId = getHeadCommitId();
        Commit parentCommit = getHeadCommit();
        //获取父提交的blobs
        HashMap<String, String> newBlobs = new HashMap<>(parentCommit.getBlobs());
        Commit newCommit = new Commit(message, parentCommitId);
        //复制父提交的blobs给newcommit
        newCommit.setBlobs(newBlobs);
        //对于addition暂存区里的所有文件，都把它放在blobs里
        for (String fileName : fileInAddition) {
            File fileToCommit = new File(STAGED_FOR_ADDITION, fileName);
            String fileContents = Utils.readContentsAsString(fileToCommit); //读取文件内容
            String fileHash = Utils.sha1(fileContents);
            writeContents(fileToCommit, fileContents);
            // 把文件内容保存为 Blob 文件，文件名是根据文件内容生成的哈希值
            File blobFile = join(BLOBS_DIR, fileHash);
            // 如果 blob 文件还不存在，则将文件内容保存进去
            if (!blobFile.exists()) {
                Utils.writeContents(blobFile, fileContents);
            }
            //把文件名保存在commit中的blobs里
            newCommit.getBlobs().put(fileName, fileHash);

        }
        //对于removal暂存区里的所有文件，都把它从blobs里删除
        for (String fileName : fileInRemoval) {
            newCommit.getBlobs().remove(fileName);
        }
        //保存新的commit
        String newCommitId =  Utils.sha1(newCommit.getTimestamp()
                + newCommit.getMessage() + newCommit.getParent());
        File commitFile = join(COMMITS_DIR, newCommitId);
        Utils.writeObject(commitFile, newCommit);
        //修改当前分支指向为最新的commit
        String branch = readContentsAsString(HEAD);
        File branchFile = join(BRANCHES_DIR, branch);
        Utils.writeContents(branchFile, newCommitId);

        //清空staging area
        clearDir(STAGED_FOR_ADDITION);
        clearDir(STAGED_FOR_REMOVAL);
    }

    private static void clearDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                clearDir(file);
            }
            file.delete();
        }
    }

    /**use to get the commit that the HEAD points to
     * */
    private static Commit  getHeadCommit() {
        String headCommitId = getHeadCommitId();
        //System.out.println(headCommitId);
        File commitFile = join(COMMITS_DIR, headCommitId);
        Commit headCommit = readObject(commitFile, Commit.class);
        return headCommit;
    }
    private static String getHeadCommitId() {
        //String headCommitId = Utils.readContentsAsString(HEAD);这边可能readContentsAsString可能有问题
        String branchName = readContentsAsString(HEAD); //先提取HEAD指向的分支名
        File branch = join(BRANCHES_DIR, branchName); //根据分支名找到存储commit的文件
        String headCommitId = readContentsAsString(branch);
        return headCommitId;
    }

    public static void rm(String filename) {
        File fileToRemove = new File(filename);
        String fileContents = Utils.readContentsAsString(fileToRemove);
        String fileHash = Utils.sha1(fileContents);
        File fileInAdditon = join(STAGED_FOR_ADDITION, filename);
        Commit currentCommit = getHeadCommit(); //得到目前的commit,然后看它有没有track这个文件
        if (!fileInAdditon.exists() && !currentCommit.getBlobs().containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        //如果rm的文件在STAGED_FOR_ADDITION,则把这里面的文件删除
        if (fileInAdditon.exists()) {
            fileInAdditon.delete(); //hhh不管了
        }
        //如果这个文件被track，则把它加到remove暂存区，并在cwd中删除这个文件
        if (currentCommit.getBlobs().containsKey(filename)) {
            File fileRe = join(STAGED_FOR_REMOVAL, filename);
            Utils.writeContents(fileRe, fileHash); //以原来的文件名保存，但是内容都是加密过的
            REMOVED_FILE.add(filename);
            File fileInCWD = new File(CWD, filename);
            fileInCWD.delete();
        }
    }
    public static void log() {
        String  currentCommitid = getHeadCommitId();
        Commit currentCommit = getHeadCommit();
        while (true) {
            System.out.println("===");
            System.out.println("commit " + currentCommitid);
            //todo 如果合并过，要再显示Merge: 4975af1 2c1ead1
            System.out.println("Date: " + currentCommit.getTimestamp());
            System.out.println(currentCommit.getMessage());
            //如果currentCommit是initialCommit则退出循环
            if (currentCommit.getParent() == null) {
                return;
            }
            //把String parent加密
            //String parentCommitid = Utils.sha1(currentCommit.getParent()); //这边有问题，getParent得到的是加密后的字符串
            String parentCommitid = currentCommit.getParent();
            File parentCommitfile = join(COMMITS_DIR, parentCommitid);
            if (!parentCommitfile.exists()) {
                System.out.println(parentCommitid);
            }
            //再把文件内容转换成Commit.class
            currentCommit = Utils.readObject(parentCommitfile, Commit.class);
            currentCommitid = Utils.sha1(currentCommit.getTimestamp()
                    + currentCommit.getMessage() + currentCommit.getParent());
            System.out.println();
        }
    }

    public static void global_log() {
        int first = 1;
        List<String> fileIncommit = plainFilenamesIn(COMMITS_DIR);
        for (String commitId : fileIncommit) {
            File commitFile = join(COMMITS_DIR, commitId);
            Commit commitPrint = Utils.readObject(commitFile, Commit.class);
            if (first == 1) {
                first = 0;
            } else {
                System.out.println();
            }
            System.out.println("===");
            System.out.println("commit " + commitId);
            System.out.println(commitPrint.getTimestamp());
            System.out.println(commitPrint.getMessage());
        }
    }

    public static void find(String message) {
        int flag = 0;
        List<String> files = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String file : files) {
            File commitFile = join(COMMITS_DIR, file);
            Commit commit = Utils.readObject(commitFile, Commit.class);
            //System.out.println(commit.getMessage() + " " + message);
            if (commit.getMessage().equals(message)) {
                flag = 1;
                System.out.println(file);
            }
        }
        if (flag == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        //1. print Branches part
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(BRANCHES_DIR);
        for (String branch : branches) {
            String currentBranch = readContentsAsString(HEAD);
            if (branch.equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();

        //2.print Staged File
        System.out.println("=== Staged Files ===");
        List<String> filesInstagadd = plainFilenamesIn(STAGED_FOR_ADDITION);
        List<String> filesInstagremove = plainFilenamesIn(STAGED_FOR_REMOVAL);
        List<String> newList = new ArrayList<>();
        newList.addAll(filesInstagremove);
        newList.addAll(filesInstagadd);
        Collections.sort(newList); //原地按照lexicographic order排序
        for (String file : newList) {
            System.out.println(file);
        }
        System.out.println();

        //3.print Removed Files
        System.out.println("=== Removed Files ===");
        Collections.sort(REMOVED_FILE);
        for (String file : REMOVED_FILE) {
            System.out.println(file);
        }
        System.out.println();

        //4.print Modification Not Stated for Commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        //todo 有点难实现 先放着
        System.out.println();

        //5.print Untracked Files
        System.out.println("=== Untracked Files ===");
    }

    public static void checkout(String commitID, String filename) {
        File commitFile = join(COMMITS_DIR, commitID);
        if (!commitFile.exists()) {
            //如果这个文件不存在，说明commitID无效
            System.out.println("No commit with that id exists.");
        }
        Commit commit = readObject(commitFile, Commit.class);
        for (Map.Entry<String, String> entry : commit.getBlobs().entrySet()) {
            //entry是每一个key—value
            if (entry.getKey().equals(filename)) { //如果有这个文件
                File fileToadd = join(CWD, entry.getKey()); //key是待添加文件的文件名
                getBlobContent(fileToadd, entry.getValue());
                return;
            }
        }
        //没有退出循环 说明没有这个文件
        System.out.println("File does not exist in that commit.");
        System.exit(0);
    }

    public static void checkout(String filename) {
        //这边可以用重载减少一点代码量
        String commitID = getHeadCommitId();
        checkout(commitID, filename);
    }

    public static void checkoutBranch(String branchName) {
        String currentBranch = readContentsAsString(HEAD);
        //如果要checkout的branch就是当前的分支，则没有必要checkout
        if (currentBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        List<String> branches = plainFilenamesIn(BRANCHES_DIR);
        for (String branch : branches) {
            if (branch.equals(branchName)) {
                File branchFile = join(BRANCHES_DIR, branch);
                Commit branchCommit = readObject(branchFile, Commit.class);
                Commit currentCommit = getHeadCommit();
                for (Map.Entry<String, String> entry : branchCommit.getBlobs().entrySet()) {
                    //获取branchCommit的每一个key&value
                    File fileToadd = join(CWD, entry.getKey());
                    // 如果currentCommit没有追踪这个文件，要报错
                    if (!currentCommit.getBlobs().containsKey(entry.getKey())) {
                        System.out.println("There is an untracked file in the way;" +
                                " delete it, or add and commit it first.");
                        System.exit(0);
                    }
                    getBlobContent(fileToadd, entry.getValue());
                }
                for (Map.Entry<String, String> entry : currentCommit.getBlobs().entrySet()) {
                    File fileToremove = join(CWD, entry.getKey());
                        if (!branchCommit.getBlobs().containsKey(entry.getKey())) {
                            fileToremove.delete();
                        }
                    }
                }
            }
        //没有在循环中结束说明没找到目标branch
        System.out.println("No such branch exists.");
        System.exit(0);
        }


    public static void getBlobContent (File file, String blobname) {
        File blobFile = join(BLOBS_DIR, blobname);//通过value去找blob文件
        String contents = readContentsAsString(blobFile);
        writeContents(file, contents);
    }

    public static void branch(String branchName) {
        File branch = join(BRANCHES_DIR,branchName);
        // 如果这个分支已经存在了就报错
        if (branch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit currentCommit = getHeadCommit();
        writeObject(branch, currentCommit);
    }

    public static void rmBranch(String branchName) {
        File branch = join(BRANCHES_DIR, branchName);
        //如果不存在 报错
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        // 如果要删除当前指针，报错
        String currentBranch = readContentsAsString(HEAD);
        if (currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.delete();
    }

    public static void reset(String commitId) {
        Commit currentCommit = getHeadCommit();
        File commitFile = join(COMMITS_DIR,commitId);
        //如果文件不存在 报错
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commitToreset = readObject(commitFile, Commit.class);
        for (Map.Entry<String,String> entry : commitToreset.getBlobs().entrySet()){
            //如果文件存在 没有被currentCommit追踪，则报错
            File fileToadd = join(CWD, entry.getKey());
            if (fileToadd.exists() && !currentCommit.getBlobs().containsKey(entry.getKey())) {
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                System.exit(0);
            }
            getBlobContent(fileToadd, entry.getValue());
        }
        for (Map.Entry<String,String> entry : currentCommit.getBlobs().entrySet()) {
            File fileToremove = join(CWD, entry.getKey());
            // 如果当前commit追踪的文件不被指定commit追踪的话，则删除
            if (fileToremove.exists() && commitToreset.getBlobs().containsKey(entry.getKey())) {
                fileToremove.delete();
            }
        }
    }
}
