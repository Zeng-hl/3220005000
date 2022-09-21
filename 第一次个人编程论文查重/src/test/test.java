package test;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class test {
    String[] exp1 = {"今天是星期天", "天气晴", "今天晚上我要去看电影"};
    String[] exp2 = {"今天是周天", "天气晴朗", "我晚上要去看电影"};

    @Test
    public void main() {
        PaperCheck(exp1, exp2,"E:\\桌面\\ans.txt");
    }

    //论文查重功能
    private static void PaperCheck(String[] originalArray, String[] addArray, String ansPath) {
        double sentenceSimilarity, cosineSimilarity = 0;   //相似度百分比
        double wordNum = 0;    //统计文章字数
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
        System.out.println("论文相似度为" + cosineSimilarity + "%");
        File file = new File(ansPath);    //根据路径创建文件，输出结果
        try {
            Writer writer = new FileWriter(file,false);
            writer.write("论文相似度为" + cosineSimilarity + "%");
            writer.close();
        } catch (IOException e) {
        }
    }
}
