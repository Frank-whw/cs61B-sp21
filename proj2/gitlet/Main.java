package gitlet;

import java.util.ResourceBundle;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Frank
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs(args, 2);
                Repository.checkInitialized();
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.checkInitialized();
                Repository.commit(args[1]);
                break;
            case "rm":
                validateNumArgs(args, 2);
                Repository.checkInitialized();
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs(args, 1);
                Repository.checkInitialized();
                Repository.log();
                break;
            case "global-log":
                validateNumArgs(args, 1);
                Repository.checkInitialized();
                Repository.global_log();
                break;
            case "find":
                validateNumArgs(args, 2);
                Repository.checkInitialized();
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs(args, 1);
                Repository.checkInitialized();
                Repository.status();
                break;
            case "checkout":
                Repository.checkInitialized();
                if (args.length == 3) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4) {
                    Repository.checkout(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                }
                break;
            case "branch":
                validateNumArgs(args, 2);
                Repository.checkInitialized();
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(args,2);
                Repository.checkInitialized();
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs(args,2);
                Repository.checkInitialized();
                Repository.reset(args[1]);
                break;
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
