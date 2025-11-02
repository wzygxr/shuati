package class032;

import java.util.*;
import java.io.*;
import java.math.BigInteger;

// AtCoder AGC020 C - Median Sum
// 题目链接: https://atcoder.jp/contests/agc020/tasks/agc020_c
// 题目大意:
// 给定N个整数A1, A2, ..., AN。
// 考虑A的所有非空子序列的和。有2^N-1个这样的和，这是一个奇数。
// 将这些和按非递减顺序排列为S1, S2, ..., S_{2^N-1}。
// 找到这个列表的中位数S_{2^{N-1}}。
//
// 约束条件:
// 1 ≤ N ≤ 2000
// 1 ≤ Ai ≤ 2000
// 所有输入值都是整数。
//
// 输入:
// 输入以以下格式从标准输入给出:
// N
// A1 A2 ... AN
//
// 输出:
// 打印A的所有非空子序列的和按排序后的中位数。
//
// 解题思路:
// 这是一个经典的bitset优化DP问题。
// 1. 使用bitset来表示所有可能的子集和
// 2. bitset的第i位为1表示存在一个子集的和为i
// 3. 对于每个元素x，我们执行: dp |= dp << x
//    这表示将之前所有可达的和都加上x，同时保留原来的和
// 4. 中位数的计算有一个技巧:
//    所有子集和的总和为sum，那么中位数就是从(sum+1)/2开始第一个可达的和
//
// 时间复杂度: O(N * sum / 32)  其中sum是所有元素的和
// 空间复杂度: O(sum) bit

public class Code04_MedianSum {
    
    // 使用Java自带的BitSet实现
    // 利用Java标准库中的BitSet类来解决问题
    static class Solution1 {
        // 主函数，处理输入并输出结果
        public static void main(String[] args) throws IOException {
            // 使用BufferedReader提高输入效率
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // 读取元素数量
            int n = Integer.parseInt(reader.readLine());
            
            // 读取所有元素
            String[] parts = reader.readLine().split(" ");
            int[] a = new int[n];
            int sum = 0;  // 所有元素的总和
            // 处理每个元素，同时计算总和
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(parts[i]);
                sum += a[i];
            }
            
            // 使用bitset优化的DP
            // dp的第i位为1表示存在一个子集的和为i
            BitSet dp = new BitSet(sum + 1);
            // 初始状态，空集的和为0
            // set(0)将dp的第0位设置为true
            dp.set(0);
            
            // 对于每个元素，更新所有可能的子集和
            for (int i = 0; i < n; i++) {
                // dp << a[i] 表示将所有现有的和都加上a[i]
                // dp | (dp << a[i]) 表示既保留原来的和，又加上a[i]后的新和
                // get(0, dp.length()) 获取dp的一个副本
                BitSet shifted = (BitSet) dp.clone();
                // 手动实现左移操作
                // 先清空高位，再执行左移
                shifted.clear();
                for (int j = dp.nextSetBit(0); j >= 0; j = dp.nextSetBit(j+1)) {
                    shifted.set(j + a[i]);
                }
                // or(shifted) 将dp与shifted按位或，合并原来的和与新和
                dp.or(shifted);
            }
            
