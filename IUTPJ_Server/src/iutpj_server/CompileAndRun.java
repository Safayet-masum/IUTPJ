/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iutpj_server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import newproblem.NewProblem;
import newsubmission.NewSubmission;

/**
 *
 * @author ASADUZZAMAN HEROK
 */
public class CompileAndRun implements Runnable {

    final private NewProblem problem;
    final private NewSubmission submission;
    private ProcessBuilder compile, run, compare;
    private File inputs, validoutputs, useroutputs, submissionfile;
    final String folderpath;
    String submissionID;
    long timetaken;
    Database db;

    public CompileAndRun(NewProblem problem, NewSubmission submission, String submissionID, Database db) {
        this.problem = problem;
        this.submission = submission;
        this.submissionID = submissionID;
        this.db = db;
        this.timetaken = -1;

        folderpath = "CompilerDir/";
        new File("CompilerDir").mkdir();

    }

    private int compileCpp() {
        submissionfile = new File(folderpath + submissionID + ".cpp");
        try {
            FileOutputStream fos = new FileOutputStream(submissionfile);
            fos.write(submission.getCodeF());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("CompileCPP File Error:\n" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("CompileCPP File Error:\n" + ex.getMessage());
        }

        compile = new ProcessBuilder("g++", folderpath + submissionID + ".cpp", "-o", folderpath + submissionID+".out");
        try {
            Process p = compile.start();
            try {
                p.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                p.destroy();
                return -1;
            }
            return p.exitValue();

        } catch (IOException ex) {
            System.out.println("CompileCPP CompileProcess Error:\n" + ex.getMessage());
            return -2;
        }

    }

    private int compileJava() {
        new File(submissionID).mkdir();
        submissionfile = new File(submissionID+"/"+ submissionID + ".java");
        try {
            FileOutputStream fos = new FileOutputStream(submissionfile);
            fos.write(submission.getCodeF());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("CompileCPP File Error:\n" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("CompileCPP File Error:\n" + ex.getMessage());
        }

        compile = new ProcessBuilder("javac",submissionID + ".java").directory( new File(submissionID));
        try {
            Process p = compile.start();
            try {
                p.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                p.destroy();
                return -1;
            }
            return p.exitValue();

        } catch (IOException ex) {
            System.out.println("CompileCPP CompileProcess Error:\n" + ex.getMessage());
            return -2;
        }

    }

    private int compileC() {
        submissionfile = new File(folderpath + submissionID + ".c");
        try {
            FileOutputStream fos = new FileOutputStream(submissionfile);
            fos.write(submission.getCodeF());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        compile = new ProcessBuilder("gcc", folderpath + submissionID + ".c", "-o", folderpath + submissionID+".out");
        try {
            Process p = compile.start();
            try {
                p.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                p.destroy();
                return -1;
            }
            return p.exitValue();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return -2;
        }

    }

    private int runCppC() {

        run = new ProcessBuilder(folderpath + submissionID + ".out");
        run.redirectInput(inputs);
        run.redirectOutput(useroutputs);
        System.out.println(Integer.parseInt(problem.getTimeLimit()));
        try {
            long starttime = System.nanoTime();
            Process p = run.start();
            try {
                if (!p.waitFor(Integer.parseInt(problem.getTimeLimit()), TimeUnit.MILLISECONDS)) {
                    long stoptime = System.nanoTime();
                    p.destroy();
                    timetaken = (stoptime - starttime)/1000000;
                    return -1;
                }
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                return -1;
            }
            long stoptime = System.nanoTime();
            timetaken = (stoptime-starttime)/1000000;
            return p.exitValue();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return -2;
        }
    }

    private int runJava() {
        File dir = new File(submissionID);
        File[] classfiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });
        String classname = classfiles[0].getName().substring(0,classfiles[0].getName().lastIndexOf("."));
        System.out.println(classname);
        String memoryLimit = "-Xmx"+problem.getMemoryLimit()+"k";
        run = new ProcessBuilder("java","-XmX1024k",classname).directory( new File(submissionID));
        run.redirectInput(inputs);
        run.redirectOutput(useroutputs);

        try {
            long starttime = System.nanoTime();
            Process p = run.start();
            try {
                if (!p.waitFor(Integer.parseInt(problem.getTimeLimit()), TimeUnit.MILLISECONDS)) {
                    long stoptime = System.nanoTime();
                    p.destroy();
                    timetaken = (stoptime - starttime)/1000000;
                    classfiles[0].delete();
                    return -1;
                }
            } catch (InterruptedException ex) {
                p.destroy();
                classfiles[0].delete();
                return -1;
            }
            long stoptime = System.nanoTime();
            timetaken = (stoptime - starttime)/1000000;
            classfiles[0].delete();
            return p.exitValue();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            classfiles[0].delete();
            return -2;
        }
    }

    @Override
    public void run() {
        System.out.println(submission.getLanguage());
        int iofilestate;

        inputs = new File(folderpath + submissionID + ".input");
        validoutputs = new File(folderpath + submissionID + ".output");
        useroutputs = new File(folderpath + "u" + submissionID + ".output");

        try {
            FileOutputStream fosInp = new FileOutputStream(inputs);
            FileOutputStream fosOup = new FileOutputStream(validoutputs);
            fosInp.write(problem.getInp());
            fosOup.write(problem.getOutp());
            fosInp.close();
            fosOup.close();
            iofilestate = 0;
        } catch (FileNotFoundException ex) {
            iofilestate = -1;
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            iofilestate = -1;
        }

        if (iofilestate < 0) {
            return;
        }
        boolean ErrBeforeCompareOutput = false;
        switch (submission.getLanguage()) {
            case "C":
                if (compileC() != 0) {
                    db.updateVerdict(submissionID, "Compilation Error",-1);
                    ErrBeforeCompareOutput = true;
                } else {
                    int vr = runCppC();
                    
                    if (vr == -1) {
                        db.updateVerdict(submissionID, "Time Limit Exceeded",(int)timetaken);
                        ErrBeforeCompareOutput = true;
                    } else if (vr != 0) {
                        db.updateVerdict(submissionID, "Run Time Error",-1);
                        ErrBeforeCompareOutput = true;
                    }
                }

                break;
            case "C++":
                if (compileCpp() != 0) {
                    db.updateVerdict(submissionID, "Compilation Error",-1);
                    ErrBeforeCompareOutput = true;
                 
                }else{
                   int vr = runCppC();
                    
                    if (vr == -1) {
                        db.updateVerdict(submissionID, "Time Limit Exceeded",(int)timetaken);
                        ErrBeforeCompareOutput = true;
                    } else if (vr != 0) {
                        db.updateVerdict(submissionID, "Run Time Error",-1);
                        ErrBeforeCompareOutput = true;
                    }
                }
                break;
            case "Java":
                if (compileJava() != 0) {
                    db.updateVerdict(submissionID, "Compilation Error",-1);
                    ErrBeforeCompareOutput = true;
                } else {
                    int vr = runJava();
                    if (vr == -1) {
                        db.updateVerdict(submissionID, "Time Limit Exceeded",(int)timetaken);
                        ErrBeforeCompareOutput = true;
                    } else if (vr != 0) {
                        db.updateVerdict(submissionID, "Run Time Error",-1);
                        ErrBeforeCompareOutput = true;
                    }
                }
                break;
            default:
                break;

        }

        if (!ErrBeforeCompareOutput) {
            int comparisonResult;
            compare = new ProcessBuilder("fc", validoutputs.getAbsolutePath(), useroutputs.getAbsolutePath());

            try {
                Process p = compare.start();
                p.waitFor(2, TimeUnit.MINUTES);
                comparisonResult = p.exitValue();

                
                if (comparisonResult == 0) {
                    db.updateVerdict(submissionID, "Accepted",(int)timetaken);
                } else if (comparisonResult == 1) {
                    db.updateVerdict(submissionID, "Wrong Answer",(int)timetaken);
                } else {
                    System.out.println("Output File Missing");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        validoutputs.delete();
        inputs.delete();
        useroutputs.delete();
        submissionfile.delete();
        new File(folderpath + submissionID + ".exe").delete();
        new File(submissionID).delete();
    }

}
