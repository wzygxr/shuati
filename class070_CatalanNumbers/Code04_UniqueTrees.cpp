/**
 * 不同结构的二叉树数量 - 卡特兰数应用
 * 
 * 问题描述：
 * 一共有n个节点，认为节点之间无差别，返回能形成多少种不同结构的二叉树
 * 
 * 数学背景：
 * 这是卡特兰数的经典应用之一。n个节点能构成的不同二叉树结构数为第n项卡特兰数。
 * 前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...
 * 
 * 解法思路：
 * 1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
 * 2. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
 * 
 * 相关题目链接：
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 95. 不同的二叉搜索树 II: https://leetcode.cn/problems/unique-binary-search-trees-ii/
 * - LintCode 1638. 不同的二叉搜索树: https://www.lintcode.com/problem/1638/
 * - 洛谷 P1044 栈: https://www.luogu.com.cn/problem/P1044
 * - POJ 1095 Trees Made to Order: http://poj.org/problem?id=1095
 * 
 * 时间复杂度分析：
 * - 动态规划方法：O(n²)
 * - 递推公式：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划方法：O(n)
 * - 递推公式：O(1)
 * 
 * 工程化考量：
 * - 数据量小用哪个公式都可以
 * - 不用考虑溢出、取模等问题
 * - 同时注意到n的范围并不大，直接使用公式4（动态规划方法）
 * - 1 <= n <= 19
 */

int numTrees(int n) {
    // 数据量小用哪个公式都可以
    // 不用考虑溢出、取模等问题
    // 同时注意到n的范围并不大，直接使用公式4
    int* f = new int[n + 1];
    f[0] = f[1] = 1;
    for (int i = 2; i <= n; i++) {
        f[i] = 0;
        for (int l = 0, r = i - 1; l < i; l++, r--) {
            f[i] += f[l] * f[r];
        }
    }
    int result = f[n];
    delete[] f;
    return result;
}

// 简单的主函数，避免使用复杂的输入输出
int main() {
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 5; // 示例值
    
    int result = numTrees(n);
    
    // 简单输出结果
    // 在实际环境中，可以使用printf或其他输出方式
    return 0;
}