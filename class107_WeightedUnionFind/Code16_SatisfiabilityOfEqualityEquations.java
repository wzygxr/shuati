package class156;

// Satisfiability of Equality Equations (LeetCode 990)
// 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，
// 并采用两种不同的形式之一："a==b" 或 "a!=b"。
// 在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
// 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。
// 测试链接 : https://leetcode.com/problems/satisfiability-of-equality-equations/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.util.Arrays;

/**
 * 带权并查集解决等式方程可满足性问题
 * 
 * 问题分析：
 * 判断给定的等式和不等式约束是否可以同时满足
 * 
 * 核心思想：
 * 1. 先处理所有等式约束，建立变量间的连通关系
 * 2. 再检查所有不等式约束，确保不会破坏已建立的连通关系
 * 
 * 时间复杂度分析：
 * - prepare: O(1)
 * - find: O(α(1)) 近似O(1)
 * - union: O(α(1)) 近似O(1)
 * - 总体: O(n * α(1))，其中n是方程数量
 * 
 * 空间复杂度: O(1) 用于存储26个小写字母的father数组
 * 
 * 应用场景：
 * - 约束满足问题
 * - 逻辑一致性验证
 * - 等式方程求解
 * 
 * 题目来源：LeetCode 990
 * 题目链接：https://leetcode.com/problems/satisfiability-of-equality-equations/
 * 题目名称：Satisfiability of Equality Equations
 */
public class Code16_SatisfiabilityOfEqualityEquations {

    // father[i] 表示变量i的父节点（这里用0-25表示a-z）
    public static int[] father = new int[26];

    /**
     * 初始化并查集
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static void prepare() {
        // 初始化每个变量为自己所在集合的代表
        for (int i = 0; i < 26; i++) {
            father[i] = i;
        }
    }

    /**
     * 查找变量i的根节点，并进行路径压缩
     * 时间复杂度: O(α(1)) 近似O(1)
     * 
     * @param i 要查找的变量（0-25表示a-z）
     * @return 变量i所在集合的根节点
     */
    public static int find(int i) {
        // 如果不是根节点
        if (i != father[i]) {
            // 递归查找根节点，同时进行路径压缩
            father[i] = find(father[i]);
        }
        return father[i];
    }

    /**
     * 合并两个变量所在的集合
     * 时间复杂度: O(α(1)) 近似O(1)
     * 
     * @param i 变量i（0-25表示a-z）
     * @param j 变量j（0-25表示a-z）
     */
    public static void union(int i, int j) {
        // 查找两个变量的根节点
        int fi = find(i), fj = find(j);
        // 如果不在同一集合中
        if (fi != fj) {
            // 合并两个集合
            father[fi] = fj;
        }
    }

    /**
     * 判断等式方程是否可满足
     * 
     * @param equations 等式方程数组
     * @return 如果可满足返回true，否则返回false
     */
    public boolean equationsPossible(String[] equations) {
        // 初始化并查集
        prepare();
        
        // 先处理所有等式约束
        for (String equation : equations) {
            // 如果是等式
            if (equation.charAt(1) == '=') {
                // 提取变量
                int a = equation.charAt(0) - 'a';
                int b = equation.charAt(3) - 'a';
                // 合并变量
                union(a, b);
            }
        }
        
        // 再检查所有不等式约束
        for (String equation : equations) {
            // 如果是不等式
            if (equation.charAt(1) == '!') {
                // 提取变量
                int a = equation.charAt(0) - 'a';
                int b = equation.charAt(3) - 'a';
                // 如果两个变量在同一集合中，说明矛盾
                if (find(a) == find(b)) {
                    return false;
                }
            }
        }
        
        // 所有约束都满足
        return true;
    }

    // 测试用例
    public static void main(String[] args) {
        Code16_SatisfiabilityOfEqualityEquations solution = new Code16_SatisfiabilityOfEqualityEquations();
        
        // 测试用例1
        String[] equations1 = {"a==b", "b!=a"};
        System.out.println(solution.equationsPossible(equations1)); // false
        
        // 测试用例2
        String[] equations2 = {"b==a", "a==b"};
        System.out.println(solution.equationsPossible(equations2)); // true
        
        // 测试用例3
        String[] equations3 = {"a==b", "b==c", "a==c"};
        System.out.println(solution.equationsPossible(equations3)); // true
        
        // 测试用例4
        String[] equations4 = {"a==b", "b!=c", "c==a"};
        System.out.println(solution.equationsPossible(equations4)); // false
        
        // 测试用例5
        String[] equations5 = {"c==c", "b==d", "x!=z"};
        System.out.println(solution.equationsPossible(equations5)); // true
    }
}