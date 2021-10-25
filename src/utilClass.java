import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utilClass {
    private static int index = 0;
    private static char[] operate = { '+', '-', '*', '÷' };

    // 遇见带有括号的表达式，提取子表达式
    static String getChildExp(String str) {
        // 开始循环string 表达式
        // 判断表达式是否带有括号
        boolean hasKuoHao = false;
        boolean stopCopy = false;
        String childExp = "";
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            // "("括号的ASCII值为40
            if (item == 40)
                hasKuoHao = true;
            if (item != 41) {
                if (hasKuoHao && !stopCopy && item != 40)
                    childExp += item;
            } else {
                // 如果碰见右括号则停止再复制子表达式
                stopCopy = true;
                index = i + 1;
            }
        }
        return childExp;
    }

    // 计算表达式结果
    static String getResult(String str) {
        /*
         * //首先计算子表达式结果，即带有括号的算数表达式，表达式可能为空串 String result = ""; String newExp =
         * ""; //如果不带有括号，则只需要计算一次 if(getChildExp(str)==""){ result =
         * getRes(str); //System.out.println("该表达式不带有括号"); } else{ //拼接新表达式
         * newExp =getRes(getChildExp(str))+ str.substring(index,str.length());
         * result = getRes(newExp); //System.out.println("新表达式为："+newExp); }
         * return result;
         */
        return sovleExpretion(str);
    }

    //核心算法，实现运算表达式结果
    public static String sovleExpretion(String exp) {
        // 去括号
        // System.out.println(exp);
        String newExp = "";
        if (exp.contains("(")) {
            int i = exp.indexOf("(");
            int j = exp.indexOf(")");
            String s = exp.substring(i + 1, j);
            if (i == 0) {
                newExp = sovleExpretion(s) + exp.substring(j + 1);
            } else {
                newExp = exp.substring(0, i) + sovleExpretion(s) + exp.substring(j + 1);
            }
        } else {
            newExp += exp;
        }
        // System.out.println("新的表达式"+newExp);
        char[] charExp = newExp.toCharArray();
        int n = newExp.length();
        List<Character> OpeList = new ArrayList<Character>();
        List<String> NumList = new ArrayList<String>();
        int pre = 0;
        for (int i = 0; i < n; i++) {
            if (charExp[i] == '+' || charExp[i] == '-' || charExp[i] == '*' || charExp[i] == '÷') {
                if (charExp[i] == '-') {
                    if (i == 0)
                        continue;
                    else {
                        if (charExp[i - 1] == '+' || charExp[i - 1] == '-' || charExp[i - 1] == '*' || charExp[i - 1] == '÷')
                            continue;
                    }
                }
                String s = "";
                for (int j = pre; j < i; j++) {
                    s += charExp[j];
                }
                pre = i + 1;
                OpeList.add(charExp[i]);
                NumList.add(s);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (charExp[i] == '+' || charExp[i] == '-' || charExp[i] == '*' || charExp[i] == '÷') {
                String s = "";
                for (int j = i + 1; j < n; j++) {
                    s += charExp[j];
                }
                NumList.add(s);
                break;
            }
        }
        // 转?号为*号
        for (int i = 0; i < OpeList.size(); i++) {
            if (OpeList.get(i) == '÷') {
                OpeList.set(i, '*');
                NumList.set(i + 1, daoShu(NumList.get(i + 1)));
            }
        }
        // 计算*法
        for (int i = 0; i < OpeList.size(); i++) {
            if (OpeList.get(i) == '*') {
                // System.out.print("计算的数");
                // for(String s:NumList) {
                // System.out.println(s);
                // }System.out.print("计算符");
                // for(char s:OpeList) {
                // System.out.println(s);
                // }
                NumList.set(i, chengFa(NumList.get(i), NumList.get(i + 1)));
                NumList.remove(i + 1);
                OpeList.remove(i);
                i--;
                // System.out.print("计算后的数");
                // for(String s:NumList) {
                // System.out.println(s);
                // }System.out.print("计算后的计算符");
                // for(char s:OpeList) {
                // System.out.println(s);
                // }
            }
        }

        // 计算+和-法
        for (int i = 0; i < OpeList.size(); i++) {
            if (OpeList.get(i) == '+' || OpeList.get(i) == '-') {
                // System.out.print("计算的数");
                // for(String s:NumList) {
                // System.out.println(s);
                // }System.out.print("计算符");
                // for(char s:OpeList) {
                // System.out.println(s);
                // }
                if (OpeList.get(i) == '+')
                    NumList.set(i, jiaFa(NumList.get(i), NumList.get(i + 1)));
                if (OpeList.get(i) == '-')
                    NumList.set(i, jianFa(NumList.get(i), NumList.get(i + 1)));
                NumList.remove(i + 1);
                OpeList.remove(i);
                i--;
                // System.out.print("计算后的数");
                // for(String s:NumList) {
                // System.out.println(s);
                // }System.out.print("计算后的计算符");
                // for(char s:OpeList) {
                // System.out.println(s);
                // }
            }
        }
        return yueFen(NumList.get(0));
    }

    // 计算减法
    public static String jianFa(String s1, String s2) {
        int[] a = decomposeFraction(s1);
        int[] b = decomposeFraction(s2);
        int[] c = new int[2];
        c[0] = a[0] * b[1] - b[0] * a[1];
        c[1] = a[1] * b[1];
        int d;
        if (c[0] < 0) {
            d = CommonFactor(-c[0], c[1]);
        } else
            d = CommonFactor(c[0], c[1]);
        c[0] /= d;
        c[1] /= d;
        if (c[1] == 1) {
            // System.out.println(c[0]+"");
            return c[0] + "";
        } else {
            // System.out.println(c[0]+"/"+c[1]);
            return c[0] + "/" + c[1];
        }
    }

    // 求公因数
    public static int CommonFactor(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    // 计算加法
    public static String jiaFa(String s1, String s2) {
        int[] a = decomposeFraction(s1);
        int[] b = decomposeFraction(s2);
        int[] c = new int[2];
        c[0] = a[0] * b[1] + b[0] * a[1];
        c[1] = a[1] * b[1];
        int d;
        if (c[0] < 0) {
            d = CommonFactor(-c[0], c[1]);
        } else
            d = CommonFactor(c[0], c[1]);
        c[0] /= d;
        c[1] /= d;
        if (c[1] == 1) {
            // System.out.println(c[0]+"");
            return c[0] + "";
        } else {
            // System.out.println(c[0]+"/"+c[1]);
            return c[0] + "/" + c[1];
        }
    }

    // 计算乘法
    public static String chengFa(String s1, String s2) {
        int[] a = decomposeFraction(s1);
        int[] b = decomposeFraction(s2);
        int[] c = new int[2];
        c[0] = a[0] * b[0];
        c[1] = a[1] * b[1];
        int d;
        if (c[0] < 0) {
            d = CommonFactor(-c[0], c[1]);
        } else
            d = CommonFactor(c[0], c[1]);
        if (c[1] == 1) {
            // System.out.println(c[0]+"");
            return c[0] + "";
        } else {
            // System.out.println(c[0]+"/"+c[1]);
            return c[0] + "/" + c[1];
        }
    }

    // 倒数
    public static String daoShu(String s) {
        if (s.contains("/")) {
            int[] a = decomposeFraction(s);
            if (s.contains("-")) {
                return (-a[1]) + "/" + (a[0]);
            } else
                return a[1] + "/" + a[0];
        } else {
            if (s.contains("-")) {
                return "-1/" + s.substring(1);
            } else
                return "1/" + s;
        }
    }

    // 分解分数
    public static int[] decomposeFraction(String s) {
        char[] fra = s.toCharArray();
        if (s.contains("'")) {
            int m = s.indexOf("'");
            int n = s.indexOf("/");
            String a = "", b = "", c = "";
            for (int i = 0; i < m; i++) {
                a += fra[i];
            }
            for (int i = m + 1; i < n; i++) {
                b += fra[i];
            }
            for (int i = n + 1; i < s.length(); i++) {
                c += fra[i];
            }
            int[] result = new int[2];
            int a1 = Integer.parseInt(a), b1 = Integer.parseInt(b), c1 = Integer.parseInt(c);
            result[0] = a1 * c1 + b1;
            result[1] = c1;
            return result;
        } else if (s.contains("/")) {
            // System.out.println("需要转化的分数： "+s);
            int n = s.indexOf("/");
            String a = "", b = "";
            if (s.contains("-")) {
                for (int i = 1; i < n; i++) {
                    a += fra[i];
                }
            } else {
                for (int i = 0; i < n; i++) {
                    a += fra[i];
                }
            }
            for (int i = n + 1; i < s.length(); i++) {
                b += fra[i];
            }
            int[] result = new int[2];
            // System.out.println("要转换的数a： "+a+" 要转换的数b： "+b);
            int a1 = Integer.parseInt(a), b1 = Integer.parseInt(b);
            if (s.contains("-")) {
                result[0] = -a1;
            } else {
                result[0] = a1;
            }
            result[1] = b1;
            return result;
        } else {
            int[] result = new int[2];
            // System.out.println("要转换的数： "+s);
            if (s.contains("-")) {
                result[0] = -Integer.parseInt(s.substring(1));
            } else {
                result[0] = Integer.parseInt(s);
            }
            result[1] = 1;
            return result;
        }
    }

    // 约分
    public static String yueFen(String s) {
        int[] c = decomposeFraction(s);
        int d;
        if (c[0] < 0) {
            d = CommonFactor(-c[0], c[1]);
        } else
            d = CommonFactor(c[0], c[1]);
        c[0] /= d;
        c[1] /= d;
        if (c[1] == 1) {
            // System.out.println(c[0]+"");
            return c[0] + "";
        } else {
            // System.out.println(c[0]+"/"+c[1]);
            return c[0] + "/" + c[1];
        }
    }

    // 计算表达式算法
    static String getRes(String str) {
        // 循环表达式
        double num1 = 0;
        double num2 = 0;

        return "";
    }

    // 去除部分题目开头和结尾为括号的表达式
    static String deleteKuoHao(String str) {
        // System.out.println(str.substring(0,1));
        // System.out.println(str.substring(str.length()-1,str.length()));
        if (str.substring(0, 1).equals("(") && str.substring(str.length() - 1, str.length()).equals(")")) {
            str = str.substring(1, str.length() - 1);
        }
        return str;
    }

    // 判断是否为整数
    public static boolean isInt(String string) {
        if (string == null)
            return false;

        String regEx1 = "[\\-|\\+]?\\d+";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

    // 获取输入的字符串
    static String getScannerStr() {
        // System.out.print("输入");
        Scanner scan = new Scanner(System.in);
        String read = scan.nextLine();
        return read;
    }

    // 获取操作符
    static char getOperate() {
        Random rand = new Random();
        int operatorNum = (int) (rand.nextInt(4)) + 1;
        return operate[operatorNum - 1];
    }

    // 获取操作数
    static String getNum(int num) {
        String str = "";

        // 决定该获取哪种类型操作数1：自然数 2：真分数 3：带分数
        Random rand0 = new Random();
        int type = ((int) (rand0.nextInt(3)) + 1);
        // System.out.println(type + "操作数类型");
        if (type == 1)
        // 生成自然数类型
        {
            Random rand = new Random();
            str = ((int) (rand.nextInt(num)) + 1) + "";
        }
        // 生成真分数类型
        if (type == 2) {
            String fenZi = "";
            String fenMu = "";
            Random rand = new Random();
            // 分母在规定操作数内生成
            fenMu = ((int) (rand.nextInt(num)) + 1) + "";
            // 分子需要比分母小
            fenZi = ((int) (rand.nextInt(Integer.parseInt(fenMu))) + 1) + "";
            str = fenZi + "/" + fenMu;
        }
        // 生成带分数类型
        if (type == 3) {
            String fenZi = "";
            String fenMu = "";
            String left = "";
            Random rand = new Random();
            // 分母在规定操作数内生成
            fenMu = ((int) (rand.nextInt(num)) + 1) + "";
            // 分子需要比分母小
            fenZi = ((int) (rand.nextInt(Integer.parseInt(fenMu))) + 1) + "";
            // 带分数左侧分母
            left = ((int) (rand.nextInt(num)) + 1) + "";
            str = left + "'" + fenZi + "/" + fenMu;
        }
        return str;
    }

}
