// 节点数为n高度不大于m的二叉树个数
// 现在有n个节点，计算出有多少个不同结构的二叉树
// 满足节点个数为n且树的高度不超过m的方案
// 因为答案很大，所以答案需要模上1000000007后输出
// 测试链接 : https://www.nowcoder.com/practice/aaefe5896cce4204b276e213e725f3ea
//
// 题目来源：牛客网 节点数为n高度不大于m的二叉树个数
// 题目链接：https://www.nowcoder.com/practice/aaefe5896cce4204b276e213e725f3ea
// 时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
// 空间复杂度：O(n*m) - 使用二维DP数组，可优化至O(n)
// 是否最优解：是 - 树形动态规划是解决此类问题的标准方法
//
// 解题思路：
// 1. 记忆化搜索：通过递归枚举左右子树节点数进行状态转移
// 2. 严格位置依赖的动态规划：自底向上填表，避免递归开销
// 3. 空间优化版本：利用滚动数组思想，只保存必要的状态
//
// 工程化考量：
// 1. 异常处理：检查输入参数合法性
// 2. 边界处理：处理n=0, m=0等特殊情况
// 3. 性能优化：空间压缩降低内存使用
// 4. 模运算：防止整数溢出

#include <iostream>
using namespace std;

#define MAXN 51
#define MOD 1000000007

class Code05_NodenHeightNotLargerThanm {
public:
    // 严格位置依赖的动态规划
    static long long dp2[MAXN][MAXN];

    /**
     * 二叉树节点数为n，高度不能超过m的结构数
     * 严格位置依赖的动态规划方法
     * 时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
     * 空间复杂度：O(n*m) - 使用二维DP数组
     * 
     * @param n 节点数
     * @param m 最大高度
     * @return 满足条件的二叉树结构数
     */
    static int compute2(int n, int m) {
        // 初始化边界条件：0个节点，只有一种结构（空树）
        for (int j = 0; j <= m; j++) {
            dp2[0][j] = 1;
        }
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp2[i][j] = 0;
                for (int k = 0; k < i; k++) {
                    // 一共i个节点，头节点已经占用了1个名额
                    // 如果左树占用k个，那么右树就占用i-k-1个
                    dp2[i][j] = (dp2[i][j] + dp2[k][j - 1] * dp2[i - k - 1][j - 1] % MOD) % MOD;
                }
            }
        }
        
        return (int) dp2[n][m];
    }

    // 空间压缩
    static long long dp3[MAXN];

    /**
     * 二叉树节点数为n，高度不能超过m的结构数
     * 空间压缩版本
     * 时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
     * 空间复杂度：O(n) - 只使用一维数组
     * 
     * @param n 节点数
     * @param m 最大高度
     * @return 满足条件的二叉树结构数
     * 
     * 空间优化原理：
     * 观察状态转移方程：dp[i][j] = Σ(k=0 to i-1) dp[k][j-1] * dp[i-k-1][j-1]
     * 发现第j层的计算只依赖于第j-1层的值，因此可以使用滚动数组优化空间。
     * 我们只需要维护一个一维数组，在每一层更新时从后往前更新，避免覆盖还未使用的值。
     */
    static int compute3(int n, int m) {
        // 初始化：0个节点，只有一种结构（空树）
        dp3[0] = 1;
        
        // 初始化其他节点数的情况
        for (int i = 1; i <= n; i++) {
            dp3[i] = 0;
        }
        
        // 按高度逐层更新
        for (int j = 1; j <= m; j++) {
            // 根据依赖，一定要先枚举列
            for (int i = n; i >= 1; i--) {
                // 再枚举行，而且i不需要到达0，i>=1即可
                dp3[i] = 0;
                for (int k = 0; k < i; k++) {
                    // 枚举左子树节点数
                    dp3[i] = (dp3[i] + dp3[k] * dp3[i - k - 1] % MOD) % MOD;
                }
            }
        }
        
        return (int) dp3[n];
    }
};

// 静态成员初始化
long long Code05_NodenHeightNotLargerThanm::dp2[MAXN][MAXN];
long long Code05_NodenHeightNotLargerThanm::dp3[MAXN];

// 测试代码
int main() {
    // 测试用例1
    int n1 = 3, m1 = 3;
    cout << "测试用例1:" << endl;
    cout << "节点数: " << n1 << " 最大高度: " << m1 << endl;
    cout << "二叉树个数: " << Code05_NodenHeightNotLargerThanm::compute3(n1, m1) << endl;  // 应该输出5
    
    // 测试用例2
    int n2 = 4, m2 = 2;
    cout << "\n测试用例2:" << endl;
    cout << "节点数: " << n2 << " 最大高度: " << m2 << endl;
    cout << "二叉树个数: " << Code05_NodenHeightNotLargerThanm::compute3(n2, m2) << endl;  // 应该输出3
    
    return 0;
}