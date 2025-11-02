package class032;

import java.util.*;
import java.io.*;

// HackerRank Java BitSet
// 题目链接: https://www.hackerrank.com/challenges/java-bitset/problem
// 题目大意:
// 给定两个BitSet，大小为n，初始时所有位都为0
// 执行一系列操作，每次操作后打印两个BitSet中1的个数

// 操作包括:
// AND 1 2: 将BitSet1与BitSet2进行按位与操作，结果存储在BitSet1中
// OR 1 2: 将BitSet1与BitSet2进行按位或操作，结果存储在BitSet1中
// XOR 1 2: 将BitSet1与BitSet2进行按位异或操作，结果存储在BitSet1中
// FLIP 1 2: 将BitSet1中下标为2的位翻转
// SET 1 2: 将BitSet1中下标为2的位设置为1

// 解题思路:
// 1. 使用Java内置的BitSet类
// 2. 根据操作类型执行相应的BitSet方法
// 3. 每次操作后打印两个BitSet中1的个数
// 时间复杂度分析:
// - AND, OR, XOR: O(n/64)
// - FLIP, SET: O(1)
// - count(): O(n/64)
// 空间复杂度: O(n)

public class Code06_HackerRankJavaBitSet {
    
    // 主函数，处理输入并输出结果
    public static void main(String[] args) throws IOException {
        // 为了提高输入输出效率，使用BufferedReader和BufferedWriter
        // BufferedReader用于高效读取输入
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // BufferedWriter用于高效输出结果
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        // 读取n和m
        // n表示BitSet的大小，m表示操作的数量
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);  // BitSet的大小
        int m = Integer.parseInt(line[1]);  // 操作的数量
        
        // 初始化两个BitSet
        // 创建一个BitSet数组，索引0不使用，1和2分别对应题目中的BitSet1和BitSet2
        BitSet[] bitSets = new BitSet[3];
        // 初始化BitSet1，大小为n，初始时所有位都为0
        bitSets[1] = new BitSet(n);
        // 初始化BitSet2，大小为n，初始时所有位都为0
        bitSets[2] = new BitSet(n);
        
        // 执行m次操作
        // 循环处理每个操作
        for (int i = 0; i < m; i++) {
            // 读取操作指令
            line = reader.readLine().split(" ");
            // 获取操作类型
            String operation = line[0];
            // 获取第一个操作数（BitSet编号）
            int set1 = Integer.parseInt(line[1]);
            // 获取第二个操作数（BitSet编号或位索引）
            int set2 = Integer.parseInt(line[2]);
            
            // 根据操作类型执行相应的操作
            switch (operation) {
                case "AND":
                    // 将BitSet[set1]与BitSet[set2]进行按位与操作，结果存储在BitSet[set1]中
                    // 按位与操作：两个位都为1时结果才为1，否则为0
                    bitSets[set1].and(bitSets[set2]);
                    break;
                case "OR":
                    // 将BitSet[set1]与BitSet[set2]进行按位或操作，结果存储在BitSet[set1]中
                    // 按位或操作：两个位中至少有一个为1时结果为1，否则为0
                    bitSets[set1].or(bitSets[set2]);
                    break;
                case "XOR":
                    // 将BitSet[set1]与BitSet[set2]进行按位异或操作，结果存储在BitSet[set1]中
                    // 按位异或操作：两个位不同时结果为1，相同时为0
                    bitSets[set1].xor(bitSets[set2]);
                    break;
                case "FLIP":
                    // 将BitSet[set1]中下标为set2的位翻转
                    // 翻转操作：0变1，1变0
                    bitSets[set1].flip(set2);
                    break;
                case "SET":
                    // 将BitSet[set1]中下标为set2的位设置为1
                    // 设置操作：将指定位置为1
                    bitSets[set1].set(set2);
                    break;
            }
            
            // 打印两个BitSet中1的个数
            // cardinality()方法返回BitSet中1的个数
            // 每次操作后都要输出两个BitSet中1的个数
            writer.write(bitSets[1].cardinality() + " " + bitSets[2].cardinality() + "\n");
        }
        
        // 刷新输出缓冲区，确保所有输出都被写入
        writer.flush();
    }
    
    // 测试用例
    // 验证程序的正确性
    public static void test() {
        System.out.println("HackerRank Java BitSet 解题测试");
        
        // 创建两个大小为5的BitSet
        BitSet bs1 = new BitSet(5);
        BitSet bs2 = new BitSet(5);
        
        // 初始状态: bs1 = "00000", bs2 = "00000"
        // cardinality()返回BitSet中1的个数
        System.out.println("Initial: " + bs1.cardinality() + " " + bs2.cardinality());  // 应该输出 "0 0"
        
        // SET 1 4 -> bs1 = "00001"
        // 将bs1中下标为4的位设置为1
        bs1.set(4);
        System.out.println("After SET 1 4: " + bs1.cardinality() + " " + bs2.cardinality());  // 应该输出 "1 0"
        
        // FLIP 2 2 -> bs2 = "00100"
        // 将bs2中下标为2的位翻转（0变1）
        bs2.flip(2);
        System.out.println("After FLIP 2 2: " + bs1.cardinality() + " " + bs2.cardinality());  // 应该输出 "1 1"
        
        // OR 2 1 -> bs2 = "00101"
        // 将bs2与bs1进行按位或操作
        bs2.or(bs1);
        System.out.println("After OR 2 1: " + bs1.cardinality() + " " + bs2.cardinality());  // 应该输出 "1 2"
    }
}