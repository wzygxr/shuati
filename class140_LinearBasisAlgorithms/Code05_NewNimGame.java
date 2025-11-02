package class137;

// 新Nim游戏
// 传统的Nim游戏是这样的：有一些火柴堆，每堆都有若干根火柴（不同堆的火柴数量可以不同）
// 两个游戏者轮流操作，每次可以选一个火柴堆拿走若干根火柴。可以只拿一根，也可以拿走整堆火柴，
// 但不能同时从超过一堆火柴中拿。拿走最后一根火柴的游戏者胜利。
// 本题的游戏稍微有些不同：在第一个回合中，双方可以直接拿走若干个整堆的火柴。
// 可以一堆都不拿，但不可以全部拿走。从第二个回合（又轮到第一个游戏者）开始，规则和Nim游戏一样。
// 如果你先拿，怎样才能保证获胜？如果可以获胜的话，还要让第一回合拿的火柴总数尽量小。
// 1 <= k <= 100
// 1 <= a_i <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4301
// 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import java.io.*;
import java.util.*;

public class Code05_NewNimGame {
    
    public static int MAXN = 101;
    public static int BIT = 31;
    
    // 存储火柴堆的数量
    public static long[] arr = new long[MAXN];
    // 线性基数组
    public static long[] basis = new long[BIT + 1];
    // 火柴堆数
    public static int k;
    
    /**
     * 计算第一回合拿走火柴数目的最小值
     * 算法思路：
     * 1. 要保证在第二个回合开始时，剩下的火柴堆异或和不为0
     * 2. 这等价于选出一些火柴堆，使得剩下的火柴堆线性无关
     * 3. 为了拿走的火柴总数最小，应该保留尽可能多的火柴堆
     * 4. 按火柴数量从大到小排序，贪心地构建线性基
     * 5. 不能插入线性基的火柴堆需要被拿走
     * 时间复杂度：O(k * BIT * log k)
     * 空间复杂度：O(BIT)
     * @return 第一回合拿走火柴数目的最小值，如果不能保证取胜返回-1
     */
    public static long compute() {
        // 按火柴数量从大到小排序
        Arrays.sort(arr, 1, k + 1);
        for (int i = 1; i <= k / 2; i++) {
            long temp = arr[i];
            arr[i] = arr[k + 1 - i];
            arr[k + 1 - i] = temp;
        }
        
        // 清空线性基
        Arrays.fill(basis, 0);
        
        long sum = 0; // 总火柴数
        long keep = 0; // 保留的火柴数
        
        // 计算总火柴数
        for (int i = 1; i <= k; i++) {
            sum += arr[i];
        }
        
        // 贪心构建线性基
        for (int i = 1; i <= k; i++) {
            if (insert(arr[i])) {
                keep += arr[i];
            }
        }
        
        // 如果线性基的大小等于k，说明所有火柴堆线性无关，无法保证获胜
        // 因为对手可以拿走一个火柴堆，使得剩余火柴堆异或和为0
        int count = 0;
        for (int i = 0; i <= BIT; i++) {
            if (basis[i] != 0) {
                count++;
            }
        }
        
        if (count == k) {
            return -1;
        }
        
        // 返回拿走的火柴数
        return sum - keep;
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
        StringTokenizer st;
        
        // 读取火柴堆数
        k = Integer.parseInt(br.readLine());
        
        // 读取各堆火柴数量
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= k; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        // 计算并输出结果
        System.out.println(compute());
    }
}