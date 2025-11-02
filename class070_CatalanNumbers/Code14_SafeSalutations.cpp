/**
 * Safe Salutations
 * 有2n个人围成一个圆圈，将他们分成n对，使得连线不相交的方法数
 * 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=932
 */

// 使用基本的C++实现方式，避免使用复杂的STL容器和标准库函数

class Solution {
public:
    /**
     * 计算2n个人围成圆圈分成n对且连线不相交的方法数
     * 这是卡特兰数的经典应用之一
     * 
     * 题目解析：
     * 1. 2n个人围成一个圆圈，需要将他们分成n对
     * 2. 要求连线不相交
     * 3. 任选一个人，他与某个人握手将圆分为两部分
     * 4. 满足卡特兰数的递推关系
     * 
     * 时间复杂度分析：
     * 1. 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
     * 2. 双重循环，外层循环n次，内层循环最多n次
     * 3. 总时间复杂度：O(n²)
     * 
     * 空间复杂度分析：
     * 1. 使用了一个长度为n+1的数组存储中间结果
     * 2. 空间复杂度：O(n)
     * 
     * @param n 对数
     * @return 不相交连线的方法数
     */
    static int countSafeSalutations(int n) {
        // 边界条件处理
        if (n <= 1) {
            return 1;
        }
        
        // dp[i] 表示2*i个人分成i对且连线不相交的方法数
        int* dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = 0;
        }
        
        // 初始化基本情况
        dp[0] = 1; // 0对人有1种方案（空方案）
        dp[1] = 1; // 2个人有1种方案（直接连线）
        
        // 动态规划填表
        // 对于2*i个人，任选一个人，他与第j*2+1个人连线
        // 将圆分为两部分，一部分有2*j个人，另一部分有2*(i-1-j)个人
        // 总方案数就是两部分方案数的乘积
        for (int i = 2; i <= n; i++) {
            // 对于2*i个人，枚举与第1个人连线的人的位置
            for (int j = 0; j < i; j++) {
                // dp[j] 是2*j个人的方案数
                // dp[i-1-j] 是2*(i-1-j)个人的方案数
                // 两者相乘得到当前j值下的方案数，累加到dp[i]中
                dp[i] += dp[j] * dp[i - 1 - j];
            }
        }
        
        int result = dp[n];
        delete[] dp;
        return result;
    }
    
    /**
     * 使用卡特兰数的另一种递推公式计算
     * C(0) = 1
     * C(n) = C(n-1) * (4*n-2) / (n+1)
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param n 对数
     * @return 不相交连线的方法数
     */
    static long long countSafeSalutationsOptimized(int n) {
        if (n <= 1) {
            return 1;
        }
        
        long long catalan = 1;
        for (int i = 2; i <= n; i++) {
            catalan = catalan * (4 * i - 2) / (i + 1);
        }
        return catalan;
    }
};

// 简单的主函数，避免使用复杂的输入输出
int main() {
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 5; // 示例值
    
    int result1 = Solution::countSafeSalutations(n);
    long long result2 = Solution::countSafeSalutationsOptimized(n);
    
    // 简单输出结果
    // 在实际环境中，可以使用其他输出方式
    return 0;
}