package class032;

import java.util.*;
import java.io.*;

// Codeforces 165E Compatible Numbers
// 题目链接: https://codeforces.com/problemset/problem/165/E
// 题目大意:
// 给定一个长度为n的数组，对于数组中的每个数，找到数组中另一个数，
// 使得这两个数的按位与结果为0。如果不存在这样的数，输出-1。

// 解题思路:
// 1. 两个数按位与结果为0，意味着它们在二进制表示中没有同为1的位
// 2. 对于每个数x，我们需要找到一个数y，使得x & y = 0
// 3. 这等价于找到一个数y，使得y是x的按位取反的子集
// 4. 我们可以使用SOS DP (Sum over Subsets DP)来预处理每个数的子集
// 5. 对于每个数x，我们查找x按位取反后是否有子集在数组中存在
// 时间复杂度: O(n + 3^k)，其中k是位数(22位)
// 空间复杂度: O(2^k)

public class Code08_CompatibleNumbers {
    
    // 主函数，处理输入并输出结果
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] parts = reader.readLine().split(" ");
        int[] a = new int[n];
        // 使用BitSet标记数组中存在哪些数
        BitSet exists = new BitSet(1 << 22);
        // 记录每个数在数组中的位置
        int[] pos = new int[1 << 22];
        // 初始化pos数组为-1，表示数不存在
        Arrays.fill(pos, -1);
        
        // 读取数组
        for (int i = 0; i < n; i++) {
            // 读取第i个元素
            a[i] = Integer.parseInt(parts[i]);
            // 标记这个数存在
            exists.set(a[i]);
            // 记录这个数在数组中的位置
            pos[a[i]] = i;
        }
        
        // 存储答案数组，初始化为-1表示未找到兼容数
        int[] answer = new int[n];
        Arrays.fill(answer, -1);
        
        // 对于每个数，找到与它兼容的数
        for (int i = 0; i < n; i++) {
            // 当前处理的数
            int x = a[i];
            // x的按位取反(22位)
            // (1 << 22) - 1 创建一个22位全为1的数
            // x ^ ((1 << 22) - 1) 对x进行按位异或，实现按位取反
            int complement = x ^ ((1 << 22) - 1);
            
            // 查找complement的子集是否有在数组中存在的
            // 使用SOS DP的思想
            // mask表示当前检查的complement的子集
            int mask = complement;
            // 循环枚举complement的所有子集
            while (mask > 0) {
                // 检查mask对应的数是否在数组中存在
                if (exists.get(mask)) {
                    // 找到兼容数，记录其在原数组中的位置
                    answer[i] = pos[mask];
                    // 找到后跳出循环
                    break;
                }
                // 枚举下一个子集
                // (mask - 1) & complement 计算mask的下一个子集
                mask = (mask - 1) & complement;
            }
            
            // 特殊情况：检查0是否在数组中
            // 0与任何数按位与都为0，所以如果数组中有0，它与任何数都兼容
            if (answer[i] == -1 && exists.get(0)) {
                answer[i] = pos[0];
            }
        }
        
        // 输出答案
        // 使用StringBuilder提高字符串拼接效率
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (answer[i] == -1) {
                // 未找到兼容数，输出-1
                sb.append("-1 ");
            } else {
                // 找到兼容数，输出该数的值
                sb.append(a[answer[i]]).append(" ");
            }
        }
        // 输出结果，使用trim()去除末尾空格
        System.out.println(sb.toString().trim());
    }
    
    // 测试用例
    public static void test() {
        System.out.println("Codeforces 165E Compatible Numbers 解题测试");
        // 由于这是在线评测题目，测试用例需要按照题目要求构造
    }
}