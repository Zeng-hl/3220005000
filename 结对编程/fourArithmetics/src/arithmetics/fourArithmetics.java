package arithmetics;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class fourArithmetics {
    //根据choice选择生成的算式
    public static String[] chooseArithmetics(int choice, int molecular, int denominator, int num1, int num2, int num3, int num4) {
        String[] returnData = new String[2];   //returnData[0]即formula，returnData[1]即answer，需要返回两个数值
        int z1,z2,mu;
        z1 = num1 * num4;
        z2 = num3 * num2;
        mu = num2 * num4;
        switch (choice){
            case 0:
                molecular = z1 + z2;
                denominator = mu;
                returnData[0] = mixNumber(num1, num2) + "+" + mixNumber(num3, num4) + "=";    //生成算式
                System.out.println(returnData[0]);
                returnData[1] = simplify(molecular, denominator);    //生成结果
                break;
            case 1:
                molecular = z1 - z2;
                denominator = mu;
                if(molecular >= 0) {
                    returnData[0] = mixNumber(num1, num2) + "-" + mixNumber(num3, num4) + "=";
                    System.out.println(returnData[0]);
                    returnData[1] = simplify(molecular, denominator);
                } else {
                    molecular = z2 - z1;
                    returnData[0] = mixNumber(num3, num4) + "-" + mixNumber(num1, num2) + "=";
                    System.out.println(returnData[0]);
                    returnData[1] = simplify(molecular, denominator);
                }
                break;
            case 2:
                molecular = num1 * num3;
                denominator = mu;
                returnData[0] = mixNumber(num1, num2) + "x" + mixNumber(num3, num4) + "=";    //生成算式
                System.out.println(returnData[0]);
                returnData[1] = simplify(molecular, denominator);    //生成结果
                break;
            case 3:
                if(num3 == 0) {
                    num3 = 1;
                }
                molecular = num1 * num4;
                denominator = num2 * num3;
                returnData[0] = mixNumber(num1, num2) + "÷" + mixNumber(num3, num4) + "=";    //生成算式
                System.out.println(returnData[0]);
                returnData[1] = simplify(molecular, denominator);    //生成结果
                break;
        }
        return returnData;
    }

    //化假分数为带分数
    public static String mixNumber(int x, int y) {
        if ( x >= y) {  //分子大于分母，假分数化带分数
            int p = x / y;
            int q = x % y;
            if (q == 0) {
                return String.valueOf(p);
            } else {
                return p + "'" + q + "/" + y;
            }
        } else {  //分子小于分母，直接输出真分数
            return x + "/" + y;
        }
    }

    //约分分子分母（用于约分结果分子分母，即计算出结果）
    public static String simplify(int x, int y) {  //x分子，y分母
        int max=1;
        for (int i=x; i>=1; i--) {
            if (x%i==0 && y%i==0) {  //分子分母都能整除，得最大公约数
                max = i;
                break;
            }
        }
        x /= max;
        y /= max;
        if (x == 0) {
            return "0";
        } else {
            return mixNumber(x, y);
        }
    }

    //题目文件
    public static void processExercises(String formulo, int s) {
        FileWriter fexe = null;   //由于后面要读取文件，因此fw声明写在try外面
        try {
            File f = new File("Exercises.txt");   //创建题目文件
            fexe =new FileWriter(f, true);
        } catch (IOException e) {
            System.out.println("触发异常 : "+e);
        }
        if (formulo != null) {
            PrintWriter pw = new PrintWriter(fexe);    //打开文件写入数据
            pw.println("第"+ (s + 1) +"道题目：" + formulo);
            pw.flush();
            try {
                fexe.flush();
                pw.close();
                fexe.close();
            } catch (IOException e) {
                System.out.println("触发异常 : "+e);
            }
        }
    }

    //答案文件
    public static void processAnswer(String answer, int s) {
        FileWriter fans = null;   //由于后面要读取文件，因此fw声明写在try外面
        try {
            File f = new File("Answer.txt");   //创建题目文件
            fans =new FileWriter(f, true);
        } catch (IOException e) {
            System.out.println("触发异常 : "+e);
        }
        if (answer != null) {
            PrintWriter pw = new PrintWriter(fans);    //打开文件写入数据
            pw.println("第"+ (s + 1) +"道题的答案：" + answer);
            pw.flush();
            try {
                fans.flush();
                pw.close();
                fans.close();
            } catch (IOException e) {
                System.out.println("触发异常 : "+e);
            }
        }
    }

    //提交答案文件
    public static String[] commitAnswer(int n) {
        Scanner sc = new Scanner(System.in);
        String theInput = sc.nextLine();
        String[] inputAnswer = new String[n];
        if(theInput.equals("OK")) {
            int j = 0;
            try {
                FileReader fr = new FileReader("C://Users//Administrator//Desktop//fourArithmetics//answerFile.txt");   //读取文件数据
                BufferedReader br;
                String s;
                for(br = new BufferedReader(fr); (s = br.readLine()) != null; j++) {   //s为读取答案文件的每一行数据，记入解答数组array
                    inputAnswer[j] = s;
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.println("该文件地址不存在，请重新检查！");
            }
        }
        return inputAnswer;
    }

    //主函数main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入生成题目的个数：");
        int n = sc.nextInt();
        System.out.println("请输入题目中的数值范围：");
        int r = sc.nextInt();
        System.out.println("自动生成题目如下。请将答案填入本目录下的answerFile.txt（每个答案之间回车分隔）~");
        System.out.println("完成后输入OK提交答案！");

        Random random = new Random();    //生成随机数
        int num1,num2,num3,num4;
        int molecular=0, denominator=0;
        int choice;
        String[] answer = new String[n];
        String[] returnData = new String[2];    //中转函数中两个返回值
        for(int i=0; i<n; i++){    //循环生成算式
            num1 = random.nextInt(r);
            num2 = random.nextInt(r);
            num3 = random.nextInt(r);
            num4 = random.nextInt(r);
            choice = random.nextInt(4);    //根据随机choice，判断生成哪一种算式
            String[] formula = new String[1];
            if(num2!=0 && num4!=0){   //除数不能为0
                returnData = chooseArithmetics(choice, molecular, denominator, num1, num2, num3, num4);
            }else {
                num2 = 1;
                num4 = 1;
                returnData = chooseArithmetics(choice, molecular, denominator, num1, num2, num3, num4);
            }
            answer[i] = returnData[1];   //answer接收答案
            formula[0] = returnData[0];    //formula接收算式
            processExercises(formula[0], i);
            processAnswer(answer[i], i);
        }
        String[] inputAnswer = new String[n];
        int[] rightArithetic = new int[n+2];
        int[] wrongArithetic = new int[n+2];
        int totalRight = 0;
        int totalWrong = 0;
        inputAnswer = commitAnswer(n);
        for(int j=0; j<n; j++) {   //比较答案
            if(inputAnswer[j].equals(answer[j])) {
                rightArithetic[j] = j + 1;
                totalRight++;
            } else {
                wrongArithetic[j] = j + 1;
                totalWrong++;
            }
        }
        //成绩文件
        FileWriter fgra = null;
        try {
            File f = new File("Grade.txt");
            fgra = new FileWriter(f, true);
        } catch (IOException e) {
            System.out.println("触发异常 : "+e);
        }
        PrintWriter pw = new PrintWriter(fgra);
        pw.print("Correct:" + totalRight + "(");  //写入正确成绩
        for(int j=0; j < n; j++) {
            if(rightArithetic[j] != 0){
                pw.print(rightArithetic[j] + ",");
            }
        }
        pw.println(")");
        pw.print("Wrong:" + totalWrong + "(");  //写入错误成绩
        for(int j=0; j <= n; j++) {
            pw.print(wrongArithetic[j] + ",");
        }
        pw.print(")");
        System.out.println("成绩已出，请在本目录Grade.txt文档中查看成绩！");
        pw.flush();
        try {
            fgra.flush();
            pw.close();
            fgra.close();
        } catch (IOException e) {
            System.out.println("触发异常 : "+e);
        }
    }

}
