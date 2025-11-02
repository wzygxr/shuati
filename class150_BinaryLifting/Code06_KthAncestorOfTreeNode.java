import java.util.*;

/**
 * LeetCode 1483. 树节点的第 K 个祖先
 * 题目描述：给定一棵树，每个节点都有一个唯一值，找到指定节点的第 K 个祖先
 * 如果不存在这样的祖先，则返回 -1
 * 
 * 最优解算法：树上倍增（Binary Lifting）
 * 时间复杂度：预处理 O(n log n)，单次查询 O(log k)
 * 空间复杂度：O(n log n)
 */
public class Code06_KthAncestorOfTreeNode {
    // 定义 KthAncestor 类，用于处理树节点的第K个祖先查询
    public static class KthAncestor {
        private int[][] jump; // jump[i][j] 表示节点 i 的 2^j 级祖先
        private int LOG;     // 最大跳步级别，即 log2(maxHeight)
        private int n;       // 节点数量

        /**
         * 构造函数，初始化树结构和倍增表
         * @param n 节点数量，节点编号为 0 到 n-1
         * @param parent 父节点数组，parent[i] 表示节点 i 的直接父节点
         */
        public KthAncestor(int n, int[] parent) {
            this.n = n;
            // 计算最大跳步级别，取 log2(n) 的上界，确保覆盖所有可能的深度
            this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            
            // 初始化倍增表，jump[LOG][n] 表示有 LOG 个层级，n 个节点
            jump = new int[LOG][n];
            
            // 第一层 jump[0][i] 就是直接父节点
            for (int i = 0; i < n; i++) {
                jump[0][i] = parent[i];
            }
            
            // 预计算倍增表的其他层
            // jump[j][i] = jump[j-1][jump[j-1][i]]
            // 即：节点 i 的 2^j 级祖先 = 节点 i 的 2^(j-1) 级祖先 的 2^(j-1) 级祖先
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i < n; i++) {
                    // 如果当前节点的 2^(j-1) 级祖先存在，则计算 2^j 级祖先
                    // 否则，保持为 -1 表示不存在
                    if (jump[j-1][i] != -1) {
                        jump[j][i] = jump[j-1][jump[j-1][i]];
                    } else {
                        jump[j][i] = -1;
                    }
                }
            }
        }

        /**
         * 查找节点 node 的第 k 个祖先
         * @param node 当前节点编号
         * @param k 祖先距离
         * @return 第 k 个祖先的节点编号，如果不存在则返回 -1
         */
        public int getKthAncestor(int node, int k) {
            // 边界条件处理
            if (k == 0) {
                return node; // 距离为0时，祖先就是自己
            }
            if (node == -1) {
                return -1; // 如果当前节点不存在，直接返回-1
            }

            // 利用二进制分解 k，跳转到第 k 个祖先
            // 遍历 k 的二进制位，如果某一位为1，则跳对应的步数
            for (int j = 0; j < LOG; j++) {
                // 检查 k 的第 j 位是否为1
                if ((k & (1 << j)) != 0) {
                    // 如果这一位为1，就跳 2^j 步
                    node = jump[j][node];
                    // 如果跳跃后节点不存在，直接返回-1
                    if (node == -1) {
                        return -1;
                    }
                }
            }

            // 返回最终到达的节点
            return node;
        }
    }

    /**
     * 主方法，用于测试 KthAncestor 类
     */
    public static void main(String[] args) {
        // 示例测试用例
        testCase1();
        testCase2();
    }

    /**
     * 测试用例1：基本测试
     * 树结构：
     * 0
     * ├── 1
     * │   ├── 2
     * │   └── 3
     * └── 4
     *     └── 5
     */
    private static void testCase1() {
        int n = 6;
        // parent[i] 表示节点i的父节点
        // 节点0是根节点，其父节点为-1
        int[] parent = {-1, 0, 1, 1, 0, 4};
        
        KthAncestor ancestor = new KthAncestor(n, parent);
        
        // 测试查询
        System.out.println("测试用例1:");
        System.out.println("节点2的第1个祖先: " + ancestor.getKthAncestor(2, 1)); // 应输出 1
        System.out.println("节点2的第2个祖先: " + ancestor.getKthAncestor(2, 2)); // 应输出 0
        System.out.println("节点5的第1个祖先: " + ancestor.getKthAncestor(5, 1)); // 应输出 4
        System.out.println("节点5的第2个祖先: " + ancestor.getKthAncestor(5, 2)); // 应输出 0
        System.out.println("节点0的第1个祖先: " + ancestor.getKthAncestor(0, 1)); // 应输出 -1
    }

    /**
     * 测试用例2：较深的树和较大的k值
     * 树结构：0 -> 1 -> 2 -> 3 -> 4 -> 5
     */
    private static void testCase2() {
        int n = 6;
        int[] parent = {-1, 0, 1, 2, 3, 4};
        
        KthAncestor ancestor = new KthAncestor(n, parent);
        
        System.out.println("\n测试用例2:");
        System.out.println("节点5的第3个祖先: " + ancestor.getKthAncestor(5, 3)); // 应输出 2
        System.out.println("节点5的第5个祖先: " + ancestor.getKthAncestor(5, 5)); // 应输出 0
        System.out.println("节点5的第6个祖先: " + ancestor.getKthAncestor(5, 6)); // 应输出 -1
    }

    /**
     * 算法优化与工程化考量：
     * 1. LOG值预计算：避免在每次查询时重新计算，提高效率
     * 2. 边界条件处理：针对k=0、节点不存在等情况做了特殊处理
     * 3. 数组初始化：利用父节点数组直接初始化第一层跳表，优化构建过程
     * 4. 提前终止：在跳转过程中发现节点不存在时立即返回-1
     * 5. 位运算优化：使用位运算判断二进制位，比模运算更高效
     * 
     * 异常场景与边界场景处理：
     * - 根节点的祖先查询（返回-1）
     * - k值超过树高的查询（返回-1）
     * - 空树或单节点树的处理
     * - 重复查询的性能优化
     */
}