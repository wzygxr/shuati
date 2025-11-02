package class122;

/**
 * LeetCode 1483. 树节点的第 K 个祖先（树上倍增法）
 * 
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
 * 
 * 题目描述：
 * 给定一棵树，每个节点有唯一的父节点，实现一个类TreeAncestor：
 * - TreeAncestor(int n, int[] parent): 初始化，n个节点，parent[i]是节点i的父节点
 * - int getKthAncestor(int node, int k): 返回节点node的第k个祖先节点
 * 
 * 算法原理：树上倍增法（二进制拆分）
 * 树上倍增法是一种高效的处理树上祖先查询的技术。
 * 
 * 解题思路：
 * 1. 使用倍增法预处理每个节点的2^i级祖先
 * 2. 对于查询getKthAncestor(node, k)，将k二进制拆分
 * 3. 从高位到低位，如果k的第i位为1，则node跳到其2^i级祖先
 * 4. 重复直到k为0或node为-1
 * 
 * 时间复杂度分析：
 * - 初始化：O(N log N)
 * - 查询：O(log K)
 * 
 * 空间复杂度分析：
 * - 倍增数组：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用二维数组存储倍增信息，便于快速查询
 * 2. 预处理阶段计算所有可能的跳跃，查询时只需常数次操作
 * 3. 边界处理：根节点的父节点为-1，查询时注意越界检查
 * 
 * 最优解分析：
 * 树上倍增法是解决此类问题的最优解，相比朴素方法的O(K)查询时间，
 * 倍增法将查询时间优化到O(log K)，在大规模数据下效率提升显著。
 */

import java.util.*;

public class Code13_LeetCode1483 {
    
    class TreeAncestor {
        private int n;           // 节点数量
        private int maxLevel;    // 最大层数（log2(n)）
        private int[][] stjump;  // 倍增数组，stjump[i][j]表示节点i的2^j级祖先
        
        /**
         * 构造函数：初始化倍增数组
         * 
         * @param n 节点数量
         * @param parent 父节点数组，parent[i]是节点i的父节点
         */
        public TreeAncestor(int n, int[] parent) {
            this.n = n;
            // 计算最大层数：log2(n)
            this.maxLevel = 0;
            while ((1 << maxLevel) <= n) {
                maxLevel++;
            }
            
            // 初始化倍增数组
            stjump = new int[n][maxLevel];
            
            // 初始化第0级祖先（直接父节点）
            for (int i = 0; i < n; i++) {
                stjump[i][0] = parent[i];
            }
            
            // 预处理倍增数组
            // 利用动态规划思想：节点i的2^j级祖先 = 节点i的2^(j-1)级祖先的2^(j-1)级祖先
            for (int j = 1; j < maxLevel; j++) {
                for (int i = 0; i < n; i++) {
                    if (stjump[i][j-1] == -1) {
                        // 如果2^(j-1)级祖先不存在，则2^j级祖先也不存在
                        stjump[i][j] = -1;
                    } else {
                        // 节点i的2^j级祖先 = 节点i的2^(j-1)级祖先的2^(j-1)级祖先
                        stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                    }
                }
            }
        }
        
        /**
         * 查询节点node的第k个祖先
         * 
         * @param node 当前节点
         * @param k 祖先级别
         * @return 第k个祖先节点，如果不存在返回-1
         * 
         * 算法原理：
         * 将k进行二进制拆分，例如k=13=1101(2)=2^3+2^2+2^0
         * 则节点node的第13个祖先 = node的第8个祖先的第4个祖先的第1个祖先
         */
        public int getKthAncestor(int node, int k) {
            // 边界检查
            if (node < 0 || node >= n || k < 0) {
                return -1;
            }
            
            // 二进制拆分k，从低位到高位检查每一位
            for (int j = 0; j < maxLevel; j++) {
                // 如果k的第j位为1
                if (((k >> j) & 1) == 1) {
                    // 节点跳到其2^j级祖先
                    node = stjump[node][j];
                    // 如果祖先不存在，直接返回-1
                    if (node == -1) {
                        return -1;
                    }
                }
            }
            
            return node;
        }
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：简单的链式结构
        int n1 = 7;
        int[] parent1 = {-1, 0, 0, 1, 1, 2, 2};
        TreeAncestor ta1 = new Code13_LeetCode1483().new TreeAncestor(n1, parent1);
        
        // 测试查询
        System.out.println("测试用例1:");
        System.out.println("节点3的第1个祖先: " + ta1.getKthAncestor(3, 1)); // 期望: 1
        System.out.println("节点3的第2个祖先: " + ta1.getKthAncestor(3, 2)); // 期望: 0
        System.out.println("节点3的第3个祖先: " + ta1.getKthAncestor(3, 3)); // 期望: -1
        
        // 测试用例2：更复杂的树结构
        int n2 = 5;
        int[] parent2 = {-1, 0, 0, 1, 2};
        TreeAncestor ta2 = new Code13_LeetCode1483().new TreeAncestor(n2, parent2);
        
        System.out.println("\n测试用例2:");
        System.out.println("节点4的第1个祖先: " + ta2.getKthAncestor(4, 1)); // 期望: 2
        System.out.println("节点4的第2个祖先: " + ta2.getKthAncestor(4, 2)); // 期望: 0
        System.out.println("节点4的第3个祖先: " + ta2.getKthAncestor(4, 3)); // 期望: -1
    }
}