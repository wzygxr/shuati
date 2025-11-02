package class032;

import java.util.*;
import java.io.*;

// POJ 3275 Ranking the Cows
// 题目链接: http://poj.org/problem?id=3275
// 题目大意:
// FJ想按照奶牛产奶的能力给她们排序。现在已知有N头奶牛（1 ≤ N ≤ 1,000）。
// FJ通过比较，已经知道了M（1 ≤ M ≤ 10,000）对相对关系。
// 问你最少还要确定多少对牛的关系才能将所有的牛按照一定顺序排序起来。

// 解题思路:
// 1. 这是一个传递闭包问题
// 2. 使用Floyd算法求传递闭包
// 3. 使用BitSet优化Floyd算法，将时间复杂度从O(N^3)优化到O(N^3/64)
// 4. 统计已知关系数，用完全图的关系数减去已知关系数就是答案
// 时间复杂度: O(N^3/64)
// 空间复杂度: O(N^2/64)

public class Code07_RankingTheCows {
    
    // 主函数，处理输入并输出结果
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取N和M
        // N表示奶牛的数量，M表示已知的关系对数
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);  // 奶牛的数量
        int m = Integer.parseInt(line[1]);  // 已知的关系对数
        
        // 使用BitSet优化的邻接矩阵
        // graph[i]表示第i头牛能直接或间接到达的所有牛
        BitSet[] graph = new BitSet[n + 1];
        // 初始化每头牛的BitSet
        for (int i = 1; i <= n; i++) {
            graph[i] = new BitSet(n + 1);
        }
        
        // 读取已知的M对关系
        // 每一对关系表示a > b，即a到b有一条有向边
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int a = Integer.parseInt(line[0]);  // 较大的奶牛编号
            int b = Integer.parseInt(line[1]);  // 较小的奶牛编号
            // a > b，即a到b有一条有向边
            // 在graph[a]中将b的位置为1，表示a能到达b
            graph[a].set(b);
        }
        
        // Floyd求传递闭包，使用BitSet优化
        // 通过Floyd算法计算所有奶牛之间的可达关系
        for (int k = 1; k <= n; k++) {
            // 枚举中间节点k
            for (int i = 1; i <= n; i++) {
                // 如果i到k有路径，则i到k能到达的所有点，i也能到达
                // get(k)检查第k位是否为1，即i是否能到达k
                if (graph[i].get(k)) {
                    // or(graph[k])将graph[i]与graph[k]按位或
                    // 这表示i能到达k能到达的所有点
                    graph[i].or(graph[k]);
                }
            }
        }
        
        // 统计已知关系数
        // 计算所有已知的奶牛之间的关系对数
        int known = 0;
        for (int i = 1; i <= n; i++) {
            // cardinality()返回BitSet中1的个数
            // 即第i头牛能到达的牛的数量
            known += graph[i].cardinality();
        }
        
        // 完全图的关系数是n*(n-1)/2
        // 答案是还需要确定的关系数
        // 完全图有n*(n-1)/2对关系，减去已知的关系数就是还需要确定的关系数
        int result = n * (n - 1) / 2 - known;
        // 输出结果
        System.out.println(result);
    }
    
    // 测试用例
    public static void test() {
        System.out.println("POJ 3275 Ranking the Cows 解题测试");
        // 由于这是在线评测题目，测试用例需要按照题目要求构造
    }
}