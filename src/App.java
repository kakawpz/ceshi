import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//主程序
// 32190905358
// 32190905355
public class App {

    public static void main(String[] args) throws Exception {

        // TODO Auto-generated method stub

        System.out.println("********四则运算程序********");
        //生成表达式并存入文件保存
        createExp();

    }

    private static void createExp() throws Exception {

        // 选择生成的题目数量
        System.out.println("请输入生成的题目数量（整数类型）→");
        String num = utilClass.getScannerStr();
        while (!utilClass.isInt(num)) {
            System.out.println("输入错误，不是整数类型，请重新输入！");
            num = utilClass.getScannerStr();
        }

        // 选择生成的题目数量
        System.out.println("请输入题目中数值（自然数、真分数和真分数分母）的范围（整数类型）→");
        String num2 = utilClass.getScannerStr();
        while (!utilClass.isInt(num2)) {
            System.out.println("输入错误，不是整数类型，请重新输入！");
            num2 = utilClass.getScannerStr();
        }

        boolean hasKuoHaoLeft = false;
        boolean hasKuoHaoRight = false;
        // 开始生成题目，生成num个题目
        for (int i = 0; i < Integer.parseInt(num); i++) {
            hasKuoHaoLeft = false;
            hasKuoHaoRight = false;
            String subject = "";
            System.out.println("第" + (i + 1) + "题：");
            // 第一步，获取该题目的运算符数量
            Random rand = new Random();
            int operatorNum = (int) (rand.nextInt(3)) + 1;
            // 记录加入左括号后是否已经生成操作符，右括号根据此来决定是否需要生成
            boolean temp = false;

            // 第二步，生成题目
            for (int j = 0; j < operatorNum; j++) {

                // 决定是否加入“（”括号
                Random rand1 = new Random();
                if (rand1.nextBoolean() && !hasKuoHaoLeft) {
                    subject = subject + '(';
                    hasKuoHaoLeft = true;
                }

                subject += utilClass.getNum(Integer.parseInt(num2));

                // 决定是否加入“)”括号
                if (hasKuoHaoLeft && !hasKuoHaoRight && temp) {
                    Random rand2 = new Random();
                    if (rand2.nextBoolean()) {
                        subject = subject + ')';
                        hasKuoHaoRight = true;
                    }
                }
                subject += utilClass.getOperate();
                if (hasKuoHaoLeft)
                    temp = true;
            }
            subject += utilClass.getNum(Integer.parseInt(num2));
            // 加入右括号
            if (hasKuoHaoLeft && !hasKuoHaoRight) {
                subject = subject + ')';
            }
            // 至此题目已经拼接完成
            // 去除部分题目开头和结尾为括号的表达式
            System.out.println(utilClass.deleteKuoHao(subject));
            subject = utilClass.deleteKuoHao(subject);
            // 将题目写入文件保存
            FileWirter.wirter((i + 1) + "：" + subject);

            // 开始计算表达式结果
            FileWirter.wirter2((i + 1) + "：" + utilClass.sovleExpretion(subject));
        }

    }
}