            // 找到中位数
            // 所有子集和的总数是2^N - 1，中位数是第2^(N-1)个
            // 有一个数学技巧: 从(sum+1)/2开始第一个可达的和就是中位数
            // 循环从(sum+1)/2开始，找到第一个dp.get(i)为true的位置
            for (int i = (sum + 1) / 2; ; i++) {
                // 检查dp的第i位是否为true
                if (dp.get(i)) {
                    // 找到中位数，输出并结束程序
                    System.out.println(i);
                    break;
                }
            }
        }
    }
    
    // 自定义BitSet实现
    // 通过自定义BitSet类来理解位运算的底层实现原理
    static class Solution2 {
        // 自定义BitSet类，用于优化DP过程
        // 使用long数组来存储位信息，每个long可以存储64位，比int的32位更高效
        static class CustomBitSet {
            // 使用long数组存储位信息，每个long可以存储64位
            private long[] bits;
            
            // 构造函数，初始化足够大的数组
            // 参数n表示需要存储的位数
            public CustomBitSet(int n) {
                // 计算需要多少个long来存储n位
                // (n + 63) / 64 是向上取整的写法
                // 例如：n=100，则需要(100+63)/64 = 2个long来存储100位
                bits = new long[(n + 63) / 64];
            }
            
            // 设置第i位为1
            // 参数i表示要设置的位索引
            public void set(int i) {
                // i / 64 确定在数组中的哪个long
                // i % 64 确定在该long中的哪一位
                // 1L << (i % 64) 创建一个只有第(i % 64)位为1的数
                // 使用按位或操作将该位置为1
                bits[i / 64] |= (1L << (i % 64));
            }
            
            // 检查第i位是否为1
            // 参数i表示要检查的位索引
            // 返回值：如果第i位为1返回true，否则返回false
            public boolean get(int i) {
                // (bits[i / 64] >> (i % 64)) 将第(i % 64)位移到最低位
                // & 1L 提取最低位
                // == 1L 判断该位是否为1
                return ((bits[i / 64] >> (i % 64)) & 1L) == 1L;
            }
            
            // 按位或操作，相当于dp |= dp << x
            // 参数other表示要与当前BitSet进行操作的另一个BitSet
            // 参数shift表示要左移的位数
            public void orShiftLeft(CustomBitSet other, int shift) {
                if (shift == 0) {
                    // 如果shift为0，直接按位或
                    for (int i = 0; i < bits.length && i < other.bits.length; i++) {
                        bits[i] |= other.bits[i];
                    }
                } else {
                    // 实现dp |= other << shift
                    // 计算需要移动的long数量
                    int longShift = shift / 64;
                    // 计算需要移动的位数
                    int bitShift = shift % 64;
                    
                    // 从高位到低位处理，避免覆盖还未处理的数据
                    for (int i = bits.length - 1; i >= 0; i--) {
                        long value = 0;
                        // 计算源位置
                        int srcPos = i - longShift;
                        
                        // 如果源位置有效，则处理当前long的低位部分
                        if (srcPos >= 0 && srcPos < other.bits.length) {
                            value |= other.bits[srcPos] << bitShift;
                        }
                        
                        // 如果bitShift大于0且下一个源位置有效，则处理当前long的高位部分
                        if (bitShift > 0 && srcPos + 1 < other.bits.length && srcPos + 1 >= 0) {
                            // >>> 表示无符号右移
                            value |= other.bits[srcPos + 1] >>> (64 - bitShift);
                        }
                        
                        // 按位或操作，将计算结果合并到当前bits中
                        bits[i] |= value;
                    }
                }
            }
        }
        
        // 主函数，处理输入并输出结果
        public static void main(String[] args) throws IOException {
            // 使用BufferedReader提高输入效率
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // 读取元素数量
            int n = Integer.parseInt(reader.readLine());
            
            // 读取所有元素
            String[] parts = reader.readLine().split(" ");
            int[] a = new int[n];
            int sum = 0;  // 所有元素的总和
            // 处理每个元素，同时计算总和
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(parts[i]);
                sum += a[i];
            }
            
            // 使用自定义bitset优化的DP
            // dp的第i位为1表示存在一个子集的和为i
            CustomBitSet dp = new CustomBitSet(sum + 1);
            // 初始状态，空集的和为0
            dp.set(0);
            
            // 对于每个元素，更新所有可能的子集和
            for (int i = 0; i < n; i++) {
                // dp |= dp << a[i]
                // 执行按位或左移操作
                CustomBitSet temp = new CustomBitSet(sum + 1);
                for (int j = 0; j < dp.bits.length * 64; j++) {
                    if (dp.get(j)) {
                        temp.set(j + a[i]);
                    }
                }
                for (int j = 0; j < dp.bits.length && j < temp.bits.length; j++) {
                    dp.bits[j] |= temp.bits[j];
                }
            }
            
            // 找到中位数
            // 从(sum+1)/2开始第一个可达的和就是中位数
            // 循环从(sum+1)/2开始，找到第一个dp.get(i)为true的位置
            for (int i = (sum + 1) / 2; ; i++) {
                // 检查dp的第i位是否为true
                if (dp.get(i)) {
                    // 找到中位数，输出并结束程序
                    System.out.println(i);
                    break;
                }
            }
        }
    }
    
    // 测试用例
    // 提供关于该问题的说明信息
    public static void main(String[] args) {
        System.out.println("AtCoder AGC020 C - Median Sum 解题方案");
        System.out.println("该问题使用bitset优化DP来高效计算所有子集和");
        System.out.println("时间复杂度: O(N * sum / 64)  其中sum是所有元素的和");
        System.out.println("空间复杂度: O(sum) bit");
        System.out.println("关键技巧: 中位数是从(sum+1)/2开始第一个可达的和");
    }
}