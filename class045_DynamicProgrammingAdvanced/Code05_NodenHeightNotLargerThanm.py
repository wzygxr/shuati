# 节点数为n高度不大于m的二叉树个数
# 现在有n个节点，计算出有多少个不同结构的二叉树
# 满足节点个数为n且树的高度不超过m的方案
# 因为答案很大，所以答案需要模上1000000007后输出
# 测试链接 : https://www.nowcoder.com/practice/aaefe5896cce4204b276e213e725f3ea
#
# 题目来源：牛客网 节点数为n高度不大于m的二叉树个数
# 题目链接：https://www.nowcoder.com/practice/aaefe5896cce4204b276e213e725f3ea
# 时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
# 空间复杂度：O(n*m) - 使用二维DP数组，可优化至O(n)
# 是否最优解：是 - 树形动态规划是解决此类问题的标准方法
#
# 解题思路：
# 1. 记忆化搜索：通过递归枚举左右子树节点数进行状态转移
# 2. 严格位置依赖的动态规划：自底向上填表，避免递归开销
# 3. 空间优化版本：利用滚动数组思想，只保存必要的状态
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界处理：处理n=0, m=0等特殊情况
# 3. 性能优化：空间压缩降低内存使用
# 4. 模运算：防止整数溢出
#
# 算法详解：
# 本题是一个典型的树形动态规划问题。我们需要计算满足特定节点数和高度限制的二叉树数量。
# 
# 状态定义：
# dp[i][j] 表示使用 i 个节点，且高度不超过 j 的二叉树数量
#
# 状态转移：
# 对于 i 个节点的树，我们可以枚举左子树使用的节点数 k（0 ≤ k < i），
# 则右子树使用的节点数就是 i-k-1（减去根节点）。
# 左子树的高度不能超过 j-1，右子树的高度也不能超过 j-1。
# 所以转移方程为：
# dp[i][j] = Σ(k=0 to i-1) dp[k][j-1] * dp[i-k-1][j-1]
#
# 边界条件：
# dp[0][j] = 1 （0个节点构成一棵空树，方案数为1）
# dp[i][0] = 0 （i>0时，高度限制为0无法构造树）

class Code05_NodenHeightNotLargerThanm:
    MAXN = 51
    MOD = 1000000007
    
    def __init__(self):
        # 记忆化搜索
        self.dp1 = [[-1 for _ in range(self.MAXN)] for _ in range(self.MAXN)]
        
        # 严格位置依赖的动态规划
        self.dp2 = [[0 for _ in range(self.MAXN)] for _ in range(self.MAXN)]
        
        # 空间压缩
        self.dp3 = [0 for _ in range(self.MAXN)]

    def compute1(self, n, m):
        """
        二叉树节点数为n，高度不能超过m的结构数
        记忆化搜索方法
        时间复杂度：O(n²*m) - 每个状态只计算一次
        空间复杂度：O(n*m) - DP数组 + 递归栈
        
        :param n: 节点数
        :param m: 最大高度
        :return: 满足条件的二叉树结构数
        """
        # 基础情况：0个节点，只有一种结构（空树）
        if n == 0:
            return 1
        
        # n > 0 但高度限制为0，无法构造
        if m == 0:
            return 0
        
        # 如果已经计算过，直接返回结果
        if self.dp1[n][m] != -1:
            return self.dp1[n][m]
        
        ans = 0
        # n个点，头占掉1个
        for k in range(n):
            # 一共n个节点，头节点已经占用了1个名额
            # 如果左树占用k个，那么右树就占用n-k-1个
            left = self.compute1(k, m - 1)
            right = self.compute1(n - k - 1, m - 1)
            ans = (ans + (left * right) % self.MOD) % self.MOD
        
        # 缓存结果并返回
        self.dp1[n][m] = ans
        return ans

    def compute2(self, n, m):
        """
        二叉树节点数为n，高度不能超过m的结构数
        严格位置依赖的动态规划方法
        时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
        空间复杂度：O(n*m) - 使用二维DP数组
        
        :param n: 节点数
        :param m: 最大高度
        :return: 满足条件的二叉树结构数
        """
        # 初始化边界条件：0个节点，只有一种结构（空树）
        for j in range(m + 1):
            self.dp2[0][j] = 1
        
        # 填充DP表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                self.dp2[i][j] = 0
                for k in range(i):
                    # 一共i个节点，头节点已经占用了1个名额
                    # 如果左树占用k个，那么右树就占用i-k-1个
                    left = self.dp2[k][j - 1]
                    right = self.dp2[i - k - 1][j - 1]
                    self.dp2[i][j] = (self.dp2[i][j] + left * right % self.MOD) % self.MOD
        
        return self.dp2[n][m]

    def compute3(self, n, m):
        """
        二叉树节点数为n，高度不能超过m的结构数
        空间压缩版本
        时间复杂度：O(n²*m) - 需要遍历所有可能的节点数和高度组合
        空间复杂度：O(n) - 只使用一维数组
        
        :param n: 节点数
        :param m: 最大高度
        :return: 满足条件的二叉树结构数
        
        空间优化原理：
        观察状态转移方程：dp[i][j] = Σ(k=0 to i-1) dp[k][j-1] * dp[i-k-1][j-1]
        发现第j层的计算只依赖于第j-1层的值，因此可以使用滚动数组优化空间。
        我们只需要维护一个一维数组，在每一层更新时从后往前更新，避免覆盖还未使用的值。
        """
        # 初始化：0个节点，只有一种结构（空树）
        self.dp3[0] = 1
        
        # 初始化其他节点数的情况
        for i in range(1, n + 1):
            self.dp3[i] = 0
        
        # 按高度逐层更新
        for j in range(1, m + 1):
            # 根据依赖，一定要先枚举列
            # 从后往前更新是为了避免在计算当前层时覆盖掉后续还需要用到的前一层的值
            for i in range(n, 0, -1):
                # 再枚举行，而且i不需要到达0，i>=1即可
                self.dp3[i] = 0
                for k in range(i):
                    # 枚举左子树节点数
                    left = self.dp3[k]
                    right = self.dp3[i - k - 1]
                    self.dp3[i] = (self.dp3[i] + left * right % self.MOD) % self.MOD
        
        return self.dp3[n]

# 测试代码
if __name__ == "__main__":
    # 创建实例
    solver = Code05_NodenHeightNotLargerThanm()
    
    # 测试用例1
    n1, m1 = 3, 3
    print("测试用例1:")
    print("节点数:", n1, "最大高度:", m1)
    print("二叉树个数:", solver.compute3(n1, m1))  # 应该输出5
    
    # 测试用例2
    n2, m2 = 4, 2
    print("\n测试用例2:")
    print("节点数:", n2, "最大高度:", m2)
    print("二叉树个数:", solver.compute3(n2, m2))  # 应该输出3
    
    # 测试用例3
    n3, m3 = 5, 3
    print("\n测试用例3:")
    print("节点数:", n3, "最大高度:", m3)
    print("二叉树个数:", solver.compute3(n3, m3))  # 应该输出24