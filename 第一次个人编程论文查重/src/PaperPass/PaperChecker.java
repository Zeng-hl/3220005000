package PaperPass;

import java.io.*;
import java.util.HashMap;  //引入HashMap类
import java.util.Map;
import java.util.Scanner;

public class PaperChecker {
    public static void main(String[] args) {
        String originalPath;    //原文路径
        String queryPath;    //查重文路径
        String ansPath;    //答案存储路径
        String[] originalArray = new String[500];
        String[] addArray = new String[500];

        Scanner in = new Scanner(System.in);
        System.out.println("请输入原文章路径:");
        originalPath = in.nextLine();
        originalArray = TxtToArray(originalPath);   //根据输入路径将原文本读取入数组，分句
        System.out.println("请输入你想要查重的文章路径:");
        queryPath = in.nextLine();
        addArray = TxtToArray(queryPath);     //读取查重文本入数组，分句
        System.out.println("请输入答案储存路径:");
        ansPath = in.nextLine();
        PaperCheck(originalArray, addArray, ansPath);
    }

    //根据标点符号划分句子
    private static int divSentence(int tempchar) {
        if ((char) tempchar == '。' || (char) tempchar == '!' || (char) tempchar == '？' || (char) tempchar == '\n'
                || (char) tempchar == ';' || (char) tempchar == '>')
            return 1;
        else return 2;
    }

    //根据相对路径逐字读取文本，判断字符类型，分词
    private static String[] TxtToArray(String paperPath) {
        String[] divisionArray = new String[2000];
        try {
            Reader reader = null;
            reader = new InputStreamReader(new FileInputStream(new File(paperPath)));   //通过流的方式读取文本数据
            int tempchar,n = 0;
            String sentence = "";
            while ((tempchar = reader.read()) != -1) {   //不断读取数据，只要未读完，则一直循环
                switch (divSentence(tempchar)) {    //根据标点符号划分句子
                    case 1:
                        if (sentence.equals("")) break;
                        else if (sentence.length() > 5) divisionArray[n++] = sentence;
                        sentence = "";
                        break;
                    case 2:
                        sentence = sentence + (char) (tempchar);
                        break;
                    default:
                        break;
                }
            }
            reader.close();   //关闭文档
        } catch (IOException e) {
            e.printStackTrace();
        }
        return divisionArray;
    }

    //查重功能
    private static void PaperCheck(String[] originalArray, String[] addArray, String ansPath) {
        double sentenceSimilarity, cosineSimilarity = 0;   //相似度百分比
        double wordNum = 0;
        for (String doc1 : originalArray) {   //根据切分好的原文文本数组进行循环
            sentenceSimilarity = 0;
            if (doc1 == null) break;    //a为空，全部循环完毕，跳出循环
            wordNum += doc1.length();
            for (String doc2: addArray) {    //根据切分好的查重文本数组进行循环
                if (doc2 == null) break;
                //对比最小编辑距离，余弦相似度
                Map<Character, int[]> algMap = new HashMap<>();   //创建集合对象hashmap，值为整数
                for (int i = 0; i < doc1.length(); i++) {
                    char d1 = doc1.charAt(i);    //逐个获取a中每个字符
                    int[] fq = algMap.get(d1);
                    if (fq != null && fq.length == 2) {
                        fq[0]++;
                    } else {
                        fq = new int[2];
                        fq[0] = 1;
                        fq[1] = 0;
                        algMap.put(d1, fq);
                    }
                }
                for (int i = 0; i < doc2.length(); i++) {
                    char d2 = doc2.charAt(i);
                    int[] fq = algMap.get(d2);
                    if (fq != null && fq.length == 2) {
                        fq[1]++;
                    } else {
                        fq = new int[2];
                        fq[0] = 0;
                        fq[1] = 1;
                        algMap.put(d2, fq);
                    }
                }
                double sqdoc1 = 0;
                double sqdoc2 = 0;
                double denuminator = 0;
                for (Map.Entry entry : algMap.entrySet()) {
                    int[] c = (int[]) entry.getValue();
                    denuminator += c[0] * c[1];
                    sqdoc1 += c[0] * c[0];
                    sqdoc2 += c[1] * c[1];
                }
                double similarPercentage = denuminator / Math.sqrt(sqdoc1 * sqdoc2);   //计算相似度百分比
                if (similarPercentage > sentenceSimilarity)
                    sentenceSimilarity = similarPercentage;
            }
            cosineSimilarity += (sentenceSimilarity * doc1.length());
        }
        cosineSimilarity = cosineSimilarity / wordNum * 100;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  //保留两位小数
        df.format(cosineSimilarity);
        System.out.println("论文相似度为" + String.format("%.2f", cosineSimilarity) + "%");  //保留两位小数
        File file = new File(ansPath);    //根据路径创建文件，输出结果
        try {
            Writer writer = new FileWriter(file,false);
            writer.write("论文相似度为" + String.format("%.2f", cosineSimilarity) + "%");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
