package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;

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
//  -branches
//      -master

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File objectsDir = join(GITLET_DIR, "Objects");
    public static final File blobsDir = join(objectsDir, "blobs");
    public static final File commitsDir = join(objectsDir,"commits");
    public static final File HEAD = join(GITLET_DIR,"HEAD");
    public static final File stagingArea = join(GITLET_DIR,"staging area");
    public static final File stagedForAddition = join(stagingArea, "staged for addition");
    public static final File stagedForRemoval = join(stagingArea, "staged for removal");
    public static final File branches = join(GITLET_DIR, "branches");
    /* TODO: fill in the rest of this class. */
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
        if (GITLET_DIR.exists()) {//如果存在的话，要报错
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;//
        } else {
            //创建".gitlet"文件
            GITLET_DIR.mkdir();

            //创建Object/commits和Object/blobs
            objectsDir.mkdir();
            commitsDir.mkdir();
            blobsDir.mkdir();

            //保存initialCommit
            ///////////////System.out.println("will save initialCommit");
            Commit initialCommit = new Commit("initial commit", null);
            //String commitID = Utils.sha1(initialCommit.getMessage() + initialCommit.getTimestamp()+initialCommit.getBlobs() + initialCommit.getParent());
            String commitID = Utils.sha1(initialCommit.getTimestamp());
            File commitFile = join(commitsDir, commitID);
            Utils.writeObject(commitFile, initialCommit);//把commit的信息写入以commit信息序列化成的字符为名的文件中保存

            //创建stagingArea
            stagingArea.mkdir();
            stagedForAddition.mkdir();
            stagedForRemoval.mkdir();

            //create a head that points at the initialCommit
            Utils.writeObject(HEAD,commitID);

            //create a branch初始branch是master
            branches.mkdir();
            File master = join(branches, "master");
            master.mkdir();
        }
    }

    public static void add(String filename) {
        File file_to_add = new File(filename);
        if (!file_to_add.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            //根据文件内容生成fileHash
            byte[] fileContents = Utils.readContents(file_to_add);
            String fileHash = Utils.sha1(fileContents);
            //根据以文件内容生成的hash值判断blobs里有没有相同版本的文件?????存疑：如果我用filename作为文件名，（不）相同文件会覆写
            File blobFile = new File(blobsDir, fileHash);
            //添加到staging area的文件名就是传入的filename

            File fileInaddition = new File(stagedForAddition, filename);
            File fileInremoval = new File(stagedForRemoval,filename);
            if (!blobFile.exists()) {
                //如果blobs中没有相同版本的file，把它加到暂存区/？？？？好像不需要
                //Utils.writeContents(blobFile, fileContents);
                Utils.writeContents(fileInaddition,fileHash);//?可能有问题
            } else if (fileInremoval.exists()) {
                //如果add的文件在stagedForRemoval,则把这里面的文件删除
                fileInremoval.delete();
            }
        }
    }

    public static void commit(String message) {
        List<String> fileInAddition = Utils.plainFilenamesIn(stagedForAddition);
        List<String> fileInRemoval = Utils.plainFilenamesIn(stagedForRemoval);
        //如果暂存区没有file，报错
        if (fileInAddition.isEmpty() && fileInRemoval.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        //获取父提交的id和commit
        String parentCommitId = getHeadCommitId();//这行及以上都是没问题的
        Commit parentCommit = getHeadCommit();//
        //获取父提交的blobs
        HashMap<String, String> newBlobs = new HashMap<>(parentCommit.getBlobs());
        Commit newCommit = new Commit(message, parentCommitId);
        //复制父提交的blobs给newcommit
        newCommit.setBlobs(newBlobs);
        //对于addition暂存区里的所有文件，都把它放在blobs里
        for (String fileName : fileInAddition) {
            File fileToCommit = new File(fileName);
            byte[] fileContents = Utils.readContents(fileToCommit);
            String fileHash = Utils.sha1(fileContents);
            //把文件名保存在commit中的blobs里
            newCommit.getBlobs().put(fileName, fileHash);
            //把文件的内容保存在blobs文件夹下
            File blobFile = Utils.join(blobsDir, fileHash);
            Utils.writeContents(blobFile, fileHash);//??????????好奇怪 这边blobsDir下面的文件名和文件里的内容都是加密后的
        }
        //对于removal暂存区里的所有文件，都把它从blobs里删除
        for (String fileName : fileInRemoval) {
            File fileToCommit = new File(fileName);
            newCommit.getBlobs().remove(fileToCommit);
        }
        //保存新的commit
        String newCommitId =  Utils.sha1(newCommit.getTimestamp());
        File commitFile = join(commitsDir, newCommitId);
        Utils.writeObject(commitFile, newCommit);
        //修改HEAD和当前分支内容
        Utils.writeObject(HEAD, newCommitId);
        //清空staging area
        clearDir(stagedForAddition);
        clearDir(stagedForRemoval);
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
        File commitFile = join(commitsDir, headCommitId);
        Commit headCommit = readObject(commitFile, Commit.class);
        return headCommit;
    }
    private static String getHeadCommitId() {
        File HEAD = join(GITLET_DIR, "HEAD");
        //String headCommitId = Utils.readContentsAsString(HEAD);这边可能readContentsAsString可能有问题
        String headCommitId = Utils.readObject(HEAD, String.class);
        return headCommitId;
    }

    public static void rm(String filename) {
        File fileToRemove = new File(filename);
        byte[] fileContents = Utils.readContents(fileToRemove);
        String fileHash = Utils.sha1(fileContents);
        File fileInAdditon = join(stagedForAddition, filename);
        Commit currentCommit = getHeadCommit();//得到目前的commit,然后看它有没有track这个文件
        if (!fileInAdditon.exists() && !currentCommit.getBlobs().containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        //如果rm的文件在stagedForAddition,则把这里面的文件删除
        if (fileInAdditon.exists()) {
            fileInAdditon.delete();//hhh不管了
        }
        //如果这个文件被track，则把它加到remove暂存区，并在cwd中删除这个文件
        if (currentCommit.getBlobs().containsKey(filename)) {
            File fileRe = join(stagedForRemoval, filename);
            Utils.writeContents(fileRe, fileHash);//以原来的文件名保存，但是内容都是加密过的
            File fileInCWD = new File(CWD,filename);
            fileInCWD.delete();
        }
    }
    public static void log() {

    }
}
