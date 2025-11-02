package class032;

import java.util.*;
import java.io.*;

// POJ 2443 Set Operation
// 题目链接: http://poj.org/problem?id=2443
// 题目大意:
// 给定N个集合，第i个集合S(i)有C(i)个元素（集合可以包含两个相同的元素）。
// 集合中的每个元素都用1~10000的正数表示。
// 查询两个给定元素i和j是否同时属于至少一个集合。
// 换句话说，确定是否存在一个数字k(1≤k≤N)，使得元素i和元素j都属于S(k)。
//
// 输入:
// 第一行包含一个整数N(1 <= N <= 1000)，表示集合的数量。
// 接下来N行，每行以数字C(i)(1 <= C(i) <= 10000)开始，然后是C(i)个数字，
// 这些数字用空格分隔，给出集合中的元素（这些C(i)个数字不需要彼此不同）。
// 第N+2行包含一个数字Q(1 <= Q <= 200000)，表示查询的数量。
// 然后是Q行。每行包含一对数字i和j(1 <= i, j <= 10000，i可以等于j)，
// 描述需要回答的元素。
//
// 输出:
// 对于每个查询，在一行中，如果存在这样的数字k，打印"Yes"；否则打印"No"。
//
// 解题思路:
// 使用bitset优化的方法:
// 1. 对于每个元素x，我们用一个bitset记录它在哪些集合中出现过
// 2. 对于查询(x,y)，我们检查vis[x] & vis[y]是否为0
//    如果不为0，说明存在至少一个集合同时包含x和y
// 时间复杂度: O(N*C + Q)  其中C是集合的平均大小
// 空间复杂度: O(10000 * N / 32) = O(312500) bit
public class Code03_SetOperation {

    // 使用Java自带的BitSet实现
    // 利用Java标准库中的BitSet类来解决问题
    static class Solution1 {
        // 主函数，处理输入并输出结果
        public static void main(String[] args) throws IOException {
            // 使用BufferedReader提高输入效率
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // 读取集合数量
            int n = Integer.parseInt(reader.readLine());
            
            // vis[x]表示元素x在哪些集合中出现过
            // 使用位运算，第i位为1表示元素x在第i个集合中出现过
            BitSet[] vis = new BitSet[10001];
            // 初始化每个元素的BitSet
            for (int i = 0; i <= 10000; i++) {
                vis[i] = new BitSet(n + 1);
            }
            
            // 读取每个集合
            for (int i = 1; i <= n; i++) {
                // 分割输入行，获取元素列表
                String[] parts = reader.readLine().split(" ");
                // 获取集合中元素的数量
                int c = Integer.parseInt(parts[0]);
                // 处理集合中的每个元素
                for (int j = 1; j <= c; j++) {
                    // 获取元素值
                    int x = Integer.parseInt(parts[j]);
                    // 标记元素x在第i个集合中出现过
                    // set(i)方法将BitSet中第i位设置为true
                    vis[x].set(i);
                }
            }
            
            // 处理查询
            // 读取查询数量
            int q = Integer.parseInt(reader.readLine());
            // 使用StringBuilder提高输出效率
            StringBuilder result = new StringBuilder();
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                // 分割查询行，获取两个元素
                String[] parts = reader.readLine().split(" ");
                // 获取查询的第一个元素
                int x = Integer.parseInt(parts[0]);
                // 获取查询的第二个元素
                int y = Integer.parseInt(parts[1]);
                
                // 检查是否存在一个集合同时包含x和y
                // 克隆vis[x]以避免修改原始数据
                BitSet intersection = (BitSet) vis[x].clone();
                // 计算vis[x]和vis[y]的交集
                intersection.and(vis[y]);
                
                // 检查交集是否为空
                if (!intersection.isEmpty()) {
                    // 存在至少一个集合同时包含x和y
                    result.append("Yes\n");
                } else {
                    // 不存在同时包含x和y的集合
                    result.append("No\n");
                }
            }
            
