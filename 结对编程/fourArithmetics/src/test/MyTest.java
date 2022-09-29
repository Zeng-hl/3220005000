package test;

import org.junit.Test;

import static arithmetics.fourArithmetics.*;

public class MyTest {
    @Test
    public void test1() {   //测试分数约分方法
        int a=8,b=3;
        String c;
        c = simplify(a,b);
        System.out.println("约分结果：" + c);
    }

    @Test
    public void test2() {    //测试转换为假分数
        int a=18,b=14;
        String c;
        c = mixNumber(a,b);
        System.out.println("转换为假分数：" + c);
    }

    @Test
    public void test3() {   //测试随机生成算式和计算结果
        int molecular = 0, denominator = 0, num1 = 3, num2 = 5, num3 = 2, num4 = 6;
        String[] returnData = new String[2];   //用数组接两个返回值
        for(int i=0; i<4; i++) {
            returnData = chooseArithmetics(i, molecular, denominator, num1, num2, num3, num4);
            System.out.println("choice=" + i + "生成算式为：" + returnData[0]);
            System.out.println("choice=" + i + "加法运算结果为：" + returnData[1]);
        }
    }
}
