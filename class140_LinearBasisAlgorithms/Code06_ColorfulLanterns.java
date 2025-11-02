package class137;

// 彩灯
// Peter女朋友的生日快到了，他亲自设计了一组彩灯，想给女朋友一个惊喜。
// 已知一组彩灯是由一排N个独立的灯泡构成的，并且有M个开关控制它们。
// 从数学的角度看，这一排彩灯的任何一个彩灯只有亮与不亮两个状态，所以共有2^N个样式。
// 由于技术上的问题，Peter设计的每个开关控制的彩灯没有什么规律，
// 当一个开关被按下的时候，它会把所有它控制的彩灯改变状态（即亮变成不亮，不亮变成亮）。
// 假如告诉你他设计的每个开关所控制的彩灯范围，你能否帮他计算出这些彩灯有多少种样式可以展示给他的女朋友？
// 注： 开始时所有彩灯都是不亮的状态。
// 1 <= N, M <= 50
// 测试链接 : https://www.luogu.com.cn/problem/P3857
// 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import java.io.*;
import java.util.*;

public class Code06_ColorfulLanterns {
    
    public static int MAXN = 51;
    public static int BIT = 50;
    
    // 线性基数组
    public static long[] basis = new long[BIT + 1];
    // 灯泡数和开关数
    public static int n, m;
    
    /**
     * 计算彩灯可以展示的样式数目
     * 算法思路：
     * 1. 每个开关控制的灯泡状态可以用一个二进制数表示
     * 2. 所有可能的样式数目等于线性基能表示的不同异或值数目
     * 3. 如果线性基中有k个元素，则能表示2^k种不同的值
     * 4. 由于结果可能很大，需要对2008取模
     * 时间复杂度：O(M * N)
     * 空间复杂度：O(N)
     * @return 彩灯可以展示的样式数目对2008取模的结果
     */
    public static int compute() {
        // 清空线性基
        Arrays.fill(basis, 0);
        
        // 读取每个开关控制的灯泡范围并构建线性基
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= m; i++) {
            String control = scanner.nextLine();
            long mask = 0;
            for (int j = 0; j < n; j++) {
                if (control.charAt(j) == 'O') {
                    mask |= (1L << j);
                }
            }
            insert(mask);
        }
        
        // 计算线性基中非零元素的个数
        int count = 0;
        for (int i = 0; i <= BIT; i++) {
            if (basis[i] != 0) {
                count++;
            }
        }
        
        // 计算2^count % 2008
        int result = 1;
        for (int i = 1; i <= count; i++) {
            result = (result * 2) % 2008;
        }
        
        return result;
    }
    
    /**
     * 将数字插入线性基
     * 算法思路：
     * 1. 从高位到低位扫描
     * 2. 如果当前位为1且线性基中该位为空，则直接插入
     * 3. 否则用线性基中该位的数异或当前数，继续处理
     * @param num 要插入的数字
     * @return 如果成功插入返回true，否则返回false
     */
    public static boolean insert(long num) {
        for (int i = BIT; i >= 0; i--) {
            if (((num >> i) & 1) != 0) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }
    
    /**
     * 主函数
     * 读取输入数据，调用计算函数，输出结果
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        // 读取灯泡数和开关数
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        // 计算并输出结果
        System.out.println(compute());
    }
}