            // 输出所有查询结果
            System.out.print(result.toString());
        }
    }
    
    // 自定义BitSet实现
    // 通过自定义BitSet类来理解位运算的底层实现原理
    static class Solution2 {
        // 自定义BitSet类，用于存储元素在哪些集合中出现过
        // 使用整数数组来模拟BitSet的功能
        static class CustomBitSet {
            // 使用int数组存储位信息，每个int可以存储32位
            private int[] bits;
            
            // 构造函数，初始化足够大的数组
            // 参数n表示需要存储的位数
            public CustomBitSet(int n) {
                // 计算需要多少个int来存储n位
                // (n + 31) / 32 是向上取整的写法
                // 例如：n=100，则需要(100+31)/32 = 4个int来存储100位
                bits = new int[(n + 31) / 32];
            }
            
            // 设置第i位为1
            // 参数i表示要设置的位索引
            public void set(int i) {
                // i / 32 确定在数组中的哪个int
                // i % 32 确定在该int中的哪一位
                // 1 << (i % 32) 创建一个只有第(i % 32)位为1的数
                // 使用按位或操作将该位置为1
                bits[i / 32] |= (1 << (i % 32));
            }
            
            // 检查第i位是否为1
            // 参数i表示要检查的位索引
            // 返回值：如果第i位为1返回true，否则返回false
            public boolean get(int i) {
                // (bits[i / 32] >> (i % 32)) 将第(i % 32)位移到最低位
                // & 1 提取最低位
                return ((bits[i / 32] >> (i % 32)) & 1) == 1;
            }
            
            // 按位与操作
            // 参数other表示要与当前BitSet进行按位与操作的另一个BitSet
            // 返回值：返回一个新的CustomBitSet，其每一位都是两个操作数对应位的按位与结果
            public CustomBitSet and(CustomBitSet other) {
                // 创建结果BitSet，大小与当前BitSet相同
                CustomBitSet result = new CustomBitSet(bits.length * 32);
                // 对每一位进行按位与操作
                for (int i = 0; i < bits.length; i++) {
                    result.bits[i] = this.bits[i] & other.bits[i];
                }
                return result;
            }
            
            // 检查是否有任何位为1
            // 返回值：如果有任何位为1返回false，如果所有位都为0返回true
            public boolean isEmpty() {
                // 检查每一位是否都为0
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i] != 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        
        // 主函数，处理输入并输出结果
        public static void main(String[] args) throws IOException {
            // 使用BufferedReader提高输入效率
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // 读取集合数量
            int n = Integer.parseInt(reader.readLine());
            
            // vis[x]表示元素x在哪些集合中出现过
            CustomBitSet[] vis = new CustomBitSet[10001];
            // 初始化每个元素的CustomBitSet
            for (int i = 0; i <= 10000; i++) {
                vis[i] = new CustomBitSet(n + 1);
            }
            
            // 读取每个集合
            for (int i = 1; i <= n; i++) {
                // 分割输入行，获取元素列表
                String[] parts = reader.readLine().split(" ");
                // 获取集合中元素的数量
                int c = Integer.parseInt(parts[0]);
                // 处理集合中的每个元素
                for (int j = 1; j <= c; j++) {
                    // 获取元素值
                    int x = Integer.parseInt(parts[j]);
                    // 标记元素x在第i个集合中出现过
                    vis[x].set(i);
                }
            }
            
            // 处理查询
            // 读取查询数量
            int q = Integer.parseInt(reader.readLine());
            // 使用StringBuilder提高输出效率
            StringBuilder result = new StringBuilder();
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                // 分割查询行，获取两个元素
                String[] parts = reader.readLine().split(" ");
                // 获取查询的第一个元素
                int x = Integer.parseInt(parts[0]);
                // 获取查询的第二个元素
                int y = Integer.parseInt(parts[1]);
                
                // 检查是否存在一个集合同时包含x和y
                // 计算vis[x]和vis[y]的按位与结果
                CustomBitSet intersection = vis[x].and(vis[y]);
                
                // 检查按位与结果是否为空
                if (!intersection.isEmpty()) {
                    // 存在至少一个集合同时包含x和y
                    result.append("Yes\n");
                } else {
                    // 不存在同时包含x和y的集合
                    result.append("No\n");
                }
            }
            
            // 输出所有查询结果
            System.out.print(result.toString());
        }
    }
    
    // 测试用例
    // 提供关于该问题的说明信息
    public static void main(String[] args) {
        System.out.println("POJ 2443 Set Operation 解题方案");
        System.out.println("该问题使用bitset优化来高效处理集合操作查询");
        System.out.println("时间复杂度: O(N*C + Q)  其中C是集合的平均大小");
        System.out.println("空间复杂度: O(10000 * N / 32) = O(312500) bit");
    }
}